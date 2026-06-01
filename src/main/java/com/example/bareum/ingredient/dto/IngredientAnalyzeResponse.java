package com.example.bareum.ingredient.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import java.util.List;

@AllArgsConstructor
@Getter

public class IngredientAnalyzeResponse {
    private String skinType;
    private String extractedText;
    private List<IngredientResult> ingredientsResult;
    private String finalExplain;

}
