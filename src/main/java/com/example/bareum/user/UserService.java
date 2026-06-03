package com.example.bareum.user;

import com.example.bareum.user.dto.LoginRequest;
import com.example.bareum.user.dto.LoginResponse;
import com.example.bareum.user.dto.SignupRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.security.crypto.password.PasswordEncoder;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public String signup(SignupRequest request) {
        if (userRepository.existsByLoginId((request.getLoginId()))) {
            return "이미 존재하는 아이디입니다.";
        }

        User user = User.builder()
            .loginId(request.getLoginId())
            .password(passwordEncoder.encode(request.getPassword()))
            .nickname(request.getNickname())
            .skinType(request.getSkinType())
            .build();

        userRepository.save(user);

        return "회원가입 성공";
    }
    public LoginResponse login(LoginRequest request) {
        User user = userRepository.findByLoginId(request.getLoginId())
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 아이디입니다."));


        if(!passwordEncoder.matches(request.getPassword(), user.getPassword())){
            throw new IllegalArgumentException("비밀번호가 틀렸습니다.");
        }

        return new LoginResponse(
                user.getId(),
                user.getLoginId(),
                user.getPassword(),
                user.getNickname(),
                user.getSkinType()
        );
    }
}
