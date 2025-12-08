package com.kmo.kome.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.kmo.kome.entity.PostTag;
import org.apache.ibatis.annotations.Mapper;

/**
 * 博客文章与标签关联的数据访问层接口
 * 定义对 post_tag 表的数据库操作
 * 继承自 BaseMapper，提供基础的 CRUD 功能
 */
@Mapper
public interface PostTagMapper extends BaseMapper<PostTag> {
}
