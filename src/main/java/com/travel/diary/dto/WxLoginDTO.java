package com.travel.diary.dto;

import lombok.Data;

/**
 * 微信登录DTO
 */
@Data
public class WxLoginDTO {
    /**
     * 微信登录code
     */
    private String code;
    
    /**
     * 用户昵称
     */
    private String nickName;
    
    /**
     * 用户头像
     */
    private String avatarUrl;
    
    /**
     * 性别 0-未知 1-男 2-女
     */
    private Integer gender;
    
    /**
     * 国家
     */
    private String country;
    
    /**
     * 省份
     */
    private String province;
    
    /**
     * 城市
     */
    private String city;
}
