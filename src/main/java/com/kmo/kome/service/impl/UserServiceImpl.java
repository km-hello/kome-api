package com.kmo.kome.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.kmo.kome.common.ResultCode;
import com.kmo.kome.common.exception.ServiceException;
import com.kmo.kome.dto.request.UserLoginRequest;
import com.kmo.kome.dto.request.UserUpdatePasswordRequest;
import com.kmo.kome.dto.request.UserUpdateRequest;
import com.kmo.kome.dto.response.UserLoginResponse;
import com.kmo.kome.dto.response.UserInfoResponse;
import com.kmo.kome.entity.User;
import com.kmo.kome.mapper.UserMapper;
import com.kmo.kome.security.CustomUserDetails;
import com.kmo.kome.service.UserService;
import com.kmo.kome.utils.JwtUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;


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
    public UserLoginResponse login(UserLoginRequest request) {
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
        return UserLoginResponse.builder()
                .token(token)
                .expiresIn(expiration)
                .username(user.getUsername())
                .nickname(user.getNickname())
                .email(user.getEmail())
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
        // 前置检查
        User user = checkAndGetUser(currentUserId);

        return UserInfoResponse
                .builder()
                .id(user.getId())
                .username(user.getUsername())
                .nickname(user.getNickname())
                .avatar(user.getAvatar())
                .email(user.getEmail())
                .description(user.getDescription())
                .socialLinks(user.getSocialLinks())
                .build();
    }

    /**
     * 根据用户 ID 更新用户信息。
     * 验证用户名和邮箱的唯一性，根据请求数据更新用户基本信息，并返回更新后的用户信息。
     * 如果用户 ID 无效或请求数据不合法，将抛出业务异常。
     *
     * @param currentUserId 当前执行操作的用户 ID，不能为 null。
     *        用于标识待更新的目标用户。
     * @param request 包含更新用户信息所需数据的请求对象，不能为 null。
     *        包括用户名、昵称、邮箱等多个字段的更新内容。
     * @return 包含用户更新后关键信息的响应对象 {@code UserInfoResponse}。
     * @throws ServiceException 当以下情况之一发生时抛出业务异常：
     *         1. 用户 ID 无效或不存在。
     *         2. 待更新的用户名或邮箱已被占用。
     */
    @Override
    public UserInfoResponse updateUserInfoById(Long currentUserId, UserUpdateRequest request) {
        // 前置检查
        User user = checkAndGetUser(currentUserId);

        // 检查用户名是否被占用
        String newUsername = request.getUsername();
        if(StringUtils.hasText(newUsername) && !newUsername.equals(user.getUsername())){
            boolean isUsernameTaken = lambdaQuery()
                    .eq(User::getUsername, newUsername)
                    .ne(User::getId, user.getId())
                    .exists();
            if(isUsernameTaken){
                throw new ServiceException(ResultCode.BAD_REQUEST, "用户名已被占用");
            }
        }

        // 检查邮箱是否被占用
        String newEmail = request.getEmail();
        if(StringUtils.hasText(newEmail) && !newEmail.equals(user.getEmail())){
            boolean isEmailTaken  = lambdaQuery()
                    .eq(User::getEmail, newEmail)
                    .ne(User::getId, user.getId())
                    .exists();
            if(isEmailTaken){
                throw new ServiceException(ResultCode.BAD_REQUEST, "邮箱已被占用");
            }
        }

        // 创建一个新的实体用于更新，只包含ID和需要更新的字段
        User updateUser = new User();
        updateUser.setId(user.getId());

        // 将 request 中的非空属性拷贝到 userToUpdate
        BeanUtils.copyProperties(request, updateUser);

        // 执行更新
        // MyBatis Plus 的 updateById 会忽略 userToUpdate 中的 null 字段
        updateById(updateUser);

        // 返回更新后的用户信息
        User updatedUser = getById(currentUserId);
        UserInfoResponse response = new UserInfoResponse();
        BeanUtils.copyProperties(updatedUser, response);

        return response;
    }

    /**
     * 根据用户 ID 更新用户密码。
     * 验证旧密码的正确性，并确保新密码与旧密码不同。
     * 如果验证失败或用户不存在，将抛出相应的业务异常。
     *
     * @param currentUserId 当前执行操作的用户 ID，不能为空，用于标识目标用户。
     * @param request 包含更新密码所需的旧密码和新密码的请求对象，不允许为空。
     * @throws ServiceException 当用户不存在、旧密码验证失败或新旧密码相同时抛出业务异常。
     */
    @Override
    public void updateUserPasswordById(Long currentUserId, UserUpdatePasswordRequest request) {
        // 前置检查
        User user = checkAndGetUser(currentUserId);
        // 验证旧密码是否正确
        if(!passwordEncoder.matches(request.getOldPassword(), user.getPassword())){
            throw new ServiceException(ResultCode.BAD_REQUEST,"旧密码错误");
        }

        // 检查新旧密码是否相同
        if(request.getNewPassword().equals(request.getOldPassword())){
            throw new ServiceException(ResultCode.BAD_REQUEST, "新密码不能与旧密码相同");
        }

        // 新密码加密
        String encodePassword = passwordEncoder.encode(request.getNewPassword());

        // 使用 Wrappers 工厂类，并只更新 password 字段
        update(Wrappers.<User>lambdaUpdate()
                .eq(User::getId, currentUserId)
                .set(User::getPassword, encodePassword)
        );
    }

    /**
     * 检查用户 ID 是否有效，并返回对应的用户信息。
     * 如果用户 ID 为空或用户不存在，将抛出自定义业务异常。
     *
     * @param currentUserId 当前用户的 ID，不能为 null。
     * @return 对应的用户实体对象 {@code User}。
     * @throws ServiceException 当用户 ID 为空或用户不存在时抛出异常。
     */
    private User checkAndGetUser(Long currentUserId) {
        // 前置检查
        if(currentUserId == null){
            throw new ServiceException(ResultCode.UNAUTHORIZED, "无效的身份凭证");
        }

        // 检查用户是否存在
        User user = getById(currentUserId);
        if(user == null){
            throw new ServiceException(ResultCode.UNAUTHORIZED, "用户不存在或已注销");
        }
        return user;
    }
}
