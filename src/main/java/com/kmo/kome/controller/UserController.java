package com.kmo.kome.controller;

import com.kmo.kome.common.Result;
import com.kmo.kome.dto.request.LoginRequest;
import com.kmo.kome.dto.request.UpdateUserRequest;
import com.kmo.kome.dto.response.LoginResponse;
import com.kmo.kome.dto.response.UserInfoResponse;
import com.kmo.kome.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

/**
 * 用户控制器类。
 * 负责处理与用户相关的请求，如登录、注册等操作。
 * 使用了 @RestController 注解标识为控制器，并通过 @RequiredArgsConstructor 自动生成构造函数注入服务对象。
 */
@RestController
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    /**
     * 处理用户登录请求。
     *
     * @param request 登录请求参数，包含用户名和密码信息。
     * @return 登录结果，包含 JWT Token、过期时间及用户基本信息。
     */
    @PostMapping("/api/user/login")
    public Result<LoginResponse> login(@RequestBody LoginRequest request) {
        return Result.success(userService.login(request));
    }

    /**
     * 根据用户 ID 获取用户详细信息。
     *
     * @param currentUserId 当前登录用户的 ID，通过认证注解自动解析。
     * @return 包含用户详细信息的响应结果，包括用户 ID、用户名、昵称、头像、邮箱和个人描述等。
     */
    @GetMapping("/api/admin/user")
    public Result<UserInfoResponse> getUserInfoById(@AuthenticationPrincipal Long currentUserId){
        return Result.success(userService.getUserInfoById(currentUserId));
    }

    @PutMapping("/api/admin/user")
    public Result<UserInfoResponse> updateUserInfoById(@AuthenticationPrincipal Long currentUserId, @Valid @RequestBody UpdateUserRequest updateUserRequest){
        return Result.success(userService.updateUserInfoById(currentUserId, updateUserRequest));
    }
}
