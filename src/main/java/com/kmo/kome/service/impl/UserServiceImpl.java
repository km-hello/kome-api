package com.kmo.kome.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.kmo.kome.dto.request.LoginRequest;
import com.kmo.kome.dto.response.LoginResponse;
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
import org.springframework.stereotype.Service;

/**
 * 用户业务实现类
 * ServiceImpl<Mapper, Entity> 是 MyBatis Plus 提供的基类，
 * 自动实现了 IService 中定义的所有基础方法。
 */
@Service
@RequiredArgsConstructor
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

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
}
