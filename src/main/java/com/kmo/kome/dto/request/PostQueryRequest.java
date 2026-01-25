package com.kmo.kome.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

/**
 * 文章查询请求类。
 * 用于封装查询文章列表时的筛选条件。
 * 支持分页以及通过关键词、标签和状态过滤文章数据。
 */
@Data
public class PostQueryRequest {
    @NotNull(message = "页码不能为空")
    @Min(value = 1, message = "页码最小为1")
    private Integer pageNum = 1;

    @NotNull(message = "每页数量不能为空")
    @Min(value = 1, message = "每页数量最少为1")
    private Integer pageSize = 10;

    // 关键词筛选
    private String keyword;
    // 按 tag 筛选
    private Long tagId;
    // 按状态筛选
    private Integer status;

    // 是否忽略置顶排序（仅管理员接口使用）
    private Boolean ignorePinned = false;
}
