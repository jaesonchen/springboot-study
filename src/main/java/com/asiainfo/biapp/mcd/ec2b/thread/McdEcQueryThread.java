package com.asiainfo.biapp.mcd.ec2b.thread;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import com.asiainfo.biapp.mcd.ec2b.domain.McdEcSendTask;
import com.asiainfo.biapp.mcd.ec2b.domain.QueryResult;
import com.asiainfo.biapp.mcd.ec2b.service.IMcdEcSendService;
import com.asiainfo.biapp.mcd.ec2b.util.EcConstants;

/**
 * @Description: TODO
 * 
 * @author       zq
 * @date         2017年10月23日  下午5:27:52
 * Copyright: 	  北京亚信智慧数据科技有限公司
 */
@Component
public class McdEcQueryThread implements Runnable {

	final Logger logger = LoggerFactory.getLogger(getClass());
	
	@Autowired
	private IMcdEcSendService service;
	public McdEcQueryThread(IMcdEcSendService service) {
		this.service = service;
	}
	
	/* 
	 * @Description: TODO
	 * @see java.lang.Runnable#run()
	 */
	@Override
	@Scheduled(fixedDelay=60000)
	public void run() {

		Thread.currentThread().setName("Ec短信轮询线程");
		//获取redis调度分布式锁
		String lockId = this.service.tryQueryLock();
		if (StringUtils.isEmpty(lockId)) {
			logger.info("未能获取到Query线程分布式锁，已有其他服务器正在执行Query线程，直接返回！");
			return;
		}
		
		int success = 0;
		int failure = 0;
		int total = 0;
		int processing = 0;
		try {
			//TimeUnit.SECONDS.sleep(5);
			logger.info("开始检查是否有等待发送结果的任务！");
			List<McdEcSendTask> queryList = this.service.queryByStatus(EcConstants.EC_SEND_STATUS_WAITTING);
			if (null == queryList || queryList.isEmpty()) {
				logger.info("没有找到正在等待发送结果的任务，直接返回！");
				return;
			}
			total = queryList.size();
			logger.info("当前有{}个任务正在等待发送结果，开始轮询短信发送结果！", queryList.size());
			for (McdEcSendTask task : queryList) {
				try {
					//查询发送结果，需要判断最长发送时间，超过限制时直接设置异常状态结束发送任务
					QueryResult result = this.service.querySendResult(task);
					//更新发送任务状态
					if (EcConstants.EC_SEND_RESULT_PROCESSING == result.getResult()) {
						processing++;
						continue;
					}
					//轮询后处理：更改发送任务状态、回写配额、回写频次
					this.service.postQueryTask(task, result);
					success++;
				} catch (Exception ex) {
					failure++;
					logger.error("轮询短信发送结果发生异常，queryId={}, \n{}", task.getQueryId(), ex);
				}
			}
		} catch (Exception ex) {
			logger.error("轮询短信发送结果发生异常，异常信息如下：\n", ex);
		} finally {
			logger.info("轮询线程结束，共{}个，处理中{}个，成功{}个，失败{}个！", total, processing, success, failure);
			//获取redis调度分布式锁
			boolean releaseFlag = this.service.releaseQueryLock(lockId);
			logger.info("轮询线程释放锁({}){}！", lockId, releaseFlag ? "成功" : "失败");
		}
	}
}
