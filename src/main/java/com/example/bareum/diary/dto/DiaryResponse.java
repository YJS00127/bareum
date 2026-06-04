package com.example.bareum.diary.dto;

import com.example.bareum.diary.Diary;
import lombok.AllArgsConstructor;
import lombok.Getter;
import java.time.LocalDate;
import java.util.List;

@Getter
@AllArgsConstructor
public class DiaryResponse {
    private Long diaryId;
    private Long userId;
    private LocalDate date;
    private Integer skinScore;
    private Integer stressScore;
    private Boolean hasTrouble;
    private String weather;
    private String memo;

    public static DiaryResponse from(Diary diary){
        return new DiaryResponse(
                diary.getId(),
                diary.getUser().getId(),
                diary.getDate(),
                diary.getSkinScore(),
                diary.getStressScore(),
                diary.getHasTrouble(),
                diary.getWeather(),
                diary.getMemo()
        );
    }
}
