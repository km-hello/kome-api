package com.kmo.kome.dto.request;

import jakarta.validation.constraints.Min;
import lombok.Data;

/**
 * 文章查询请求类。
 * 用于封装查询文章列表时的筛选条件。
 * 支持分页以及通过关键词、标签和状态过滤文章数据。
 */
@Data
public class PostQueryRequest {
    @Min(value = 1, message = "页码最小为1")
    private Integer pageNum = 1;

    @Min(value = 1, message = "每页数量最少为1")
    private Integer pageSize = 10;

    // 关键词筛选
    private String keyword;
    // 按 tag 筛选
    private Long tagId;
    // 按状态筛选
    private Integer status;
}
