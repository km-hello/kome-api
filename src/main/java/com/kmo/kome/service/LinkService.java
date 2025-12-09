package com.kmo.kome.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.kmo.kome.dto.request.LinkQueryRequest;
import com.kmo.kome.dto.response.LinkResponse;
import com.kmo.kome.entity.Link;

import java.util.List;

/**
 * 友链业务接口
 * <p>
 * 继承自 IService<Link>，提供友链相关的基础 CRUD 功能。
 * 主要用于管理和操作与友链实体相关的业务逻辑。
 */
public interface LinkService extends IService<Link> {
    List<LinkResponse> getLinkList(LinkQueryRequest request);
}
