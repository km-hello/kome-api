package com.kmo.kome.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.kmo.kome.dto.request.TagCreateRequest;
import com.kmo.kome.dto.request.TagUpdateRequest;
import com.kmo.kome.dto.response.TagResponse;
import com.kmo.kome.entity.Tag;

public interface TagService extends IService<Tag> {
    TagResponse createTag(String name);

    TagResponse updateTagById(Long id, String name);

    Void deleteTagById(Long id);
}
