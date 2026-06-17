package com.qk.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.qk.common.PageResult;
import com.qk.dto.ActivityDto;
import com.qk.entity.Activity;

public interface ActivityService extends IService<Activity> {
    PageResult<Activity> pageQuery(ActivityDto dto);
    void add(Activity activity);
    void update(Activity activity);
    void deleteById(Integer id);
}