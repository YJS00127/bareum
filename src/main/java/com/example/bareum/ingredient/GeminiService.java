package com.example.bareum.ingredient;

import com.example.bareum.ingredient.dto.IngredientResult;
import com.google.genai.Client;
import com.google.genai.types.GenerateContentResponse;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GeminiService {

    private static final String MODEL_NAME = "gemini-2.5-flash";

    private final Client client = new Client();

    public String makeSummary(String skinType,
                              List<IngredientResult> results,
                              String productStatus){
        String prompt = buildPrompt(skinType, results, productStatus);

        try {
            GenerateContentResponse response =
                    client.models.generateContent(MODEL_NAME, prompt, null);

            String text = response.text();

            if(text == null || text.isBlank()){
                throw new RuntimeException("Gemini 응답이 비어있습니다.");
            }
            return text.trim();

        } catch(Exception e){
            throw new RuntimeException("Gemini API 호출 중 오류 발생", e);
        }
    }

    private String toKoreanStatus(String status) {
        if ("GOOD".equalsIgnoreCase(status)) {
            return "적합";
        }
        if ("CAUTION".equalsIgnoreCase(status)) {
            return "주의 필요";
        }
        return "보통";
    }

    private String buildPrompt(String skinType,
                               List<IngredientResult> results,
                               String productStatus) {

        StringBuilder sb = new StringBuilder();

        String productStatusKo = toKoreanStatus(productStatus);

        sb.append("너는 화장품 성분 분석 결과를 자연스러운 한국어 문장으로 정리하는 역할만 한다.\n");
        sb.append("절대로 새로운 성분 효능, 위험성, 이유, 함량, 농도를 추측하지 않는다.\n\n");

        sb.append("사용자 피부 타입: ").append(skinType).append("\n");
        sb.append("AI 모델의 제품 전체 판단 라벨: ").append(productStatus).append("\n");
        sb.append("제품 전체 판단의 한국어 표현: ").append(productStatusKo).append("\n\n");

        sb.append("라벨 표현 규칙:\n");
        sb.append("- GOOD은 '적합'이라고 표현한다.\n");
        sb.append("- NORMAL은 '보통'이라고 표현한다.\n");
        sb.append("- CAUTION은 '주의 필요'라고 표현한다.\n");
        sb.append("- GOOD/NORMAL/CAUTION을 한국어 동사처럼 쓰지 마라. 예: 'CAUTION합니다' 금지.\n");
        sb.append("- 성분 효능을 새로 설명하지 마라. 예: '글리세린은 보습 성분' 같은 표현 금지.\n");
        sb.append("- 성분 함량을 추측하지 마라. 예: '많이 들어있다', '적게 들어있다', '풍부하다' 금지.\n");
        sb.append("- 구매 조언, 생활 습관 조언, 사용 팁을 추가하지 마라.\n\n");

        sb.append("성분별 AI 모델 결과:\n");

        for (IngredientResult result : results) {
            sb.append("- ")
                    .append(result.getName())
                    .append(": ")
                    .append(result.getStatus())
                    .append(" / 한국어 표현: ")
                    .append(toKoreanStatus(result.getStatus()))
                    .append("\n");
        }

        sb.append("\n출력 조건:\n");
        sb.append("1. 3~5문장으로 작성한다.\n");
        sb.append("2. 제품 전체 판단을 먼저 말한다.\n");
        sb.append("3. 성분별 결과는 라벨 중심으로만 정리한다.\n");
        sb.append("4. 마지막 문장에는 '성분표만으로는 정확한 함량을 알 수 없으므로 참고용으로 확인해 주세요.'를 포함한다.\n\n");
        sb.append("원하는 출력 예시:\n");
        sb.append(skinType).append(" 피부 기준으로 이 제품의 전체 판단은 '")
                .append(productStatusKo).append("'입니다.\n");
        sb.append("성분별로는 GOOD, NORMAL, CAUTION으로 분류된 결과를 기준으로 확인할 수 있습니다.\n");
        sb.append("성분표만으로는 정확한 함량을 알 수 없으므로 참고용으로 확인해 주세요.\n");

        return sb.toString();
    }
}
