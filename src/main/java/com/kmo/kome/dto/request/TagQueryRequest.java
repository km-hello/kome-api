package com.kmo.kome.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

/**
 * 标签查询请求类。
 * 用于封装查询标签列表时的分页信息。
 * 包括页码和每页数量的最小约束。
 */
@Data
public class TagQueryRequest {
    @NotNull(message = "页码不能为空")
    @Min(value = 1, message = "页码最小为1")
    private Integer pageNum = 1;

    @NotNull(message = "每页数量不能为空")
    @Min(value = 1, message = "每页数量最少为1")
    private Integer pageSize = 10;

    // 关键词筛选
    private String keyword;
}
