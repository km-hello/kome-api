package com.kmo.kome.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.baomidou.mybatisplus.extension.handlers.JacksonTypeHandler;
import com.kmo.kome.dto.SocialLink;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 用户实体类
 * 对应数据库表: user
 */
@Data
@TableName(value = "user", autoResultMap = true)
public class User {
    // 主键自增
    @TableId(type = IdType.AUTO)
    private Long id;

    // 登录用户名 (唯一)
    private String username;
    // 加密后的密码 (不要明文存储)
    private String password;

    private String nickname;
    private String avatar;
    private String email;
    private String description;

    // 社交链接 JSON 数组
    @TableField(typeHandler = JacksonTypeHandler.class)
    private List<SocialLink> socialLinks;

    // 是否为站点所有者: 1=是，0=否, 默认 0
    private Boolean isOwner;
    // 逻辑删除字段 (MyBatis Plus会自动处理)
    @TableLogic
    private Boolean isDeleted;

    private LocalDateTime createTime;
    private LocalDateTime updateTime;
}
