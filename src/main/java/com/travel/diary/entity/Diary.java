package com.travel.diary.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 日记实体类
 */
@Data
@TableName("diaries")
public class Diary implements Serializable {
    
    @TableId(type = IdType.AUTO)
    private Long id;
    
    private Long userId;
    private String title;
    private String content;
    private String coverImage;
    
    private String location;
    private BigDecimal latitude;
    private BigDecimal longitude;
    private String city;
    private String province;
    private String country;
    
    private LocalDate travelDate;
    private String weather;
    private String mood;
    
    private Integer isPublic;
    private Integer viewCount;
    private Integer likeCount;
    private Integer commentCount;
    private Integer collectCount;
    
    @TableLogic
    private Integer deleted;
    
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createdAt;
    
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updatedAt;
    
    // 非数据库字段
    @TableField(exist = false)
    private List<String> images;
    
    @TableField(exist = false)
    private List<String> tags;
    
    @TableField(exist = false)
    private String userNickname;
    
    @TableField(exist = false)
    private String userAvatar;
    
    @TableField(exist = false)
    private Boolean isLiked;
    
    @TableField(exist = false)
    private Boolean isCollected;
}
