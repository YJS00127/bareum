package com.example.bareum.ingredient;

import com.example.bareum.ingredient.dto.IngredientResult;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class IngredientAIService {

    public List<IngredientResult> analyze(List<String> ingredientNames, String skinType) {
        List<IngredientResult> results = new ArrayList<>();

        for (String ingredientName : ingredientNames) {
            results.add(predict(ingredientName, skinType));
        }

        return results;
    }

    private IngredientResult predict(String ingredientName, String skinType) {
        // TODO: 나중에 Python AI 서버 호출 코드로 교체

        return new IngredientResult(
                ingredientName,
                "NORMAL",
                "AI 모델 연결 전 임시 결과입니다."
        );
    }
}