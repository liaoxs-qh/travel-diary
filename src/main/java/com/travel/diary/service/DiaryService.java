package com.travel.diary.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.travel.diary.dto.DiaryCreateDTO;
import com.travel.diary.dto.DiaryQueryDTO;
import com.travel.diary.dto.DiaryUpdateDTO;
import com.travel.diary.entity.Diary;

/**
 * 日记Service接口
 */
public interface DiaryService {
    
    /**
     * 创建日记
     */
    Diary createDiary(Long userId, DiaryCreateDTO dto);
    
    /**
     * 更新日记
     */
    Diary updateDiary(Long userId, Long diaryId, DiaryUpdateDTO dto);
    
    /**
     * 删除日记
     */
    void deleteDiary(Long userId, Long diaryId);
    
    /**
     * 获取日记详情
     */
    Diary getDiaryDetail(Long diaryId, Long currentUserId);
    
    /**
     * 分页查询日记列表
     */
    Page<Diary> getDiaryList(DiaryQueryDTO queryDTO, Long currentUserId);
    
    /**
     * 获取用户的日记列表
     */
    Page<Diary> getUserDiaries(Long userId, Integer pageNum, Integer pageSize, Long currentUserId);
    
    /**
     * 获取关注用户的日记（动态流）
     */
    Page<Diary> getFollowingDiaries(Long userId, Integer pageNum, Integer pageSize);
    
    /**
     * 点赞/取消点赞日记
     */
    void toggleLike(Long userId, Long diaryId);
    
    /**
     * 收藏/取消收藏日记
     */
    void toggleCollect(Long userId, Long diaryId);
    
    /**
     * 增加浏览量
     */
    void incrementViewCount(Long diaryId);
}
