package com.qk.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.qk.common.PageResult;
import com.qk.dto.CourseDto;
import com.qk.entity.Course;
import java.util.List;

public interface CourseService extends IService<Course> {
    PageResult<Course> pageQuery(CourseDto dto);
    List<Course> listAll();
    void add(Course course);
    void update(Course course);
    void deleteById(Integer id);
}