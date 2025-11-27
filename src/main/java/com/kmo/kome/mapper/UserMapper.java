package com.kmo.kome.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.kmo.kome.entity.User;
import org.apache.ibatis.annotations.Mapper;

/**
 * 用户数据访问层
 * 继承 BaseMapper 即可获得基础 CRUD 能力
 */
@Mapper
public interface UserMapper extends BaseMapper<User> {
}
