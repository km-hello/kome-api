package com.kmo.kome.dto.request;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 标签查询请求类。
 * 用于封装查询标签列表时的分页信息。
 * 包括页码和每页数量的最小约束。
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class TagQueryRequest extends BaseQueryRequest {
    // 关键词筛选
    private String keyword;
}
