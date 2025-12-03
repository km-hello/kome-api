package com.kmo.kome.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
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
        User user = checkAndGetUser(currentUserId);

        // 检查用户名是否被占用
        String newUsername = updateUserRequest.getUsername();
        if(StringUtils.hasText(newUsername) && !newUsername.equals(user.getUsername())){
            boolean isUsernameTaken = this.lambdaQuery()
                    .eq(User::getUsername, newUsername)
                    .ne(User::getId, user.getId())
                    .exists();
            if(isUsernameTaken){
                throw new ServiceException(ResultCode.BAD_REQUEST, "用户名已存在");
            }
        }

        // 创建一个新的实体用于更新，只包含ID和需要更新的字段
        User updateUser = new User();
        updateUser.setId(user.getId());

        // 将 updateUserRequest 中的非空属性拷贝到 userToUpdate
        BeanUtils.copyProperties(updateUserRequest, updateUser);

        // 执行更新
        // MyBatis Plus 的 updateById 会忽略 userToUpdate 中的 null 字段
        this.updateById(updateUser);

        // 返回更新后的用户信息
        User updatedUser = this.getById(currentUserId);
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
     * @param updatePasswordRequest 包含更新密码所需的旧密码和新密码的请求对象，不允许为空。
     * @throws ServiceException 当用户不存在、旧密码验证失败或新旧密码相同时抛出业务异常。
     */
    @Override
    public void updateUserPasswordById(Long currentUserId, UpdatePasswordRequest updatePasswordRequest) {
        // 前置检查
        User user = checkAndGetUser(currentUserId);
        // 验证旧密码是否正确
        if(!passwordEncoder.matches(updatePasswordRequest.getOldPassword(), user.getPassword())){
            throw new ServiceException(ResultCode.BAD_REQUEST,"旧密码错误");
        }

        // 检查新旧密码是否相同
        if(updatePasswordRequest.getNewPassword().equals(updatePasswordRequest.getOldPassword())){
            throw new ServiceException(ResultCode.BAD_REQUEST, "新密码不能与旧密码相同");
        }

        // 新密码加密
        String encodePassword = passwordEncoder.encode(updatePasswordRequest.getNewPassword());

        // 使用 Wrappers 工厂类，并只更新 password 字段
        this.update(Wrappers.<User>lambdaUpdate()
                .eq(User::getId, currentUserId)
                .set(User::getPassword, encodePassword)
        );
    }

    /**
     * 检查用户ID是否合法并获取对应的用户信息。
     * 如果用户ID为空或对应的用户不存在，则抛出业务异常。
     *
     * @param currentUserId 当前用户的ID，不允许为 null。
     * @return 存在的用户对象 {@code User}。
     * @throws ServiceException 当用户ID为空时抛出未授权异常（401）；当未找到对应用户时抛出未找到异常（404）。
     */
    private User checkAndGetUser(Long currentUserId) {
        // 前置检查
        if(currentUserId == null){
            throw new ServiceException(ResultCode.UNAUTHORIZED);
        }

        // 检查用户是否存在
        User user = this.getById(currentUserId);
        if(user == null){
            throw new ServiceException(ResultCode.NOT_FOUND);
        }
        return user;
    }
}
