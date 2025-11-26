package com.travel.diary.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 日记图片实体类
 */
@Data
@TableName("diary_images")
public class DiaryImage implements Serializable {
    
    @TableId(type = IdType.AUTO)
    private Long id;
    
    private Long diaryId;
    private String imageUrl;
    private Integer sortOrder;
    
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createdAt;
}
