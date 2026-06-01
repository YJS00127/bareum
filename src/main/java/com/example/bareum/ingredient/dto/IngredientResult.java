package com.example.bareum.ingredient.dto;

import lombok.Getter;
import lombok.AllArgsConstructor;

@Getter
@AllArgsConstructor

public class IngredientResult {
    private String name;
    private String status;
    private String reason;
}
