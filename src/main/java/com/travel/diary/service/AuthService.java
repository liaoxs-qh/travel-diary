package com.travel.diary.service;

import com.travel.diary.dto.PhoneAuthDTO;
import com.travel.diary.dto.WxLoginDTO;

import java.util.Map;

/**
 * 认证服务接口
 */
public interface AuthService {
    
    /**
     * 微信登录
     * @param loginDTO 微信登录信息（包含code和用户信息）
     * @return 登录结果（包含token、userId、userInfo）
     */
    Map<String, Object> wxLogin(WxLoginDTO loginDTO);

    /**
     * 获取微信手机号
     * @param phoneAuthDTO 手机号授权信息
     * @return 手机号
     */
    String getPhoneNumber(PhoneAuthDTO phoneAuthDTO);

    /**
     * 调试用：直接调用微信 jscode2session，用于比对结果
     * @param code 微信登录 code
     * @return 微信原始返回字符串
     */
    default String debugWxCode(String code) {
        // 默认实现留空，具体调试由实现类覆盖（当前实现类中未直接使用）
        throw new UnsupportedOperationException("Not implemented");
    }
}
