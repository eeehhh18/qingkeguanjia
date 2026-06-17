package com.qk.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@TableName("course")
public class Course {
    @TableId(type = IdType.AUTO)
    private Integer id;
    private Integer subject;
    private String name;
    private Integer price;
    private Integer target;
    private String description;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
}