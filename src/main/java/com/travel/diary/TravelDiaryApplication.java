package com.travel.diary;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * 旅行日记应用启动类
 */
@SpringBootApplication
@MapperScan("com.travel.diary.mapper")
public class TravelDiaryApplication {
    
    public static void main(String[] args) {
        SpringApplication.run(TravelDiaryApplication.class, args);
        System.out.println("========================================");
        System.out.println("旅行日记后端服务启动成功！");
        System.out.println("API文档地址: http://localhost:8080/doc.html");
        System.out.println("========================================");
    }
}
