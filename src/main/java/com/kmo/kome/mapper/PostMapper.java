package com.kmo.kome.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.kmo.kome.entity.Post;
import org.apache.ibatis.annotations.Mapper;

/**
 * 博客文章数据访问层接口
 * 继承自 BaseMapper<Post>，提供对 post 表的基础 CRUD 功能。
 */
@Mapper
public interface PostMapper extends BaseMapper<Post> {
}
