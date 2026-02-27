package com.kmo.kome.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.kmo.kome.dto.response.MemoStatsResponse;
import com.kmo.kome.entity.Memo;
import org.apache.ibatis.annotations.Mapper;

/**
 *  Memo 数据访问层接口
 * 继承自 BaseMapper<Memo>，提供对 Memo  (memo) 表的基础 CRUD 功能。
 */
@Mapper
public interface MemoMapper extends BaseMapper<Memo> {
    /**
     * 查询说说（Memo）的统计数据，汇总总数量、总字数、本月新增数量以及最新发布时间等信息。
     *
     * @return 包含说说统计信息的 {@code MemoStatsResponse} 对象
     */
    MemoStatsResponse selectMemoStats();
}
