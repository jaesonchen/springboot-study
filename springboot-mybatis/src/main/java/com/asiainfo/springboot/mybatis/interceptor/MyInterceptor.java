package com.asiainfo.springboot.mybatis.interceptor;

import java.sql.Statement;
import java.util.Properties;

import org.apache.ibatis.executor.statement.StatementHandler;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.plugin.Intercepts;
import org.apache.ibatis.plugin.Invocation;
import org.apache.ibatis.plugin.Plugin;
import org.apache.ibatis.plugin.Signature;
import org.apache.ibatis.session.ResultHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @Description: 拦截sql执行，统计时间
 * 
 * @author chenzq  
 * @date 2019年7月17日 下午2:32:38
 * @version V1.0
 * @Copyright: Copyright(c) 2019 jaesonchen.com Inc. All rights reserved.
 */
@Intercepts({
		@Signature(type = StatementHandler.class, method = "query", args = { Statement.class, ResultHandler.class }),
		@Signature(type = StatementHandler.class, method = "update", args = { Statement.class }) })
public class MyInterceptor implements Interceptor {

    final Logger logger = LoggerFactory.getLogger(getClass());
    
	Properties properties;

	@Override
	public Object intercept(Invocation invocation) throws Throwable {
		
	    StatementHandler statementHandler = (StatementHandler) invocation.getTarget();
	    BoundSql boundSql = statementHandler.getBoundSql();
	    logger.info("执行SQL: \n{}", boundSql.getSql());

		long beginTime = System.currentTimeMillis();
		Object result = invocation.proceed();
		logger.info("执行时间: {}ms", (System.currentTimeMillis() - beginTime));
		return result;
	}

	@Override
	public Object plugin(Object target) {
		return Plugin.wrap(target, this);
	}

	@Override
	public void setProperties(Properties properties) {
	    logger.info("注入属性: {}", properties);
		this.properties = properties;
	}
}
