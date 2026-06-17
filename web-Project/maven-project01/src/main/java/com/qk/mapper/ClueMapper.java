package com.qk.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.qk.dto.ClueDto;
import com.qk.entity.Clue;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * 线索 Mapper 接口
 */
@Mapper
public interface ClueMapper extends BaseMapper<Clue> {

    /**
     * 分页查询线索列表（关联用户表查询归属人姓名）
     *
     * @param page    分页对象
     * @param clueDto 查询条件
     * @return 分页结果
     */
    IPage<Clue> pageQuery(IPage<Clue> page, @Param("clueDto") ClueDto clueDto);
}
