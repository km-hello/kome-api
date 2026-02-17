package com.kmo.kome.dto.request;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 *  Memo 查询请求类。
 * 用于封装查询 Memo 列表时的筛选条件和分页信息。
 * 支持通过关键词和状态进行筛选，同时具备分页功能。
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class MemoQueryRequest extends BaseQueryRequest {
    // 关键词筛选
    private String keyword;
    // 按状态筛选
    private Integer status;

    // 是否忽略置顶排序（仅管理员接口使用）
    private Boolean ignorePinned = false;
}
