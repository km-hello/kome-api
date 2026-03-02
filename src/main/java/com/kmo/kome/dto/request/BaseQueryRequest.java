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
    @NotNull(message = "{validation.query.pageNum.notNull}")
    @Min(value = 1, message = "{validation.query.pageNum.min}")
    private Integer pageNum = 1;

    @NotNull(message = "{validation.query.pageSize.notNull}")
    @Min(value = 1, message = "{validation.query.pageSize.min}")
    private Integer pageSize = 10;
}
