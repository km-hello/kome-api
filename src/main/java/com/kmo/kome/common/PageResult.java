package com.kmo.kome.common;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * 表示分页查询结果的通用类。
 * 该类用于封装分页查询返回的记录列表、总记录数、每页记录数以及当前页码等信息。
 *
 * @param <T> 分页查询记录的类型。
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PageResult<T> {
    private List<T> records;
    private Long total;
    private Long size;
    private Long current;
}
