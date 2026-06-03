package com.example.bareum.ingredient;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.EntityGraph;

import java.util.List;

public interface IngredientSkinRuleRepository extends JpaRepository<IngredientSkinRule, Long> {

    @EntityGraph(attributePaths = {"ingredient", "skinType"})
    List<IngredientSkinRule> findBySkinType_NameAndIngredient_NameIn(
            String skinTypeName,
            List<String> ingredientNames
    );
}