package com.example.bareum.user.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor

public class LoginResponse {
    private Long userId;
    private String loginId;
    private String password;
    private String nickname;
    private String skinType;
}
