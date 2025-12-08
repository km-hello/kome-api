package com.kmo.kome.dto;

import lombok.Data;

/**
 * 表示带有帖子ID的标签数据传输对象。
 * 包含帖子ID、标签ID以及标签名称的相关信息。
 */
@Data
public class TagWhitPostIdDTO {
    private Long postId;

    private Long tagId;
    private String tagName;
}
