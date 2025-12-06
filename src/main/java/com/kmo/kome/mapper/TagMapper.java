package com.kmo.kome.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.kmo.kome.dto.TagWhitPostIdDTO;
import com.kmo.kome.dto.request.TagQueryRequest;
import com.kmo.kome.dto.response.TagPostCountResponse;
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

    /**
     * 根据文章 ID 列表查询关联的标签信息。
     *
     * @param postIds 文章 ID 的列表，用于批量查找每篇文章的关联标签。
     * @return 包含帖子 ID 和其对应标签信息的列表，每个元素表示一篇文章的标签详情。
     */
    List<TagWhitPostIdDTO> findTagsByPostIds(@Param("postIds") List<Long> postIds);

    /**
     * 查询后台标签分页数据。
     * 根据传入的分页参数，获取包含标签及其对应文章数量的分页信息。
     *
     * @param page 分页参数，包含当前页码和每页数量。
     * @return 包含标签及其关联文章数量信息的分页结果。
     */
    Page<TagPostCountResponse> selectAdminTagPage(Page<TagPostCountResponse> page);

    /**
     * 查询所有公开标签的列表。
     * 该方法返回包含每个标签及其关联文章数量的信息，通常用于前端展示标签列表。
     *
     * @return 包含标签信息及其关联文章数量的列表，每个元素表示一个公开标签详情。
     */
    List<TagPostCountResponse> selectPublicTagList();
}
