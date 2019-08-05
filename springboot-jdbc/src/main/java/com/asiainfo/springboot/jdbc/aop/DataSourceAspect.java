package com.asiainfo.springboot.jdbc.aop;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import com.asiainfo.springboot.jdbc.route.DataSourceHolder;
import com.asiainfo.springboot.jdbc.route.DataSourceType;

/**
 * @Description: aop切面，进行数据源选择
 *               Order注解使Aop切面比事务切面(非auto的事务管理器)晚注册，从而更早执行，否则在事务开启的地方会getConnection，如果事务切面在最外围被执行则取不到key，而使用默认的数据源
 * 
 * @author chenzq  
 * @date 2019年7月12日 下午1:57:35
 * @version V1.0
 * @Copyright: Copyright(c) 2019 jaesonchen.com Inc. All rights reserved.
 */
@Component
@Aspect
@Order(10)
public class DataSourceAspect {
    
	final Logger logger = LoggerFactory.getLogger(getClass());
	
	@Pointcut("!@annotation(com.asiainfo.springboot.jdbc.route.Master) " 
	        + "&& (execution(* com.asiainfo.springboot.jdbc.service..*.select*(..)) " 
	        + "|| execution(* com.asiainfo.springboot.jdbc.service..*.get*(..)) "
	        + "|| execution(* com.asiainfo.springboot.jdbc.service..*.find*(..)))")
    public void readPointcut() {
    }

    @Pointcut("@annotation(com.asiainfo.springboot.jdbc.route.Master) " +
            "|| execution(* com.asiainfo.springboot.jdbc.service..*.insert*(..)) " +
            "|| execution(* com.asiainfo.springboot.jdbc.service..*.add*(..)) " +
            "|| execution(* com.asiainfo.springboot.jdbc.service..*.save*(..)) " +
            "|| execution(* com.asiainfo.springboot.jdbc.service..*.update*(..)) " +
            "|| execution(* com.asiainfo.springboot.jdbc.service..*.delete*(..)) " +
            "|| execution(* com.asiainfo.springboot.jdbc.service..*.remove*(..))")
    public void writePointcut() {
    }
    
	@Before("readPointcut()")
	public void beforeRead(JoinPoint point) {
		logger.info("before read {}", point);
		DataSourceHolder.setDataSource(DataSourceType.SLAVE);
	}
	
    @Before("writePointcut()")
    public void beforeWrite(JoinPoint point) {
        logger.info("before write {}", point);
        DataSourceHolder.setDataSource(DataSourceType.MASTER);
    }

	@AfterReturning("readPointcut() || writePointcut()")
	public void afterReturn(JoinPoint joinPoint) {
		logger.info("afterReturn {}", joinPoint);
		DataSourceHolder.clear();
	}
}
