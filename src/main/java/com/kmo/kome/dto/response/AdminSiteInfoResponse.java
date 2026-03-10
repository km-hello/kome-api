package com.kmo.kome.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 后台站点信息响应 DTO。
 * 仅返回管理后台统计信息，不包含公开 owner 数据。
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AdminSiteInfoResponse {
    private Long publishedPostCount;
    private Long draftPostCount;
    private Long usedTagCount;
    private Long unusedTagCount;
    private Long publishedMemoCount;
    private Long draftMemoCount;
    private Long publishedLinkCount;
    private Long draftLinkCount;
}
