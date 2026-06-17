package com.qk.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.qk.common.PageResult;
import com.qk.dto.ClueDto;
import com.qk.entity.Clue;
import com.qk.entity.ClueTrackRecord;

/**
 * 线索 Service 接口
 */
public interface ClueService extends IService<Clue> {

    /**
     * 分页查询线索列表
     *
     * @param clueDto 查询条件
     * @return 分页结果
     */
    PageResult<Clue> pageQuery(ClueDto clueDto);

    /**
     * 新增线索
     *
     * @param clue 线索信息
     */
    void add(Clue clue);

    /**
     * 分配线索（修改归属人）
     *
     * @param clueId 线索id
     * @param userId 归属人id
     */
    void assign(Integer clueId, Integer userId);

    /**
     * 线索跟进
     *
     * @param clueId      线索id
     * @param trackRecord 跟进记录
     */
    void track(Integer clueId, ClueTrackRecord trackRecord);

    /**
     * 线索转商机
     *
     * @param clueId 线索id
     * @param userId 当前操作人id
     */
    void convertToOpportunity(Integer clueId, Integer userId);
}
