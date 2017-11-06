package com.asiainfo.biapp.mcd.ec2b.thread;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import com.asiainfo.biapp.mcd.ec2b.domain.McdEcExecLog;
import com.asiainfo.biapp.mcd.ec2b.domain.McdEcScheduleTask;
import com.asiainfo.biapp.mcd.ec2b.domain.McdEcSendTask;
import com.asiainfo.biapp.mcd.ec2b.service.IMcdEcSplitService;
import com.asiainfo.biapp.mcd.ec2b.util.EcConstants;
import com.asiainfo.biapp.mcd.ec2b.util.McdEcExecLogQueueUtils;

/**
 * @Description: 拆分发送任务到mcd_ec_send_task，任务大小依赖于配置和配额控制
 * 
 * @author       zq
 * @date         2017年10月23日  下午12:28:49
 * Copyright: 	  北京亚信智慧数据科技有限公司
 */
@Component
public class McdEcSplitThread implements Runnable {

	final Logger logger = LoggerFactory.getLogger(getClass());
	
	@Autowired
	private IMcdEcSplitService service;
	public McdEcSplitThread(IMcdEcSplitService service) {
		this.service = service;
	}
	
	/* 
	 * @Description: TODO
	 * @see java.lang.Runnable#run()
	 */
	@Override
	@Scheduled(fixedDelay=60000)
	public void run() {
		
		Thread.currentThread().setName("Ec任务拆分线程");
		//获取redis调度分布式锁
		String lockId = this.service.trySplitLock();
		if (StringUtils.isEmpty(lockId)) {
			logger.info("未能获取到Split线程分布式锁，已有其他服务器正在执行Split线程，直接返回！");
			return;
		}
		try {
			logger.info("开始检查mcd_ec_schedule_task调度任务状态，将状态EC_SCHEDULE_STATUS_SCHEDULING复位！");
			int reset = this.service.resetScheduleStatus(EcConstants.EC_SCHEDULE_STATUS_SCHEDULING, EcConstants.EC_SCHEDULE_STATUS_READY);
			logger.info("成功复位{}个调度任务！", reset);
		} catch (Exception ex) {
			logger.error("复位mcd_ec_schedule_task调度任务状态时发生异常，异常信息如下：\n", ex);
		}
		
		int success = 0;
		int failure = 0;
		int total = 0;
		try {
			logger.info("拆分任务开始运行，检查mcd_ec_schedule_task表是否有EC_SCHEDULE_STATUS_READY状态任务需要拆分！");
			//查询mcd_ec_schedule_task所有状态为EC_SCHEDULE_STATUS_READY的调度任务
			List<McdEcScheduleTask> scheduleList = this.service.queryReadySchedule();
			if (null == scheduleList || scheduleList.isEmpty()) {
				logger.info("没有需要拆分的调度任务，本次拆分调度直接返回！");
			}
			total = scheduleList.size();
			logger.info("查询到{}个READY调度任务，需要判断配额并生成发送任务！", total);
			for (McdEcScheduleTask task : scheduleList) {
				try {
					//处理实时调度，调用动静匹配加载客户群
					if (!StringUtils.isEmpty(task.getCepEventId())) {
						logger.info("实时短信任务，需要调用动静匹配加载客户群， task={}", task);
						if (this.service.callCepService(task)) {
							this.service.updateScheduleStatus(task.getId(), EcConstants.EC_SCHEDULE_STATUS_FINISH);
							success++;
						} else {
							logger.error("实时短信调用动静匹配加载客户群失败，task={}", task);
							failure++;
						}
						continue;
					}
					
					//判断当前正在运行的发送任务数是否超出并发数
					if (this.service.exceedMaxParallel()) {
						logger.info("当前正在运行的发送任务超出最大并发任务{}，不再拆分新的发送任务，本轮调度直接返回！", 
								this.service.getMaxParallel());
						break;
					}
					//更新mcd_camp_task_date表状态为EC_TASK_STATUS_SCHEDULING
					this.service.updateScheduleStatus(task.getId(), EcConstants.EC_SCHEDULE_STATUS_SCHEDULING);
					//生成发送任务，修改调度任务坐标，配额判断，判断是否已完成调度任务拆分
					McdEcSendTask sendTask = this.service.generateSendTask(task);
					success++;
					if (null == sendTask) {
						continue;
					}
					//写执行日志到日志队列
					McdEcExecLogQueueUtils.getInstance().offer(new McdEcExecLog());
					
					//判断拆分方式，如果是等量发送直接continue，如果是顺序发送直接break
					if (EcConstants.EC_SEND_POLICY_ORDER == this.service.getSendPolicy()) {
						logger.info("发送策略为顺序发送，结束当前任务等待下一次调度继续拆分当前调度任务!");
						break;
					}
					logger.info("发送策略为等量发送，开始拆分下一个调度任务!");
				} catch (Exception e) {
					logger.error("拆分任务(scheduleId={}, campsegId={}, taskId={}, dataDate={})时发生异常，异常信息如下：\n", 
							task.getId(), task.getCampsegId(), task.getTaskId(), task.getDataDate(), e);
					failure++;
					//写执行日志到日志队列
				}
			}
		} catch (Exception ex) {
			logger.error("拆分任务发生异常，异常信息如下：\n", ex);
			//写执行日志到日志队列
		} finally {
			logger.info("拆分任务结束，共{}个，成功{}个，失败{}个！", total, success, failure);
			//释放redis调度分布式锁
			boolean releaseFlag = this.service.releaseSplitLock(lockId);
			logger.info("拆分线程释放锁({}){}！", lockId, releaseFlag ? "成功" : "失败");
		}
	}
}
