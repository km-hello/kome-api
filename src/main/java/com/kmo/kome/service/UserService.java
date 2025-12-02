package com.kmo.kome.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.kmo.kome.dto.request.LoginRequest;
import com.kmo.kome.dto.request.UpdatePasswordRequest;
import com.kmo.kome.dto.request.UpdateUserRequest;
import com.kmo.kome.dto.response.LoginResponse;
import com.kmo.kome.dto.response.UserInfoResponse;
import com.kmo.kome.entity.User;
import jakarta.validation.Valid;

/**
 * 用户业务接口
 * <p>
 * 继承 IService<User> 获得基础 CRUD 能力，并扩展自定义业务方法。
 */
public interface UserService extends IService<User> {

    /**
     * 处理用户登录业务
     *
     * @param request 登录请求参数
     * @return 登录成功后的响应数据 (Token等)
     */
    LoginResponse login(LoginRequest request);

    UserInfoResponse getUserInfoById(Long currentUserId);

    UserInfoResponse updateUserInfoById(Long currentUserId, @Valid UpdateUserRequest updateUserRequest);

    void updateUserPasswordById(Long currentUserId, @Valid UpdatePasswordRequest updatePasswordRequest);
}
