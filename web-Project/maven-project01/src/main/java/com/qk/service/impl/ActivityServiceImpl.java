package com.qk.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.qk.common.PageResult;
import com.qk.dto.ActivityDto;
import com.qk.entity.Activity;
import com.qk.mapper.ActivityMapper;
import com.qk.service.ActivityService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;

@Service
@Slf4j
public class ActivityServiceImpl extends ServiceImpl<ActivityMapper, Activity> implements ActivityService {

    @Override
    public PageResult<Activity> pageQuery(ActivityDto dto) {
        LambdaQueryWrapper<Activity> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(dto.getChannel() != null, Activity::getChannel, dto.getChannel())
                .eq(dto.getType() != null, Activity::getType, dto.getType())
                .orderByDesc(Activity::getCreateTime);
        Page<Activity> page = new Page<>(dto.getPageNum(), dto.getPageSize());
        this.page(page, wrapper);
        return PageResult.of(page.getTotal(), page.getRecords());
    }

    @Override
    public void add(Activity activity) {
        activity.setCreateTime(LocalDateTime.now());
        activity.setUpdateTime(LocalDateTime.now());
        this.save(activity);
        log.info("新增活动：{}", activity.getName());
    }

    @Override
    public void update(Activity activity) {
        activity.setUpdateTime(LocalDateTime.now());
        this.updateById(activity);
        log.info("修改活动：{}", activity.getName());
    }

    @Override
    public void deleteById(Integer id) {
        this.removeById(id);
        log.info("删除活动：{}", id);
    }
}