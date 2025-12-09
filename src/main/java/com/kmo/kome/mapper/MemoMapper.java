package com.kmo.kome.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.kmo.kome.entity.Memo;
import org.apache.ibatis.annotations.Mapper;

/**
 * 备忘录数据访问层接口
 * 继承自 BaseMapper<Memo>，提供对备忘录 (memo) 表的基础 CRUD 功能。
 */
@Mapper
public interface MemoMapper extends BaseMapper<Memo> {
}
