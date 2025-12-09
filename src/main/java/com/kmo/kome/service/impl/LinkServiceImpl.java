package com.kmo.kome.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.kmo.kome.dto.request.LinkQueryRequest;
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
     * 查询友链列表，根据查询条件筛选并按创建时间升序排列，最终返回友链响应对象列表。
     *
     * @param request 封装查询条件的请求对象，其中包含关键词字段用于模糊查询友链名称。
     * @return 返回封装为响应对象的友链列表，按创建时间从新到旧排序。
     */
    @Override
    public List<LinkResponse> getLinkList(LinkQueryRequest request) {
        List<Link> links = list(new LambdaQueryWrapper<Link>()
                .like(StringUtils.hasText(request.getKeyword()), Link::getName, request.getKeyword())
                .orderByAsc(Link::getCreateTime)
        );

        return links.stream()
                .map(this::toResponse)
                .toList();
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
