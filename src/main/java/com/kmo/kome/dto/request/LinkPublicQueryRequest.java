package com.kmo.kome.dto.request;

import lombok.Data;

/**
 * 公开友链查询请求类。
 * 用于封装查询公开友链列表时的筛选条件。
 * 不继承分页基类，公开友链接口返回全量数据。
 */
@Data
public class LinkPublicQueryRequest {
    // 关键词筛选
    private String keyword;
}
