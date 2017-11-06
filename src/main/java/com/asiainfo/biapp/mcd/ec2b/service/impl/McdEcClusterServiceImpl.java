package com.asiainfo.biapp.mcd.ec2b.service.impl;

import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.asiainfo.biapp.mcd.ec2b.service.IMcdEcClusterService;
import com.asiainfo.biapp.mcd.redis.IRedisService;

/**
 * TODO
 * 
 * @author       zq
 * @date         2017年10月26日  上午11:19:44
 * Copyright: 	  北京亚信智慧数据科技有限公司
 */
@Service
public class McdEcClusterServiceImpl implements IMcdEcClusterService {

	final Logger logger = LoggerFactory.getLogger(getClass());
	
	@Autowired
	private IRedisService redisService;
	
	/* 
	 * TODO
	 * @param lockName
	 * @param expire
	 * @param unit
	 * @return
	 * @see com.asiainfo.biapp.mcd.ec2b.service.IMcdEcClusterService#acquireLock(java.lang.String, long, java.util.concurrent.TimeUnit)
	 */
	@Override
	public String acquireLock(String lockName, long expire, TimeUnit unit) {
		logger.debug("获取锁：lockName={}, expire={}", lockName, expire);
		return this.redisService.acquireLock(lockName, unit.toSeconds(expire));
	}

	/* 
	 * TODO
	 * @param key
	 * @param lockId
	 * @return
	 * @see com.asiainfo.biapp.mcd.ec2b.service.IMcdEcClusterService#releaseLock(java.lang.String, java.lang.String)
	 */
	@Override
	public boolean releaseLock(String lockName, String lockId) {
		logger.debug("释放锁：lockName={}, lockId={}", lockName, lockId);
		return this.redisService.releaseLock(lockName, lockId);
	}
}
