package com.example.bareum.diary;

import com.example.bareum.diary.dto.DiaryRequest;
import com.example.bareum.diary.dto.DiaryResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/diaries")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:5173")
public class DiaryController {

    private final DiaryService diaryService;

    @PostMapping
    public DiaryResponse createDiary(@RequestBody DiaryRequest request){
        return diaryService.createDiary(request);
    }

    @GetMapping("/{userId}")
    public List<DiaryResponse> getDiaries(@PathVariable long userId){
        return diaryService.getDiaries(userId);
    }

    @DeleteMapping("/{diaryId}")
    public String deleteDiary(@PathVariable Long diaryId){
        diaryService.deleteDiary(diaryId);
        return "삭제 성공";
    }

}
