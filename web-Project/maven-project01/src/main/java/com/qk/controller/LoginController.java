package com.qk.controller;

import com.qk.common.Result;
import com.qk.dto.LoginDto;
import com.qk.entity.User;
import com.qk.service.UserService;
import com.qk.utils.JwtUtils;
import com.qk.vo.LoginVo;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * 登录注册 Controller
 */
@Slf4j
@RestController
public class LoginController {

    @Autowired
    private UserService userService;

    @Autowired
    private JwtUtils jwtUtils;

    /**
     * 用户登录
     *
     * @param loginDto 登录参数（用户名、密码）
     * @return 用户信息 + token
     */
    @PostMapping("/login")
    public Result<LoginVo> login(@Valid @RequestBody LoginDto loginDto) {
        log.info("用户登录：{}", loginDto.getUsername());

        // 1. 调用 Service 查询用户
        User user = userService.login(loginDto.getUsername(), loginDto.getPassword());

        // 2. 验证用户
        if (user == null) {
            return Result.error("用户名或密码错误");
        }

        // 3. 生成 token
        String token = jwtUtils.generateToken(user.getId(), user.getUsername());

        // 4. 封装返回数据
        LoginVo loginVo = new LoginVo();
        BeanUtils.copyProperties(user, loginVo);
        loginVo.setToken(token);

        return Result.success(loginVo);
    }

    /**
     * 用户注册
     *
     * @param user 用户信息（username, name, phone, email, password）
     * @return 统一响应
     */
    @PostMapping("/register")
    public Result register(@Valid @RequestBody User user) {
        log.info("用户注册：{}", user.getUsername());
        userService.add(user);
        return Result.success();
    }
}
