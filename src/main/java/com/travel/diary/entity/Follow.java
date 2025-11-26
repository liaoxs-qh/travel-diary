package com.travel.diary.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 关注实体类
 */
@Data
@TableName("follows")
public class Follow implements Serializable {
    
    @TableId(type = IdType.AUTO)
    private Long id;
    
    private Long followerId;    // 关注者
    private Long followingId;   // 被关注者
    
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createdAt;
}
