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

    public DiaryResponse createDiary(DiaryRequest request){
        User user = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new IllegalArgumentException("사용자가 없습니다."));

        Diary diary = Diary.builder()
                .user(user)
                .date(request.getDate())
                .skinScore(request.getSkinScore())
                .stressScore(request.getStressScore())
                .hasTrouble(request.getHasTrouble())
                .weather(request.getWeather())
                .memo(request.getMemo())
                .productsNames(request.getProductNames())
                .build();

        return DiaryResponse.from(diaryRepository.save(diary));
    }

    public List<DiaryResponse> getDiaries(Long userId){
        return diaryRepository.findByUserId(userId)
                .stream()
                .map(DiaryResponse::from)
                .toList();
    }

    public void deleteDiary(Long diaryId){
        diaryRepository.deleteById(diaryId);
    }
}
