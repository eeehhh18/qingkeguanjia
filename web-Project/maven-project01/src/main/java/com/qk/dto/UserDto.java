package com.qk.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 用户查询条件 DTO
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserDto {

    /**
     * 页码（默认第1页）
     */
    private Integer pageNum = 1;

    /**
     * 每页条数（默认10条）
     */
    private Integer pageSize = 10;

    /**
     * 姓名（模糊查询）
     */
    private String name;

    /**
     * 手机号（精确查询）
     */
    private String phone;

    /**
     * 状态（精确查询）
     */
    private Integer status;

    /**
     * 部门id（精确查询）
     */
    private Integer deptId;
}
