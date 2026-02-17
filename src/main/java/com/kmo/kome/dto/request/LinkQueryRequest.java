package com.kmo.kome.dto.request;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 友链查询请求类。
 * 用于封装查询友链数据时的筛选条件。
 * 仅支持通过关键词进行筛选操作。
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class LinkQueryRequest extends BaseQueryRequest {
    // 关键词筛选
    private String keyword;
    // 按状态筛选
    private Integer status;
}
