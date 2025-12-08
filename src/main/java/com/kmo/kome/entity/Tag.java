package com.kmo.kome.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 标签实体类
 * 对应数据库表: tag
 * 用于存储和管理标签的相关信息
 */
@Data
@TableName("tag")
public class Tag {
    @TableId(type = IdType.AUTO)
    private Long id;
    private String name;

    private LocalDateTime createTime;
    private LocalDateTime updateTime;
}
