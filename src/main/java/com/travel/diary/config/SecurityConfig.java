package com.travel.diary.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;

/**
 * Spring Security配置类
 * 注意：当前配置为开发模式，所有接口都可访问
 * 生产环境需要配置JWT认证
 */
@Configuration
@EnableWebSecurity
public class SecurityConfig {
    
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                // 禁用CSRF（前后端分离项目）
                .csrf(AbstractHttpConfigurer::disable)
                // 允许所有请求（开发阶段）
                .authorizeHttpRequests(auth -> auth
                        .anyRequest().permitAll()
                );
        
        return http.build();
    }
}
