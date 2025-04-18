package com.luckyvicky.config.web;

import com.luckyvicky.common.interceptor.AuthInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addInterceptors(InterceptorRegistry registry) {

        registry.addInterceptor(new AuthInterceptor())
                .addPathPatterns("/admin/**")
                .excludePathPatterns("/css/**", "/js/**", "/admin/auth/login", "/api/admin/auth/login");
    }
}
