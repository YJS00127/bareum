package com.example.bareum.user;

import com.example.bareum.user.dto.LoginRequest;
import com.example.bareum.user.dto.LoginResponse;
import com.example.bareum.user.dto.SignupRequest;
import com.example.bareum.user.dto.SignupResponse;
import com.example.bareum.user.dto.SkinTypeUpdateRequest;
import com.example.bareum.user.dto.SkinTypeUpdateResponse;
import com.example.bareum.user.dto.UserActionResponse;
import com.example.bareum.user.dto.PasswordChangeRequest;
import com.example.bareum.user.dto.DeleteAccountRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:5173")
public class UserController {

    private final UserService userService;

    @PostMapping("/signup")
    public SignupResponse signup(@RequestBody SignupRequest request) {
        return userService.signup(request);
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest request) {
        LoginResponse response = userService.login(request);

        if (!response.isSuccess()) {
            return ResponseEntity
                    .status(HttpStatus.UNAUTHORIZED)
                    .body(response);
        }

        return ResponseEntity.ok(response);
    }

    @PostMapping("{userId}/skin-type")
    public SkinTypeUpdateResponse updateSkinType(
            @PathVariable Long userId,
            @RequestBody SkinTypeUpdateRequest request
    ) {
        return userService.updateSkinType(userId, request);
    }

    @PatchMapping("/{userId}/password")
    public ResponseEntity<UserActionResponse> changePassword(
            @PathVariable Long userId,
            @RequestBody PasswordChangeRequest request
    ) {
        UserActionResponse response = userService.changePassword(userId, request);

        if (!response.isSuccess()) {
            return ResponseEntity
                    .status(HttpStatus.UNAUTHORIZED)
                    .body(response);
        }

        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{userId}")
    public ResponseEntity<UserActionResponse> deleteAccount(
            @PathVariable Long userId,
            @RequestBody DeleteAccountRequest request
    ) {
        UserActionResponse response = userService.deleteAccount(userId, request);

        if (!response.isSuccess()) {
            return ResponseEntity
                    .status(HttpStatus.UNAUTHORIZED)
                    .body(response);
        }

        return ResponseEntity.ok(response);
    }
}