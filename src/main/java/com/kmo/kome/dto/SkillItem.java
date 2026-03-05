package com.kmo.kome.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Range;

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
    @NotBlank(message = "{validation.skill.name.notBlank}")
    @Size(max = 50, message = "{validation.skill.name.size}")
    private String name;

    /**
     * 熟练程度: 1=Basic, 2=Familiar, 3=Proficient
     */
    @Range(min = 1, max = 3, message = "{validation.skill.level.range}")
    private Integer level;

    /**
     * 显示排序（值越小越靠前）
     */
    private Integer order;
}
