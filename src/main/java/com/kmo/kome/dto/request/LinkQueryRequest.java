package com.kmo.kome.dto.request;

import lombok.Data;


/**
 * 友链查询请求类。
 * 用于封装查询友链数据时的筛选条件。
 * 仅支持通过关键词进行筛选操作。
 */
@Data
public class LinkQueryRequest {
    private String keyword;
}
