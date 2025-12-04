package com.kmo.kome.controller;

import com.kmo.kome.common.Result;
import com.kmo.kome.dto.request.UserLoginRequest;
import com.kmo.kome.dto.request.UserUpdatePasswordRequest;
import com.kmo.kome.dto.request.UserUpdateRequest;
import com.kmo.kome.dto.response.UserLoginResponse;
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
    public Result<UserLoginResponse> login(@RequestBody UserLoginRequest request) {
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
    public Result<UserInfoResponse> updateUserInfoById(@AuthenticationPrincipal Long currentUserId, @Valid @RequestBody UserUpdateRequest request){
        return Result.success(userService.updateUserInfoById(currentUserId, request));
    }

    /**
     * 更新指定用户的密码。
     * 接收当前用户的 ID 和密码更新请求数据，调用服务层方法进行密码更新。
     *
     * @param currentUserId 当前登录用户的 ID，由认证框架自动解析。
     * @param request 更新密码请求参数，包含旧密码和新密码信息。
     * @return 一个空的 {@code Result<Void>} 对象，表示操作成功。
     */
    @PutMapping("/api/admin/user/password")
    public Result<Void> updateUserPasswordById(@AuthenticationPrincipal Long currentUserId, @Valid @RequestBody UserUpdatePasswordRequest request){
        userService.updateUserPasswordById(currentUserId, request);
        return Result.success();
    }
}
