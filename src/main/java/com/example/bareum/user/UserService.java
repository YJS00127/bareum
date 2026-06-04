package com.example.bareum.user;

import com.example.bareum.diary.Diary;
import com.example.bareum.diary.DiaryRepository;
import com.example.bareum.user.dto.PasswordChangeRequest;
import com.example.bareum.user.dto.DeleteAccountRequest;
import com.example.bareum.user.dto.UserActionResponse;
import jakarta.transaction.Transactional;
import com.example.bareum.user.dto.LoginRequest;
import com.example.bareum.user.dto.LoginResponse;
import com.example.bareum.user.dto.SignupRequest;
import com.example.bareum.user.dto.SignupResponse;
import com.example.bareum.user.dto.SkinTypeUpdateRequest;
import com.example.bareum.user.dto.SkinTypeUpdateResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.security.crypto.password.PasswordEncoder;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final DiaryRepository diaryRepository;

    public SignupResponse signup(SignupRequest request) {
        if (userRepository.existsByLoginId((request.getLoginId()))) {
            return new SignupResponse(false, "이미 존재하는 아이디입니다.");
        }

        User user = User.builder()
            .loginId(request.getLoginId())
            .password(passwordEncoder.encode(request.getPassword()))
            .nickname(request.getNickname())
            .skinType(request.getSkinType())
            .build();

        userRepository.save(user);

        return new SignupResponse(true, "회원가입 성공");
    }

    public LoginResponse login(LoginRequest request) {
        User user = userRepository.findByLoginId(request.getLoginId())
                .orElse(null);
        if (user == null){
            return new LoginResponse(false, null);
        }

        if(!passwordEncoder.matches(request.getPassword(), user.getPassword())){
            return new LoginResponse(false, null);
        }

        LoginResponse.UserInfo userInfo = new LoginResponse.UserInfo(
                user.getId(),
                user.getLoginId(),
                user.getNickname(),
                user.getSkinType()
        );

        return new LoginResponse(true, userInfo);
    }

    public UserActionResponse changePassword(Long userId, PasswordChangeRequest request) {
        User user = userRepository.findById(userId)
                .orElse(null);

        if (user == null) {
            return new UserActionResponse(false);
        }

        if (!passwordEncoder.matches(request.getCurrentPassword(), user.getPassword())) {
            return new UserActionResponse(false);
        }

        user.setPassword(passwordEncoder.encode(request.getNewPassword()));

        userRepository.save(user);

        return new UserActionResponse(true);
    }

    @Transactional
    public UserActionResponse deleteAccount(Long userId, DeleteAccountRequest request) {
        User user = userRepository.findById(userId)
                .orElse(null);

        if (user == null) {
            return new UserActionResponse(false);
        }

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            return new UserActionResponse(false);
        }

        List<Diary> diaries = diaryRepository.findByUserId(userId);
        diaryRepository.deleteAll(diaries);

        userRepository.delete(user);

        return new UserActionResponse(true);
    }

    public SkinTypeUpdateResponse updateSkinType(Long userId, SkinTypeUpdateRequest request) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("사용자가 없습니다."));

        user.setSkinType(request.getSkinType());

        userRepository.save(user);

        SkinTypeUpdateResponse.UserInfo userInfo =
                new SkinTypeUpdateResponse.UserInfo(
                        user.getId(),
                        user.getLoginId(),
                        user.getNickname(),
                        user.getSkinType()
                );

        return new SkinTypeUpdateResponse(true, userInfo);
    }
}