package com.example.bareum.diary;

import com.example.bareum.diary.dto.DiaryRequest;
import com.example.bareum.diary.dto.DiaryResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/diaries")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:5173")
public class DiaryController {

    private final DiaryService diaryService;

    @PostMapping
    public DiaryResponse saveDiary(@RequestBody DiaryRequest request) {
        return diaryService.saveDiary(request);
    }

    @GetMapping("/{userId}")
    public List<DiaryResponse> getDiaries(@PathVariable Long userId) {
        return diaryService.getDiaries(userId);
    }

    @GetMapping("/{userId}/{date}")
    public DiaryResponse getDiaryByDate(
            @PathVariable Long userId,
            @PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date
    ) {
        return diaryService.getDiaryByDate(userId, date);
    }

    @DeleteMapping("/{diaryId}")
    public String deleteDiary(@PathVariable Long diaryId) {
        diaryService.deleteDiary(diaryId);
        return "삭제 성공";
    }
}