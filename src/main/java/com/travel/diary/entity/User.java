package com.travel.diary.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 用户实体类
 */
@Data
@TableName("users")
public class User implements Serializable {
    
    @TableId(type = IdType.AUTO)
    private Long id;
    
    private String openid;
    private String unionid;
    private String nickname;
    private String avatar;
    private String phone;  // 手机号
    private Integer gender;
    private String bio;
    private String coverImage;
    
    private Integer totalDiaries;
    private Integer totalPhotos;
    private Integer totalCities;
    private Integer totalLikes;
    private Integer totalFollowers;
    private Integer totalFollowing;
    
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createdAt;
    
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updatedAt;
}
