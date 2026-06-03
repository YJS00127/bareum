package com.example.bareum.ingredient;

import com.example.bareum.ingredient.dto.IngredientRuleResponse;
import com.example.bareum.ingredient.dto.IngredientResult;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
@RequiredArgsConstructor
public class IngredientRuleService {
    private static final double GOOD_RATE_THRESHOLD = 0.2;

    private final IngredientSkinRuleRepository ingredientSkinRuleRepository;

    @Transactional(readOnly = true)
    public IngredientRuleResponse analyze(List<String> ingredientNames, String skinType) {

        List<IngredientSkinRule> rules =
                ingredientSkinRuleRepository.findBySkinType_NameAndIngredient_NameIn(
                        skinType,
                        ingredientNames
                );

        Map<String, String> labelMap = new HashMap<>();

        for (IngredientSkinRule rule : rules) {
            String ingredientName = rule.getIngredient().getName();
            String label = rule.getLabel();

            labelMap.put(ingredientName, label.toUpperCase());
        }

        List<IngredientResult> results = new ArrayList<>();

        for (String ingredientName : ingredientNames) {
            String label = labelMap.get(ingredientName);
            results.add(new IngredientResult(
                    ingredientName,
                    label
            ));
        }

        String productStatus = decideProductStatus(results);

        IngredientRuleResponse response = new IngredientRuleResponse();
        response.setResults(results);
        response.setProductStatus(productStatus);

        return response;
    }

    private String decideProductStatus(List<IngredientResult> results) {
        int totalCount = results.size();

        if (totalCount == 0) {
            return "NORMAL";
        }

        long cautionCount = results.stream()
                .filter(result -> "CAUTION".equalsIgnoreCase(result.getStatus()))
                .count();

        if (cautionCount > 0) {
            return "CAUTION";
        }

        long goodCount = results.stream()
                .filter(result -> "GOOD".equalsIgnoreCase(result.getStatus()))
                .count();

        double goodRate = (double) goodCount / totalCount;

        if (goodRate >= GOOD_RATE_THRESHOLD) {
            return "GOOD";
        }

        return "NORMAL";
    }
}