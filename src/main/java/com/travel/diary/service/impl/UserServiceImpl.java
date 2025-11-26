package com.travel.diary.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.travel.diary.common.exception.BusinessException;
import com.travel.diary.dto.UserUpdateDTO;
import com.travel.diary.entity.Follow;
import com.travel.diary.entity.User;
import com.travel.diary.mapper.FollowMapper;
import com.travel.diary.mapper.UserMapper;
import com.travel.diary.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

/**
 * 用户服务实现类
 */
@Service
@RequiredArgsConstructor
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {
    
    private final FollowMapper followMapper;
    
    @Override
    public User getByOpenid(String openid) {
        LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(User::getOpenid, openid);
        return this.getOne(wrapper);
    }
    
    @Override
    public User createOrUpdate(String openid, String nickname, String avatar, Integer gender) {
        User user = getByOpenid(openid);
        
        if (user == null) {
            // 新用户，创建
            user = new User();
            user.setOpenid(openid);
            user.setNickname(nickname);
            user.setAvatar(avatar);
            user.setGender(gender);
            user.setTotalDiaries(0);
            user.setTotalPhotos(0);
            user.setTotalCities(0);
            this.save(user);
        } else {
            // 老用户，更新信息
            user.setNickname(nickname);
            user.setAvatar(avatar);
            user.setGender(gender);
            this.updateById(user);
        }
        
        return user;
    }
    
    @Override
    public User updateUser(Long userId, UserUpdateDTO dto) {
        User user = this.getById(userId);
        if (user == null) {
            throw new BusinessException("用户不存在");
        }
        
        if (StringUtils.hasText(dto.getNickname())) {
            user.setNickname(dto.getNickname());
        }
        if (dto.getAvatar() != null) {
            user.setAvatar(dto.getAvatar());
        }
        if (dto.getGender() != null) {
            user.setGender(dto.getGender());
        }
        if (dto.getBio() != null) {
            user.setBio(dto.getBio());
        }
        if (dto.getCoverImage() != null) {
            user.setCoverImage(dto.getCoverImage());
        }
        
        this.updateById(user);
        return user;
    }
    
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void toggleFollow(Long followerId, Long followingId) {
        if (followerId.equals(followingId)) {
            throw new BusinessException("不能关注自己");
        }
        
        // 检查是否已关注
        LambdaQueryWrapper<Follow> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Follow::getFollowerId, followerId)
                .eq(Follow::getFollowingId, followingId);
        
        Follow existingFollow = followMapper.selectOne(wrapper);
        
        if (existingFollow != null) {
            // 取消关注
            followMapper.deleteById(existingFollow.getId());
            
            // 更新统计
            UpdateWrapper<User> updateWrapper1 = new UpdateWrapper<>();
            updateWrapper1.eq("id", followerId)
                    .setSql("total_following = total_following - 1");
            this.update(null, updateWrapper1);
            
            UpdateWrapper<User> updateWrapper2 = new UpdateWrapper<>();
            updateWrapper2.eq("id", followingId)
                    .setSql("total_followers = total_followers - 1");
            this.update(null, updateWrapper2);
        } else {
            // 关注
            Follow follow = new Follow();
            follow.setFollowerId(followerId);
            follow.setFollowingId(followingId);
            followMapper.insert(follow);
            
            // 更新统计
            UpdateWrapper<User> updateWrapper1 = new UpdateWrapper<>();
            updateWrapper1.eq("id", followerId)
                    .setSql("total_following = total_following + 1");
            this.update(null, updateWrapper1);
            
            UpdateWrapper<User> updateWrapper2 = new UpdateWrapper<>();
            updateWrapper2.eq("id", followingId)
                    .setSql("total_followers = total_followers + 1");
            this.update(null, updateWrapper2);
        }
    }
    
    @Override
    public boolean isFollowing(Long followerId, Long followingId) {
        Long count = followMapper.selectCount(
                new LambdaQueryWrapper<Follow>()
                        .eq(Follow::getFollowerId, followerId)
                        .eq(Follow::getFollowingId, followingId));
        return count > 0;
    }
}
