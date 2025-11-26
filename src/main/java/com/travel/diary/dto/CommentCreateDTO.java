package com.travel.diary.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

/**
 * 创建评论DTO
 */
@Data
public class CommentCreateDTO {
    
    @NotNull(message = "日记ID不能为空")
    private Long diaryId;
    
    private Long parentId;
    
    @NotBlank(message = "评论内容不能为空")
    private String content;
}
