package com.qk.dto;

import lombok.Data;

@Data
public class RoleDto {
    private Integer pageNum = 1;
    private Integer pageSize = 10;
    private String name;
}