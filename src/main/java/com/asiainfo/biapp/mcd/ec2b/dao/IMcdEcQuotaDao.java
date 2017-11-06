package com.asiainfo.biapp.mcd.ec2b.dao;

import com.asiainfo.biapp.mcd.ec2b.domain.McdEcQuotaConfig;

/**
 * TODO
 * 
 * @author       zq
 * @date         2017年10月26日  上午11:14:29
 * Copyright: 	  北京亚信智慧数据科技有限公司
 */
public interface IMcdEcQuotaDao {

	McdEcQuotaConfig queryQuota(String dataDate, int type);
	
	McdEcQuotaConfig queryUsed(String dataDate, int type);
	
	void saveUsed(McdEcQuotaConfig quota);
	
	void updateUsed(McdEcQuotaConfig quota);
	
	void incUsed(McdEcQuotaConfig quota, int count);
	
	void decUsed(McdEcQuotaConfig quota, int count);
}
