package com.kmo.kome.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.kmo.kome.common.PageResult;
import com.kmo.kome.common.exception.ServiceException;
import com.kmo.kome.dto.request.LinkCreateRequest;
import com.kmo.kome.dto.request.LinkPublicQueryRequest;
import com.kmo.kome.dto.request.LinkQueryRequest;
import com.kmo.kome.dto.request.LinkUpdateRequest;
import com.kmo.kome.dto.response.LinkResponse;
import com.kmo.kome.entity.Link;
import jakarta.validation.Valid;

import java.util.List;

/**
 * 友链业务接口
 * <p>
 * 继承自 IService<Link>，提供友链相关的基础 CRUD 功能。
 * 主要用于管理和操作与友链实体相关的业务逻辑。
 */
public interface LinkService extends IService<Link> {

    /**
     * 获取所有公开友链列表。
     * 仅返回已发布状态的友链。
     *
     * @param request 包含筛选条件的查询请求对象。
     * @return 包含公开友链信息的列表。
     */
    List<LinkResponse> getPublicLinkList(LinkPublicQueryRequest request);

    /**
     * 创建新友链并保存到数据库中。
     *
     * @param request 创建友链的请求对象，包含名称、链接地址、头像、描述等信息。
     * @return 新创建的友链 ID。
     */
    Long createLink(@Valid LinkCreateRequest request);

    /**
     * 根据友链 ID 更新对应的友链信息。
     *
     * @param id 友链的唯一标识符。
     * @param request 包含更新信息的请求对象。
     * @return 空值，表示更新操作已完成。
     * @throws ServiceException 如果友链不存在。
     */
    Void updateLinkById(Long id, @Valid LinkUpdateRequest request);

    /**
     * 根据友链 ID 删除对应的友链记录。
     *
     * @param id 友链的唯一标识符。
     * @return 空值，表示删除操作已完成。
     * @throws ServiceException 如果友链不存在。
     */
    Void deleteLinkById(Long id);

    /**
     * 获取后台管理友链分页列表。
     * 支持按关键词和状态筛选。
     *
     * @param request 包含分页参数和筛选条件的查询请求对象。
     * @return 包含友链信息的分页结果。
     */
    PageResult<LinkResponse> getAdminLinkPage(@Valid LinkQueryRequest request);
}
