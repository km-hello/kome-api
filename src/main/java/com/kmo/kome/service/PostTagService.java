package com.kmo.kome.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.kmo.kome.entity.PostTag;

/**
 * 博客文章标签业务接口
 * <p>
 * 继承 IService<PostTag>，用于提供博客文章与标签关联的基础 CRUD 操作能力。
 * 主要用于处理博客文章与标签之间的多对多关联关系。
 */
public interface PostTagService extends IService<PostTag> {
}
