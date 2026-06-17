package com.qk.dto;

import lombok.Data;

@Data
public class ActivityDto {
    private Integer pageNum = 1;
    private Integer pageSize = 10;
    private Integer channel;
    private Integer type;
}