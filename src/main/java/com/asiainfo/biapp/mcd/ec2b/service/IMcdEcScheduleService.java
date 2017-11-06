package com.asiainfo.biapp.mcd.ec2b.service;

import java.util.List;

import com.asiainfo.biapp.mcd.ec2b.domain.ImportResult;
import com.asiainfo.biapp.mcd.ec2b.domain.McdCampTaskDate;
import com.asiainfo.biapp.mcd.ec2b.domain.McdEcScheduleTask;

/**
 * 调度任务服务类，用于从mcd_camp_task_date表生成调度任务，并导入客户群数据
 * 
 * @author       zq
 * @date         2017年10月23日  下午1:36:59
 * Copyright: 	  北京亚信智慧数据科技有限公司
 */
public interface IMcdEcScheduleService {

	/**
	 * 重置mcd_camp_task_date表任务状态
	 * 
	 * @param current		要重置的状态
	 * @param objective		重置后的状态
	 * @return
	 */
	int resetTaskStatus(int current, int objective);
	
	/**
	 * 查询mcd_camp_task_date表任务状态为ready的任务
	 * 
	 * @return
	 */
	List<McdCampTaskDate> queryReadyTaskDate();
	
	/**
	 * 更新mcd_camp_task表任务状态
	 * 
	 * @param campsegId
	 * @param taskId
	 * @param status
	 */
	void updateTaskStatus(String campsegId, String taskId, int status);
	
	/**
	 * 更新mcd_camp_task_date表任务状态
	 * 
	 * @param taskId
	 * @param dataDate
	 * @param status
	 */
	void updateTaskDateStatus(String taskId, String dataDate, int status);
	
	/**
	 * 生成调度任务对应的数据表名称
	 * 
	 * @param campsegId
	 * @param taskId
	 * @param dataDate
	 * @return
	 */
	String generateDataTableName(String campsegId, String taskId, String dataDate);
	
	/**
	 * 创建表，如果表已存在根据cleanIfExit决定是否清楚表里的数据
	 * 
	 * @param tableName
	 * @param cleanIfExit
	 */
	void createDataTable(String tableName, boolean cleanIfExit);
	
	/**
	 * 将客户群表数据导入调度数据表，需要进行免打扰过滤
	 * 
	 * @param task
	 * @param tableName
	 * @return
	 */
	ImportResult importDataAfterFilter(McdCampTaskDate task, String tableName);
	
	/**
	 * 生成调度任务，更改mcd_camp_task_date表状态为running
	 * 
	 * @param task
	 * @param result
	 * @return
	 */
	McdEcScheduleTask generateScheduleTask(McdCampTaskDate task, ImportResult result);
	
	/**
	 * 尝试获取调度锁
	 * 
	 * @return 成功返回锁id，失败返回null
	 */
	String tryScheduleLock();
	
	/**
	 * 释放指定锁id
	 * 
	 * @param lockId
	 * @return
	 */
	boolean releaseScheduleLock(String lockId);
}
