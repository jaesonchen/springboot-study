package com.asiainfo.springboot.mybatis.handler;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.MappedJdbcTypes;
import org.apache.ibatis.type.MappedTypes;

import com.asiainfo.springboot.mybatis.enums.EnumValue;
import com.asiainfo.springboot.mybatis.enums.RoleType;

/**   
 * @Description: @MappedTypes 注册使用转换器的enum枚举类，mybatis启动时会使用具体的enum类型构建TypeHandler实例。
 * 
 * @author chenzq  
 * @date 2019年7月17日 下午3:16:26
 * @version V1.0
 * @Copyright: Copyright(c) 2019 jaesonchen.com Inc. All rights reserved. 
 */
@MappedJdbcTypes(JdbcType.VARCHAR)
@MappedTypes({ RoleType.class })
public class MyEnumTypeHandler<E extends Enum<E>> extends BaseTypeHandler<E> {

    private Class<E> type;
    
    public MyEnumTypeHandler(Class<E> type) {
        if (type == null) {
            throw new IllegalArgumentException("Type argument cannot be null");
        }
        this.type = type;
    }
    
    @Override
    public void setNonNullParameter(PreparedStatement ps, int i, E parameter, JdbcType jdbcType) throws SQLException {
        // jdbcType由 #{roleType, jdbcType=VARCHAR} 指定，如果没有指定为null
        if (jdbcType == null) {
            System.out.println("jdbcType = null");
            ps.setString(i, getValue(parameter));
        } else {
            System.out.println("jdbcType = " + jdbcType);
            ps.setObject(i, getValue(parameter), jdbcType.TYPE_CODE);
        }
    }

    @Override
    public E getNullableResult(ResultSet rs, String columnName) throws SQLException {
        String str = rs.getString(columnName);
        return str == null ? null : codeOf(type, str);
    }

    @Override
    public E getNullableResult(ResultSet rs, int columnIndex) throws SQLException {
        String str = rs.getString(columnIndex);
        return str == null ? null : codeOf(type, str);
    }

    @Override
    public E getNullableResult(CallableStatement cs, int columnIndex) throws SQLException {
        String str = cs.getString(columnIndex);
        return str == null ? null : codeOf(type, str);
    }
    
    // 转换为enum
    @SuppressWarnings("unchecked")
    public E codeOf(Class<E> enumClass, String code) {
        if (EnumValue.class.isAssignableFrom(enumClass)) {
            for (E e : enumClass.getEnumConstants()) {
                if (((EnumValue<String>) e).getValue().equals(code)) {
                    return e;
                }
            }
        }
        return Enum.valueOf(enumClass, code);
    }
    
    // 从enum获取写入值
    @SuppressWarnings("unchecked")
    public String getValue(E parameter) {
        if (parameter instanceof EnumValue) {
            return ((EnumValue<String>) parameter).getValue();
        }
        return parameter.name();
    }
}
