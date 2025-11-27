package com.travel.diary.dto;

import lombok.Data;

/**
 * 微信手机号授权DTO
 */
@Data
public class PhoneAuthDTO {
    private String code;           // 手机号授权 code
    private String encryptedData;  // 加密数据（可选，新版API不需要）
    private String iv;             // 初始向量（可选，新版API不需要）
}
