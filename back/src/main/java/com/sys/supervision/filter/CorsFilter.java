package com.sys.supervision.filter;

import lombok.extern.slf4j.Slf4j;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 跨域拦截器
 *
 * 浏览器发送跨域请求前会发送一个options请求进行预检，后端直接通过该请求，直接返回200，并附上对应的请求头
 */
@Component
@Slf4j
@Order(1)
public class CorsFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, FilterChain filterChain) throws ServletException, IOException {
        String methodName = httpServletRequest.getMethod();
        httpServletResponse.addHeader("Access-Control-Allow-Origin", httpServletRequest.getHeader("Origin"));
        httpServletResponse.addHeader("Access-Control-Allow-Methods","GET, OPTIONS, HEAD, PUT, POST, DELETE");
        httpServletResponse.addHeader("Access-Control-Allow-Headers", "x-auth-token,Content-Type,type,Content-Disposition");
        httpServletResponse.addHeader("Access-Control-Expose-Headers", "Content-Disposition");
        if ("OPTIONS".equals(methodName)) {
            httpServletResponse.setStatus(HttpServletResponse.SC_OK);
            return;
        }
        filterChain.doFilter(httpServletRequest, httpServletResponse);
    }
}
