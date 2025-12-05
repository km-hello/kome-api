package com.kmo.kome.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
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
    @TableId(type = IdType.AUTO)
    private Long id;
    private String title;
    private String slug;
    private String summary;
    private String content;
    private String coverImage;
    private Integer views;
    private Integer readTime;

    // 状态: 1=已发布, 0=草稿, 默认0
    private Integer status;
    // 是否置顶: 1=是, 0=否, 默认0
    private Boolean isPinned;
    // 逻辑删除
    @TableLogic
    private Boolean isDeleted;

    private LocalDateTime createTime;
    private LocalDateTime updateTime;
}
