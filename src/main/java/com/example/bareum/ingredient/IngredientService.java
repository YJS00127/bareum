package com.example.bareum.ingredient;

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
    private final IngredientAIService ingredientRuleService;
    private final GeminiService geminiService;

    public IngredientAnalyzeResponse analyze(Long userId, MultipartFile image){
        User user = userRepository.findById(userId)
        .orElseThrow(() -> new IllegalArgumentException("사용자가 없습니다."));

        String skinType = user.getSkinType();

        String extractedText = visionService.extractText(image);

        List<String> ingredientNames = ingredientParserService.parse(extractedText);

        List<IngredientResult> ingredientResults =
                ingredientRuleService.analyze(ingredientNames, skinType);

        String finalExplain = geminiService.makeSummary(skinType, ingredientResults);

        return new IngredientAnalyzeResponse(
                skinType,
                extractedText,
                ingredientResults,
                finalExplain
        );
    }

}
