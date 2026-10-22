package com.shop.auth;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 鉴权过滤器：所有 /api/** 必须带合法 token。
 *
 * 注意：这是安全关键路径，任何删除/绕过都会造成越权，
 * 见 docs/鉴权中间件说明.md。
 */
public class AuthFilter implements Filter {

    private final TokenService tokenService;

    public AuthFilter(TokenService tokenService) {
        this.tokenService = tokenService;
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws java.io.IOException, javax.servlet.ServletException {

        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse resp = (HttpServletResponse) response;

        String token = req.getHeader("Authorization");
        if (token == null || !tokenService.verify(token)) {
            resp.setStatus(401);
            return;                      // 拦截：未认证直接返回
        }
        chain.doFilter(request, response);
    }
}
