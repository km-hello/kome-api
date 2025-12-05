package com.kmo.kome.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 博客文章与标签的关联实体类
 * 对应数据库表: post_tag
 * 用于表示博客文章与标签之间的多对多关系
 */
@Data
@TableName("post_tag")
public class PostTag {
    private Long postId;
    private Long tagId;

    private LocalDateTime createTime;
}
