package com.asiainfo.biapp.mcd.ec2b.service;

import java.util.List;

import com.asiainfo.biapp.mcd.ec2b.domain.FilterResult;
import com.asiainfo.biapp.mcd.ec2b.domain.McdEcCampInfo;
import com.asiainfo.biapp.mcd.ec2b.domain.McdEcSendTask;
import com.asiainfo.biapp.mcd.ec2b.domain.QueryResult;

/**
 * 短信发送服务类，用于mcd_ec_send_task表获取发送任务，并调用短信服务和查询服务
 * 
 * @author       zq
 * @date         2017年10月23日  下午3:32:56
 * Copyright: 	  北京亚信智慧数据科技有限公司
 */
public interface IMcdEcSendService {

	/**
	 * 重置mcd_ec_send_task表任务状态
	 * 
	 * @param current
	 * @param objective
	 * @return
	 */
	int resetSendStatus(int current, int objective);
	
	/**
	 * 查询mcd_ec_send_task状态为ready的发送任务
	 * 
	 * @return
	 */
	List<McdEcSendTask> queryReadyTask();
	
	/**
	 * 查询mcd_ec_send_task 指定状态的发送任务
	 * 
	 * @param status
	 * @return
	 */
	List<McdEcSendTask> queryByStatus(int status);
	
	/**
	 * 更新指定发送任务的状态
	 * 
	 * @param id
	 * @param status
	 */
	void updateSendStatus(String id, int status);
	
	/**
	 * 查询发送任务对应的发送清单
	 * 
	 * @param task
	 * @return
	 */
	List<String> querySendList(McdEcSendTask task);
	
	/**
	 * 调用频次过滤服务
	 * 
	 * @param list
	 * @param task
	 * @return
	 */
	FilterResult filter(List<String> list, McdEcSendTask task);
	
	/**
	 * 调用短信发送服务
	 * 
	 * @param list
	 * @param task
	 * @return
	 */
	String send(List<String> list, McdEcSendTask task);
	
	/**
	 * 发送任务后处理：更新发送任务状态、回写配额、判断是否完成当前周期的发送
	 * 
	 * @param task
	 * @param queryId
	 * @param sendList
	 * @param filterList
	 */
	void postSendTask(McdEcSendTask task, String queryId, List<String> sendList, List<String> filterList);
	
	/**
	 * 当前日期是否节假日
	 * 
	 * @return
	 */
	boolean isHoliday();
	
	/**
	 * 当前时段是否免打扰时段
	 * 
	 * @return
	 */
	boolean isBotherAvoidPeriod();
	
	/**
	 * 调用短信服务查询发送批次的发送结果
	 * 
	 * @param task
	 * @return
	 */
	QueryResult querySendResult(McdEcSendTask task);
	
	/**
	 * 轮询任务后处理：更新发送任务状态、回写配额、频次
	 * 
	 * @param task
	 * @param result
	 */
	void postQueryTask(McdEcSendTask task, QueryResult result);
	
	/**
	 * 尝试获取发送锁
	 * 
	 * @return
	 */
	String trySendLock();
	
	/**
	 * 释放发送锁
	 * 
	 * @param lockId
	 * @return
	 */
	boolean releaseSendLock(String lockId);
	
	/**
	 * 尝试获取查询锁
	 * 
	 * @return
	 */
	String tryQueryLock();
	
	/**
	 * 释放查询锁
	 * 
	 * @param lockId
	 * @return
	 */
	boolean releaseQueryLock(String lockId);
	
	/**
	 * 根据事件id获取对应的活动
	 * 
	 * @param eventId
	 * @return
	 */
	McdEcCampInfo queryByEventId(String eventId);
}
