package com.kmo.kome.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 文章归档响应 DTO 类。
 * 用于封装文章按年份和月份归档的数据信息并返回给前端。
 * 通常在文章归档接口中使用。
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PostArchiveResponse {

    // 第一层：年
    private Integer year;
    private Integer total;  // 该年份的总文章数
    private List<MonthGroup> months;

    // 内部类：第二层：月
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class MonthGroup {
        private Integer month;
        private Integer total;  // 该月份的文章数
        private List<ArchiveSimplePost> posts;
    }

    // 内部类：第三层：简单文章对象（只保留归档所需要的字段）
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ArchiveSimplePost {
        private Long id;
        private String title;
        private String slug;
        private List<TagResponse> tags;

        private LocalDateTime createTime;
    }

}
