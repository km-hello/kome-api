package com.kmo.kome.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.kmo.kome.common.PageResult;
import com.kmo.kome.common.exception.ServiceException;
import com.kmo.kome.dto.TagWhitPostIdDTO;
import com.kmo.kome.dto.request.TagQueryRequest;
import com.kmo.kome.dto.response.TagPostCountResponse;
import com.kmo.kome.dto.response.TagResponse;
import com.kmo.kome.entity.Tag;
import jakarta.validation.Valid;

import java.util.List;

/**
 * 标签业务接口
 * <p>
 * 继承自 IService<Tag>，提供标签相关的基础 CRUD 功能。
 * 同时扩展标签的自定义功能，支持创建、更新、删除以及与文章关联的复杂业务操作。
 */
public interface TagService extends IService<Tag> {

    /**
     * 创建一个新的标签。
     *
     * @param name 新标签的名称。
     * @return 创建成功的标签信息。
     * @throws ServiceException 如果标签名称已存在。
     */
    TagResponse createTag(String name);

    /**
     * 更新指定的标签信息。
     *
     * @param id   要更新的标签的唯一标识。
     * @param name 要更新的标签的新名称。
     * @return 更新后的标签信息。
     * @throws ServiceException 如果标签不存在或名称已被占用。
     */
    TagResponse updateTagById(Long id, String name);

    /**
     * 根据 ID 删除指定的标签。
     * 如果标签正在被文章使用，则无法删除。
     *
     * @param id 要删除的标签的唯一标识。
     * @return 空值，表示删除操作已完成。
     * @throws ServiceException 如果标签不存在或正在被使用。
     */
    Void deleteTagById(Long id);

    /**
     * 获取后台管理标签分页数据，包含每个标签关联的文章数量。
     *
     * @param request 标签查询请求，包含分页参数。
     * @return 包含标签及其关联文章数量的分页结果。
     */
    PageResult<TagPostCountResponse> getAdminTagPage(@Valid TagQueryRequest request);

    /**
     * 获取所有公开标签列表，包含每个标签关联的文章数量。
     *
     * @return 包含标签及其文章数量的列表。
     */
    List<TagPostCountResponse> getPublicTagList();

    /**
     * 根据文章 ID 查询关联的标签列表。
     *
     * @param postId 文章的唯一标识。
     * @return 包含标签信息的列表。
     */
    List<TagResponse> findTagsByPostId(Long postId);

    /**
     * 根据文章 ID 列表批量查询关联的标签信息。
     *
     * @param postIds 文章 ID 的列表。
     * @return 包含文章 ID 和对应标签信息的列表。
     */
    List<TagWhitPostIdDTO> findTagsByPostIds(List<Long> postIds);

    /**
     * 获取后台管理标签完整列表，按创建时间倒序排序。
     *
     * @return 包含标签信息的列表。
     */
    List<TagResponse> getAdminTagList();
}
