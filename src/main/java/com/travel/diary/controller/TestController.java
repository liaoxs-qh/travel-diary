package com.travel.diary.controller;

import com.travel.diary.common.Result;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

/**
 * 测试控制器
 */
@Tag(name = "测试接口")
@RestController
@RequestMapping("/api/v1/test")
public class TestController {
    
    @Operation(summary = "健康检查")
    @GetMapping("/health")
    public Result<Map<String, Object>> health() {
        Map<String, Object> data = new HashMap<>();
        data.put("status", "UP");
        data.put("timestamp", LocalDateTime.now());
        data.put("message", "旅行日记后端服务运行正常");
        return Result.success(data);
    }
    
    @Operation(summary = "Hello World")
    @GetMapping("/hello")
    public Result<String> hello() {
        return Result.success("Hello, Travel Diary!");
    }
}
