package com.travel.diary.controller;

import com.travel.diary.common.Result;
import com.travel.diary.dto.PhoneAuthDTO;
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
        Map<String, Object> result = authService.wxLogin(loginDTO);
        return Result.success(result);
    }

    /**
     * 微信手机号授权
     */
    @Operation(summary = "微信手机号授权")
    @PostMapping("/phone-auth")
    public Result<String> phoneAuth(@RequestBody PhoneAuthDTO phoneAuthDTO) {
        String phone = authService.getPhoneNumber(phoneAuthDTO);
        return Result.success(phone);
    }

    /**
     * 调试用：直接使用提供的 code 调用微信 jscode2session
     * 方便在浏览器或 Postman 中比对返回结果
     */
    @Operation(summary = "微信code调试接口")
    @GetMapping("/wx-code-debug")
    public Result<Map<String, Object>> debugWxCode(@RequestParam String code) {
        WxLoginDTO dto = new WxLoginDTO();
        dto.setCode(code);
        // 复用现有的 wxLogin 调用链路，但不做用户创建，只把微信原始返回打印出来
        // 为避免影响正常逻辑，这里只返回一个简单的结果提示前端去看日志
        authService.wxLogin(dto);
        return Result.error(400, "仅用于调试：请查看服务器日志中的微信接口返回内容");
    }
}
