package com.asiainfo.biapp.mcd.ec2b.thread;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.asiainfo.biapp.mcd.ec2b.domain.McdEcCampInfo;
import com.asiainfo.biapp.mcd.ec2b.service.IMcdEcQuotaService;
import com.asiainfo.biapp.mcd.ec2b.util.EcConstants;
import com.asiainfo.biapp.mcd.fqc.service.IFrequencyFilterService;
import com.asiainfo.biapp.mcd.sms.model.Message;
import com.asiainfo.biapp.mcd.sms.service.ISendService;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * TODO
 * 
 * @author       zq
 * @date         2017年11月3日  下午5:48:22
 * Copyright: 	  北京亚信智慧数据科技有限公司
 */
public class McdEcRealtimeThread implements Runnable {

	final Logger logger = LoggerFactory.getLogger(getClass());
	
	private IMcdEcQuotaService quotaService;
	private IFrequencyFilterService filterService;
	private ISendService smsService;
	private McdEcCampInfo camp;
	private String productNo;
	
	public McdEcRealtimeThread(IMcdEcQuotaService quotaService, IFrequencyFilterService filterService, 
			ISendService smsService, McdEcCampInfo camp, String productNo) {
		this.quotaService = quotaService;
		this.filterService = filterService;
		this.smsService = smsService;
		this.camp = camp;
		this.productNo = productNo;
	}
	
	/* 
	 * TODO
	 * @see java.lang.Runnable#run()
	 */
	@Override
	public void run() {

		Thread.currentThread().setName("Ec实时短信发送(" + this.productNo + ")");
		try {
			//实时配额
			String today = new SimpleDateFormat("yyyyMMdd").format(new Date());
			if (this.quotaService.getQuota(today, EcConstants.EC_QUOTA_TYPE_REALTIME) <= 0) {
				logger.debug("配额不够，消息被丢弃，today={}、campsegId={}", today, camp.getCampsegId());
				return;
			}
			//频次控制
			Map<String, String> map = new HashMap<String, String>();
	        map.put("CAMPSEG_ID", camp.getCampsegId());
	        map.put("CHANNEL_ID", "901");
	        map.put("CITY_ID", camp.getCityId());
	        String otherInfo = "";
	        try {
	        	otherInfo = new ObjectMapper().writeValueAsString(map);
				if (!this.filterService.filter(productNo, otherInfo, 1, false)) {
					logger.debug("被频次过滤，消息被丢弃，campsegId={}、cityId={}", camp.getCampsegId(), camp.getCityId());
					return;
				}
	        } catch (Exception ex) {
	        	logger.error("调用频次时发生异常!", ex);
	        	return;
	        }
	        //实时短信发送
	        boolean sendFlag = false;
	        try {
				this.smsService.sendMessage(this.convertMessage(productNo, camp), otherInfo);
				sendFlag = true;
	        } catch (Exception ex) {
	        	logger.error("发送短信时发生异常!", ex);
	        }
	        //发送后处理
	        if (sendFlag) {
	        	//发送成功更新配额
	        	this.quotaService.updateQuota(today, 1, EcConstants.EC_QUOTA_TYPE_REALTIME);
	        } else {
	        	//失败频次回写
	        	this.filterService.increaseCount(productNo, otherInfo, -1, 1, false);
	        }
		} catch (Exception ex) {
			logger.error("运行实时短信发送时发生异常！", ex);
		}
	}
	
	//转换消息体
	protected Message convertMessage(String productNo, McdEcCampInfo camp) {
		
		Message msg = new Message();
		msg.setProductNo(productNo);
		String content = camp.getExecContent();
		if (1 == camp.getIfHaveVar()) {
			content.replace("$PRODUCT_NO$", productNo);
		}
		msg.setContent(content);
		return msg;
	}
}
