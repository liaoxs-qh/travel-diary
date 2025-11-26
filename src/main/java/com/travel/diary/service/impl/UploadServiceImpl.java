package com.travel.diary.service.impl;

import com.travel.diary.common.exception.BusinessException;
import com.travel.diary.service.UploadService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

/**
 * 文件上传Service实现类
 * 简单实现：保存到本地文件系统
 * 生产环境建议使用OSS
 */
@Slf4j
@Service
public class UploadServiceImpl implements UploadService {
    
    @Value("${upload.path:./uploads}")
    private String uploadPath;
    
    @Value("${upload.base-url:http://localhost:8080}")
    private String baseUrl;
    
    private static final long MAX_FILE_SIZE = 10 * 1024 * 1024; // 10MB
    private static final String[] ALLOWED_EXTENSIONS = {".jpg", ".jpeg", ".png", ".gif", ".webp"};
    
    @Override
    public String uploadImage(MultipartFile file) {
        if (file == null || file.isEmpty()) {
            throw new BusinessException("文件不能为空");
        }
        
        // 检查文件大小
        if (file.getSize() > MAX_FILE_SIZE) {
            throw new BusinessException("文件大小不能超过10MB");
        }
        
        // 检查文件类型
        String originalFilename = file.getOriginalFilename();
        if (originalFilename == null) {
            throw new BusinessException("文件名不能为空");
        }
        
        String extension = originalFilename.substring(originalFilename.lastIndexOf(".")).toLowerCase();
        boolean isAllowed = false;
        for (String ext : ALLOWED_EXTENSIONS) {
            if (ext.equals(extension)) {
                isAllowed = true;
                break;
            }
        }
        if (!isAllowed) {
            throw new BusinessException("只支持上传图片文件（jpg, jpeg, png, gif, webp）");
        }
        
        try {
            // 生成文件名：日期/UUID.扩展名
            String date = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyyMMdd"));
            String filename = UUID.randomUUID().toString() + extension;
            String relativePath = date + "/" + filename;
            
            // 创建目录
            Path dirPath = Paths.get(uploadPath, date);
            if (!Files.exists(dirPath)) {
                Files.createDirectories(dirPath);
            }
            
            // 保存文件
            Path filePath = Paths.get(uploadPath, relativePath);
            file.transferTo(filePath.toFile());
            
            // 返回URL
            return baseUrl + "/uploads/" + relativePath;
            
        } catch (IOException e) {
            log.error("文件上传失败", e);
            throw new BusinessException("文件上传失败");
        }
    }
    
    @Override
    public String[] uploadImages(MultipartFile[] files) {
        if (files == null || files.length == 0) {
            throw new BusinessException("文件不能为空");
        }
        
        String[] urls = new String[files.length];
        for (int i = 0; i < files.length; i++) {
            urls[i] = uploadImage(files[i]);
        }
        return urls;
    }
}
