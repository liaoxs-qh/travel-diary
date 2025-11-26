package com.travel.diary.dto;

import lombok.Data;

/**
 * 日记查询DTO
 */
@Data
public class DiaryQueryDTO {
    
    private Long userId;
    private String city;
    private String province;
    private String keyword;
    private String tag;
    private Integer isPublic;
    
    // 分页参数
    private Integer pageNum = 1;
    private Integer pageSize = 10;
    
    // 排序字段
    private String orderBy = "created_at";
    private String orderDirection = "DESC";
}
