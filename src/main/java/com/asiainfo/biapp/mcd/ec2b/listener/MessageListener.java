package com.asiainfo.biapp.mcd.ec2b.listener;

import java.util.Map;
import java.util.concurrent.RejectedExecutionException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import com.asiainfo.biapp.mcd.ec2b.domain.McdEcCampInfo;
import com.asiainfo.biapp.mcd.ec2b.service.IMcdEcQuotaService;
import com.asiainfo.biapp.mcd.ec2b.service.IMcdEcSendService;
import com.asiainfo.biapp.mcd.ec2b.thread.McdEcRealtimeThread;
import com.asiainfo.biapp.mcd.ec2b.util.EcConstants;
import com.asiainfo.biapp.mcd.ec2b.util.ServiceUtil;
import com.asiainfo.biapp.mcd.ec2b.util.ThreadPoolUtils;
import com.asiainfo.biapp.mcd.fqc.service.IFrequencyFilterService;
import com.asiainfo.biapp.mcd.sms.service.ISendService;

/**
 * TODO
 * 
 * @author       zq
 * @date         2017年11月2日  下午3:57:26
 * Copyright: 	  北京亚信智慧数据科技有限公司
 */
@Component
@Profile("kafka")
public class MessageListener {

	final Logger logger = LoggerFactory.getLogger(getClass());
	
	@Autowired
	private IMcdEcSendService sendService;
	@Autowired
	private IMcdEcQuotaService quotaService;
	@Autowired
	private IFrequencyFilterService filterService;
	@Autowired
	private ISendService smsService;
	
	@Value("${mcd.ec.sms.realtime.event-key:epl_id}")
	private String eventKey;
	
	@KafkaListener(topics="com.asiainfo.bdx.cgf.response")
    public void processMessage(String message) {
		
		Thread.currentThread().setName("Ec实时短信kafka消费线程");
		logger.info("开始处理kafka实时短信消息：message={}", message);
		try {
			if (StringUtils.isEmpty(message)) {
				logger.debug("消息体为空，消息被丢弃！");
				return;
			}
			//节假日、免打扰时段 （缓存）
			if (this.sendService.isBotherAvoidPeriod() || this.sendService.isHoliday()) {
				logger.debug("节假日、免打扰时段过滤，消息被丢弃，message={}", message);
				return;
			}
			//json解析
			Map<String, Object> msg = ServiceUtil.parseJsonString(message);
			String productNo = null == msg.get("product_no") ? null : String.valueOf(msg.get("product_no"));
			String eplId = (null == msg.get(eventKey) && null == msg.get("epl_id")) ? 
					null : String.valueOf(null == msg.get(eventKey) ? msg.get("epl_id") : msg.get(eventKey));
			if (StringUtils.isEmpty(productNo) || StringUtils.isEmpty(eplId)) {
				logger.debug("非法格式json字符串，消息被丢弃，message={}", message);
				return;
			}
			//检查消息体时间戳（进入kafka的时间戳），超过一定时间，直接丢弃
			
			//活动信息
			McdEcCampInfo camp = this.sendService.queryByEventId(eplId);
			if (camp == null 
					|| EcConstants.EC_CAMPSEG_STATUS_FINISH == camp.getExecStatus()
					|| EcConstants.EC_CAMPSEG_STATUS_TERMINATE == camp.getExecStatus()) {
				logger.debug("活动已结束、终止，消息被丢弃，campsegId={}", camp.getCampsegId());
				return;
			}
			
			//放入线程池，线程池大小10，等待队列大小1000，等待队列满时直接丢弃消息
			try {
				ThreadPoolUtils.getInstance().execute(new McdEcRealtimeThread(
						quotaService, filterService, smsService, camp, productNo));
			} catch (RejectedExecutionException ex) {
				logger.error("消息发送太慢，丢弃当前消息：message={}", message);
			} catch (Exception ex) {
				logger.error("放入发送队列时出现异常，丢弃当前消息：message={}, \n{}", message, ex);
			}
		} catch (Exception ex) {
			logger.error("处理kafka实时消息时发生异常， message={}, \n{}", message, ex);
		}
	}
}
