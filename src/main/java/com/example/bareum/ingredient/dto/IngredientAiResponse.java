package com.example.bareum.ingredient.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor

public class IngredientAiResponse {
    private List<IngredientResult> results;
    private String productStatus;
}
