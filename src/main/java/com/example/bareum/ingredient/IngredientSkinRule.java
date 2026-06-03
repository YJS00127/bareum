package com.example.bareum.ingredient;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(
        name = "ingredient_skin_rules",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = {"ingredient_id", "skin_type_id"})
        },
        indexes = {
                @Index(name = "idx_rule_ingredient_skin", columnList = "ingredient_id, skin_type_id")
        }
)
@Getter
@Setter
@NoArgsConstructor
public class IngredientSkinRule {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ingredient_id", nullable = false)
    private Ingredient ingredient;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "skin_type_id", nullable = false)
    private SkinType skinType;

    @Column(nullable = false)
    private String label;
}