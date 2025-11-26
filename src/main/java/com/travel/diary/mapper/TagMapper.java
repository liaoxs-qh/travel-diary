package com.travel.diary.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.travel.diary.entity.Tag;
import org.apache.ibatis.annotations.Mapper;

/**
 * 标签Mapper
 */
@Mapper
public interface TagMapper extends BaseMapper<Tag> {
}
