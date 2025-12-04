package com.kmo.kome.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.kmo.kome.dto.request.UserLoginRequest;
import com.kmo.kome.dto.request.UserUpdatePasswordRequest;
import com.kmo.kome.dto.request.UserUpdateRequest;
import com.kmo.kome.dto.response.UserLoginResponse;
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
    UserLoginResponse login(UserLoginRequest request);

    UserInfoResponse getUserInfoById(Long currentUserId);

    UserInfoResponse updateUserInfoById(Long currentUserId, @Valid UserUpdateRequest request);

    void updateUserPasswordById(Long currentUserId, @Valid UserUpdatePasswordRequest request);
}
