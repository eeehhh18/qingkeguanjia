package com.qk.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.qk.common.PageResult;
import com.qk.dto.RoleDto;
import com.qk.entity.Role;
import java.util.List;

public interface RoleService extends IService<Role> {
    PageResult<Role> pageQuery(RoleDto dto);
    List<Role> listAll();
    void add(Role role);
    void update(Role role);
    void deleteById(Integer id);
}