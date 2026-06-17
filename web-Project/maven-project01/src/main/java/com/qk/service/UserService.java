package com.qk.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.qk.common.PageResult;
import com.qk.dto.UserDto;
import com.qk.entity.User;

/**
 * 用户 Service 接口
 */
public interface UserService extends IService<User> {

    /**
     * 分页查询用户列表
     *
     * @param userDto 查询条件
     * @return 分页结果
     */
    PageResult<User> pageQuery(UserDto userDto);

    /**
     * 新增用户
     *
     * @param user 用户信息
     */
    void add(User user);

    /**
     * 修改用户
     *
     * @param user 用户信息
     */
    void update(User user);

    /**
     * 删除用户
     *
     * @param id 用户id
     */
    void deleteById(Integer id);

    /**
     * 用户登录
     *
     * @param username 用户名
     * @param password 密码
     * @return 用户信息
     */
    User login(String username, String password);
}
