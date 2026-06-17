package com.qk.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.qk.common.PageResult;
import com.qk.entity.Dept;

/**
 * 部门 Service 接口
 */
public interface DeptService extends IService<Dept> {

    /**
     * 分页查询部门列表
     *
     * @param pageNum  页码
     * @param pageSize 每页条数
     * @param name     部门名称（模糊查询）
     * @param status   状态（精确匹配）
     * @return 分页结果
     */
    PageResult<Dept> pageQuery(Integer pageNum, Integer pageSize, String name, Integer status);

    /**
     * 新增部门
     *
     * @param dept 部门信息
     */
    void add(Dept dept);

    /**
     * 修改部门
     *
     * @param dept 部门信息
     */
    void update(Dept dept);

    /**
     * 删除部门
     *
     * @param id 部门id
     */
    void deleteById(Integer id);
}
