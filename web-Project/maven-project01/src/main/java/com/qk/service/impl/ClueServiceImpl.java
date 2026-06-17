package com.qk.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.qk.common.PageResult;
import com.qk.dto.ClueDto;
import com.qk.entity.Clue;
import com.qk.entity.ClueTrackRecord;
import com.qk.mapper.ClueMapper;
import com.qk.mapper.ClueTrackRecordMapper;
import com.qk.service.ClueService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

/**
 * 线索 Service 实现类
 */
@Slf4j
@Service
public class ClueServiceImpl extends ServiceImpl<ClueMapper, Clue> implements ClueService {

    @Autowired
    private ClueTrackRecordMapper clueTrackRecordMapper;

    /**
     * 分页查询线索列表（关联用户表）
     */
    @Override
    public PageResult<Clue> pageQuery(ClueDto clueDto) {
        IPage<Clue> page = new Page<>(clueDto.getPageNum(), clueDto.getPageSize());
        IPage<Clue> result = baseMapper.pageQuery(page, clueDto);
        return PageResult.of(result.getTotal(), result.getRecords());
    }

    /**
     * 新增线索
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void add(Clue clue) {
        log.info("新增线索，手机号：{}", clue.getPhone());

        // 设置默认状态：待分配
        if (clue.getStatus() == null) {
            clue.setStatus(1);
        }

        // 设置创建时间和修改时间
        LocalDateTime now = LocalDateTime.now();
        clue.setCreateTime(now);
        clue.setUpdateTime(now);

        save(clue);
    }

    /**
     * 分配线索（修改归属人，状态改为待跟进）
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void assign(Integer clueId, Integer userId) {
        log.info("分配线索，clueId：{}，userId：{}", clueId, userId);

        Clue clue = new Clue();
        clue.setId(clueId);
        clue.setUserId(userId);
        clue.setStatus(2); // 待跟进
        clue.setUpdateTime(LocalDateTime.now());

        updateById(clue);
    }

    /**
     * 线索跟进
     * 1. 更新线索的 status、next_time
     * 2. 插入 clue_track_record 跟进记录
     * 使用 @Transactional 保证原子性
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void track(Integer clueId, ClueTrackRecord trackRecord) {
        log.info("线索跟进，clueId：{}", clueId);

        // 更新线索状态为"跟进中"
        Clue clue = new Clue();
        clue.setId(clueId);
        clue.setStatus(3); // 跟进中
        clue.setNextTime(trackRecord.getNextTime());
        clue.setUpdateTime(LocalDateTime.now());
        updateById(clue);

        // 插入跟进记录
        trackRecord.setClueId(clueId);
        trackRecord.setCreateTime(LocalDateTime.now());
        clueTrackRecordMapper.insert(trackRecord);
    }

    /**
     * 线索转商机
     * 1. 更新线索 status = 5（转商机）
     * 2. 插入一条跟进记录说明转商机
     * 使用 @Transactional 保证原子性
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void convertToOpportunity(Integer clueId, Integer userId) {
        log.info("线索转商机，clueId：{}", clueId);

        // 更新线索状态
        Clue clue = new Clue();
        clue.setId(clueId);
        clue.setStatus(5); // 转商机
        clue.setUpdateTime(LocalDateTime.now());
        updateById(clue);

        // 插入转商机跟进记录
        ClueTrackRecord record = new ClueTrackRecord();
        record.setClueId(clueId);
        record.setUserId(userId);
        record.setContent("线索已转为商机");
        record.setCreateTime(LocalDateTime.now());
        clueTrackRecordMapper.insert(record);
    }
}
