package com.example.bareum.user.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter

public class SignupRequest {
    private String loginId;
    private String password;
    private String userName;
    private String skinType;
}
