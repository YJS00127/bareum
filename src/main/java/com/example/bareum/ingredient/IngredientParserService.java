package com.example.bareum.ingredient;

import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class IngredientParserService {

    public List<String> parse(String text) {
        String cleanedText = preprocess(text);

        return Arrays.stream(cleanedText.split("[,\\n/·ㆍ]+"))
                .map(String::trim)
                .filter(s -> !s.isBlank())
                .filter(s -> s.length() >= 2)
                .filter(s -> !isUnnecessaryText(s))
                .distinct()
                .collect(Collectors.toList());
    }

    private String preprocess(String text) {
        if (text == null) {
            return "";
        }

        return text
                .replace("\r", "\n")
                .replace(":", ",")
                .replace(";", ",")
                .replace("，", ",")
                .replace("ㆍ", ",")
                .replace("·", ",")
                .replace("/", ",")
                .replace("[", " ")
                .replace("]", " ")
                .replace("(", " ")
                .replace(")", " ")
                .replaceAll("\\s+", " ")
                .trim();
    }

    private boolean isUnnecessaryText(String text) {
        return text.contains("전성분")
                || text.contains("사용")
                || text.contains("주의")
                || text.contains("화장품")
                || text.contains("용량")
                || text.contains("제조")
                || text.contains("판매")
                || text.contains("책임")
                || text.contains("고객")
                || text.contains("문의")
                || text.contains("보관")
                || text.contains("개봉")
                || text.contains("기한");
    }
}