package com.travel.diary.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.travel.diary.entity.Follow;
import org.apache.ibatis.annotations.Mapper;

/**
 * 关注Mapper
 */
@Mapper
public interface FollowMapper extends BaseMapper<Follow> {
}
