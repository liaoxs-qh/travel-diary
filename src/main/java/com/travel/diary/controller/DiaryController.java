package com.travel.diary.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.travel.diary.common.Result;
import com.travel.diary.dto.DiaryCreateDTO;
import com.travel.diary.dto.DiaryQueryDTO;
import com.travel.diary.dto.DiaryUpdateDTO;
import com.travel.diary.entity.Diary;
import com.travel.diary.service.DiaryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

/**
 * 日记Controller
 */
@Tag(name = "日记管理")
@RestController
@RequestMapping("/api/v1/diaries")
@RequiredArgsConstructor
public class DiaryController {
    
    private final DiaryService diaryService;
    
    @Operation(summary = "创建日记")
    @PostMapping
    public Result<Diary> createDiary(
            @RequestHeader(value = "X-User-Id", required = false, defaultValue = "1") Long userId,
            @Valid @RequestBody DiaryCreateDTO dto) {
        Diary diary = diaryService.createDiary(userId, dto);
        return Result.success(diary);
    }
    
    @Operation(summary = "更新日记")
    @PutMapping("/{id}")
    public Result<Diary> updateDiary(
            @RequestHeader(value = "X-User-Id", required = false, defaultValue = "1") Long userId,
            @PathVariable Long id,
            @RequestBody DiaryUpdateDTO dto) {
        Diary diary = diaryService.updateDiary(userId, id, dto);
        return Result.success(diary);
    }
    
    @Operation(summary = "删除日记")
    @DeleteMapping("/{id}")
    public Result<Void> deleteDiary(
            @RequestHeader(value = "X-User-Id", required = false, defaultValue = "1") Long userId,
            @PathVariable Long id) {
        diaryService.deleteDiary(userId, id);
        return Result.success();
    }
    
    @Operation(summary = "获取日记详情")
    @GetMapping("/{id}")
    public Result<Diary> getDiaryDetail(
            @RequestHeader(value = "X-User-Id", required = false) Long userId,
            @PathVariable Long id) {
        // 增加浏览量
        diaryService.incrementViewCount(id);
        Diary diary = diaryService.getDiaryDetail(id, userId);
        return Result.success(diary);
    }
    
    @Operation(summary = "查询日记列表")
    @GetMapping
    public Result<Page<Diary>> getDiaryList(
            @RequestHeader(value = "X-User-Id", required = false) Long userId,
            DiaryQueryDTO queryDTO) {
        Page<Diary> page = diaryService.getDiaryList(queryDTO, userId);
        return Result.success(page);
    }
    
    @Operation(summary = "获取用户的日记列表")
    @GetMapping("/user/{userId}")
    public Result<Page<Diary>> getUserDiaries(
            @RequestHeader(value = "X-User-Id", required = false) Long currentUserId,
            @PathVariable Long userId,
            @RequestParam(defaultValue = "1") Integer pageNum,
            @RequestParam(defaultValue = "10") Integer pageSize) {
        Page<Diary> page = diaryService.getUserDiaries(userId, pageNum, pageSize, currentUserId);
        return Result.success(page);
    }
    
    @Operation(summary = "获取关注用户的日记（动态流）")
    @GetMapping("/following")
    public Result<Page<Diary>> getFollowingDiaries(
            @RequestHeader(value = "X-User-Id", required = false, defaultValue = "1") Long userId,
            @RequestParam(defaultValue = "1") Integer pageNum,
            @RequestParam(defaultValue = "10") Integer pageSize) {
        Page<Diary> page = diaryService.getFollowingDiaries(userId, pageNum, pageSize);
        return Result.success(page);
    }
    
    @Operation(summary = "点赞/取消点赞日记")
    @PostMapping("/{id}/like")
    public Result<Void> toggleLike(
            @RequestHeader(value = "X-User-Id", required = false, defaultValue = "1") Long userId,
            @PathVariable Long id) {
        diaryService.toggleLike(userId, id);
        return Result.success();
    }
    
    @Operation(summary = "收藏/取消收藏日记")
    @PostMapping("/{id}/collect")
    public Result<Void> toggleCollect(
            @RequestHeader(value = "X-User-Id", required = false, defaultValue = "1") Long userId,
            @PathVariable Long id) {
        diaryService.toggleCollect(userId, id);
        return Result.success();
    }
}
