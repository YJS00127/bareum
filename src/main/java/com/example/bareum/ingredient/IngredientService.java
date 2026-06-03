package com.example.bareum.ingredient;

import com.example.bareum.ingredient.dto.IngredientRuleResponse;
import com.example.bareum.ingredient.dto.IngredientAnalyzeResponse;
import com.example.bareum.ingredient.dto.IngredientResult;
import com.example.bareum.user.User;
import com.example.bareum.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import java.util.List;

@Service
@RequiredArgsConstructor
public class IngredientService {
    private final UserRepository userRepository;
    private final VisionService visionService;
    private final IngredientParserService ingredientParserService;
    private final IngredientRuleService ingredientRuleService;
    private final GeminiService geminiService;

    public IngredientAnalyzeResponse analyze(Long userId, MultipartFile image){
        User user = userRepository.findById(userId)
        .orElseThrow(() -> new IllegalArgumentException("사용자가 없습니다."));

        String skinType = user.getSkinType();

        String extractedText = visionService.extractText(image);

        List<String> ingredientNames = ingredientParserService.parse(extractedText);

        if (ingredientNames.isEmpty()) {
            throw new RuntimeException("이미지에서 등록된 성분명을 찾지 못했습니다.");
        }

        IngredientRuleResponse aiResponse =
                ingredientRuleService.analyze(ingredientNames, skinType);

        List<IngredientResult> ingredientResults = aiResponse.getResults();
        String productStatus = aiResponse.getProductStatus();

        String finalExplain =
                geminiService.makeSummary(skinType, ingredientResults, productStatus);

        return new IngredientAnalyzeResponse(
                productStatus,
                finalExplain
        );
    }

}
