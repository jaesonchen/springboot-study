package com.asiainfo.biapp.mcd.ec2b.thread;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import com.asiainfo.biapp.mcd.ec2b.domain.FilterResult;
import com.asiainfo.biapp.mcd.ec2b.domain.McdEcExecLog;
import com.asiainfo.biapp.mcd.ec2b.domain.McdEcSendTask;
import com.asiainfo.biapp.mcd.ec2b.service.IMcdEcSendService;
import com.asiainfo.biapp.mcd.ec2b.util.EcConstants;
import com.asiainfo.biapp.mcd.ec2b.util.McdEcExecLogQueueUtils;

/**
 * @Description: 从mcd_ec_send_task中获取任务进行发送，并处理发送结果
 * 
 * @author       zq
 * @date         2017年10月23日  下午12:23:41
 * Copyright: 	  北京亚信智慧数据科技有限公司
 */
@Component
public class McdEcSendThread implements Runnable {

	final Logger logger = LoggerFactory.getLogger(getClass());
	
	@Autowired
	private IMcdEcSendService service;
	public McdEcSendThread(IMcdEcSendService service) {
		this.service = service;
	}
	
	/* 
	 * @Description: TODO
	 * @see java.lang.Runnable#run()
	 */
	@Override
	@Scheduled(fixedDelay=60000)
	public void run() {
		
		Thread.currentThread().setName("Ec短信发送线程");
		//获取redis调度分布式锁
		String lockId = this.service.trySendLock();
		if (StringUtils.isEmpty(lockId)) {
			logger.info("未能获取到Send线程分布式锁，已有其他服务器正在执行Send线程，直接返回！");
			return;
		}
		try {
			logger.info("开始检查mcd_ec_send_task发送任务状态，将状态EC_SEND_STATUS_SENDING复位！");
			int reset = this.service.resetSendStatus(EcConstants.EC_SEND_STATUS_SENDING, EcConstants.EC_SEND_STATUS_READY);
			logger.info("成功复位{}个发送任务！", reset);
		} catch (Exception ex) {
			logger.error("复位mcd_ec_send_task发送任务状态时发生异常，异常信息如下：\n", ex);
		}
		
		int success = 0;
		int failure = 0;
		int total = 0;
		try {
			//节假日、时段
			if (this.service.isHoliday() || this.service.isBotherAvoidPeriod()) {
				logger.info("当前发送日期是节假日、免打扰时段，不允许发生短信，直接返回！");
				return;
			}
			logger.info("发送任务开始运行，检查mcd_ec_send_task表是否有EC_SEND_STATUS_READY状态任务需要发送！");
			//查询mcd_ec_send_task所有状态为EC_SEND_STATUS_READY的发送任务
			List<McdEcSendTask> sendList = this.service.queryReadyTask();
			if (null == sendList || sendList.isEmpty()) {
				logger.info("没有需要发送的任务，本次发送调度直接返回！");
			}
			total = sendList.size();
			logger.info("查询到{}个READY发送任务，需要进行频次控制并调用短信发送服务！", total);
			for (McdEcSendTask task : sendList) {
				try {
					//更新mcd_ec_send_task表状态为EC_SEND_STATUS_SENDING
					this.service.updateSendStatus(task.getId(), EcConstants.EC_SEND_STATUS_SENDING);
					//读取发送清单
					List<String> list = this.service.querySendList(task);
					//调用频次控制
					FilterResult result = this.service.filter(list, task);
					//调用短信服务
					String queryId = this.service.send(result.getSuccess(), task);
					//更新发送状态EcConstants.EC_SEND_STATUS_WAITTING、queryId、fqcFilter、
					//回写配额、判断是否完成当前周期所有发送、以更改task、task_date、mcd_camp_def状态（需要在一个事物内）
					this.service.postSendTask(task, queryId, result.getSuccess(), result.getFailure());
					success++;
					//写执行日志到日志队列
					McdEcExecLogQueueUtils.getInstance().offer(new McdEcExecLog());
				} catch (Exception e) {
					logger.error("发送任务(sendId={}, campsegId={}, taskId={}, dataDate={})时发生异常，异常信息如下：\n", 
							task.getId(), task.getCampsegId(), task.getTaskId(), task.getDataDate(), e);
					failure++;
					//写执行日志到日志队列
				}
			}
		} catch (Exception ex) {
			logger.error("发送任务调度发生异常，异常信息如下：\n", ex);
			//写执行日志到日志队列
		} finally {
			logger.info("发送任务调度结束，共{}个，成功{}个，失败{}个！", total, success, failure);
			//释放redis调度分布式锁
			boolean releaseFlag = this.service.releaseSendLock(lockId);
			logger.info("发送线程释放锁({}){}！", lockId, releaseFlag ? "成功" : "失败");
		}
	}
}
