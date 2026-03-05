package com.kmo.kome.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

/**
 *  Memo 实体类
 * 对应数据库表: memo
 * 用于存储和管理 Memo 的相关信息
 */
@Data
@TableName("memo")
public class Memo {
    /** 主键 ID */
    @TableId(type = IdType.AUTO)
    private Long id;

    /** 纯文本内容 */
    private String content;

    /** 是否置顶: 0=否, 1=是 */
    private Boolean isPinned;

    /** 状态: 0=草稿, 1=已发布 */
    private Integer status;

    /** 逻辑删除: 0=正常, 1=已删除 */
    @TableLogic
    private Boolean isDeleted;

    /** 创建时间 */
    private LocalDateTime createTime;

    /** 修改时间 */
    private LocalDateTime updateTime;
}
