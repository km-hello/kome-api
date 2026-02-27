package com.kmo.kome.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.kmo.kome.common.PageResult;
import com.kmo.kome.common.exception.ServiceException;
import com.kmo.kome.dto.request.PostArchiveQueryRequest;
import com.kmo.kome.dto.request.PostCreateRequest;
import com.kmo.kome.dto.request.PostQueryRequest;
import com.kmo.kome.dto.request.PostUpdateRequest;
import com.kmo.kome.dto.response.PostArchiveResponse;
import com.kmo.kome.dto.response.PostDetailResponse;
import com.kmo.kome.dto.response.PostSimpleResponse;
import com.kmo.kome.entity.Post;
import jakarta.validation.Valid;

import java.util.List;

/**
 * 博客文章业务接口
 * <p>
 * 继承 IService<Post> 接口，提供文章相关的基础 CRUD 操作能力。
 * 扩展了创建文章的自定义业务方法。
 */
public interface PostService extends IService<Post> {

    /**
     * 创建新文章。
     * 校验文章别名唯一性和标签合法性，保存文章并处理标签关联关系。
     *
     * @param request 创建文章的请求对象，包含标题、内容、摘要、状态、标签等信息。
     * @return 新创建的文章 ID。
     * @throws ServiceException 如果文章别名重复或关联的标签无效。
     */
    Long createPost(@Valid PostCreateRequest request);

    /**
     * 根据文章 ID 删除文章及其关联数据。
     * 删除文章与标签的关联记录，并执行逻辑删除。
     *
     * @param id 待删除文章的唯一标识符。
     * @return 空值，表示删除操作已完成。
     * @throws ServiceException 如果文章不存在。
     */
    Void deletePostById(Long id);

    /**
     * 根据文章 ID 更新文章及其关联的标签数据。
     *
     * @param id 待更新文章的唯一标识符。
     * @param request 文章更新请求对象，包含标题、摘要、内容、状态、标签等信息。
     * @return 空值，表示更新操作已完成。
     * @throws ServiceException 如果文章不存在、别名重复或标签无效。
     */
    Void updatePostById(Long id, @Valid PostUpdateRequest request);

    /**
     * 根据文章 ID 获取文章详情（后台接口，不增加阅读量）。
     *
     * @param id 文章的唯一标识符。
     * @return 包含文章详细信息的响应对象。
     * @throws ServiceException 如果文章不存在。
     */
    PostDetailResponse getPostById(Long id);

    /**
     * 根据文章别名（Slug）获取文章详情（公开接口，增加阅读量）。
     *
     * @param slug 文章的别名，用于唯一定位文章记录。
     * @return 包含文章详细信息的响应对象。
     * @throws ServiceException 如果文章不存在或未发布。
     */
    PostDetailResponse getPostBySlug(String slug);

    /**
     * 获取后台管理文章分页列表。
     * 支持按关键词、标签、状态筛选。
     *
     * @param request 查询请求对象，包含分页参数和筛选条件。
     * @return 包含文章概要信息的分页结果。
     */
    PageResult<PostSimpleResponse> getAdminPostPage(@Valid PostQueryRequest request);

    /**
     * 获取公开文章分页列表。
     * 仅返回已发布的文章，按照置顶优先排序。
     *
     * @param request 查询请求对象，包含分页参数和筛选条件。
     * @return 包含文章概要信息的分页结果。
     */
    PageResult<PostSimpleResponse> getPublicPostPage(@Valid PostQueryRequest request);

    /**
     * 获取文章归档列表。
     * 按年份和月份对已发布的文章进行分组归档。
     *
     * @param request 归档查询请求对象，包含关键词、标签等过滤条件。
     * @return 按年份和月份分组的文章归档列表。
     */
    List<PostArchiveResponse> getArchivePosts(PostArchiveQueryRequest request);
}

