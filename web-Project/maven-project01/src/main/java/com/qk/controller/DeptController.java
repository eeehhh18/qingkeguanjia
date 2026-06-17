package com.qk.controller;

import com.qk.common.PageResult;
import com.qk.common.Result;
import com.qk.entity.Dept;
import com.qk.service.DeptService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * 部门管理 Controller
 */
@Slf4j
@RestController
@RequestMapping("/dept")
public class DeptController {

    @Autowired
    private DeptService deptService;

    /**
     * 分页查询部门列表
     *
     * @param pageNum  页码
     * @param pageSize 每页条数
     * @param name     部门名称（模糊查询）
     * @param status   状态（精确匹配）
     * @return 分页结果
     */
    @GetMapping
    public Result<PageResult<Dept>> pageList(
            @RequestParam(defaultValue = "1") Integer pageNum,
            @RequestParam(defaultValue = "10") Integer pageSize,
            @RequestParam(required = false) String name,
            @RequestParam(required = false) Integer status) {
        log.info("分页查询部门列表，参数：pageNum={}, pageSize={}, name={}, status={}", pageNum, pageSize, name, status);
        PageResult<Dept> pageResult = deptService.pageQuery(pageNum, pageSize, name, status);
        return Result.success(pageResult);
    }

    /**
     * 新增部门
     *
     * @param dept 部门信息
     * @return 统一响应
     */
    @PostMapping
    public Result add(@RequestBody Dept dept) {
        log.info("新增部门：{}", dept);
        deptService.add(dept);
        return Result.success();
    }

    /**
     * 修改部门
     *
     * @param dept 部门信息
     * @return 统一响应
     */
    @PutMapping
    public Result update(@RequestBody Dept dept) {
        log.info("修改部门：{}", dept);
        deptService.update(dept);
        return Result.success();
    }

    /**
     * 删除部门
     *
     * @param id 部门id
     * @return 统一响应
     */
    @DeleteMapping("/{id}")
    public Result delete(@PathVariable Integer id) {
        log.info("删除部门，id：{}", id);
        deptService.deleteById(id);
        return Result.success();
    }
}
