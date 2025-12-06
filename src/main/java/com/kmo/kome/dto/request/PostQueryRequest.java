package com.kmo.kome.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

/**
 * 查询文章请求类。
 * 用于封装文章查询时提交的参数。
 * 包括分页参数（当前页码和每页数量）、关键词筛选、标签筛选及状态筛选等字段。
 * 主要应用于文章列表的查询操作请求中。
 */
@Data
public class PostQueryRequest {
    @NotNull(message = "当前页码不能为空")
    @Min(value = 1, message = "页码最小为1")
    private Integer pageNum;

    @NotNull(message = "每页数量不能为空")
    @Min(value = 1, message = "每页数量最少为1")
    private Integer pageSize;

    // 关键词筛选
    private String keyword;
    // 按 tag 筛选
    private Long tagId;
    // 按状态筛选
    private Integer status;
}
