package com.qk.config;

import com.qk.utils.JwtUtils;
import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;

/**
 * Token 过滤器 - 拦截所有请求验证 JWT
 * 注意：不要加 @Component，由 FilterConfig 统一注册
 */
@Slf4j
public class TokenFilter implements Filter {

    private final JwtUtils jwtUtils;

    public TokenFilter(JwtUtils jwtUtils) {
        this.jwtUtils = jwtUtils;
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse resp = (HttpServletResponse) response;

        // 1. 获取请求路径
        String uri = req.getRequestURI();

        // 2. 放行登录/注册接口、静态资源和 OPTIONS 预检请求
        if ("/login".equals(uri) || "/register".equals(uri) || uri.startsWith("/uploads/") || uri.startsWith("/user/upload") || "OPTIONS".equalsIgnoreCase(req.getMethod())) {
            chain.doFilter(request, response);
            return;
        }

        // 3. 获取 token（从 Header 中）
        String token = req.getHeader("Authorization");

        // 4. 验证 token
        if (token == null || !jwtUtils.validateToken(token)) {
            resp.setContentType("application/json;charset=UTF-8");
            resp.setStatus(401);
            resp.getWriter().write("{\"code\":0,\"msg\":\"未登录或token已过期\"}");
            return;
        }

        // 5. 将用户信息存入 request，供后续使用
        Integer userId = jwtUtils.getUserIdFromToken(token);
        String username = jwtUtils.getUsernameFromToken(token);
        req.setAttribute("userId", userId);
        req.setAttribute("username", username);

        // 6. 放行
        chain.doFilter(request, response);
    }
}
