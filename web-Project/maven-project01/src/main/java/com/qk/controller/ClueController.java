package com.qk.controller;

import com.qk.common.PageResult;
import com.qk.common.Result;
import com.qk.dto.ClueDto;
import com.qk.entity.Clue;
import com.qk.entity.ClueTrackRecord;
import com.qk.service.ClueService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * 线索管理 Controller
 */
@Slf4j
@RestController
@RequestMapping("/clue")
public class ClueController {

    @Autowired
    private ClueService clueService;

    /**
     * 分页查询线索列表
     *
     * @param clueDto 查询条件（包含分页参数）
     * @return 分页结果
     */
    @GetMapping("/page")
    public Result<PageResult<Clue>> pageList(ClueDto clueDto) {
        log.info("分页查询线索列表，参数：{}", clueDto);
        PageResult<Clue> pageResult = clueService.pageQuery(clueDto);
        return Result.success(pageResult);
    }

    /**
     * 新增线索
     *
     * @param clue 线索信息
     * @return 统一响应
     */
    @PostMapping
    public Result add(@RequestBody Clue clue) {
        log.info("新增线索：{}", clue.getName());
        clueService.add(clue);
        return Result.success();
    }

    /**
     * 分配线索（修改归属人）
     *
     * @param clueId 线索id
     * @param userId 归属人id
     * @return 统一响应
     */
    @PutMapping("/assign")
    public Result assign(@RequestParam Integer clueId, @RequestParam Integer userId) {
        log.info("分配线索，clueId：{}，userId：{}", clueId, userId);
        clueService.assign(clueId, userId);
        return Result.success();
    }

    /**
     * 线索跟进
     *
     * @param clueId      线索id
     * @param trackRecord 跟进记录
     * @param request     请求对象（获取当前登录用户）
     * @return 统一响应
     */
    @PostMapping("/track")
    public Result track(@RequestParam Integer clueId, @RequestBody ClueTrackRecord trackRecord, HttpServletRequest request) {
        log.info("线索跟进，clueId：{}", clueId);
        Integer userId = (Integer) request.getAttribute("userId");
        trackRecord.setUserId(userId);
        clueService.track(clueId, trackRecord);
        return Result.success();
    }

    /**
     * 线索转商机
     *
     * @param id      线索id
     * @param request 请求对象（获取当前登录用户）
     * @return 统一响应
     */
    @PutMapping("/convert/{id}")
    public Result convertToOpportunity(@PathVariable Integer id, HttpServletRequest request) {
        log.info("线索转商机，id：{}", id);
        Integer userId = (Integer) request.getAttribute("userId");
        clueService.convertToOpportunity(id, userId);
        return Result.success();
    }
}
