package com.travel.diary.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 点赞实体类
 */
@Data
@TableName("likes")
public class Like implements Serializable {
    
    @TableId(type = IdType.AUTO)
    private Long id;
    
    private Long userId;
    private String targetType;  // diary, comment
    private Long targetId;
    
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createdAt;
}
