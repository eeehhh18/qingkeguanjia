package com.qk.service.impl;

import cn.hutool.crypto.digest.DigestUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.qk.common.PageResult;
import com.qk.dto.UserDto;
import com.qk.entity.User;
import com.qk.mapper.UserMapper;
import com.qk.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

/**
 * 用户 Service 实现类
 */
@Slf4j
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

    /**
     * 分页查询用户列表（关联部门表）
     */
    @Override
    public PageResult<User> pageQuery(UserDto userDto) {
        // 构建分页对象
        IPage<User> page = new Page<>(userDto.getPageNum(), userDto.getPageSize());

        // 调用自定义 Mapper 方法进行关联查询
        IPage<User> result = baseMapper.pageQuery(page, userDto);

        return PageResult.of(result.getTotal(), result.getRecords());
    }

    /**
     * 新增用户
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void add(User user) {
        log.info("新增用户：{}", user.getUsername());

        // 校验用户名是否已存在
        if (lambdaQuery().eq(User::getUsername, user.getUsername()).exists()) {
            throw new RuntimeException("用户名已存在");
        }

        // 设置默认值
        if (user.getGender() == null) {
            user.setGender(1);
        }
        if (user.getStatus() == null) {
            user.setStatus(1);
        }

        // 密码 MD5 加密
        user.setPassword(DigestUtil.md5Hex(user.getPassword()));

        // 设置创建时间和修改时间
        LocalDateTime now = LocalDateTime.now();
        user.setCreateTime(now);
        user.setUpdateTime(now);

        save(user);
    }

    /**
     * 修改用户
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(User user) {
        log.info("修改用户，id：{}", user.getId());

        // 设置修改时间
        user.setUpdateTime(LocalDateTime.now());

        // 修改时不清除密码（如果前端未传密码则不更新）
        if (user.getPassword() == null || user.getPassword().isEmpty()) {
            user.setPassword(null);
        } else {
            user.setPassword(DigestUtil.md5Hex(user.getPassword()));
        }

        updateById(user);
    }

    /**
     * 删除用户
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteById(Integer id) {
        log.info("删除用户，id：{}", id);
        removeById(id);
    }

    /**
     * 用户登录
     */
    @Override
    public User login(String username, String password) {
        log.info("用户登录：{}", username);

        // 使用 MD5 加密密码后查询
        String encryptedPassword = DigestUtil.md5Hex(password);

        // 根据用户名查询用户
        User user = lambdaQuery()
                .eq(User::getUsername, username)
                .eq(User::getPassword, encryptedPassword)
                .one();

        return user;
    }
}
