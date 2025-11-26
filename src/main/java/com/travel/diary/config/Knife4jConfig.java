package com.travel.diary.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Knife4j API文档配置
 */
@Configuration
public class Knife4jConfig {
    
    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("旅行日记API文档")
                        .version("1.0.0")
                        .description("微信小程序旅行日记后端接口文档")
                        .contact(new Contact()
                                .name("开发团队")
                                .email("dev@example.com")));
    }
}
