package com.asiainfo.biapp.mcd.ec2b.service.impl;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import com.asiainfo.biapp.mcd.ec2b.cache.CacheManager;
import com.asiainfo.biapp.mcd.ec2b.dao.IMcdEcCampDao;
import com.asiainfo.biapp.mcd.ec2b.dao.IMcdEcScheduleTaskDao;
import com.asiainfo.biapp.mcd.ec2b.dao.IMcdEcSendTaskDao;
import com.asiainfo.biapp.mcd.ec2b.domain.FilterResult;
import com.asiainfo.biapp.mcd.ec2b.domain.McdEcCampInfo;
import com.asiainfo.biapp.mcd.ec2b.domain.McdEcScheduleTask;
import com.asiainfo.biapp.mcd.ec2b.domain.McdEcSendTask;
import com.asiainfo.biapp.mcd.ec2b.domain.QueryResult;
import com.asiainfo.biapp.mcd.ec2b.service.IMcdEcClusterService;
import com.asiainfo.biapp.mcd.ec2b.service.IMcdEcQuotaService;
import com.asiainfo.biapp.mcd.ec2b.service.IMcdEcSendService;
import com.asiainfo.biapp.mcd.ec2b.util.EcConstants;
import com.asiainfo.biapp.mcd.fqc.FqcCodes;
import com.asiainfo.biapp.mcd.fqc.model.FrequencyRequest;
import com.asiainfo.biapp.mcd.fqc.model.FrequencyResult;
import com.asiainfo.biapp.mcd.fqc.service.IFrequencyFilterService;
import com.asiainfo.biapp.mcd.sms.SmsCodes;
import com.asiainfo.biapp.mcd.sms.model.Message;
import com.asiainfo.biapp.mcd.sms.model.Result;
import com.asiainfo.biapp.mcd.sms.service.ISendService;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * @Description: TODO
 * 
 * @author       zq
 * @date         2017年10月23日  下午4:32:26
 * Copyright: 	  北京亚信智慧数据科技有限公司
 */

@Service
@Transactional
public class McdEcSendServiceImpl implements IMcdEcSendService {

	final Logger logger = LoggerFactory.getLogger(getClass());
	
	@Autowired
	private IMcdEcClusterService clusterService;
	
	@Autowired
	private IFrequencyFilterService filterService;
	
	@Autowired
	private ISendService sendService;
	
	@Autowired
	private IMcdEcQuotaService quotaService;
	
	@Autowired
	private IMcdEcSendTaskDao sendDao;
	
	@Autowired
	private IMcdEcScheduleTaskDao scheduleDao;
	
	@Autowired
	private IMcdEcCampDao campDao;
	
	@Value("${mcd.ec.sms.maxwait:3600}")
	private int maxWait;
	
	/* 
	 * @Description: TODO
	 * @param current
	 * @param objective
	 * @return
	 * @see com.asiainfo.biapp.mcd.ec2b.service.IMcdEcSendService#resetSendStatus(int, int)
	 */
	@Override
	public int resetSendStatus(int current, int objective) {

		logger.info("重置发送任务状态：当前={}, 修改后={}", current, objective);
		List<McdEcSendTask> list = this.sendDao.queryByStatus(current);
		if (null == list || list.isEmpty()) {
			return 0;
		}
		for (McdEcSendTask task : list) {
			this.sendDao.updateTaskStatus(task.getId(), objective);
		}
		return list.size();
	}

	/* 
	 * @Description: TODO
	 * @return
	 * @see com.asiainfo.biapp.mcd.ec2b.service.IMcdEcSendService#queryReadyTask()
	 */
	@Override
	public List<McdEcSendTask> queryReadyTask() {
		return this.sendDao.queryByStatus(EcConstants.EC_SEND_STATUS_READY);
	}

	/* 
	 * @Description: TODO
	 * @param id
	 * @param status
	 * @see com.asiainfo.biapp.mcd.ec2b.service.IMcdEcSendService#updateSendStatus(java.lang.String, int)
	 */
	@Override
	public void updateSendStatus(String id, int status) {
		logger.info("更新发送任务状态：id={}、status={}", id, status);
		this.sendDao.updateTaskStatus(id, status);
	}

	/* 
	 * @Description: TODO
	 * @param task
	 * @return
	 * @see com.asiainfo.biapp.mcd.ec2b.service.IMcdEcSendService#querySendList(com.asiainfo.biapp.mcd.ec2b.domain.McdEcSendTask)
	 */
	@Override
	public List<String> querySendList(McdEcSendTask task) {
		logger.info("查询发送任务对应的清单：task={}", task);
		return this.sendDao.querySendList(task.getDataTable(), task.getStartIndex(), task.getEndIndex());
	}

	/* 
	 * @Description: TODO
	 * @param list
	 * @param task
	 * @return
	 * @see com.asiainfo.biapp.mcd.ec2b.service.IMcdEcSendService#send(java.util.List, com.asiainfo.biapp.mcd.ec2b.domain.McdEcSendTask)
	 */
	@Override
	public String send(List<String> list, McdEcSendTask task) {

		logger.info("开始调用短信批量发送接口，批次(id={})、发送{}条！", task.getId(), null == list ? 0 : list.size());
		String queryId = null;
		if (null == list || list.isEmpty()) {
			logger.info("批次(id={})经过频次过滤后的发送清单为0，不需要发送，直接返回！");
			return null;
		}
		Map<String, String> map = new HashMap<String, String>();
        map.put("CAMPSEG_ID", task.getCampsegId());
        map.put("CHANNEL_ID", "901");
        map.put("CITY_ID", task.getCityId());
        String otherInfo = "";
        try {
        	otherInfo = new ObjectMapper().writeValueAsString(map);
        } catch (Exception ex) {}
        try {
			Result result = this.sendService.sendBatchMessage(this.convertSendMessage(list, task)
					, otherInfo, SmsCodes.RETURN_TYPE_IMMEDIATE);
			queryId = result.getQueryId();
			logger.info("批次(id={})完成调用短信发送平台，发送结果查询码(queryId={})", task.getId(), queryId);
        } catch (Exception ex) {
        	logger.error("发送批量短信是发生异常，campsegId={}、taskId={}、dataDate={}、size={}, \n{}", 
        			task.getCampsegId(), task.getTaskId(), task.getDataDate(), list.size(), ex);
        	throw new RuntimeException("发送批量短信是发生异常", ex);
        }
		return queryId;
	}

	/* 
	 * @Description: TODO
	 * @param status
	 * @return
	 * @see com.asiainfo.biapp.mcd.ec2b.service.IMcdEcSendService#queryByStatus(int)
	 */
	@Override
	public List<McdEcSendTask> queryByStatus(int status) {
		logger.info("查询发送任务：status={}", status);
		return this.sendDao.queryByStatus(status);
	}

	/* 
	 * @Description: TODO
	 * @return
	 * @see com.asiainfo.biapp.mcd.ec2b.service.IMcdEcSendService#isHoliday()
	 */
	@Override
	public boolean isHoliday() {
		
		String today = new SimpleDateFormat("yyyyMMdd").format(new Date());
		logger.info("查询当前日期({})是否节假日！", today);
		//读缓存，失效时间24小时
		return false;
	}

	/* 
	 * @Description: TODO
	 * @return
	 * @see com.asiainfo.biapp.mcd.ec2b.service.IMcdEcSendService#isBotherAvoidPeriod()
	 */
	@Override
	public boolean isBotherAvoidPeriod() {
		
		Calendar cal = Calendar.getInstance();
		int hour = cal.get(Calendar.HOUR_OF_DAY);
		int minute = cal.get(Calendar.MINUTE);
		logger.info("查询当前时段({}:{})是否免打扰时段！", hour, minute);
		//读缓存，失效时间24小时
		return false;
	}

	/* 
	 * @Description: TODO
	 * @param list
	 * @param task
	 * @return
	 * @see com.asiainfo.biapp.mcd.ec2b.service.IMcdEcSendService#filter(java.util.List, com.asiainfo.biapp.mcd.ec2b.domain.McdEcSendTask)
	 */
	@Override
	public FilterResult filter(List<String> list, McdEcSendTask task) {
		
		logger.info("开始进行频次控制，id={}、campsegId={}、taskId={}、dataDate={}、total={}", 
				task.getId(), task.getCampsegId(), task.getTaskId(), task.getDataDate(), null == list ? 0 : list.size());
		FilterResult result = new FilterResult();
		FrequencyRequest request = new FrequencyRequest();
		request.setCampsegId(task.getCampsegId());
		request.setChannelId("901");
		request.setCityId(task.getCityId());
		request.setPhoneList(list);
		FrequencyResult response = this.filterService.filter(request, FqcCodes.FREQUENCY_TYPE_CAMPSEG_CONTROL, false);
		result.setQuery(list);
		if (response != null) {
			result.setSuccess(response.getSuccess() == null ? new ArrayList<String>() : response.getSuccess());
			result.setFailure(response.getFailure() == null ? new ArrayList<String>() : response.getFailure());
		}
		logger.info("批次(id={})的频次控制过滤结果：total={}、success={}、failure={}", 
				task.getId(), null == list ? 0 : list.size(), result.getSuccess().size(), result.getFailure().size());
		return result;
	}

	/* 
	 * @Description: TODO
	 * @param task
	 * @param queryId
	 * @param sendList
	 * @param filterList
	 * @see com.asiainfo.biapp.mcd.ec2b.service.IMcdEcSendService#postSendTask(com.asiainfo.biapp.mcd.ec2b.domain.McdEcSendTask, java.lang.String, java.util.List, java.util.List)
	 */
	@Override
	public void postSendTask(McdEcSendTask task, String queryId, List<String> sendList, List<String> filterList) {

		logger.info("批次(id={})发送后处理：queryId={}、send={}、fqcFilter={}", 
				task.getId(), queryId, null == sendList ? 0 : sendList.size(), null == filterList ? 0 : filterList.size());
		//回写配额
		if (null != filterList && filterList.size() > 0) {
			logger.info("批次(id={})回写{}配额，userId={}、cityId={}", 
					task.getId(), filterList.size(), task.getUserId(), task.getCityId());
			this.quotaService.releaseQuota(task.getDataDate(), filterList.size(), EcConstants.EC_QUOTA_TYPE_BATCH);
		}
		//更新发送任务
		task.setQueryId(queryId);
		task.setSendNum(null == sendList ? 0 : sendList.size());
		task.setFqcFilter(null == filterList ? 0 : filterList.size());
		task.setStatus(StringUtils.isEmpty(queryId) ? EcConstants.EC_SEND_STATUS_FINISH : EcConstants.EC_SEND_STATUS_WAITTING);
		task.setSendTime(new Date());
		this.sendDao.update(task);
		
		//判断是否完成当前周期所有发送、以更改task_date、mcd_camp_def状态
		McdEcScheduleTask scheduleTask = this.scheduleDao.queryScheduleTask(task.getCampsegId(), task.getTaskId(), task.getDataDate());
		if (EcConstants.EC_SCHEDULE_STATUS_FINISH == scheduleTask.getStatus()) {
			List<McdEcSendTask> sendTaskList = this.sendDao.queryByStatus(
					task.getCampsegId(), task.getTaskId(), task.getDataDate(), 
					Arrays.asList(new Integer[] {EcConstants.EC_SEND_STATUS_READY, 
							EcConstants.EC_SEND_STATUS_SENDING, EcConstants.EC_SEND_STATUS_WAITTING}));
			//没有发送中的任务，则认为当前周期完成
			if (null == sendTaskList || sendTaskList.isEmpty()) {
				logger.info("批次(id={})发送后处理：调度(scheduleId={})已完成所有拆分任务的短信发送，直接更改调度的状态为({})！",
						task.getId(), scheduleTask.getId(), 
						EcConstants.EC_CUSTGROUP_CYCLE_ONCE == task.getCustCycle() ? "完成" : "周期完成");
				//一次性客户群task_date设置为完成、周期性设置为 周期完成
				this.campDao.updateTaskDateStatus(task.getTaskId(), task.getDataDate(), 
						EcConstants.EC_CUSTGROUP_CYCLE_ONCE == task.getCustCycle() ? 
								EcConstants.EC_TASK_STATUS_FINISH : EcConstants.EC_TASK_STATUS_CYCLEFINISH);
			}
		}
	}
	
	/* 
	 * TODO
	 * @param task
	 * @return
	 * @see com.asiainfo.biapp.mcd.ec2b.service.IMcdEcSendService#querySendResult(com.asiainfo.biapp.mcd.ec2b.domain.McdEcSendTask)
	 */
	@Override
	public QueryResult querySendResult(McdEcSendTask task) {
		
		logger.info("开始查询发送任务(sendId={})的发送结果：queryId={}", task.getId(), task.getQueryId());
		QueryResult queryResult = new QueryResult();
		queryResult.setQueryId(task.getQueryId());
		//判断发送时间是否已超出限制，如果超出限制还未完成发送，则直接完成当前发送任务
		if (this.maxWait > 0) {
			long limit = 1000L * this.maxWait;
			long sendTime = task.getSendTime() == null ? System.currentTimeMillis() : task.getSendTime().getTime();
			if (limit < (System.currentTimeMillis() - sendTime)) {
				queryResult.setResult(EcConstants.EC_SEND_RESULT_ERROR);
				logger.info("发送任务(id={})的发送时间超过{}s未完成发送，视为发送异常！", task.getId(), this.maxWait);
				return queryResult;
			}
		}
		Result result = this.sendService.queryForSendResult(task.getQueryId());
		queryResult.setSuccess(null == result.getSuccess() ? new ArrayList<String>() : result.getSuccess());
		queryResult.setFailure(null == result.getFailure() ? new ArrayList<String>() : result.getFailure());
		queryResult.setResult(result.getResult());
		logger.info("发送任务(id={})的查询结果：success={}、failure={}", 
				task.getId(), queryResult.getSuccess().size(), queryResult.getFailure().size());
		return queryResult;
	}

	/* 
	 * @Description: TODO
	 * @param task
	 * @param result
	 * @see com.asiainfo.biapp.mcd.ec2b.service.IMcdEcSendService#postQueryTask(com.asiainfo.biapp.mcd.ec2b.domain.McdEcSendTask, com.asiainfo.biapp.mcd.ec2b.domain.QueryResult)
	 */
	@Override
	public void postQueryTask(McdEcSendTask task, QueryResult result) {
		
		logger.info("发送任务(id={})查询后处理：success={}、 failure={}", 
				task.getId(), result.getSuccess().size(), result.getFailure().size());
		//更改任务状态
		this.sendDao.updateTaskStatus(task.getId(), EcConstants.EC_SEND_STATUS_FINISH);
		//返还配额、频次（发送异常的任务暂时不返还）
		if (EcConstants.EC_SEND_RESULT_COMPLETE == result.getResult() && result.getFailure().size() > 0) {
			this.quotaService.releaseQuota(task.getDataDate(), result.getFailure().size(), EcConstants.EC_QUOTA_TYPE_BATCH);
			FrequencyRequest request = new FrequencyRequest();
			request.setPhoneList(result.getFailure());
			request.setCampsegId(task.getCampsegId());
			request.setCityId(task.getCityId());
			request.setChannelId("901");
			logger.info("发送任务(id={})查询后处理：调用频次控制返还发送失败频次，size={}、count=-1", 
					task.getId(), result.getFailure().size());
			this.filterService.increaseCount(request, -1, FqcCodes.FREQUENCY_TYPE_CAMPSEG_CONTROL, false);
		}
	}

	/* 
	 * @Description: TODO
	 * @return
	 * @see com.asiainfo.biapp.mcd.ec2b.service.IMcdEcSendService#tryQueryLock()
	 */
	@Override
	public String tryQueryLock() {
		return this.clusterService.acquireLock(EcConstants.EC_LOCKNAME_QUERY, 5, TimeUnit.MINUTES);
	}

	/* 
	 * @Description: TODO
	 * @param lockId
	 * @return
	 * @see com.asiainfo.biapp.mcd.ec2b.service.IMcdEcSendService#releaseQueryLock(java.lang.String)
	 */
	@Override
	public boolean releaseQueryLock(String lockId) {
		return this.clusterService.releaseLock(EcConstants.EC_LOCKNAME_QUERY, lockId);
	}

	/* 
	 * @Description: TODO
	 * @return
	 * @see com.asiainfo.biapp.mcd.ec2b.service.IMcdEcSendService#trySendLock()
	 */
	@Override
	public String trySendLock() {
		return this.clusterService.acquireLock(EcConstants.EC_LOCKNAME_SEND, 30, TimeUnit.MINUTES);
	}

	/* 
	 * @Description: TODO
	 * @param lockId
	 * @return
	 * @see com.asiainfo.biapp.mcd.ec2b.service.IMcdEcSendService#releaseSendLock(java.lang.String)
	 */
	@Override
	public boolean releaseSendLock(String lockId) {
		return this.clusterService.releaseLock(EcConstants.EC_LOCKNAME_SEND, lockId);
	}

	/* 
	 * TODO
	 * @param eventId
	 * @return
	 * @see com.asiainfo.biapp.mcd.ec2b.service.IMcdEcSendService#queryByEventId(java.lang.String)
	 */
	@Override
	public McdEcCampInfo queryByEventId(String eventId) {
		
		//读缓存，活动信息过期时间10m
		McdEcCampInfo camp = (McdEcCampInfo) CacheManager.getInstance().get(
				EcConstants.EC_CACHE_KEY_CAMPSEG_PREFIX + eventId, 10, TimeUnit.MINUTES);
		//没有缓存读数据库
		if (null == camp) {
			camp = this.campDao.queryByEventId(eventId);
			CacheManager.getInstance().put(EcConstants.EC_CACHE_KEY_CAMPSEG_PREFIX + eventId, camp);
		}
		return camp;
	}
	
	//转换成短信消息
	protected List<Message> convertSendMessage(List<String> list, McdEcSendTask task) {
		
		List<Message> result = new ArrayList<>();
		//变量替换(待实现)
		for (String phone : list) {
			Message message = new Message();
			message.setProductNo(phone);
			message.setContent(task.getExecContent());
			result.add(message);
		}
		return result;
	}

	public static void main(String[] args) {
		
		Calendar cal = Calendar.getInstance();
		System.out.println(cal.get(Calendar.HOUR_OF_DAY));
		System.out.println(cal.get(Calendar.MINUTE));
	}
}
