package com.qk.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.qk.common.PageResult;
import com.qk.dto.RoleDto;
import com.qk.entity.Role;
import com.qk.mapper.RoleMapper;
import com.qk.service.RoleService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import java.time.LocalDateTime;
import java.util.List;

@Service
@Slf4j
public class RoleServiceImpl extends ServiceImpl<RoleMapper, Role> implements RoleService {

    @Override
    public PageResult<Role> pageQuery(RoleDto dto) {
        LambdaQueryWrapper<Role> wrapper = new LambdaQueryWrapper<>();
        wrapper.like(StringUtils.hasText(dto.getName()), Role::getName, dto.getName())
                .orderByDesc(Role::getCreateTime);
        Page<Role> page = new Page<>(dto.getPageNum(), dto.getPageSize());
        this.page(page, wrapper);
        return PageResult.of(page.getTotal(), page.getRecords());
    }

    @Override
    public List<Role> listAll() {
        return this.list(new LambdaQueryWrapper<Role>().orderByAsc(Role::getName));
    }

    @Override
    public void add(Role role) {
        if (this.count(new LambdaQueryWrapper<Role>().eq(Role::getName, role.getName())) > 0) {
            throw new RuntimeException("角色名称已存在");
        }
        if (this.count(new LambdaQueryWrapper<Role>().eq(Role::getLabel, role.getLabel())) > 0) {
            throw new RuntimeException("角色标识已存在");
        }
        role.setCreateTime(LocalDateTime.now());
        role.setUpdateTime(LocalDateTime.now());
        this.save(role);
    }

    @Override
    public void update(Role role) {
        if (this.count(new LambdaQueryWrapper<Role>()
                .eq(Role::getName, role.getName())
                .ne(Role::getId, role.getId())) > 0) {
            throw new RuntimeException("角色名称已存在");
        }
        if (this.count(new LambdaQueryWrapper<Role>()
                .eq(Role::getLabel, role.getLabel())
                .ne(Role::getId, role.getId())) > 0) {
            throw new RuntimeException("角色标识已存在");
        }
        role.setUpdateTime(LocalDateTime.now());
        this.updateById(role);
    }

    @Override
    public void deleteById(Integer id) {
        this.removeById(id);
    }
}