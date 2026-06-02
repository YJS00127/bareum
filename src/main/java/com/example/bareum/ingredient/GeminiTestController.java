package com.example.bareum.ingredient;

import com.example.bareum.ingredient.dto.IngredientResult;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/test")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:5173")
public class GeminiTestController {

    private final GeminiService geminiService;

    @GetMapping("/gemini")
    public String testGemini() {
        String skinType = "건성";

        List<IngredientResult> results = List.of(
                new IngredientResult("글리세린", "GOOD"),
                new IngredientResult("알코올", "CAUTION"),
                new IngredientResult("정제수", "NORMAL")
        );

        String productStatus = "CAUTION";

        return geminiService.makeSummary(skinType, results, productStatus);
    }
}