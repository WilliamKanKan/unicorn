package com.sztus.unicorn.user.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class InterceptorAdapterConfig  implements WebMvcConfigurer {

    private final LoginInterceptor loginInterceptor;
    private final ChatInterceptor chatInterceptor;

    public InterceptorAdapterConfig(LoginInterceptor loginInterceptor,ChatInterceptor chatInterceptor) {
        this.loginInterceptor = loginInterceptor;
        this.chatInterceptor = chatInterceptor;
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry)
    {
        //注册自己的拦截器并设置拦截的请求路径
        registry.addInterceptor(loginInterceptor).addPathPatterns("/**")
                .excludePathPatterns("/login","/user/limit_cut","/register","/send_code","/reset","/search/*","/user/query","/user/queryable","/test/chat","/search/query_by_id");
        registry.addInterceptor(chatInterceptor).addPathPatterns("/search/chat","/search/query","/user/query","/search/delete","/test/chat");


    }
}


