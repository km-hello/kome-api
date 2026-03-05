package com.kmo.kome.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 友链实体类
 * 对应数据库表: link
 * 用于存储和管理友链的相关信息
 */
@Data
@TableName("link")
public class Link {
    /** 主键ID */
    @TableId(type = IdType.AUTO)
    private Long id;

    /** 站点名称 */
    private String name;

    /** 站点链接 */
    private String url;

    /** 站点 Logo URL (可为空) */
    @TableField(updateStrategy = FieldStrategy.ALWAYS)
    private String avatar;

    /** 一句话描述 (可为空) */
    @TableField(updateStrategy = FieldStrategy.ALWAYS)
    private String description;

    /** 状态: 0=隐藏, 1=公开 */
    private Integer status;

    /** 创建时间 */
    private LocalDateTime createTime;

    /** 修改时间 */
    private LocalDateTime updateTime;
}
