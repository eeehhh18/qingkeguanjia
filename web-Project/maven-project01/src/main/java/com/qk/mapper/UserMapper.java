package com.qk.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.qk.dto.UserDto;
import com.qk.entity.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * 用户 Mapper 接口
 */
@Mapper
public interface UserMapper extends BaseMapper<User> {

    /**
     * 分页查询用户列表（关联部门表获取部门名称）
     *
     * @param page    分页对象
     * @param userDto 查询条件
     * @return 分页结果
     */
    IPage<User> pageQuery(IPage<User> page, @Param("userDto") UserDto userDto);
}
