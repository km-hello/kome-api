package com.kmo.kome.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.kmo.kome.common.ResultCode;
import com.kmo.kome.common.exception.ServiceException;
import com.kmo.kome.dto.request.LoginRequest;
import com.kmo.kome.dto.request.UpdatePasswordRequest;
import com.kmo.kome.dto.request.UpdateUserRequest;
import com.kmo.kome.dto.response.LoginResponse;
import com.kmo.kome.dto.response.UserInfoResponse;
import com.kmo.kome.entity.User;
import com.kmo.kome.mapper.UserMapper;
import com.kmo.kome.security.CustomUserDetails;
import com.kmo.kome.service.UserService;
import com.kmo.kome.utils.JwtUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.Optional;

/**
 * 用户业务实现类
 * ServiceImpl<Mapper, Entity> 是 MyBatis Plus 提供的基类，
 * 自动实现了 IService 中定义的所有基础方法。
 */
@Service
@RequiredArgsConstructor
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtUtils jwtUtils;

    // 过期时间 (毫秒)
    @Value("${jwt.expiration}")
    private long expiration;

    /**
     * 登录逻辑实现
     *
     * @param request 登录请求参数
     * @return 登录响应对象
     */
    @Override
    public LoginResponse login(LoginRequest request) {
        // 1. 调用 Spring Security 进行认证
        // 该方法会调用 UserDetailsServiceImpl 加载用户，并使用 PasswordEncoder 比对密码
        // 如果认证失败，会抛出 AuthenticationException
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword())
        );

        // 2. 【核心修改】直接从 Authentication 对象中获取用户信息，不再查询数据库
        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
        User user = userDetails.getUser();

        // 3. 【核心修改】使用 userId 生成 JWT Token
        String token = jwtUtils.generateToken(user.getId());

        // 4. 组装并返回 Response
        return LoginResponse.builder()
                .token(token)
                .username(user.getUsername())
                .nickname(user.getNickname())
                .expiresIn(expiration)
                .avatar(user.getAvatar())
                .build();
    }

    /**
     * 根据用户ID获取用户信息。
     * 如果用户ID为空或对应的用户不存在，将抛出业务异常。
     *
     * @param currentUserId 当前用户的ID，不允许为空。
     * @return 包含用户关键信息的响应对象 {@code UserInfoResponse}。
     * @throws ServiceException 当用户ID为空时抛出未授权异常；当未找到对应用户信息时抛出未找到异常。
     */
    @Override
    public UserInfoResponse getUserInfoById(Long currentUserId) {
        if(currentUserId == null){
            throw new ServiceException(ResultCode.UNAUTHORIZED.getCode(),"未登录");
        }

        User user = this.getById(currentUserId);

        if(user == null){
            throw new ServiceException(ResultCode.NOT_FOUND.getCode(), "未找到用户信息");
        }

        return UserInfoResponse
                .builder()
                .id(user.getId())
                .username(user.getUsername())
                .nickname(user.getNickname())
                .avatar(user.getAvatar())
                .email(user.getEmail())
                .description(user.getDescription())
                .build();
    }

    /**
     * 更新指定用户的基本信息。
     * 如果当前用户 ID 为空，抛出未登录异常。
     * 如果用户不存在或更新的用户名已被占用，将抛出相应的业务异常。
     *
     * @param currentUserId 当前用户的 ID，不允许为空。
     * @param updateUserRequest 包含更新数据的请求对象，可能包括用户名、昵称、头像、邮箱和描述等字段。
     * @return 更新后的用户信息响应对象 {@code UserInfoResponse}。
     * @throws ServiceException 当用户未登录、用户不存在或用户名已被占用时抛出相应的异常。
     */
    @Override
    public UserInfoResponse updateUserInfoById(Long currentUserId, UpdateUserRequest updateUserRequest) {
        // 前置检查
        if(currentUserId == null){
            throw new ServiceException(ResultCode.UNAUTHORIZED.getCode(),"未登录");
        }

        // 检查用户是否存在
        User user = this.getById(currentUserId);
        if(user == null){
            throw new ServiceException(ResultCode.NOT_FOUND.getCode(), "用户不存在");
        }

        // 检查用户名是否被占用
        String newUsername = updateUserRequest.getUsername();
        if(StringUtils.hasText(newUsername) && !newUsername.equals(user.getUsername())){
            QueryWrapper<User> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("username", newUsername)
                        .ne("id", currentUserId);
            if(this.count(queryWrapper) > 0){
                throw new ServiceException(ResultCode.FAILED.getCode(), "用户名已存在");
            }
            user.setUsername(newUsername);
        }

        // 设置其他需要更新的字段
        if(updateUserRequest.getNickname() != null){
            user.setNickname(updateUserRequest.getNickname());
        }

        if(updateUserRequest.getAvatar() != null){
            user.setAvatar(updateUserRequest.getAvatar());
        }

        if(updateUserRequest.getEmail() != null){
            user.setEmail(updateUserRequest.getEmail());
        }

        if(updateUserRequest.getDescription() != null){
            user.setDescription(updateUserRequest.getDescription());
        }

        this.updateById(user);

        return UserInfoResponse.builder()
                .id(user.getId())
                .username(user.getUsername())
                .nickname(user.getNickname())
                .avatar(user.getAvatar())
                .email(user.getEmail())
                .description(user.getDescription())
                .build();
    }

    /**
     * 根据用户 ID 更新用户密码。
     * 验证旧密码的正确性，并确保新密码与旧密码不同。
     * 如果验证失败或用户不存在，将抛出相应的业务异常。
     *
     * @param currentUserId 当前执行操作的用户 ID，不能为空，用于标识目标用户。
     * @param updatePasswordRequest 包含更新密码所需的旧密码和新密码的请求对象，不允许为空。
     * @throws ServiceException 当用户不存在、旧密码验证失败或新旧密码相同时抛出业务异常。
     */
    @Override
    public void updateUserPasswordById(Long currentUserId, UpdatePasswordRequest updatePasswordRequest) {
        // 1. 获取当前用户
        User user = Optional.ofNullable(this.getById(currentUserId))
                .orElseThrow(() -> new ServiceException(ResultCode.NOT_FOUND.getCode(), "用户状态异常"));
        // 2. 验证旧密码是否正确
        if(!passwordEncoder.matches(updatePasswordRequest.getOldPassword(), user.getPassword())){
            throw new ServiceException(ResultCode.FAILED.getCode(),"旧密码错误");
        }

        // 3. 检查新旧密码是否相同
        if(updatePasswordRequest.getNewPassword().equals(updatePasswordRequest.getOldPassword())){
            throw new ServiceException(ResultCode.FAILED.getCode(), "新密码不能与旧密码相同");
        }

        // 4. 新密码加密
        String encodePassword = passwordEncoder.encode(updatePasswordRequest.getNewPassword());

        // 5. 更新用户的新密码
        user.setPassword(encodePassword);
        this.updateById(user);
    }
}
