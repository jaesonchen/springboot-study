package com.asiainfo.springboot.mybatis.annotation;

import java.util.List;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.InsertProvider;
import org.apache.ibatis.annotations.Many;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.One;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.SelectProvider;
import org.apache.ibatis.annotations.UpdateProvider;

import com.asiainfo.springboot.mybatis.model.Contact;
import com.asiainfo.springboot.mybatis.model.Department;
import com.asiainfo.springboot.mybatis.model.User;
import com.asiainfo.springboot.mybatis.provider.UserProvider;

/**   
 * @Description: TODO
 * 
 * @author chenzq  
 * @date 2019年7月19日 下午2:44:00
 * @version V1.0
 * @Copyright: Copyright(c) 2019 jaesonchen.com Inc. All rights reserved. 
 */
@Mapper
public interface AnnotationUserDao {

    @Options(useCache = false)
    @Select("select * from user")
    @Results({
            @Result(id = true, property = "id", column = "id"),
            @Result(property = "userName", column = "user_name"), 
            @Result(property = "password", column = "password"), 
            @Result(property = "dept", column = "dept_id", one = @One(select = "findByDeptId"))
    })
    public List<User> findAll();
    
    @Select("select * from contact where user_id=#{id}")
    public List<Contact> findContactById(Long id);
    
    @Select("select * from department where id=#{id}")
    public Department findByDeptId(Long id);
    
    @SelectProvider(type = UserProvider.class, method = "findById")
    @Results({
        @Result(id = true, property = "id", column = "id"), 
        @Result(property = "userName", column = "user_name"), 
        @Result(property = "password", column = "password"), 
        @Result(property = "dept", column = "dept_id", one = @One(select = "findByDeptId")), 
        @Result(property = "contacts", column = "id", many = @Many(select = "findContactById"))
    })
    public User findById(@Param("id") Long id);
    
    @SelectProvider(type = UserProvider.class, method = "findByUserName")
    @Results({
        @Result(id = true, property = "id", column = "id"), 
        @Result(property = "userName", column = "user_name"), 
        @Result(property = "password", column = "password"), 
        @Result(property = "dept", column = "dept_id", one = @One(select = "findByDeptId")), 
        @Result(property = "contacts", column = "id", many = @Many(select = "findContactById"))
    })
    public List<User> findByUserName(String userName);
    
    @Options(useGeneratedKeys = true, keyProperty="id", keyColumn="id")
    @InsertProvider(type = UserProvider.class, method = "save")
    public int save(User user);
    
    @Options(useGeneratedKeys = true, keyProperty="id", keyColumn="id")
    @Insert({
        "<script>", 
            "insert into contact(user_id, address, zip_code) values ", 
            "<foreach collection='list' item='item' index='index' separator=','>", 
                "(#{item.userId}, #{item.address}, #{item.zipCode})", 
            "</foreach>", 
        "</script>"
    })
    public int batchSave(List<Contact> list);
    
    @UpdateProvider(type = UserProvider.class, method = "update")
    public int update(Long id, @Param("userName") String name, String password);
    
    @Delete("delete from user where id=#{id}")
    public int delete(Long id);
    
    @Delete("delete from user_role where user_id=#{id}")
    public int deleteUserRole(Long id);
    
    @Delete("delete from contact where user_id=#{id}")
    public int deleteContact(Long id);
}
