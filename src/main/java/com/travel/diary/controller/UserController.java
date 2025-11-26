package com.travel.diary.controller;

import com.travel.diary.common.Result;
import com.travel.diary.dto.UserUpdateDTO;
import com.travel.diary.entity.User;
import com.travel.diary.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

/**
 * 用户Controller
 */
@Tag(name = "用户管理")
@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class UserController {
    
    private final UserService userService;
    
    @Operation(summary = "获取当前用户信息")
    @GetMapping("/me")
    public Result<User> getCurrentUser(
            @RequestHeader(value = "X-User-Id", required = false, defaultValue = "1") Long userId) {
        User user = userService.getById(userId);
        return Result.success(user);
    }
    
    @Operation(summary = "获取用户信息")
    @GetMapping("/{id}")
    public Result<User> getUserById(@PathVariable Long id) {
        User user = userService.getById(id);
        return Result.success(user);
    }
    
    @Operation(summary = "更新用户信息")
    @PutMapping("/me")
    public Result<User> updateUser(
            @RequestHeader(value = "X-User-Id", required = false, defaultValue = "1") Long userId,
            @RequestBody UserUpdateDTO dto) {
        User user = userService.updateUser(userId, dto);
        return Result.success(user);
    }
    
    @Operation(summary = "获取用户统计信息")
    @GetMapping("/{id}/stats")
    public Result<Map<String, Object>> getUserStats(@PathVariable Long id) {
        User user = userService.getById(id);
        Map<String, Object> stats = new HashMap<>();
        stats.put("totalDiaries", user.getTotalDiaries());
        stats.put("totalPhotos", user.getTotalPhotos());
        stats.put("totalCities", user.getTotalCities());
        stats.put("totalLikes", user.getTotalLikes());
        stats.put("totalFollowers", user.getTotalFollowers());
        stats.put("totalFollowing", user.getTotalFollowing());
        return Result.success(stats);
    }
    
    @Operation(summary = "关注/取消关注用户")
    @PostMapping("/{id}/follow")
    public Result<Void> toggleFollow(
            @RequestHeader(value = "X-User-Id", required = false, defaultValue = "1") Long userId,
            @PathVariable Long id) {
        userService.toggleFollow(userId, id);
        return Result.success();
    }
    
    @Operation(summary = "检查是否关注某用户")
    @GetMapping("/{id}/is-following")
    public Result<Boolean> isFollowing(
            @RequestHeader(value = "X-User-Id", required = false, defaultValue = "1") Long userId,
            @PathVariable Long id) {
        boolean isFollowing = userService.isFollowing(userId, id);
        return Result.success(isFollowing);
    }
}
