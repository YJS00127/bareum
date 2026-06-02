package com.example.bareum.ingredient;

import com.example.bareum.ingredient.dto.IngredientAnalyzeResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.http.MediaType;

@RestController
@RequestMapping("/api/ingredients")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:5173")
public class IngredientController {

    private final IngredientService ingredientService;

    @PostMapping(
            value = "/analyze",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE
    )
    public IngredientAnalyzeResponse analyze(
            @RequestParam("userId") Long userId,
            @RequestParam("image") MultipartFile image
    ) {
        return ingredientService.analyze(userId, image);
    }
}
