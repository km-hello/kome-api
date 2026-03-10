package com.kmo.kome.service.impl;

import com.kmo.kome.common.ResultCode;
import com.kmo.kome.common.exception.ServiceException;
import com.kmo.kome.dto.request.AuthLoginRequest;
import com.kmo.kome.dto.response.AuthInfoResponse;
import com.kmo.kome.dto.response.AuthLoginResponse;
import com.kmo.kome.entity.User;
import com.kmo.kome.security.CustomUserDetails;
import com.kmo.kome.service.AuthService;
import com.kmo.kome.service.UserService;
import com.kmo.kome.utils.JwtUtils;
import com.kmo.kome.utils.MessageHelper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

/**
 * 认证业务实现类。
 * <p>
 * 实现了 {@link AuthService} 接口，提供用户登录认证与当前认证信息获取功能。
 * 依赖 Spring Security 的 {@link AuthenticationManager} 进行凭据验证，
 * 并使用 {@link JwtUtils} 生成 JWT 令牌。
 */
@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    /** JWT 令牌类型前缀 */
    private static final String TOKEN_TYPE = "Bearer";

    private final AuthenticationManager authenticationManager;
    private final JwtUtils jwtUtils;
    private final UserService userService;
    private final MessageHelper messageHelper;

    /** JWT 令牌过期时间（毫秒），从配置文件 jwt.expiration 读取 */
    @Value("${jwt.expiration}")
    private long expirationMillis;

    /**
     * 用户登录并创建 JWT 会话。
     * <p>
     * 核心流程：
     * 1. 使用 {@link AuthenticationManager} 验证用户名和密码。
     * 2. 验证通过后，从 {@link CustomUserDetails} 中提取用户实体。
     * 3. 基于用户 ID 生成 JWT 令牌，并计算过期时间戳。
     * 4. 返回包含令牌和当前认证信息的响应对象。
     * <p>
     * 如果用户名或密码错误，{@link AuthenticationManager} 会抛出
     * {@code BadCredentialsException}，由 Spring Security 异常处理机制统一处理。
     *
     * @param request 登录请求参数，包含用户名和密码。
     * @return 登录响应，包含 accessToken、tokenType、expiresAt 和当前认证信息。
     */
    @Override
    public AuthLoginResponse login(AuthLoginRequest request) {
        // 委托 Spring Security 进行凭据验证（该方法会调用 UserDetailsServiceImpl 加载用户，并使用 PasswordEncoder 比对密码, 失败时自动抛出 AuthenticationException）
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword())
        );

        // 从已认证的 Principal 中提取用户实体, 不再查询数据库
        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
        User user = userDetails.user();

        // 构建登录响应：生成 JWT 令牌 + 计算过期时间 + 附带当前认证信息
        return AuthLoginResponse.builder()
                // 使用 userId 生成 JWT Token
                .accessToken(jwtUtils.generateToken(user.getId()))
                .tokenType(TOKEN_TYPE)
                .expiresAt(System.currentTimeMillis() + expirationMillis)
                .user(toAuthInfo(user))
                .build();
    }

    /**
     * 获取当前认证信息。
     * 根据用户 ID 查询用户实体，并转换为认证信息响应对象。
     *
     * @param currentUserId 当前登录用户 ID，不能为 null。
     * @return 当前认证信息。
     * @throws ServiceException 当用户 ID 为空或用户不存在时抛出未授权异常。
     */
    @Override
    public AuthInfoResponse getAuthInfo(Long currentUserId) {
        if (currentUserId == null) {
            throw new ServiceException(ResultCode.UNAUTHORIZED, messageHelper.get("error.auth.sessionExpired"));
        }

        User user = userService.getById(currentUserId);
        if (user == null) {
            throw new ServiceException(ResultCode.UNAUTHORIZED, messageHelper.get("error.auth.sessionExpired"));
        }

        return toAuthInfo(user);
    }

    /**
     * 将用户实体转换为认证信息响应对象。
     * 仅提取前端展示所需的基本字段（不含密码等敏感信息）。
     *
     * @param user 用户实体。
     * @return 认证信息响应对象。
     */
    private AuthInfoResponse toAuthInfo(User user) {
        return AuthInfoResponse.builder()
                .id(user.getId())
                .username(user.getUsername())
                .nickname(user.getNickname())
                .avatar(user.getAvatar())
                .email(user.getEmail())
                .build();
    }
}
