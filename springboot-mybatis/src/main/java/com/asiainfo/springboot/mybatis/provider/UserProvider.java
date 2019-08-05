package com.asiainfo.springboot.mybatis.provider;

import java.util.Map;

import org.apache.ibatis.jdbc.SQL;

import com.asiainfo.springboot.mybatis.model.User;

/**   
 * @Description: mybatis provider 动态sql处理
 * 
 * 1. Mapper接口方法只有一个参数，它接受一个与Mapper接口方法相同类型的参数。如果使用了@Param("userName")进行定义，那么接收参数必须是userName或者param1
 * 2. Mapper接口有多个输入参数，可以使用参数类型为java.util.Map的方法作为SQLprovider方法参数。
 *    Mapper接口方法所有的输入参数将会被放到map中，以参数名、@Param定义名称为key；也可以将param1, param2, param3等作为key，将输入参数按序作为value。
 * 3. 拼接sql中使用的#{name} 变量名是传递到UserProvider里的方法参数名称，包括参数名、@Param定义名称、param1, param2, param3等。
 * 4. provider里的方法参数不要使用数据类型，应该使用保证类型，dao里的参数在映射时会被转为保证类型，如果provider里是基本类型在createSqlSource时会匹配不上报错。
 *    @Param 注解的参数不会有这个问题。
 * 
 * @author chenzq
 * @date 2019年7月19日 下午2:45:16
 * @version V1.0
 * @Copyright: Copyright(c) 2019 jaesonchen.com Inc. All rights reserved. 
 */
public class UserProvider {

    // @SelectProvider(type = UserProvider.class, method = "findById")
    public String findById(Long id) {
        //System.out.println(map);
        return new SQL() {
            {
                SELECT(" * ");
                FROM("user");
                WHERE("id=#{id}");
            }
        }.toString();
    }
    
    // @SelectProvider(type = UserProvider.class, method = "findByUserName")
    public String findByUserName(String param1) {
        return new SQL() {
            {
                SELECT(" * ");
                FROM("user");
                WHERE("user_name like concat('%', #{param1}, '%') ");
            }
        }.toString();
    }
    
    // @InsertProvider(type = UserProvider.class, method = "save")
    public String save(User user) {
        StringBuilder sql = new StringBuilder("insert into user(user_name, password");
        sql.append(user.getDept() != null ? ", dept_id)" : ")");
        sql.append(" values(#{userName}, #{password}");
        sql.append(user.getDept() != null ? ", #{dept.id})" : ")");
        return sql.toString();
    }
    
    // @UpdateProvider(type = UserProvider.class, method = "update")
    public String update(Map<String, Object> map) {
        // map = {password=1234asdf, id=6, userName=czq, param3=1234asdf, param1=6, param2=czq}
        return new SQL() {
            {
                UPDATE("user");
                SET("user_name=#{userName}");
                SET("password=#{password}");
                WHERE("id=#{id}");
            }
        }.toString();
    }
}
