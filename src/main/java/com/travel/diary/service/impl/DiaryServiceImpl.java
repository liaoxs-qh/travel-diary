package com.travel.diary.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.travel.diary.common.exception.BusinessException;
import com.travel.diary.dto.DiaryCreateDTO;
import com.travel.diary.dto.DiaryQueryDTO;
import com.travel.diary.dto.DiaryUpdateDTO;
import com.travel.diary.entity.*;
import com.travel.diary.mapper.*;
import com.travel.diary.service.DiaryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 日记Service实现类
 */
@Service
@RequiredArgsConstructor
public class DiaryServiceImpl implements DiaryService {
    
    private final DiaryMapper diaryMapper;
    private final DiaryImageMapper diaryImageMapper;
    private final TagMapper tagMapper;
    private final LikeMapper likeMapper;
    private final UserMapper userMapper;
    private final FollowMapper followMapper;
    
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Diary createDiary(Long userId, DiaryCreateDTO dto) {
        // 创建日记
        Diary diary = new Diary();
        diary.setUserId(userId);
        diary.setTitle(dto.getTitle());
        diary.setContent(dto.getContent());
        diary.setCoverImage(dto.getCoverImage());
        diary.setLocation(dto.getLocation());
        diary.setLatitude(dto.getLatitude());
        diary.setLongitude(dto.getLongitude());
        diary.setCity(dto.getCity());
        diary.setProvince(dto.getProvince());
        diary.setCountry(dto.getCountry());
        diary.setTravelDate(dto.getTravelDate());
        diary.setWeather(dto.getWeather());
        diary.setMood(dto.getMood());
        diary.setIsPublic(dto.getIsPublic());
        diary.setViewCount(0);
        diary.setLikeCount(0);
        diary.setCommentCount(0);
        diary.setCollectCount(0);
        
        diaryMapper.insert(diary);
        
        // 保存图片
        if (dto.getImages() != null && !dto.getImages().isEmpty()) {
            for (int i = 0; i < dto.getImages().size(); i++) {
                DiaryImage image = new DiaryImage();
                image.setDiaryId(diary.getId());
                image.setImageUrl(dto.getImages().get(i));
                image.setSortOrder(i);
                diaryImageMapper.insert(image);
            }
        }
        
        // 保存标签
        if (dto.getTags() != null && !dto.getTags().isEmpty()) {
            saveDiaryTags(diary.getId(), dto.getTags());
        }
        
        // 更新用户统计
        updateUserStats(userId);
        
        return diary;
    }
    
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Diary updateDiary(Long userId, Long diaryId, DiaryUpdateDTO dto) {
        Diary diary = diaryMapper.selectById(diaryId);
        if (diary == null) {
            throw new BusinessException("日记不存在");
        }
        if (!diary.getUserId().equals(userId)) {
            throw new BusinessException("无权限修改此日记");
        }
        
        // 更新基本信息
        if (StringUtils.hasText(dto.getTitle())) {
            diary.setTitle(dto.getTitle());
        }
        if (StringUtils.hasText(dto.getContent())) {
            diary.setContent(dto.getContent());
        }
        if (dto.getCoverImage() != null) {
            diary.setCoverImage(dto.getCoverImage());
        }
        if (dto.getLocation() != null) {
            diary.setLocation(dto.getLocation());
        }
        if (dto.getLatitude() != null) {
            diary.setLatitude(dto.getLatitude());
        }
        if (dto.getLongitude() != null) {
            diary.setLongitude(dto.getLongitude());
        }
        if (dto.getCity() != null) {
            diary.setCity(dto.getCity());
        }
        if (dto.getProvince() != null) {
            diary.setProvince(dto.getProvince());
        }
        if (dto.getTravelDate() != null) {
            diary.setTravelDate(dto.getTravelDate());
        }
        if (dto.getWeather() != null) {
            diary.setWeather(dto.getWeather());
        }
        if (dto.getMood() != null) {
            diary.setMood(dto.getMood());
        }
        if (dto.getIsPublic() != null) {
            diary.setIsPublic(dto.getIsPublic());
        }
        
        diaryMapper.updateById(diary);
        
        // 更新图片
        if (dto.getImages() != null) {
            // 删除旧图片
            diaryImageMapper.delete(new LambdaQueryWrapper<DiaryImage>()
                    .eq(DiaryImage::getDiaryId, diaryId));
            // 保存新图片
            for (int i = 0; i < dto.getImages().size(); i++) {
                DiaryImage image = new DiaryImage();
                image.setDiaryId(diaryId);
                image.setImageUrl(dto.getImages().get(i));
                image.setSortOrder(i);
                diaryImageMapper.insert(image);
            }
        }
        
        // 更新标签
        if (dto.getTags() != null) {
            saveDiaryTags(diaryId, dto.getTags());
        }
        
        return diary;
    }
    
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteDiary(Long userId, Long diaryId) {
        Diary diary = diaryMapper.selectById(diaryId);
        if (diary == null) {
            throw new BusinessException("日记不存在");
        }
        if (!diary.getUserId().equals(userId)) {
            throw new BusinessException("无权限删除此日记");
        }
        
        // 逻辑删除
        diaryMapper.deleteById(diaryId);
        
        // 更新用户统计
        updateUserStats(userId);
    }
    
    @Override
    public Diary getDiaryDetail(Long diaryId, Long currentUserId) {
        Diary diary = diaryMapper.selectById(diaryId);
        if (diary == null) {
            throw new BusinessException("日记不存在");
        }
        
        // 填充额外信息
        enrichDiaryInfo(diary, currentUserId);
        
        return diary;
    }
    
    @Override
    public Page<Diary> getDiaryList(DiaryQueryDTO queryDTO, Long currentUserId) {
        Page<Diary> page = new Page<>(queryDTO.getPageNum(), queryDTO.getPageSize());
        
        LambdaQueryWrapper<Diary> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(queryDTO.getUserId() != null, Diary::getUserId, queryDTO.getUserId())
                .eq(queryDTO.getCity() != null, Diary::getCity, queryDTO.getCity())
                .eq(queryDTO.getProvince() != null, Diary::getProvince, queryDTO.getProvince())
                .eq(queryDTO.getIsPublic() != null, Diary::getIsPublic, queryDTO.getIsPublic())
                .and(StringUtils.hasText(queryDTO.getKeyword()), w -> 
                    w.like(Diary::getTitle, queryDTO.getKeyword())
                     .or()
                     .like(Diary::getContent, queryDTO.getKeyword()))
                .orderByDesc(Diary::getCreatedAt);
        
        Page<Diary> result = diaryMapper.selectPage(page, wrapper);
        
        // 填充额外信息
        result.getRecords().forEach(diary -> enrichDiaryInfo(diary, currentUserId));
        
        return result;
    }
    
    @Override
    public Page<Diary> getUserDiaries(Long userId, Integer pageNum, Integer pageSize, Long currentUserId) {
        Page<Diary> page = new Page<>(pageNum, pageSize);
        
        LambdaQueryWrapper<Diary> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Diary::getUserId, userId);
        
        // 如果不是本人，只显示公开的日记
        if (currentUserId == null || !currentUserId.equals(userId)) {
            wrapper.eq(Diary::getIsPublic, 1);
        }
        
        wrapper.orderByDesc(Diary::getCreatedAt);
        
        Page<Diary> result = diaryMapper.selectPage(page, wrapper);
        result.getRecords().forEach(diary -> enrichDiaryInfo(diary, currentUserId));
        
        return result;
    }
    
    @Override
    public Page<Diary> getFollowingDiaries(Long userId, Integer pageNum, Integer pageSize) {
        // 获取关注的用户ID列表
        List<Long> followingIds = followMapper.selectList(
                new LambdaQueryWrapper<Follow>().eq(Follow::getFollowerId, userId))
                .stream()
                .map(Follow::getFollowingId)
                .collect(Collectors.toList());
        
        if (followingIds.isEmpty()) {
            return new Page<>(pageNum, pageSize);
        }
        
        Page<Diary> page = new Page<>(pageNum, pageSize);
        LambdaQueryWrapper<Diary> wrapper = new LambdaQueryWrapper<>();
        wrapper.in(Diary::getUserId, followingIds)
                .eq(Diary::getIsPublic, 1)
                .orderByDesc(Diary::getCreatedAt);
        
        Page<Diary> result = diaryMapper.selectPage(page, wrapper);
        result.getRecords().forEach(diary -> enrichDiaryInfo(diary, userId));
        
        return result;
    }
    
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void toggleLike(Long userId, Long diaryId) {
        // 检查是否已点赞
        LambdaQueryWrapper<Like> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Like::getUserId, userId)
                .eq(Like::getTargetType, "diary")
                .eq(Like::getTargetId, diaryId);
        
        Like existingLike = likeMapper.selectOne(wrapper);
        
        if (existingLike != null) {
            // 取消点赞
            likeMapper.deleteById(existingLike.getId());
            
            UpdateWrapper<Diary> updateWrapper = new UpdateWrapper<>();
            updateWrapper.eq("id", diaryId)
                    .setSql("like_count = like_count - 1");
            diaryMapper.update(null, updateWrapper);
        } else {
            // 点赞
            Like like = new Like();
            like.setUserId(userId);
            like.setTargetType("diary");
            like.setTargetId(diaryId);
            likeMapper.insert(like);
            
            UpdateWrapper<Diary> updateWrapper = new UpdateWrapper<>();
            updateWrapper.eq("id", diaryId)
                    .setSql("like_count = like_count + 1");
            diaryMapper.update(null, updateWrapper);
        }
    }
    
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void toggleCollect(Long userId, Long diaryId) {
        // 检查是否已收藏
        LambdaQueryWrapper<Like> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Like::getUserId, userId)
                .eq(Like::getTargetType, "collect")
                .eq(Like::getTargetId, diaryId);
        
        Like existingCollect = likeMapper.selectOne(wrapper);
        
        if (existingCollect != null) {
            // 取消收藏
            likeMapper.deleteById(existingCollect.getId());
            
            UpdateWrapper<Diary> updateWrapper = new UpdateWrapper<>();
            updateWrapper.eq("id", diaryId)
                    .setSql("collect_count = collect_count - 1");
            diaryMapper.update(null, updateWrapper);
        } else {
            // 收藏
            Like collect = new Like();
            collect.setUserId(userId);
            collect.setTargetType("collect");
            collect.setTargetId(diaryId);
            likeMapper.insert(collect);
            
            UpdateWrapper<Diary> updateWrapper = new UpdateWrapper<>();
            updateWrapper.eq("id", diaryId)
                    .setSql("collect_count = collect_count + 1");
            diaryMapper.update(null, updateWrapper);
        }
    }
    
    @Override
    public void incrementViewCount(Long diaryId) {
        UpdateWrapper<Diary> updateWrapper = new UpdateWrapper<>();
        updateWrapper.eq("id", diaryId)
                .setSql("view_count = view_count + 1");
        diaryMapper.update(null, updateWrapper);
    }
    
    /**
     * 填充日记的额外信息（图片、标签、用户信息等）
     */
    private void enrichDiaryInfo(Diary diary, Long currentUserId) {
        // 获取图片列表
        List<DiaryImage> images = diaryImageMapper.selectList(
                new LambdaQueryWrapper<DiaryImage>()
                        .eq(DiaryImage::getDiaryId, diary.getId())
                        .orderByAsc(DiaryImage::getSortOrder));
        diary.setImages(images.stream().map(DiaryImage::getImageUrl).collect(Collectors.toList()));
        
        // 获取用户信息
        User user = userMapper.selectById(diary.getUserId());
        if (user != null) {
            diary.setUserNickname(user.getNickname());
            diary.setUserAvatar(user.getAvatar());
        }
        
        // 检查当前用户是否点赞
        if (currentUserId != null) {
            Long likeCount = likeMapper.selectCount(new LambdaQueryWrapper<Like>()
                    .eq(Like::getUserId, currentUserId)
                    .eq(Like::getTargetType, "diary")
                    .eq(Like::getTargetId, diary.getId()));
            diary.setIsLiked(likeCount > 0);
            
            // 检查是否收藏
            Long collectCount = likeMapper.selectCount(new LambdaQueryWrapper<Like>()
                    .eq(Like::getUserId, currentUserId)
                    .eq(Like::getTargetType, "collect")
                    .eq(Like::getTargetId, diary.getId()));
            diary.setIsCollected(collectCount > 0);
        }
    }
    
    /**
     * 保存日记标签
     */
    private void saveDiaryTags(Long diaryId, List<String> tagNames) {
        // TODO: 实现标签保存逻辑
        // 这里需要创建 diary_tags 表的实体类和Mapper
    }
    
    /**
     * 更新用户统计信息
     */
    private void updateUserStats(Long userId) {
        // 统计用户的日记数
        Long diaryCount = diaryMapper.selectCount(
                new LambdaQueryWrapper<Diary>().eq(Diary::getUserId, userId));
        
        // 统计点亮的城市数
        List<String> cities = diaryMapper.selectList(
                new LambdaQueryWrapper<Diary>()
                        .eq(Diary::getUserId, userId)
                        .select(Diary::getCity)
                        .groupBy(Diary::getCity))
                .stream()
                .map(Diary::getCity)
                .filter(StringUtils::hasText)
                .distinct()
                .collect(Collectors.toList());
        
        // 更新用户表
        User user = new User();
        user.setId(userId);
        user.setTotalDiaries(diaryCount.intValue());
        user.setTotalCities(cities.size());
        userMapper.updateById(user);
    }
}
