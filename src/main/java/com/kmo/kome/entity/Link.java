package com.kmo.kome.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
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
    @TableId(type = IdType.AUTO)
    private Long id;
    private String name;
    private String url;
    private String avatar;
    private String description;

    // 状态: 1=公开, 0=隐藏, 默认0
    private Integer status;

    private LocalDateTime createTime;
    private LocalDateTime updateTime;
}
