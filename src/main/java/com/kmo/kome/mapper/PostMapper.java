package com.kmo.kome.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.kmo.kome.dto.request.PostQueryRequest;
import com.kmo.kome.dto.response.PostSimpleResponse;
import com.kmo.kome.entity.Post;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * 博客文章数据访问层接口
 * 继承自 BaseMapper<Post>，提供对 post 表的基础 CRUD 功能。
 */
@Mapper
public interface PostMapper extends BaseMapper<Post> {
    /**
     * 根据分页参数和查询条件查询文章分页数据。
     *
     * @param page 分页参数，包含当前页码和每页数量。
     * @param query 查询条件，包含关键词、标签筛选及状态筛选等参数。
     * @return 包含文章概要信息的分页结果。
     */
    Page<PostSimpleResponse> selectPostPage(Page<Post> page, @Param("query")PostQueryRequest query);
}
