package com.example.bareum.ingredient;

import com.example.bareum.ingredient.dto.IngredientAnalyzeResponse;
import com.example.bareum.ingredient.dto.IngredientResult;
import com.google.genai.Client;
import com.google.genai.types.GenerateContentResponse;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GeminiService {

    private static final String MODEL_NAME = "gemini-2.5-flash";

    private final Client client = new Client();

    public String makeSummary(String skinType, List<IngredientResult> results){
        String prompt = buildPrompt(skinType, results);

        try {
            GenerateContentResponse response =
                    client.models.generateContent(MODEL_NAME, prompt, null);

            String text = response.text();

            if(text == null || text.isBlank()){
                return makeFallbackSummary(skinType, results);
            }

            return text.trim();
        } catch(Exception e){
            return makeFallbackSummary(skinType, results);
        }

    }

    private String buildPrompt(String skinType, List<IngredientResult> results){
        StringBuilder sb = new StringBuilder();

        sb.append("너는 친근한 화장품 성분 전문가야.\n");
        sb.append("사용자의 피부 타입은 ").append(skinType).append("이야.\n\n");

        sb.append("아래는 AI/분석 로직이 이미 판단한 성분별 결과야.\n");
        sb.append("너는 이 결과를 바탕으로 사용자가 이해하기 쉬운 설명문을 작성해줘.\n\n");

        sb.append("작성 조건:\n");
        sb.append("1. 친근하지만 전문가처럼 설명해줘.\n");
        sb.append("2. 없는 효능이나 없는 위험성을 새로 만들지 마.\n");
        sb.append("3. 의학적 진단처럼 단정하지 마.\n");
        sb.append("4. 주의 성분이 있으면 먼저 알려줘.\n");
        sb.append("5. 사용자가 이해하기 쉽게 3~6문장 정도로 정리해줘.\n");
        sb.append("6. 마지막에는 간단한 사용 팁을 덧붙여줘.\n\n");

        sb.append("성분 분석 결과:\n");

        for(IngredientResult result : results){
            sb.append("성분명 : ").append(result.getName()).append("\n");
            sb.append("상태 : ").append(result.getStatus()).append("\n");
            sb.append("이유 : ").append(result.getReason()).append("\n");
        }

        return sb.toString();
    }
    private String makeFallbackSummary(String skinType, List<IngredientResult> results) {
        boolean hasCaution = results.stream()
                .anyMatch(result ->
                        "CAUTION".equalsIgnoreCase(result.getStatus())
                                || "DANGER".equalsIgnoreCase(result.getStatus())
                );

        if (hasCaution) {
            return "사용자님의 피부 타입은 " + skinType + "입니다. "
                    + "분석 결과 일부 성분은 피부 상태에 따라 주의가 필요할 수 있습니다. "
                    + "처음 사용할 때는 소량으로 테스트해보고, 자극이 느껴지면 사용을 중단하는 것이 좋습니다.";
        }

        return "사용자님의 피부 타입은 " + skinType + "입니다. "
                + "분석 결과 특별히 주의가 필요한 성분은 크게 보이지 않습니다. "
                + "다만 개인마다 피부 반응은 다를 수 있으므로 처음 사용할 때는 가볍게 테스트해보는 것이 좋습니다.";
    }
}
