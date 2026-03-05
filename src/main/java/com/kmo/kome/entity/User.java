package com.kmo.kome.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.baomidou.mybatisplus.extension.handlers.JacksonTypeHandler;
import com.kmo.kome.dto.SkillItem;
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
    /** 主键 ID */
    @TableId(type = IdType.AUTO)
    private Long id;

    /** 登录账号 */
    private String username;

    /** 加密后的密码 (BCrypt) */
    private String password;

    /** 展示昵称 (可为空) */
    @TableField(updateStrategy = FieldStrategy.ALWAYS)
    private String nickname;

    /** 头像 URL (可为空) */
    @TableField(updateStrategy = FieldStrategy.ALWAYS)
    private String avatar;

    /** 联系邮箱 (可为空) */
    @TableField(updateStrategy = FieldStrategy.ALWAYS)
    private String email;

    /** 个人简介 (侧边栏展示, 可为空) */
    @TableField(updateStrategy = FieldStrategy.ALWAYS)
    private String description;

    /** 社交链接 JSON 数组 (可为空) */
    @TableField(updateStrategy = FieldStrategy.ALWAYS, typeHandler = JacksonTypeHandler.class)
    private List<SocialLink> socialLinks;

    /** 技能列表 JSON 数组 (可为空) */
    @TableField(updateStrategy = FieldStrategy.ALWAYS, typeHandler = JacksonTypeHandler.class)
    private List<SkillItem> skills;

    /** 是否为站点所有者: 0=否, 1=是 */
    private Boolean isOwner;

    /** 逻辑删除: 0=正常, 1=已删除 */
    @TableLogic
    private Boolean isDeleted;

    /** 创建时间 */
    private LocalDateTime createTime;

    /** 修改时间 */
    private LocalDateTime updateTime;
}
