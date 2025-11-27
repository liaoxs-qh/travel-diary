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
}
