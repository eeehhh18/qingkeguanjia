package com.qk.dto;

import lombok.Data;

@Data
public class CourseDto {
    private Integer pageNum = 1;
    private Integer pageSize = 10;
    private Integer subject;
    private String name;
    private Integer target;
}