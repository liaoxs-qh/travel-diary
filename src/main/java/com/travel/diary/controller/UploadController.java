package com.travel.diary.controller;

import com.travel.diary.common.Result;
import com.travel.diary.service.UploadService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

/**
 * 文件上传Controller
 */
@Tag(name = "文件上传")
@RestController
@RequestMapping("/api/v1/upload")
@RequiredArgsConstructor
public class UploadController {
    
    private final UploadService uploadService;
    
    @Operation(summary = "上传单张图片")
    @PostMapping("/image")
    public Result<String> uploadImage(@RequestParam("file") MultipartFile file) {
        String url = uploadService.uploadImage(file);
        return Result.success(url);
    }
    
    @Operation(summary = "批量上传图片")
    @PostMapping("/images")
    public Result<String[]> uploadImages(@RequestParam("files") MultipartFile[] files) {
        String[] urls = uploadService.uploadImages(files);
        return Result.success(urls);
    }
}
