package com.kmo.kome.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

/**
 * 分页查询基类。
 * 提供通用的分页参数（页码和每页数量），所有需要分页的查询请求类应继承此类。
 */
@Data
public class BaseQueryRequest {
    @NotNull(message = "页码不能为空")
    @Min(value = 1, message = "页码最小为1")
    private Integer pageNum = 1;

    @NotNull(message = "每页数量不能为空")
    @Min(value = 1, message = "每页数量最少为1")
    private Integer pageSize = 10;
}
