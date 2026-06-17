package com.qk.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.qk.entity.Dept;
import org.apache.ibatis.annotations.Mapper;

/**
 * 部门 Mapper 接口
 */
@Mapper
public interface DeptMapper extends BaseMapper<Dept> {
}
