package com.qk.controller;

import com.qk.common.PageResult;
import com.qk.common.Result;
import com.qk.dto.RoleDto;
import com.qk.entity.Role;
import com.qk.service.RoleService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/role")
@Slf4j
public class RoleController {

    @Autowired
    private RoleService roleService;

    @GetMapping("/page")
    public Result<PageResult<Role>> pageQuery(RoleDto dto) {
        log.info("分页查询角色：{}", dto);
        return Result.success(roleService.pageQuery(dto));
    }

    @GetMapping("/list")
    public Result<List<Role>> listAll() {
        return Result.success(roleService.listAll());
    }

    @PostMapping
    public Result<Void> add(@RequestBody Role role) {
        log.info("新增角色：{}", role);
        roleService.add(role);
        return Result.success();
    }

    @PutMapping
    public Result<Void> update(@RequestBody Role role) {
        log.info("修改角色：{}", role);
        roleService.update(role);
        return Result.success();
    }

    @DeleteMapping("/{id}")
    public Result<Void> delete(@PathVariable Integer id) {
        log.info("删除角色：{}", id);
        roleService.deleteById(id);
        return Result.success();
    }
}