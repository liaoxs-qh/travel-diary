package com.travel.diary.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.travel.diary.dto.UserUpdateDTO;
import com.travel.diary.entity.User;

/**
 * 用户服务接口
 */
public interface UserService extends IService<User> {
    
    /**
     * 根据openid查询用户
     */
    User getByOpenid(String openid);
    
    /**
     * 创建或更新用户
     */
    User createOrUpdate(String openid, String nickname, String avatar, Integer gender);
    
    /**
     * 更新用户信息
     */
    User updateUser(Long userId, UserUpdateDTO dto);
    
    /**
     * 关注/取消关注用户
     */
    void toggleFollow(Long followerId, Long followingId);
    
    /**
     * 检查是否关注某用户
     */
    boolean isFollowing(Long followerId, Long followingId);
}
