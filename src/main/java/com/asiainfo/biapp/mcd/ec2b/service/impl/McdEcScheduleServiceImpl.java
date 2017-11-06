package com.asiainfo.biapp.mcd.ec2b.service.impl;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.asiainfo.biapp.mcd.ec2b.dao.IMcdEcCampDao;
import com.asiainfo.biapp.mcd.ec2b.dao.IMcdEcScheduleTaskDao;
import com.asiainfo.biapp.mcd.ec2b.domain.ImportResult;
import com.asiainfo.biapp.mcd.ec2b.domain.McdCampTaskDate;
import com.asiainfo.biapp.mcd.ec2b.domain.McdEcScheduleTask;
import com.asiainfo.biapp.mcd.ec2b.service.IMcdEcClusterService;
import com.asiainfo.biapp.mcd.ec2b.service.IMcdEcScheduleService;
import com.asiainfo.biapp.mcd.ec2b.util.EcConstants;
import com.asiainfo.biapp.mcd.ec2b.util.IdGenerator;

/**
 * @Description: TODO
 * 
 * @author       zq
 * @date         2017年10月23日  下午4:31:39
 * Copyright: 	  北京亚信智慧数据科技有限公司
 */

@Service
@Transactional
public class McdEcScheduleServiceImpl implements IMcdEcScheduleService {

	final Logger logger = LoggerFactory.getLogger(getClass());
	
	@Autowired
	private IMcdEcClusterService clusterService;
	
	@Autowired
	private IMcdEcCampDao campDao;
	
	@Autowired
	private IMcdEcScheduleTaskDao scheduleDao;
	
	@Autowired
	private IdGenerator generator;
	
	/* 
	 * @Description: TODO
	 * @param current
	 * @param objective
	 * @return
	 * @see com.asiainfo.biapp.mcd.ec2b.service.IMcdEcScheduleService#resetTaskStatus(int, int)
	 */
	@Override
	public int resetTaskStatus(int current, int objective) {
		
		logger.info("重置task_date任务状态：当前={}, 修改后={}", current, objective);
		List<McdCampTaskDate> list = this.campDao.queryTaskDateByStatus(Arrays.asList(new Integer[] {current}));
		if (null == list || list.isEmpty()) {
			return 0;
		}
		for (McdCampTaskDate task : list) {
			//this.campDao.updateTaskStatus(task.getCampsegId(), task.getTaskId(), objective);
			this.campDao.updateTaskDateStatus(task.getTaskId(), task.getDataDate(), objective);
		}
		return list.size();
	}

	/* 
	 * @Description: TODO
	 * @return
	 * @see com.asiainfo.biapp.mcd.ec2b.service.IMcdEcScheduleService#queryReadyTaskDate()
	 */
	@Override
	public List<McdCampTaskDate> queryReadyTaskDate() {
		logger.info("查询task_date状态为50的任务！");
		return this.campDao.queryTaskDateByStatus(Arrays.asList(new Integer[] {50}));
	}

	/* 
	 * @Description: TODO
	 * @param campsegId
	 * @param taskId
	 * @param status
	 * @see com.asiainfo.biapp.mcd.ec2b.service.IMcdEcScheduleService#updateTaskStatus(java.lang.String, java.lang.String, int)
	 */
	@Override
	public void updateTaskStatus(String campsegId, String taskId, int status) {

		logger.info("更新camp_task任务状态：campsegId={}、taskId={}、status={}", campsegId, taskId, status);
		this.campDao.updateTaskStatus(campsegId, taskId, status);
	}

	/* 
	 * @Description: TODO
	 * @param taskId
	 * @param dataDate
	 * @param status
	 * @see com.asiainfo.biapp.mcd.ec2b.service.IMcdEcScheduleService#updateTaskDateStatus(java.lang.String, java.lang.String, int)
	 */
	@Override
	public void updateTaskDateStatus(String taskId, String dataDate, int status) {

		logger.info("更新task_date任务状态：taskId={}、dataDate={}、status={}", taskId, dataDate, status);
		this.campDao.updateTaskDateStatus(taskId, dataDate, status);
	}

	/* 
	 * @Description: TODO
	 * @param campsegId
	 * @param taskId
	 * @param dataDate
	 * @return
	 * @see com.asiainfo.biapp.mcd.ec2b.service.IMcdEcScheduleService#generateDataTableName(java.lang.String, java.lang.String, java.lang.String)
	 */
	@Override
	public String generateDataTableName(String campsegId, String taskId, String dataDate) {
		
		logger.info("生成清单表名称：campsegId={}、taskId={}、dataDate={}", campsegId, taskId, dataDate);
		StringBuilder result = new StringBuilder();
		result.append(EcConstants.EC_DATA_TABLE_PREFIX)
			.append(taskId).append("_").append(dataDate);
		return result.toString();
	}

	/* 
	 * @Description: TODO
	 * @param tableName
	 * @param cleanIfExit
	 * @see com.asiainfo.biapp.mcd.ec2b.service.IMcdEcScheduleService#createDataTable(java.lang.String, boolean)
	 */
	@Override
	public synchronized void createDataTable(String tableName, boolean cleanIfExit) {

		logger.info("创建清单表：tableName={}、cleanIfExit={}", tableName, cleanIfExit);
		if (this.campDao.existTable(tableName)) {
			if (cleanIfExit) {
				this.campDao.removeAll(tableName);
			}
		} else {
			this.campDao.createTable(tableName);
		}
	}

	/* 
	 * @Description: TODO
	 * @param task
	 * @param result
	 * @return
	 * @see com.asiainfo.biapp.mcd.ec2b.service.IMcdEcScheduleService#generateScheduleTask(com.asiainfo.biapp.mcd.ec2b.domain.McdCampTaskDate, com.asiainfo.biapp.mcd.ec2b.domain.ImportResult)
	 */
	@Override
	public McdEcScheduleTask generateScheduleTask(McdCampTaskDate task, ImportResult importResult) {

		logger.info("生成调度任务：campsegId={}、taskId={}、dataDate={}", 
				task.getCampsegId(), task.getTaskId(), task.getDataDate());
		
		McdEcScheduleTask schedule = new McdEcScheduleTask();
		BeanUtils.copyProperties(task, schedule);
		BeanUtils.copyProperties(importResult, schedule);
		schedule.setCreateTime(new Date());
		schedule.setCurrentIndex(1);
		schedule.setStatus(EcConstants.EC_SCHEDULE_STATUS_READY);
		schedule.setId(generator.generateTimestampId());
		//保存调度任务
		this.scheduleDao.save(schedule);
		//更新mcd_camp_task_date表状态为EC_TASK_STATUS_RUNNING，finish由McdEcSendThread触发
		this.updateTaskDateStatus(task.getTaskId(), task.getDataDate(), EcConstants.EC_TASK_STATUS_RUNNING);

		return schedule;
	}

	/* 
	 * @Description: TODO
	 * @param task
	 * @param tableName
	 * @return
	 * @see com.asiainfo.biapp.mcd.ec2b.service.IMcdEcScheduleService#importDataAfterFilter(com.asiainfo.biapp.mcd.ec2b.domain.McdCampTaskDate, java.lang.String)
	 */
	@Override
	public ImportResult importDataAfterFilter(McdCampTaskDate task, String tableName) {
		
		logger.info("导入客户群数据：custTableName={}、tableName={}、campsegId={}、taskId={}、dataDate={}", 
				task.getCustgroupTableName(), tableName, task.getCampsegId(), task.getTaskId(), task.getDataDate());
		
		ImportResult result = new ImportResult();
		BeanUtils.copyProperties(task, result);
		result.setDataTable(tableName);
		//导入客户群数据到调度数据表
		ImportResult in = this.campDao.insertByCondition(tableName, task.getCustgroupTableName(), null, null);
		result.setTotal(in.getTotal());
		result.setBaFilter(in.getBaFilter());
		return result;
	}

	/* 
	 * @Description: TODO
	 * @return
	 * @see com.asiainfo.biapp.mcd.ec2b.service.IMcdEcScheduleService#tryScheduleLock()
	 */
	@Override
	public String tryScheduleLock() {
		return this.clusterService.acquireLock(EcConstants.EC_LOCKNAME_SCHEDULE, 30, TimeUnit.MINUTES);
	}
	
	/* 
	 * @Description: TODO
	 * @param lockId
	 * @see com.asiainfo.biapp.mcd.ec2b.service.IMcdEcScheduleService#releaseScheduleLock(java.lang.String)
	 */
	@Override
	public boolean releaseScheduleLock(String lockId) {
		return this.clusterService.releaseLock(EcConstants.EC_LOCKNAME_SCHEDULE, lockId);
	}
}
