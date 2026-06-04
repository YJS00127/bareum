package com.example.bareum.diary;

import com.example.bareum.diary.dto.DiaryRequest;
import com.example.bareum.diary.dto.DiaryResponse;
import com.example.bareum.user.User;
import com.example.bareum.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class DiaryService {

    private final DiaryRepository diaryRepository;
    private final UserRepository userRepository;

    public DiaryResponse saveDiary(DiaryRequest request) {
        User user = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new IllegalArgumentException("사용자가 없습니다."));

        Diary diary = diaryRepository
                .findByUserIdAndDate(request.getUserId(), request.getDate())
                .orElse(
                        Diary.builder()
                                .user(user)
                                .date(request.getDate())
                                .build()
                );

        diary.setSkinScore(request.getSkinScore());
        diary.setStressScore(request.getStressScore());
        diary.setHasTrouble(request.getHasTrouble());
        diary.setWeather(request.getWeather());
        diary.setMemo(request.getMemo());

        return DiaryResponse.from(diaryRepository.save(diary));
    }

    public List<DiaryResponse> getDiaries(Long userId) {
        return diaryRepository.findByUserId(userId)
                .stream()
                .map(DiaryResponse::from)
                .toList();
    }

    public DiaryResponse getDiaryByDate(Long userId, java.time.LocalDate date) {
        Diary diary = diaryRepository.findByUserIdAndDate(userId, date)
                .orElseThrow(() -> new IllegalArgumentException("해당 날짜의 다이어리가 없습니다."));

        return DiaryResponse.from(diary);
    }

    public void deleteDiary(Long diaryId) {
        diaryRepository.deleteById(diaryId);
    }
}