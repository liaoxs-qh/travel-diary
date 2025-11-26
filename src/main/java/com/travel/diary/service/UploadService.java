package com.travel.diary.service;

import org.springframework.web.multipart.MultipartFile;

/**
 * 文件上传Service接口
 */
public interface UploadService {
    
    /**
     * 上传图片
     * @param file 图片文件
     * @return 图片URL
     */
    String uploadImage(MultipartFile file);
    
    /**
     * 批量上传图片
     * @param files 图片文件数组
     * @return 图片URL列表
     */
    String[] uploadImages(MultipartFile[] files);
}
