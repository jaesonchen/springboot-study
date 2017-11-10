package com.asiainfo.springboot.mybatis;

import java.util.List;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

/**
 * TODO
 * 
 * @author       zq
 * @date         2017年11月10日  上午10:55:16
 * Copyright: 	  北京亚信智慧数据科技有限公司
 */
@Mapper
public interface UserDao {
    
    @Select("SELECT * FROM user")
    List<User> findAll();

    @Select("SELECT * FROM user WHERE user_name=#{userName}")
    @Results({
            @Result(property = "userId", column = "user_id"),
            @Result(property = "userName", column = "user_name")
    })
    User findByName(@Param("userName") String userName);
    
    @Insert("INSERT INTO user(user_id, user_name) VALUES(#{userId}, #{userName})")
    int insert(User user);
    
    @Update("UPDATE user SET user_name=#{userName} WHERE user_id=#{userId}")
    void update(User user);

    @Delete("DELETE FROM user WHERE user_id =#{userId}")
    void delete(int userId);
}
