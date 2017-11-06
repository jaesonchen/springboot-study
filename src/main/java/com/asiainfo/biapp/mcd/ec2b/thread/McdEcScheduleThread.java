package com.asiainfo.biapp.mcd.ec2b.thread;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import com.asiainfo.biapp.mcd.ec2b.domain.ImportResult;
import com.asiainfo.biapp.mcd.ec2b.domain.McdCampTaskDate;
import com.asiainfo.biapp.mcd.ec2b.domain.McdEcExecLog;
import com.asiainfo.biapp.mcd.ec2b.service.IMcdEcScheduleService;
import com.asiainfo.biapp.mcd.ec2b.util.EcConstants;
import com.asiainfo.biapp.mcd.ec2b.util.McdEcExecLogQueueUtils;

/**
 * @Description: 执行mcd_camp_task_date表中状态为50的新任务，从客户群提取数据生成data_date数据表，并生成mcd_ec_schedule_task调度任务
 * 
 * @author       zq
 * @date         2017年10月23日  下午12:23:27
 * Copyright: 	  北京亚信智慧数据科技有限公司
 */
@Component
public class McdEcScheduleThread implements Runnable {

	final Logger logger = LoggerFactory.getLogger(getClass());
	
	@Autowired
	private IMcdEcScheduleService service;
	public McdEcScheduleThread(IMcdEcScheduleService service) {
		this.service = service;
	}
	
	/* 
	 * @Description: TODO
	 * @see java.lang.Runnable#run()
	 */
	@Override
	@Scheduled(fixedDelay=60000)
	public void run() {
		
		Thread.currentThread().setName("Ec调度线程");
		logger.info("尝试获得集群环境下的Schedule线程分布式调度锁！");
		//获取redis分布式调度锁
		String lockId = this.service.tryScheduleLock();
		if (StringUtils.isEmpty(lockId)) {
			logger.info("未能获取到Schedule线程分布式锁，已有其他服务器正在执行Schedule线程，直接返回！");
			return;
		}
		try {
			logger.info("开始检查mcd_camp_task_date的状态，将状态位EC_TASK_STATUS_SCHEDULING复位！");
			int reset = this.service.resetTaskStatus(EcConstants.EC_TASK_STATUS_SCHEDULING, EcConstants.EC_TASK_STATUS_READY);
			logger.info("成功复位{}个mcd_camp_task_date活动！", reset);
			//写执行日志到日志队列
		} catch (Exception ex) {
			logger.error("复位mcd_camp_task_date的活动状态时发生异常，异常信息如下：\n", ex);
			//写执行日志到日志队列
		}
		
		int success = 0;
		int failure = 0;
		try {
			logger.info("调度任务开始运行，检查mcd_camp_task_date表是否有活动需要生成调度任务！");
			//查询mcd_camp_task_date所有状态为50的活动
			List<McdCampTaskDate> taskList = this.service.queryReadyTaskDate();
			if (null == taskList || taskList.isEmpty()) {
				logger.info("没有需要生成调度任务的活动，本次任务调度直接返回！");
				//写执行日志到日志队列
				return;
			}
			logger.info("查询到{}个新的活动，需要从客户群表导入调度数据，并生成调度任务！", taskList.size());
			for (McdCampTaskDate task : taskList) {
				try {
					//更新mcd_camp_task_date表状态为EC_TASK_STATUS_SCHEDULING
					//this.service.updateTaskStatus(task.getCampsegId(), task.getTaskId(), EcConstants.EC_TASK_STATUS_SCHEDULING);
					this.service.updateTaskDateStatus(task.getTaskId(), task.getDataDate(), EcConstants.EC_TASK_STATUS_SCHEDULING);
					//生成新的调度数据表名称
					String tableName = this.service.generateDataTableName(task.getCampsegId(), task.getTaskId(), task.getDataDate());
					//创建表，如果已存在则清空数据
					this.service.createDataTable(tableName, true);
					//从客户群中导入到调度数据表，需要经过免打扰过滤
					ImportResult result = this.service.importDataAfterFilter(task, tableName);
					//生成调度任务（需要保证调度任务和更新task_date状态在一个事物）
					this.service.generateScheduleTask(task, result);
					success++;
					//写执行日志到日志队列
					McdEcExecLogQueueUtils.getInstance().offer(new McdEcExecLog());
				} catch (Exception e) {
					logger.error("调度任务(campsegId={}, taskId={}, dataDate={})时发生异常，异常信息如下：\n", 
							task.getCampsegId(), task.getTaskId(), task.getDataDate(), e);
					failure++;
					//写执行日志到日志队列
				}
			}
		} catch (Exception ex) {
			logger.error("调度任务发生异常，异常信息如下：\n", ex);
			//写执行日志到日志队列
		} finally {
			logger.info("调度任务结束，调度成功{}个，失败{}个！", success, failure);
			//释放redis调度分布式锁
			boolean releaseFlag = this.service.releaseScheduleLock(lockId);
			logger.info("调度线程释放锁({}){}！", lockId, releaseFlag ? "成功" : "失败");
		}
	}
}
