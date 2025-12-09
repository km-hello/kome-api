package com.kmo.kome.dto.request;

import jakarta.validation.constraints.Min;
import lombok.Data;

/**
 *  Memo 查询请求类。
 * 用于封装查询 Memo 列表时的筛选条件和分页信息。
 * 支持通过关键词和状态进行筛选，同时具备分页功能。
 */
@Data
public class MemoQueryRequest {
    @Min(value = 1, message = "页码最小为1")
    private Integer pageNum = 1;

    @Min(value = 1, message = "每页数量最少为1")
    private Integer pageSize = 10;

    // 关键词筛选
    private String keyword;
    // 按状态筛选
    private Integer status;
}
