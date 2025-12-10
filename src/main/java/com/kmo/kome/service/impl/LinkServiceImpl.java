package com.kmo.kome.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.kmo.kome.common.PageResult;
import com.kmo.kome.common.ResultCode;
import com.kmo.kome.common.exception.ServiceException;
import com.kmo.kome.dto.request.LinkCreateRequest;
import com.kmo.kome.dto.request.LinkQueryRequest;
import com.kmo.kome.dto.request.LinkUpdateRequest;
import com.kmo.kome.dto.response.LinkResponse;
import com.kmo.kome.entity.Link;
import com.kmo.kome.mapper.LinkMapper;
import com.kmo.kome.service.LinkService;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;

/**
 * 友链服务实现类
 * 实现了 LinkService 接口，提供管理友链的具体业务逻辑。
 * 继承了 ServiceImpl<LinkMapper, Link>，通过 MyBatis-Plus 提供的基础功能实现了友链的通用增删查改。
 * 可在此基础上扩展和实现更复杂的业务逻辑。
 */
@Service
public class LinkServiceImpl extends ServiceImpl<LinkMapper, Link> implements LinkService {

    /**
     * 创建新友链并保存到数据库中。
     * 根据请求对象中的字段构造友链实体，并将其持久化存储后返回生成的主键。
     *
     * @param request 创建友链所需的请求对象，其中包含名称、链接地址、头像、描述等信息。
     * @return 返回新创建的友链的主键 ID。
     */
    @Override
    public Long createLink(LinkCreateRequest request) {
        Link link = new Link();
        BeanUtils.copyProperties(request, link);
        save(link);
        return link.getId();
    }

    /**
     * 根据友链 ID 更新对应的友链信息。
     * 根据提供的更新请求参数更新数据库中的友链记录。
     * 如果 ID 对应的友链不存在，则抛出 NOT_FOUND 异常。
     *
     * @param id 友链的唯一标识符，用于定位要更新的记录。
     * @param request 包含更新友链信息的请求对象，包含名称、链接地址、头像、描述等字段。
     * @return 返回 null，表示更新操作没有返回具体值。
     */
    @Override
    public Void updateLinkById(Long id, LinkUpdateRequest request) {
        Link link = getById(id);
        if(link == null){
            throw new ServiceException(ResultCode.NOT_FOUND, "友链不存在");
        }
        Link updateLink = new Link();
        BeanUtils.copyProperties(request, updateLink);
        updateLink.setId(id);
        updateById(updateLink);
        return null;
    }

    /**
     * 根据友链的唯一标识符删除对应的友链记录。
     * 如果指定的 ID 对应的友链不存在，抛出 NOT_FOUND 异常。
     *
     * @param id 友链的唯一标识符，用于定位需要删除的记录。
     * @return 返回 null，表示删除操作没有返回具体值。
     */
    @Override
    public Void deleteLinkById(Long id) {
        Link link = getById(id);
        if(link == null){
            throw new ServiceException(ResultCode.NOT_FOUND, "友链不存在");
        }
        removeById(id);
        return null;
    }

    /**
     * 获取所有公开友链的列表。
     * 此方法会将请求中的分页设置为不分页，从而返回所有符合条件的友链列表。
     *
     * @param request 包含筛选条件的查询请求对象，其中：
     *                - pageNum 表示当前页码（默认值为 1）；
     *                - pageSize 表示每页数量（此处会被设置为 -1）；
     *                - keyword 是用于友链名称模糊查询的关键词（可选）。
     * @return 返回一个包含所有公开友链的列表，每个元素为 LinkResponse 类型的对象。
     */
    @Override
    public List<LinkResponse> getPublicLinkList(LinkQueryRequest request) {
        // 设置不分页
        request.setPageSize(-1);
        return getAdminLinkPage(request).getRecords();
    }

    /**
     * 获取后台管理友链分页列表。
     * 根据查询请求中提供的条件，分页查询友链数据，并将结果封装为响应对象返回。
     *
     * @param request 包含分页参数和筛选条件的查询请求对象。其中：
     *                - pageNum 表示当前页码；
     *                - pageSize 表示每页记录数；
     *                - keyword 是用于模糊查询友链名称的关键词（可选）。
     * @return 一个封装为分页结果的对象，其中包含友链响应对象列表、总记录数、每页记录数及当前页码等信息。
     */
    @Override
    public PageResult<LinkResponse> getAdminLinkPage(LinkQueryRequest request) {
        // 构建分页对象
        Page<Link> page = new Page<>(request.getPageNum(), request.getPageSize());
        // 如果不分页，则不进行 count 操作
        if(page.getSize() == -1){
            page.setSearchCount(false);
        }

        // 构建查询条件
        LambdaQueryWrapper<Link> wrapper = new LambdaQueryWrapper<>();
        wrapper.like(StringUtils.hasText(request.getKeyword()), Link::getName, request.getKeyword())
                .orderByAsc(Link::getCreateTime);

        // 查询数据库
        Page<Link> linkPage = page(page, wrapper);

        // 转换记录列表 (List<Link> -> List<LinkResponse>)
        List<LinkResponse> responseList = linkPage.getRecords().stream()
                .map(this::toResponse)
                .toList();

        // 构建返回
        return PageResult.<LinkResponse>builder()
                .records(responseList)
                .total(linkPage.getTotal())
                .size(linkPage.getSize())
                .current(linkPage.getCurrent())
                .build();
    }

    /**
     * 将 Link 对象转换为 LinkResponse 对象。
     * 该方法利用 BeanUtils 进行属性拷贝，将包含的字段数据映射到响应对象中。
     *
     * @param link 要转换的 Link 实例，包含友链的详细信息。
     * @return 转换后的 LinkResponse 对象，用于响应层通信。
     */
    private LinkResponse toResponse(Link link) {
        LinkResponse response = new LinkResponse();
        BeanUtils.copyProperties(link, response);
        return response;
    }
}
