package com.kmo.kome.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 博客文章实体类
 * 对应数据库表: post
 * 用于存储和管理博客文章的相关信息
 */
@Data
@TableName("post")
public class Post {
    /** 主键 ID */
    @TableId(type = IdType.AUTO)
    private Long id;

    /** 文章标题 */
    private String title;

    /** 文章别名 */
    private String slug;

    /** 文章摘要 (可为空) */
    @TableField(updateStrategy = FieldStrategy.ALWAYS)
    private String summary;

    /** Markdown 原始内容 */
    private String content;

    /** 封面图 URL (可为空) */
    @TableField(updateStrategy = FieldStrategy.ALWAYS)
    private String coverImage;

    /** 阅读量 */
    private Integer views;

    /** 阅读耗时(分钟) */
    private Integer readTime;

    /** 状态: 0=草稿, 1=已发布 */
    private Integer status;

    /** 是否置顶: 0=否, 1=是 */
    private Boolean isPinned;

    /** 逻辑删除: 0=正常, 1=已删除 */
    @TableLogic
    private Boolean isDeleted;

    /** 创建时间 */
    private LocalDateTime createTime;

    /** 修改时间 */
    private LocalDateTime updateTime;
}
