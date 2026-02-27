package com.kmo.kome.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.kmo.kome.common.exception.ServiceException;
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
     * 用户登录。
     * 通过 Spring Security 进行认证，认证成功后生成 JWT Token 并返回用户信息。
     *
     * @param request 登录请求参数，包含用户名和密码。
     * @return 包含 Token 和用户基本信息的登录响应对象。
     */
    UserLoginResponse login(@Valid UserLoginRequest request);

    /**
     * 根据用户 ID 获取用户信息。
     *
     * @param currentUserId 当前用户的 ID。
     * @return 包含用户关键信息的响应对象。
     * @throws ServiceException 当用户 ID 为空或用户不存在时抛出。
     */
    UserInfoResponse getUserInfoById(Long currentUserId);

    /**
     * 根据用户 ID 更新用户信息。
     * 验证用户名和邮箱的唯一性后执行更新。
     *
     * @param currentUserId 当前用户的 ID。
     * @param request 包含更新信息的请求对象。
     * @return 更新后的用户信息响应对象。
     * @throws ServiceException 当用户不存在或用户名、邮箱已被占用时抛出。
     */
    UserInfoResponse updateUserInfoById(Long currentUserId, @Valid UserUpdateRequest request);

    /**
     * 根据用户 ID 更新用户密码。
     * 验证旧密码正确性，并确保新密码与旧密码不同。
     *
     * @param currentUserId 当前用户的 ID。
     * @param request 包含旧密码和新密码的请求对象。
     * @throws ServiceException 当用户不存在、旧密码错误或新旧密码相同时抛出。
     */
    void updateUserPasswordById(Long currentUserId, @Valid UserUpdatePasswordRequest request);
}
