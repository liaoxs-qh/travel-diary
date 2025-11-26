package com.travel.diary.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.travel.diary.entity.Diary;
import org.apache.ibatis.annotations.Mapper;

/**
 * 日记Mapper接口
 */
@Mapper
public interface DiaryMapper extends BaseMapper<Diary> {
}
