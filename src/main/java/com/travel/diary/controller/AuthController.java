package com.travel.diary.controller;

import com.travel.diary.common.Result;
import com.travel.diary.dto.WxLoginDTO;
import com.travel.diary.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * 认证控制器
 */
@Tag(name = "认证接口")
@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    /**
     * 微信登录
     */
    @Operation(summary = "微信登录")
    @PostMapping("/wx-login")
    public Result<Map<String, Object>> wxLogin(@RequestBody WxLoginDTO loginDTO) {
        Map<String, Object> result = authService.wxLogin(loginDTO.getCode());
        return Result.success(result);
    }
}
