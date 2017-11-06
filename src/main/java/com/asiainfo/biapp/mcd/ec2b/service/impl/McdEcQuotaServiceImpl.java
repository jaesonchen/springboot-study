package com.asiainfo.biapp.mcd.ec2b.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.asiainfo.biapp.mcd.ec2b.dao.IMcdEcQuotaDao;
import com.asiainfo.biapp.mcd.ec2b.domain.McdEcQuotaConfig;
import com.asiainfo.biapp.mcd.ec2b.service.IMcdEcQuotaService;
import com.asiainfo.biapp.mcd.ec2b.util.EcConstants;

/**
 * TODO
 * 
 * @author       zq
 * @date         2017年10月26日  上午11:16:03
 * Copyright: 	  北京亚信智慧数据科技有限公司
 */
@Service
@Transactional
public class McdEcQuotaServiceImpl implements IMcdEcQuotaService {

	final Logger logger = LoggerFactory.getLogger(getClass());
	
	@Autowired
	private IMcdEcQuotaDao quotaDao;
	
	@Value("${mcd.ec.sms.quota.batch.default:10000000}")
	private int defaultBatchQuota;
	
	@Value("${mcd.ec.sms.quota.realtime.default:1000000}")
	private int defaultRealtimeQuota;
	
	/* 
	 * TODO
	 * @param dataDate
	 * @param type
	 * @return
	 * @see com.asiainfo.biapp.mcd.ec2b.service.IMcdEcQuotaService#getQuota(java.lang.String, int)
	 */
	@Override
	public int getQuota(String dataDate, int type) {
		
		logger.debug("获取配额，dataDate={}", dataDate);
		McdEcQuotaConfig config = this.quotaDao.queryQuota(dataDate, type);
		int defaultQuota = EcConstants.EC_QUOTA_TYPE_BATCH == type ? this.defaultBatchQuota : this.defaultRealtimeQuota;
		int allQuota = (null == config) ? defaultQuota : config.getQuotaNum();
		McdEcQuotaConfig used = this.quotaDao.queryUsed(dataDate, type);
		if (null == used) {
			return allQuota;
		}
		int sendNum = used.getQuotaNum();
		return (allQuota < sendNum) ? 0 : (allQuota - sendNum);
	}

	/* 
	 * TODO
	 * @param dataDate
	 * @param count
	 * @param type
	 * @see com.asiainfo.biapp.mcd.ec2b.service.IMcdEcQuotaService#updateQuota(java.lang.String, int, int)
	 */
	@Override
	public void updateQuota(String dataDate, int count, int type) {
		
		logger.debug("更新配额，dataDate={}、count={}", dataDate, count);
		McdEcQuotaConfig config = this.quotaDao.queryUsed(dataDate, type);
		if (null != config) {
			config.setSendNum(config.getSendNum() + count);
			this.quotaDao.incUsed(config, count);
			return;
		}
		config = new McdEcQuotaConfig();
		config.setDataDate(dataDate);
		config.setQuotaType(type);
		config.setSendNum(count);
		this.quotaDao.saveUsed(config);
	}

	/* 
	 * TODO
	 * @param dataDate
	 * @param count
	 * @param type
	 * @see com.asiainfo.biapp.mcd.ec2b.service.IMcdEcQuotaService#releaseQuota(java.lang.String, int, int)
	 */
	@Override
	public void releaseQuota(String dataDate, int count, int type) {
		
		logger.debug("返还配额，dataDate={}、count={}", dataDate, count);
		McdEcQuotaConfig config = this.quotaDao.queryUsed(dataDate, type);
		if (null != config) {
			config.setSendNum(config.getSendNum() < count ? 0 : (config.getSendNum() - count));
			this.quotaDao.decUsed(config, count);
			return;
		}
		logger.error("返回已使用配额发生异常，查不到已使用的配额记录，dataDate={}、count={}、type={}", dataDate, count, type);
	}
}
