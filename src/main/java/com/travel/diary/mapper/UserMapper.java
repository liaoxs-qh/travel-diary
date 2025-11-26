package com.travel.diary.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.travel.diary.entity.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

/**
 * 用户Mapper接口
 */
@Mapper
public interface UserMapper extends BaseMapper<User> {
    
    /**
     * 根据openid查询用户
     */
    @Select("SELECT * FROM users WHERE openid = #{openid}")
    User selectByOpenid(String openid);
}
