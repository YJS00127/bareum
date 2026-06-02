package com.example.bareum.ingredient;

import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

@Service
public class IngredientParserService {

    private final List<String> ingredientNames = new ArrayList<>();

    @PostConstruct
    public void loadIngredients() throws Exception {
        BufferedReader reader = new BufferedReader(
                new InputStreamReader(
                        getClass().getResourceAsStream("/data/ingredients_name.txt"),
                        StandardCharsets.UTF_8
                )
        );

        String line;

        while ((line = reader.readLine()) != null) {
            String ingredient = line.trim();

            if (!ingredient.isBlank()) {
                ingredientNames.add(ingredient);
            }
        }

        reader.close();
    }

    public List<String> parse(String text) {
        String cleanedText = preprocess(text);

        List<String> result = new ArrayList<>();

        for (String ingredient : ingredientNames) {
            String cleanedIngredient = preprocess(ingredient);

            if (cleanedText.contains(cleanedIngredient)) {
                if (!result.contains(ingredient)) {
                    result.add(ingredient);
                }
            }
        }

        return result;
    }

    private String preprocess(String text) {
        if (text == null) {
            return "";
        }

        return text
                .replace("\r", "")
                .replace("\n", "")
                .replace(" ", "")
                .replace(",", "")
                .replace(".", "")
                .replace(":", "")
                .replace(";", "")
                .replace("，", "")
                .replace("ㆍ", "")
                .replace("·", "")
                .replace("/", "")
                .replace("[", "")
                .replace("]", "")
                .replace("(", "")
                .replace(")", "")
                .trim();
    }
}