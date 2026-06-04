package com.example.bareum.user.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class SkinTypeUpdateResponse {
    private boolean success;
    private UserInfo user;

    @Getter
    @AllArgsConstructor
    public static class UserInfo {
        private Long userId;
        private String loginId;
        private String nickname;
        private String skinType;
    }
}