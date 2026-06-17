package com.qk.controller;

import com.qk.common.PageResult;
import com.qk.common.Result;
import com.qk.dto.UserDto;
import com.qk.entity.User;
import com.qk.service.UserService;
import com.qk.utils.OssUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

/**
 * 用户管理 Controller
 */
@Slf4j
@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private OssUtils ossUtils;

    /**
     * 分页查询用户列表
     *
     * @param userDto 查询条件（包含分页参数）
     * @return 分页结果
     */
    @GetMapping("/page")
    public Result<PageResult<User>> pageList(UserDto userDto) {
        log.info("分页查询用户列表，参数：{}", userDto);
        PageResult<User> pageResult = userService.pageQuery(userDto);
        return Result.success(pageResult);
    }

    /**
     * 新增用户
     *
     * @param user 用户信息
     * @return 统一响应
     */
    @PostMapping
    public Result add(@RequestBody User user) {
        log.info("新增用户：{}", user.getUsername());
        userService.add(user);
        return Result.success();
    }

    /**
     * 修改用户
     *
     * @param user 用户信息
     * @return 统一响应
     */
    @PutMapping
    public Result update(@RequestBody User user) {
        log.info("修改用户，id：{}", user.getId());
        userService.update(user);
        return Result.success();
    }

    /**
     * 删除用户
     *
     * @param id 用户id
     * @return 统一响应
     */
    @DeleteMapping("/{id}")
    public Result delete(@PathVariable Integer id) {
        log.info("删除用户，id：{}", id);
        userService.deleteById(id);
        return Result.success();
    }

    /**
     * 上传头像
     *
     * @param file 头像文件
     * @return 图片访问URL
     */
    @PostMapping("/upload")
    public Result<String> upload(@RequestParam("file") MultipartFile file) {
        log.info("上传头像，文件名：{}", file.getOriginalFilename());
        String imageUrl = ossUtils.upload(file);
        return Result.success(imageUrl);
    }
}
