package com.asiainfo.biapp.mcd.ec2b.service;

/**
 * 配额管理服务类
 * 
 * @author       zq
 * @date         2017年10月24日  下午5:33:41
 * Copyright: 	  北京亚信智慧数据科技有限公司
 */
public interface IMcdEcQuotaService {

	/**
	 * 获取当前剩余配额
	 * 
	 * @param task
	 * @return
	 */
	int getQuota(String dataDate, int type);
	
	/**
	 * 占用配额
	 * 
	 * @param dataDate
	 * @param cityId
	 * @param channelId
	 * @param count
	 */
	void updateQuota(String dataDate, int count, int type);
	
	/**
	 * 返还配额
	 * 
	 * @param dataDate
	 * @param cityId
	 * @param channelId
	 * @param count
	 */
	void releaseQuota(String dataDate, int count, int type);
}
