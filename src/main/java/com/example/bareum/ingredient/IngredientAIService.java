package com.example.bareum.ingredient;

import com.example.bareum.ingredient.dto.IngredientAiRequest;
import com.example.bareum.ingredient.dto.IngredientAiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Service
@RequiredArgsConstructor
public class IngredientAiService {

    @Value("${ai.server.url}")
    private String aiServerUrl;

    private final RestTemplate restTemplate = new RestTemplate();

    public IngredientAiResponse analyze(List<String> ingredientNames, String skinType) {
        IngredientAiRequest request = new IngredientAiRequest(
                skinType,
                ingredientNames
        );

        IngredientAiResponse response = restTemplate.postForObject(
                aiServerUrl + "/predict",
                request,
                IngredientAiResponse.class
        );

        if (response == null) {
            throw new RuntimeException("AI 서버 응답이 비어 있습니다.");
        }

        if (response.getResults() == null) {
            throw new RuntimeException("AI 서버 성분 분석 결과가 비어 있습니다.");
        }

        if (response.getProductStatus() == null) {
            throw new RuntimeException("AI 서버 제품 전체 판단 결과가 비어 있습니다.");
        }

        return response;
    }
}