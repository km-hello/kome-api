package com.kmo.kome.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 站点信息响应 DTO 类。
 * 用于封装站点的基本信息，包括所有者信息和统计数据，并返回给前端。
 * 通常在获取站点信息的接口中使用。
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SiteInfoResponse {

    private OwnerInfo owner;
    private Stats stats;

    // 内部类：所有者信息
    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class OwnerInfo{
        private String nickname;
        private String avatar;
        private String description;
    }

    // 内部类：站点统计信息
    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static  class Stats{
        private Long postCount;
        private Long tagCount;
        private Long memoCount;
        private Long linkCount;
    }

}
