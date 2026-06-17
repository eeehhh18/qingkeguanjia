package com.qk.controller;

import com.qk.common.PageResult;
import com.qk.common.Result;
import com.qk.dto.ActivityDto;
import com.qk.entity.Activity;
import com.qk.service.ActivityService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/activity")
@Slf4j
public class ActivityController {

    @Autowired
    private ActivityService activityService;

    @GetMapping("/page")
    public Result<PageResult<Activity>> pageQuery(ActivityDto dto) {
        log.info("分页查询活动：{}", dto);
        return Result.success(activityService.pageQuery(dto));
    }

    @PostMapping
    public Result<Void> add(@RequestBody Activity activity) {
        log.info("新增活动：{}", activity);
        activityService.add(activity);
        return Result.success();
    }

    @PutMapping
    public Result<Void> update(@RequestBody Activity activity) {
        log.info("修改活动：{}", activity);
        activityService.update(activity);
        return Result.success();
    }

    @DeleteMapping("/{id}")
    public Result<Void> delete(@PathVariable Integer id) {
        log.info("删除活动：{}", id);
        activityService.deleteById(id);
        return Result.success();
    }
}