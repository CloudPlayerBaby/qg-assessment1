package com.yuriyuri.config;

import com.yuriyuri.interceptors.LoginInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Autowired
    private LoginInterceptor loginInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        //登录接口和注册接口不拦截
        registry.addInterceptor(loginInterceptor).excludePathPatterns(
                "/users/login",
                "/users/register",
                "/swagger-ui/**",          // Swagger UI 资源
                "/v3/api-docs/**",         // OpenAPI 文档 JSON
                "/swagger-ui.html"         // Swagger UI 入口
        );
    }
}
