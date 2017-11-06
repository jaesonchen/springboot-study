package com.asiainfo.biapp.mcd.ec2b.service;

import java.util.concurrent.TimeUnit;

/**
 * 集群环境下的执行锁服务类
 * 
 * @author       zq
 * @date         2017年10月25日  上午10:40:43
 * Copyright: 	  北京亚信智慧数据科技有限公司
 */
public interface IMcdEcClusterService {

	/**
	 * 获取锁，设置锁过期时间
	 * 
	 * @param lockName	锁名称
	 * @param expire	过期时间
	 * @param unit
	 * @return			成功返回锁id、失败返回null
	 */
	String acquireLock(String lockName, long expire, TimeUnit unit);
	
	/**
	 * 释放指定锁id的锁
	 * 
	 * @param lockName	锁名称
	 * @param lockId	锁id
	 * @return			成功返回true、失败false
	 */
	boolean releaseLock(String lockName, String lockId);
}
