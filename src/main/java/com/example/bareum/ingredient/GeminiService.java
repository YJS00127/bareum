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
                              String productStatus) {
        String prompt = buildPrompt(skinType, results, productStatus);

        try {
            GenerateContentResponse response =
                    client.models.generateContent(MODEL_NAME, prompt, null);

            String text = response.text();

            if (text == null || text.isBlank()) {
                throw new RuntimeException("Gemini 응답이 비어있습니다.");
            }

            return text.trim();

        } catch (Exception e) {
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

        List<String> goodIngredients = results.stream()
                .filter(result -> "GOOD".equalsIgnoreCase(result.getStatus()))
                .map(IngredientResult::getName)
                .toList();

        List<String> cautionIngredients = results.stream()
                .filter(result -> "CAUTION".equalsIgnoreCase(result.getStatus()))
                .map(IngredientResult::getName)
                .toList();

        String goodIngredientText = toQuotedText(goodIngredients);
        String cautionIngredientText = toQuotedText(cautionIngredients);

        sb.append("너는 화장품 성분 분석 결과를 자연스러운 한국어 문장으로 정리하는 역할만 한다.\n");
        sb.append("제공된 피부타입, 제품 전체 판단, 성분별 라벨 결과만 사용한다.\n");
        sb.append("성분의 효능, 위험성, 이유, 함량, 농도를 새로 추측하지 않는다.\n");
        sb.append("'CAUTION으로 분류된', 'GOOD으로 분류된'이라는 표현을 쓰지 않는다.\n");
        sb.append("'함량', '농도', '많이', '적게', '풍부하게' 같은 표현을 쓰지 않는다.\n\n");

        sb.append("사용자 피부 타입: ").append(skinType).append("\n");
        sb.append("제품 전체 판단 라벨: ").append(productStatus).append("\n");
        sb.append("제품 전체 판단 한국어 표현: ").append(productStatusKo).append("\n\n");

        sb.append("GOOD 성분 목록: ");
        if (goodIngredients.isEmpty()) {
            sb.append("없음\n");
        } else {
            sb.append(goodIngredientText).append("\n");
        }

        sb.append("CAUTION 성분 목록: ");
        if (cautionIngredients.isEmpty()) {
            sb.append("없음\n");
        } else {
            sb.append(cautionIngredientText).append("\n");
        }

        sb.append("\n출력 규칙:\n");
        sb.append("1. 반드시 3문장으로 작성한다.\n");
        sb.append("2. 첫 문장은 피부타입과 제품 전체 판단을 말한다.\n");
        sb.append("3. productStatus가 CAUTION이면 CAUTION 성분 목록의 성분명을 사용해서 두 번째 문장을 작성한다.\n");
        sb.append("4. productStatus가 GOOD이면 GOOD 성분 목록의 성분명을 사용해서 두 번째 문장을 작성한다.\n");
        sb.append("5. productStatus가 NORMAL이면 전체적으로 보통으로 판단되었다고 작성한다.\n");
        sb.append("6. 'CAUTION으로 분류된', 'GOOD으로 분류된'이라는 표현은 절대 쓰지 않는다.\n");
        sb.append("7. 마지막 문장은 반드시 '성분표만으로는 정확한 함량을 알 수 없으므로 참고용으로 확인해 주세요.'로 작성한다.\n\n");

        sb.append("출력 예시:\n");

        if ("CAUTION".equalsIgnoreCase(productStatus)) {
            sb.append(skinType).append(" 피부 기준으로 이 제품은 '주의 필요'로 판단되었습니다.\n");
            sb.append(cautionIngredientText).append(" 성분이 포함되어 있어 사용 시 주의가 필요합니다.\n");
        } else if ("GOOD".equalsIgnoreCase(productStatus)) {
            sb.append(skinType).append(" 피부 기준으로 이 제품은 '적합'으로 판단되었습니다.\n");
            sb.append(goodIngredientText).append(" 성분이 포함되어 있어 사용자 피부 타입에 비교적 적합한 제품으로 볼 수 있습니다.\n");
        } else {
            sb.append(skinType).append(" 피부 기준으로 이 제품은 '보통'으로 판단되었습니다.\n");
            sb.append("특별히 강한 주의 또는 적합 판단에 해당하는 성분 비율이 높지 않아 보통으로 확인됩니다.\n");
        }
        sb.append("성분표만으로는 정확한 함량을 알 수 없으므로 참고용으로 확인해 주세요.\n");

        return sb.toString();
    }

    private String toQuotedText(List<String> ingredientNames) {
        return ingredientNames.stream()
                .map(name -> "'" + name + "'")
                .reduce((a, b) -> a + ", " + b)
                .orElse("");
    }
}