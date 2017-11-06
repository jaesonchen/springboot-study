package com.asiainfo.biapp.mcd.ec2b.service;

import java.util.List;

import com.asiainfo.biapp.mcd.ec2b.domain.McdEcScheduleTask;
import com.asiainfo.biapp.mcd.ec2b.domain.McdEcSendTask;

/**
 * 调度任务拆分服务类，用于从mcd_ec_schedule_task表获取调度任务，进行任务拆分，拆分后的任务放入mcd_ec_send_task
 * 
 * @author       zq
 * @date         2017年10月23日  下午2:47:07
 * Copyright: 	  北京亚信智慧数据科技有限公司
 */
public interface IMcdEcSplitService {

	/**
	 * 重置mcd_ec_schedule_task表任务状态
	 * 
	 * @param current
	 * @param objective
	 * @return
	 */
	int resetScheduleStatus(int current, int objective);
	
	/**
	 * 查询mcd_ec_schedule_task状态为ready的调度任务
	 * 
	 * @return
	 */
	List<McdEcScheduleTask> queryReadySchedule();
	
	/**
	 * 更新指定调度任务的状态
	 * 
	 * @param id
	 * @param status
	 */
	void updateScheduleStatus(String id, int status);
	
	/**
	 * 根据调度任务拆分发送任务、需要判断配额、修改调度状态、调度坐标
	 * 
	 * @param task
	 * @return
	 */
	McdEcSendTask generateSendTask(McdEcScheduleTask task);
	
	/**
	 * 调用cep动静匹配
	 * 
	 * @param task
	 * @return
	 */
	boolean callCepService(McdEcScheduleTask task);
	
	/**
	 * 返回发送策略：轮流、顺序
	 * 
	 * @return
	 */
	int getSendPolicy();
	
	/**
	 * 返回最大并发发送任务数
	 * 
	 * @return
	 */
	int getMaxParallel();
	
	/**
	 * 当前正在运行的发送任务是否超过配置的最大并发数
	 * 
	 * @return
	 */
	boolean exceedMaxParallel();
	
	/**
	 * 尝试获取调度任务拆分锁
	 * 
	 * @return
	 */
	String trySplitLock();
	
	/**
	 * 释放拆分锁
	 * 
	 * @param lockId
	 * @return
	 */
	boolean releaseSplitLock(String lockId);
}
