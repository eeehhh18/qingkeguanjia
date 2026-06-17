package com.qk.controller;

import com.qk.common.PageResult;
import com.qk.common.Result;
import com.qk.dto.CourseDto;
import com.qk.entity.Course;
import com.qk.service.CourseService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/course")
@Slf4j
public class CourseController {

    @Autowired
    private CourseService courseService;

    @GetMapping("/page")
    public Result<PageResult<Course>> pageQuery(CourseDto dto) {
        log.info("分页查询课程：{}", dto);
        return Result.success(courseService.pageQuery(dto));
    }

    @GetMapping("/list")
    public Result<List<Course>> listAll() {
        return Result.success(courseService.listAll());
    }

    @PostMapping
    public Result<Void> add(@RequestBody Course course) {
        log.info("新增课程：{}", course);
        courseService.add(course);
        return Result.success();
    }

    @PutMapping
    public Result<Void> update(@RequestBody Course course) {
        log.info("修改课程：{}", course);
        courseService.update(course);
        return Result.success();
    }

    @DeleteMapping("/{id}")
    public Result<Void> delete(@PathVariable Integer id) {
        log.info("删除课程：{}", id);
        courseService.deleteById(id);
        return Result.success();
    }
}