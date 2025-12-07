package com.kmo.kome.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.kmo.kome.common.PageResult;
import com.kmo.kome.dto.TagWhitPostIdDTO;
import com.kmo.kome.dto.request.TagQueryRequest;
import com.kmo.kome.dto.response.TagPostCountResponse;
import com.kmo.kome.dto.response.TagResponse;
import com.kmo.kome.entity.Tag;

import java.util.List;

/**
 * 标签业务接口
 * <p>
 * 继承自 IService<Tag>，提供标签相关的基础 CRUD 功能。
 * 同时扩展标签的自定义功能，支持创建、更新、删除以及与文章关联的复杂业务操作。
 */
public interface TagService extends IService<Tag> {
    TagResponse createTag(String name);

    TagResponse updateTagById(Long id, String name);

    Void deleteTagById(Long id);

    PageResult<TagPostCountResponse> getAdminTagPage(TagQueryRequest request);

    List<TagPostCountResponse> getPublicTagList();

    List<TagResponse> findTagsByPostId(Long postId);

    List<TagWhitPostIdDTO> findTagsByPostIds(List<Long> postIds);
}
