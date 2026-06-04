package com.example.bareum.diary.dto;

import lombok.Getter;
import lombok.Setter;
import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
public class DiaryRequest {
    private Long userId;
    private LocalDate date;
    private Integer skinScore;
    private Integer stressScore;
    private Boolean hasTrouble;
    private String weather;
    private String memo;
}
