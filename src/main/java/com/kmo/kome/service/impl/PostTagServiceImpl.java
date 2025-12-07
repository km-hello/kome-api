package com.kmo.kome.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.kmo.kome.entity.PostTag;
import com.kmo.kome.mapper.PostTagMapper;
import com.kmo.kome.service.PostTagService;
import org.springframework.stereotype.Service;

@Service
public class PostTagServiceImpl extends ServiceImpl<PostTagMapper, PostTag> implements PostTagService {
}
