package com.kmo.kome.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * 说说统计响应 DTO 类。
 * <p>
 * 用于封装说说（Memo）相关的统计信息并返回给前端，
 * 包括说说的总数量、总字数、本月新增数量以及最新发布时间等。
 * <p>
 * 该类常用于获取说说概览的接口返回体。
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MemoStatsResponse {
    private Long totalCount;
    private Long totalWords;
    private Long thisMonthCount;
    private LocalDateTime latestDate;
}
