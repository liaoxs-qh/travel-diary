package com.travel.diary.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 评论实体类
 */
@Data
@TableName("comments")
public class Comment implements Serializable {
    
    @TableId(type = IdType.AUTO)
    private Long id;
    
    private Long diaryId;
    private Long userId;
    private Long parentId;
    private String content;
    private Integer likeCount;
    
    @TableLogic
    private Integer deleted;
    
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createdAt;
    
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updatedAt;
    
    // 非数据库字段
    @TableField(exist = false)
    private String userNickname;
    
    @TableField(exist = false)
    private String userAvatar;
}
