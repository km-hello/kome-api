package com.kmo.kome.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 备忘录实体类
 * 对应数据库表: memo
 * 用于存储和管理备忘录的相关信息
 */
@Data
@TableName("memo")
public class Memo {
    @TableId(type = IdType.AUTO)
    private Long id;
    private String content;

    // 是否置顶: 1=是, 0=否, 默认0
    private Boolean isPinned;
    // 状态: 1=已发布, 0=草稿, 默认0
    private Integer status;
    @TableLogic
    private Boolean isDeleted;

    private LocalDateTime createTime;
    private LocalDateTime updateTime;
}
