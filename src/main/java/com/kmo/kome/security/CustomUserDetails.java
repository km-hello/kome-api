package com.kmo.kome.security;

import com.kmo.kome.entity.User;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

/**
 * 自定义用户详情类，用于适配 Spring Security 的 UserDetails 接口。
 * 关联用户实体类 User，并提供用户信息供安全框架使用。
 */
@Getter
@RequiredArgsConstructor
public class CustomUserDetails implements UserDetails {

    private final User user;

    /**
     * 获取用户的唯一标识 ID。
     * 从关联的 User 对象中提取其主键值。
     *
     * @return 用户的唯一标识 ID。
     */
    public Long getId() {
        return user.getId();
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of();
    }

    @Override
    public String getPassword() {
        return user.getPassword();
    }

    @Override
    public String getUsername() {
        return user.getUsername();
    }
}
