package com.travel.diary.dto;

import lombok.Data;

/**
 * 用户更新DTO
 */
@Data
public class UserUpdateDTO {
    
    private String nickname;
    private String avatar;
    private Integer gender;
    private String bio;
    private String coverImage;
}
