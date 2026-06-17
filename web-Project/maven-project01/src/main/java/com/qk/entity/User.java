package com.qk.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * 用户信息实体类
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@TableName("user")
public class User {

    /**
     * 用户id
     */
    @TableId(type = IdType.AUTO)
    private Integer id;

    /**
     * 用户名
     */
    private String username;

    /**
     * 密码
     */
    private String password;

    /**
     * 姓名
     */
    private String name;

    /**
     * 手机号
     */
    private String phone;

    /**
     * 邮箱
     */
    private String email;

    /**
     * 性别：1男，2女
     */
    private Integer gender;

    /**
     * 状态：1正常，0停用
     */
    private Integer status;

    /**
     * 部门id
     */
    private Integer deptId;

    /**
     * 角色id
     */
    private Integer roleId;

    /**
     * 头像url
     */
    private String image;

    /**
     * 备注
     */
    private String remark;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 修改时间
     */
    private LocalDateTime updateTime;

    /**
     * 部门名称（扩展字段，数据库不存在）
     */
    @TableField(exist = false)
    private String deptName;

    /**
     * 角色名称（扩展字段，数据库不存在）
     */
    @TableField(exist = false)
    private String roleName;
}
