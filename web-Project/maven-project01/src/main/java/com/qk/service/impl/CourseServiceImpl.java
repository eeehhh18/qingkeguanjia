package com.qk.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.qk.common.PageResult;
import com.qk.dto.CourseDto;
import com.qk.entity.Course;
import com.qk.mapper.CourseMapper;
import com.qk.service.CourseService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import java.time.LocalDateTime;
import java.util.List;

@Service
@Slf4j
public class CourseServiceImpl extends ServiceImpl<CourseMapper, Course> implements CourseService {

    @Override
    public PageResult<Course> pageQuery(CourseDto dto) {
        LambdaQueryWrapper<Course> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(dto.getSubject() != null, Course::getSubject, dto.getSubject())
                .like(StringUtils.hasText(dto.getName()), Course::getName, dto.getName())
                .eq(dto.getTarget() != null, Course::getTarget, dto.getTarget())
                .orderByDesc(Course::getCreateTime);
        Page<Course> page = new Page<>(dto.getPageNum(), dto.getPageSize());
        this.page(page, wrapper);
        return PageResult.of(page.getTotal(), page.getRecords());
    }

    @Override
    public List<Course> listAll() {
        return this.list(new LambdaQueryWrapper<Course>().orderByAsc(Course::getName));
    }

    @Override
    public void add(Course course) {
        course.setCreateTime(LocalDateTime.now());
        course.setUpdateTime(LocalDateTime.now());
        this.save(course);
        log.info("新增课程：{}", course.getName());
    }

    @Override
    public void update(Course course) {
        course.setUpdateTime(LocalDateTime.now());
        this.updateById(course);
        log.info("修改课程：{}", course.getName());
    }

    @Override
    public void deleteById(Integer id) {
        this.removeById(id);
        log.info("删除课程：{}", id);
    }
}