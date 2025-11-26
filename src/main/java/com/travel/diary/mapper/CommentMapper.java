package com.travel.diary.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.travel.diary.entity.Comment;
import org.apache.ibatis.annotations.Mapper;

/**
 * 评论Mapper
 */
@Mapper
public interface CommentMapper extends BaseMapper<Comment> {
}
