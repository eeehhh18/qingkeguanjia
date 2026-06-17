package com.qk.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.qk.common.PageResult;
import com.qk.entity.Dept;
import com.qk.mapper.DeptMapper;
import com.qk.service.DeptService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;

/**
 * 部门 Service 实现类
 */
@Slf4j
@Service
public class DeptServiceImpl extends ServiceImpl<DeptMapper, Dept> implements DeptService {

    /**
     * 分页查询部门列表
     */
    @Override
    public PageResult<Dept> pageQuery(Integer pageNum, Integer pageSize, String name, Integer status) {
        // 构建分页对象
        Page<Dept> page = new Page<>(pageNum, pageSize);

        // 构建查询条件
        LambdaQueryWrapper<Dept> wrapper = new LambdaQueryWrapper<>();
        wrapper.like(StringUtils.hasText(name), Dept::getName, name);
        wrapper.eq(status != null, Dept::getStatus, status);
        wrapper.orderByDesc(Dept::getUpdateTime);

        // 执行分页查询
        Page<Dept> result = page(page, wrapper);

        return PageResult.of(result.getTotal(), result.getRecords());
    }

    /**
     * 新增部门
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void add(Dept dept) {
        log.info("新增部门：{}", dept.getName());
        // 设置创建时间和修改时间
        LocalDateTime now = LocalDateTime.now();
        dept.setCreateTime(now);
        dept.setUpdateTime(now);
        save(dept);
    }

    /**
     * 修改部门
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(Dept dept) {
        log.info("修改部门，id：{}", dept.getId());
        // 设置修改时间
        dept.setUpdateTime(LocalDateTime.now());
        updateById(dept);
    }

    /**
     * 删除部门
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteById(Integer id) {
        log.info("删除部门，id：{}", id);
        removeById(id);
    }
}
