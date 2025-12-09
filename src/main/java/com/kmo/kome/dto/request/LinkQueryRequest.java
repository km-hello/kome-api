package com.kmo.kome.dto.request;

import jakarta.validation.constraints.Min;
import lombok.Data;


/**
 * 友链查询请求类。
 * 用于封装查询友链数据时的筛选条件。
 * 仅支持通过关键词进行筛选操作。
 */
@Data
public class LinkQueryRequest {
    @Min(value = 1, message = "页码最小为1")
    private Integer pageNum = 1;

    @Min(value = 1, message = "每页数量最少为1")
    private Integer pageSize = 10;

    // 关键词筛选
    private String keyword;
}
