package com.travel.diary.dto;

import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

/**
 * 更新日记DTO
 */
@Data
public class DiaryUpdateDTO {
    
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
    
    private List<String> images;
    private List<String> tags;
    
    private Integer isPublic;
}
