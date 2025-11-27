package com.kmo.kome.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.kmo.kome.entity.User;
import com.kmo.kome.mapper.UserMapper;
import com.kmo.kome.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collections;

/**
 * Spring Security 用户详情加载服务
 * <p>
 * 实现 UserDetailsService 接口，用于在认证过程中从数据库查找用户信息。
 */
@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UserMapper userMapper;

    /**
     * 根据用户名加载用户
     *
     * @param username 前端提交的用户名
     * @return Spring Security 要求的 UserDetails 对象
     * @throws UsernameNotFoundException 如果用户不存在
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // 1. 查询数据库
        User user = userMapper.selectOne(new LambdaQueryWrapper<User>()
                .eq(User::getUsername, username));

        // 2. 检查用户是否存在
        if (user == null) {
            // 默认情况下 Spring Security 会将此异常转换为 BadCredentialsException (为了安全，防枚举)
            throw new UsernameNotFoundException("用户不存在");
        }

        // 3. 转换为 UserDetails 对象
        return new org.springframework.security.core.userdetails.User(
                user.getUsername(),
                user.getPassword(), // 数据库中的密文
                Collections.emptyList() // 权限列表（暂为空）
        );
    }
}
