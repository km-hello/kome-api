package com.kmo.kome.dto.request;

import lombok.Data;

/**
 * 文章归档查询请求类。
 * 用于封装查询文章归档数据时的筛选条件。
 * 不继承分页基类，归档接口返回全量数据。
 */
@Data
public class PostArchiveQueryRequest {
    // 关键词筛选
    private String keyword;
    // 按 tag 筛选
    private Long tagId;
}
