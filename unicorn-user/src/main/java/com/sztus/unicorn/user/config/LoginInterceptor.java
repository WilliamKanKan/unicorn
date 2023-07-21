package com.sztus.unicorn.user.config;

import com.sztus.unicorn.lib.cache.core.SimpleRedisRepository;
import com.sztus.unicorn.lib.core.type.RedisKeyType;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Slf4j
@Component
public class LoginInterceptor implements HandlerInterceptor {
    @Autowired
    private SimpleRedisRepository simpleRedisRepository;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        log.info("进入到拦截器中:preHandle() 方法");
        log.info(request.getLocalAddr());
        log.info(request.getLocalName());
        System.out.println(request.getServletPath());
        String tokenKey = request.getHeader("Access-Token");
        log.info(tokenKey);
        // 根据token从Redis中获取用户信息
        String userKey = simpleRedisRepository.generateKey(RedisKeyType.USER, tokenKey);
        String userInfoJson = simpleRedisRepository.get(userKey);
        log.info(userInfoJson);
        if (userInfoJson != null) {
            return true;
        } else {
            // 拦截请求，返回未登录或其他错误信息
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getWriter().write("Unauthorized");
            return false;
        }
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        setCorsHeaders(response); // 设置跨域头部配置
    }

    private void setCorsHeaders(HttpServletResponse response) {
        response.setHeader("Access-Control-Allow-Origin", "*");
        response.setHeader("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE");
        response.setHeader("Access-Control-Allow-Headers", "*");
        response.setHeader("Access-Control-Allow-Credentials", "true");
    }

}
