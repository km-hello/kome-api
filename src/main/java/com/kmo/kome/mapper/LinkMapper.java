package com.kmo.kome.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.kmo.kome.entity.Link;
import org.apache.ibatis.annotations.Mapper;

/**
 * 友链数据访问层接口
 * 继承自 BaseMapper<Link>，提供对友链 (link) 表的基础 CRUD 功能。
 */
@Mapper
public interface LinkMapper extends BaseMapper<Link> {
}
