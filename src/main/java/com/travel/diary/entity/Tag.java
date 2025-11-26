package com.travel.diary.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 标签实体类
 */
@Data
@TableName("tags")
public class Tag implements Serializable {
    
    @TableId(type = IdType.AUTO)
    private Long id;
    
    private String name;
    private Integer useCount;
    
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createdAt;
}
