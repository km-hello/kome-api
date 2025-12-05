package com.kmo.kome.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.kmo.kome.dto.response.TagResponse;
import com.kmo.kome.entity.Tag;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface TagMapper extends BaseMapper<Tag> {
    /**
     * 根据文章 ID 查询关联的标签列表。
     *
     * @param postId 文章的唯一标识，用于查找其关联的标签。
     * @return 包含标签信息的列表，每个元素表示一个标签。
     */
    List<TagResponse> findTagsByPostId(@Param("postId") Long postId);
}
