package com.travel.diary.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

/**
 * 创建日记DTO
 */
@Data
public class DiaryCreateDTO {
    
    @NotBlank(message = "标题不能为空")
    private String title;
    
    @NotBlank(message = "内容不能为空")
    private String content;
    
    private String coverImage;
    
    // 位置信息
    private String location;
    private BigDecimal latitude;
    private BigDecimal longitude;
    private String city;
    private String province;
    private String country;
    
    // 旅行信息
    private LocalDate travelDate;
    private String weather;
    private String mood;
    
    // 图片列表
    private List<String> images;
    
    // 标签列表
    private List<String> tags;
    
    // 是否公开
    private Integer isPublic = 1;
}
