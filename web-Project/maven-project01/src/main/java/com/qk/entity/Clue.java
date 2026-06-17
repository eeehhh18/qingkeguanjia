package com.qk.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 线索信息实体类
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@TableName("clue")
public class Clue {

    /**
     * 线索id
     */
    @TableId(type = IdType.AUTO)
    private Integer id;

    /**
     * 手机号
     */
    private String phone;

    /**
     * 渠道：1线上活动，2推广介绍
     */
    private Integer channel;

    /**
     * 活动id
     */
    private Integer activityId;

    /**
     * 客户姓名
     */
    private String name;

    /**
     * 性别：1男，2女
     */
    private Integer gender;

    /**
     * 年龄
     */
    private Integer age;

    /**
     * 微信号
     */
    private String wechat;

    /**
     * QQ号
     */
    private String qq;

    /**
     * 归属人id
     */
    private Integer userId;

    /**
     * 状态：1待分配，2待跟进，3跟进中，4伪线索，5转商机
     */
    private Integer status;

    /**
     * 意向学科
     */
    private Integer subject;

    /**
     * 意向等级：1近期学习，2考虑中，3了解，4打酱油
     */
    private Integer level;

    /**
     * 下次跟进时间
     */
    private LocalDateTime nextTime;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 修改时间
     */
    private LocalDateTime updateTime;

    /**
     * 归属人姓名（扩展字段，数据库不存在）
     */
    @TableField(exist = false)
    private String assignName;

    /**
     * 跟进记录（扩展字段，数据库不存在）
     */
    @TableField(exist = false)
    private List<ClueTrackRecord> trackRecords;
}
