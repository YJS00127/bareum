package com.example.bareum.ingredient.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor

public class IngredientAiRequest {
    private String skinType;
    private List<String> ingredientNames;
}
