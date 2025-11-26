package com.travel.diary.service;

import java.util.Map;

/**
 * 认证服务接口
 */
public interface AuthService {
    
    /**
     * 微信登录
     * @param code 微信登录code
     * @return 登录结果（包含token、userId、userInfo）
     */
    Map<String, Object> wxLogin(String code);
}
