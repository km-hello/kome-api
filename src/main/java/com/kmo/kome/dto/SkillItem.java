package com.kmo.kome.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 技能项 DTO
 * 用于存储用户的技能标签信息
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SkillItem {
    /**
     * 技能名称
     */
    private String name;

    /**
     * 熟练程度: 1=Basic, 2=Familiar, 3=Proficient
     */
    private Integer level;
}
