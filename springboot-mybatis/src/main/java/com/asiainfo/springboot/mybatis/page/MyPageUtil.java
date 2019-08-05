package com.asiainfo.springboot.mybatis.page;

import java.util.ArrayList;
import java.util.List;

import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.ResultMap;
import org.apache.ibatis.mapping.ResultMapping;
import org.springframework.util.StringUtils;

/**   
 * @Description: TODO
 * 
 * @author chenzq  
 * @date 2019年7月18日 下午3:33:20
 * @version V1.0
 * @Copyright: Copyright(c) 2019 jaesonchen.com Inc. All rights reserved. 
 */
public class MyPageUtil {

    public static final String COUNT = "_count";
    
    // 创建用于count的statement
    public static MappedStatement newCountMappedStatement(MappedStatement ms) {
        
        // 使用查询语句的配置
        MappedStatement.Builder builder = new MappedStatement.Builder(ms.getConfiguration(), 
                ms.getId() + COUNT, ms.getSqlSource(), ms.getSqlCommandType());
        builder.resource(ms.getResource());
        builder.fetchSize(ms.getFetchSize());
        builder.statementType(ms.getStatementType());
        builder.keyGenerator(ms.getKeyGenerator());
        builder.timeout(ms.getTimeout());
        builder.cache(ms.getCache());
        builder.flushCacheRequired(ms.isFlushCacheRequired());
        builder.useCache(ms.isUseCache());
        if (ms.getKeyProperties() != null && ms.getKeyProperties().length > 0) {
            builder.keyProperty(StringUtils.arrayToDelimitedString(ms.getKeyProperties(), ","));
        }
        
        // 查询语句参数映射
        builder.parameterMap(ms.getParameterMap());
        // count查询返回值映射
        List<ResultMap> resultMaps = new ArrayList<ResultMap>();
        ResultMap resultMap = new ResultMap.Builder(ms.getConfiguration(), ms.getId(), Integer.class, new ArrayList<ResultMapping>()).build();
        resultMaps.add(resultMap);
        builder.resultMaps(resultMaps);
        builder.resultSetType(ms.getResultSetType());

        return builder.build();
    }
    
    // 分页sql
    public static String getPageSql(String sql, String type, int defaultPageSize) {
        
        MyPageParameter parameter = MyPageHelper.getMyPageParameter();
        int pageSize = parameter.getPageSize() > 0 ? parameter.getPageSize() : defaultPageSize;
        int pageNum = parameter.getPageNum() <= 0 ? 1 : parameter.getPageNum();
        int start = (pageNum - 1) * pageSize;
        int end = pageNum * pageSize;
        if ("mysql".equals(type)) {
            return sql.trim() + " limit " + start + ", " + end;
        } else if ("oracle".equals(type)) {
            StringBuilder sb = new StringBuilder();
            sb.append("  select * ")
                .append("from (select a.*, rownum rn from (").append(sql.trim()).append(") a ")
                .append("      where rownum <= ").append(end).append(") ")
                .append("where rn > ").append(start);
            return sb.toString();
        }
        return sql;
    }
    
    // 分页sql
    public static String getPageSql(String sql, String type, int defaultPageSize, int total) {
        
        MyPageParameter param = MyPageHelper.getMyPageParameter();
        int pageSize = param.getPageSize() > 0 ? param.getPageSize() : defaultPageSize;
        int pages = total % pageSize == 0 ? total / pageSize : (total / pageSize + 1);
        int pageNum = param.getPageNum() <= 0 ? 1 : (param.getPageNum() > pages ? pages : param.getPageNum());
        int start = pageNum <= 0 ? 0 : (pageNum - 1) * pageSize;
        int end = pageNum <= 0 ? pageSize : pageNum * pageSize;
        if ("mysql".equals(type)) {
            return sql.trim() + " limit " + start + ", " + end;
        } else if ("oracle".equals(type)) {
            StringBuilder sb = new StringBuilder();
            sb.append("  select * ")
                .append("from (select a.*, rownum rn from (").append(sql.trim()).append(") a ")
                .append("      where rownum <= ").append(end).append(") ")
                .append("where rn > ").append(start);
            return sb.toString();
        }
        return sql;
    }

    // 总数sql
    public static String getCountSql(String sql, String type) {
        
        StringBuilder sb = new StringBuilder();
        sb.append("select count(1) from (");
        sb.append(sql);
        sb.append(") my_page_count ");
        return sb.toString();
    }
}
