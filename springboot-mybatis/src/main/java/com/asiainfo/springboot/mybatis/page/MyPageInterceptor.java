package com.asiainfo.springboot.mybatis.page;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import org.apache.ibatis.cache.CacheKey;
import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.plugin.Intercepts;
import org.apache.ibatis.plugin.Invocation;
import org.apache.ibatis.plugin.Plugin;
import org.apache.ibatis.plugin.Signature;
import org.apache.ibatis.session.ResultHandler;
import org.apache.ibatis.session.RowBounds;

/**   
 * @Description: TODO
 * 
 * @author chenzq  
 * @date 2019年7月18日 下午2:31:08
 * @version V1.0
 * @Copyright: Copyright(c) 2019 jaesonchen.com Inc. All rights reserved. 
 */
@Intercepts({
    @Signature(type = Executor.class, method = "query", args = { MappedStatement.class, Object.class, RowBounds.class, ResultHandler.class }),
    @Signature(type = Executor.class, method = "query", args = { MappedStatement.class, Object.class, RowBounds.class, ResultHandler.class, CacheKey.class, BoundSql.class })
})
public class MyPageInterceptor implements Interceptor {

    // 每页显示的条目数
    private int pageSize;
    // 数据库类型
    private String dbType;
    
    @Override
    public Object intercept(Invocation invocation) throws Throwable {

        // 拦截的方法参数列表
        Object[] args = invocation.getArgs();
        // statement
        MappedStatement statement = (MappedStatement) args[0];
        // statement 参数数组
        Object parameter = args[1];
        RowBounds rowBounds = (RowBounds) args[2];
        ResultHandler<?> resultHandler = (ResultHandler<?>) args[3];
        Executor executor = (Executor) invocation.getTarget();
        // 缓存key
        CacheKey cacheKey;
        // sql
        BoundSql boundSql;
        // 4 个参数时
        if (args.length == 4) {
            boundSql = statement.getBoundSql(parameter);
            cacheKey = executor.createCacheKey(statement, parameter, rowBounds, boundSql);
        // 6 个参数时
        } else {
            cacheKey = (CacheKey) args[4];
            boundSql = (BoundSql) args[5];
        }
        
        // 是否使用了MyPageHelper.startPage来开启分页
        if (MyPageHelper.isInPage()) {
            try {
                // 总数查询sql
                String countSql = MyPageUtil.getCountSql(boundSql.getSql(), dbType);
                // 总数查询的statement
                MappedStatement countStatement = MyPageUtil.newCountMappedStatement(statement);
                BoundSql countBoundSql = new BoundSql(countStatement.getConfiguration(), countSql, boundSql.getParameterMappings(), parameter);
                // 总数查询缓存key
                CacheKey countCacheKey = executor.createCacheKey(countStatement, parameter, RowBounds.DEFAULT, countBoundSql);
                // 执行 count 查询
                Object countResultList = executor.query(countStatement, parameter, RowBounds.DEFAULT, resultHandler, countCacheKey, countBoundSql);
                Integer total = (Integer) ((List<?>) countResultList).get(0);
                // 总数为0时，直接返回
                if (total == 0) {
                    return new ArrayList<>();
                }
                
                // 分页查询
                String pageSql = MyPageUtil.getPageSql(boundSql.getSql(), dbType, pageSize, total);
                BoundSql pageBoundSql = new BoundSql(statement.getConfiguration(), pageSql, boundSql.getParameterMappings(), parameter);
                CacheKey pageCacheKey = executor.createCacheKey(statement, parameter, rowBounds, pageBoundSql);
                List<?> list = executor.query(statement, parameter, RowBounds.DEFAULT, resultHandler, pageCacheKey, pageBoundSql);
                return new MyPage<>(list, total, pageSize);
            } finally {
                MyPageHelper.clearPage();
            }
        }
        
        // 不分页
        return executor.query(statement, parameter, rowBounds, resultHandler, cacheKey, boundSql);
    }

    @Override
    public Object plugin(Object target) {
        return Plugin.wrap(target, this);
    }

    @Override
    public void setProperties(Properties properties) {
        this.pageSize = Integer.valueOf(properties.getProperty("pigeSize", "10"));
        this.dbType = properties.getProperty("dbType", "mysql");
    }
}
