package com.kmo.kome.controller;

import com.kmo.kome.dto.request.LoginRequest;
import com.kmo.kome.dto.response.LoginResponse;
import com.kmo.kome.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * 用户登录、个人信息相关 API
 */
@RestController
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    /**
     * Handles user login requests.
     *
     * @param request the {@link LoginRequest} object containing the username and password for login
     * @return a {@link ResponseEntity} containing the {@link LoginResponse}, which includes the authentication token and user information
     */
    @PostMapping("/api/user/login")
    public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest request) {
        return ResponseEntity.ok(userService.login(request));
    }
}
