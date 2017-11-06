package com.asiainfo.biapp.mcd.ec2b.service.impl;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import com.asiainfo.biapp.mcd.ec2b.dao.IMcdEcScheduleTaskDao;
import com.asiainfo.biapp.mcd.ec2b.dao.IMcdEcSendTaskDao;
import com.asiainfo.biapp.mcd.ec2b.domain.McdEcScheduleTask;
import com.asiainfo.biapp.mcd.ec2b.domain.McdEcSendTask;
import com.asiainfo.biapp.mcd.ec2b.service.IMcdEcClusterService;
import com.asiainfo.biapp.mcd.ec2b.service.IMcdEcQuotaService;
import com.asiainfo.biapp.mcd.ec2b.service.IMcdEcSplitService;
import com.asiainfo.biapp.mcd.ec2b.util.EcConstants;
import com.asiainfo.biapp.mcd.ec2b.util.IdGenerator;

/**
 * @Description: TODO
 * 
 * @author       zq
 * @date         2017年10月23日  下午4:32:04
 * Copyright: 	  北京亚信智慧数据科技有限公司
 */
@Service
@Transactional
public class McdEcSplitServiceImpl implements IMcdEcSplitService {

	final Logger logger = LoggerFactory.getLogger(getClass());
	
	@Autowired
	private IMcdEcClusterService clusterService;
	
	@Autowired
	private IMcdEcQuotaService quotaService;
	
	@Autowired
	private IMcdEcScheduleTaskDao scheduleDao;
	
	@Autowired
	private IMcdEcSendTaskDao sendDao;
	
	@Autowired
	private IdGenerator generator;
	
	@Value("${mcd.ec.cgf.cep.resturl}")
	private String resturl;
	
	@Value("${mcd.ec.sms.maxparallel:10}")
	private int maxParallel;
	
	@Value("${mcd.ec.sms.policy:1}")
	private int policy;
	
	@Value("${mcd.ec.sms.limit:10000}")
	private int sendLimit;
	
	/* 
	 * @Description: TODO
	 * @param current
	 * @param objective
	 * @return
	 * @see com.asiainfo.biapp.mcd.ec2b.service.IMcdEcSplitService#resetScheduleStatus(int, int)
	 */
	@Override
	public int resetScheduleStatus(int current, int objective) {

		logger.info("重置调度任务状态：当前={}, 修改后={}", current, objective);
		List<McdEcScheduleTask> list = this.scheduleDao.queryByStatus(current);
		if (null == list || list.isEmpty()) {
			return 0;
		}
		for (McdEcScheduleTask task : list) {
			this.scheduleDao.updateScheduleStatus(task.getId(), objective);
		}
		return list.size();
	}

	/* 
	 * @Description: TODO
	 * @return
	 * @see com.asiainfo.biapp.mcd.ec2b.service.IMcdEcSplitService#queryReadySchedule()
	 */
	@Override
	public List<McdEcScheduleTask> queryReadySchedule() {
		logger.info("查询状态为0的调度任务！");
		return this.scheduleDao.queryByStatus(EcConstants.EC_SCHEDULE_STATUS_READY);
	}

	/* 
	 * @Description: TODO
	 * @param id
	 * @param status
	 * @see com.asiainfo.biapp.mcd.ec2b.service.IMcdEcSplitService#updateScheduleStatus(java.lang.String, int)
	 */
	@Override
	public void updateScheduleStatus(String id, int status) {
		logger.info("更新调度任务状态id={}、status={}", id, status);
		this.scheduleDao.updateScheduleStatus(id, status);
	}

	/* 
	 * @Description: TODO
	 * @param task
	 * @return
	 * @see com.asiainfo.biapp.mcd.ec2b.service.IMcdEcSplitService#generateSendTask(com.asiainfo.biapp.mcd.ec2b.domain.McdEcScheduleTask)
	 */
	@Override
	public McdEcSendTask generateSendTask(McdEcScheduleTask task) {

		logger.info("开始拆分调度任务，生成发送子任务，id={}、campsegId={}、taskId={}、dataDate={}、",
				task.getId(), task.getCampsegId(), task.getTaskId(), task.getDataDate());
		final String today = new SimpleDateFormat("yyyyMMdd").format(new Date());
		//获取剩余配额
		int quotaNum = this.quotaService.getQuota(today, EcConstants.EC_QUOTA_TYPE_BATCH);
		int sendNum = this.sendLimit > quotaNum ? quotaNum : this.sendLimit;
		int endIndex = task.getTotal() > (task.getCurrentIndex() + sendNum - 1) ? 
				(task.getCurrentIndex() + sendNum - 1) : task.getTotal();
		
		//没有配额时，不拆分
		if (quotaNum <= 0) {
			logger.info("调度任务(id={})当前没有可以使用的配额，不拆分发送任务！", task.getId());
			return null;
		}
		//保存McdEcSendTask
		McdEcSendTask send = new McdEcSendTask();
		BeanUtils.copyProperties(task, send);
		send.setId(generator.generateTimestampId());
		send.setStartIndex(task.getCurrentIndex());
		send.setEndIndex(endIndex);
		send.setBaFilter(0);
		send.setFqcFilter(0);
		send.setCreateTime(new Date());
		send.setTotal(endIndex < task.getCurrentIndex() ? 0 : (endIndex - task.getCurrentIndex() + 1));
		send.setStatus(EcConstants.EC_SEND_STATUS_READY);
		this.sendDao.save(send);
		
		//更新配额
		this.quotaService.updateQuota(today, sendNum, EcConstants.EC_QUOTA_TYPE_BATCH);
		//更新McdEcScheduleTask的currentIndex
		this.scheduleDao.updateCurrentIndex(task.getId(), endIndex + 1);
		//判断拆分任务是否已完成
		if (endIndex >= task.getTotal()) {
			logger.info("调度任务(scheduleId={})已完成调度拆分，状态设置为完成!", task.getId());
			this.updateScheduleStatus(task.getId(), EcConstants.EC_SCHEDULE_STATUS_FINISH);
		} else {
			//更新mcd_camp_task_date表状态为EC_SCHEDULE_STATUS_READY
			this.updateScheduleStatus(task.getId(), EcConstants.EC_SCHEDULE_STATUS_READY);
		}
		return send;
	}

	/* 
	 * @Description: TODO
	 * @return
	 * @see com.asiainfo.biapp.mcd.ec2b.service.IMcdEcSplitService#getSendPolicy()
	 */
	@Override
	public int getSendPolicy() {
		return this.policy;
	}

	/* 
	 * @Description: TODO
	 * @return
	 * @see com.asiainfo.biapp.mcd.ec2b.service.IMcdEcSplitService#exceedMaxParallel()
	 */
	@Override
	public boolean exceedMaxParallel() {
		
		List<McdEcSendTask> list = this.sendDao.queryByStatus(Arrays.asList(
				new Integer[] {EcConstants.EC_SEND_STATUS_READY, EcConstants.EC_SEND_STATUS_SENDING, EcConstants.EC_SEND_STATUS_WAITTING}));
		
		return null == list || list.isEmpty() ? false : this.maxParallel <= list.size();
	}

	/* 
	 * @Description: TODO
	 * @return
	 * @see com.asiainfo.biapp.mcd.ec2b.service.IMcdEcSplitService#getMaxParallel()
	 */
	@Override
	public int getMaxParallel() {
		return this.maxParallel;
	}

	/* 
	 * @Description: TODO
	 * @return
	 * @see com.asiainfo.biapp.mcd.ec2b.service.IMcdEcSplitService#trySplitLock()
	 */
	@Override
	public String trySplitLock() {
		return this.clusterService.acquireLock(EcConstants.EC_LOCKNAME_SPLIT, 10, TimeUnit.MINUTES);
	}

	/* 
	 * @Description: TODO
	 * @param lockId
	 * @return
	 * @see com.asiainfo.biapp.mcd.ec2b.service.IMcdEcSplitService#releaseSplitLock(java.lang.String)
	 */
	@Override
	public boolean releaseSplitLock(String lockId) {
		return this.clusterService.releaseLock(EcConstants.EC_LOCKNAME_SPLIT, lockId);
	}

	/* 
	 * TODO
	 * @param task
	 * @return
	 * @see com.asiainfo.biapp.mcd.ec2b.service.IMcdEcSplitService#callCepService(com.asiainfo.biapp.mcd.ec2b.domain.McdEcScheduleTask)
	 */
	@Override
	public boolean callCepService(McdEcScheduleTask task) {
		
		logger.info("开始调用cep接口进行实时数据加载，task={}", task);
		boolean flag = true;
		try {
			//调用动静匹配
			String requestUrl = this.resturl;
			String params = "{cepEventId}/{custListTabName}/{dataDate}/1";
			requestUrl += this.resturl.endsWith("/") ? params : ("/" + params);
			logger.info("动静客户群匹配restful加载地址：url={}", requestUrl);
			logger.info("动静客户群匹配restful加载参数：cepEventId={}, custListTabName={}, dataDate={}, ctrl=1", 
					task.getCepEventId(), task.getDataTable(), task.getDataDate());
			String cepResult = new RestTemplate().getForObject(requestUrl, String.class, 
					task.getCepEventId(), task.getDataTable(), task.getDataDate(), 1);
			if (!"1".equals(cepResult)) {
				logger.error("动静客户群加载失败，返回码：{}, 请查看cgf日志定位问题！", cepResult);
				return false;
			}
			//调用ocsp
		} catch (Exception ex) {
			flag = false;
			logger.error("调用cep接口时发生异常，请仔细查看cep restful接口的url配置！", ex);
		}
		return flag;
	}
}
