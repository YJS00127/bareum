package com.example.bareum.user.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PasswordChangeRequest {
    private String newPassword;
}