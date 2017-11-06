package com.asiainfo.biapp.mcd.ec2b.dao.impl;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.asiainfo.biapp.mcd.ec2b.dao.IMcdEcQuotaDao;
import com.asiainfo.biapp.mcd.ec2b.domain.McdEcQuotaConfig;

/**
 * TODO
 * 
 * @author       zq
 * @date         2017年11月2日  下午4:06:05
 * Copyright: 	  北京亚信智慧数据科技有限公司
 */
@Repository
public class McdEcQuotaDaoImpl implements IMcdEcQuotaDao {

	final Logger logger = LoggerFactory.getLogger(getClass());
	
	@Autowired
	private JdbcTemplate template;
	
	/* 
	 * TODO
	 * @param dataDate
	 * @param type
	 * @return
	 * @see com.asiainfo.biapp.mcd.ec2b.dao.IMcdEcQuotaDao#queryQuota(java.lang.String, int)
	 */
	@Override
	public McdEcQuotaConfig queryQuota(String dataDate, int type) {

		StringBuilder sql = new StringBuilder();
		sql.append(" select * from mcd_ec_quota_config ")
			.append("where data_date=? and quota_type=?");
		logger.debug("查询当前剩余配额sql={}", sql.toString());
		logger.debug("查询当前剩余配额参数dataDate={}、type={}", dataDate, type);
		List<McdEcQuotaConfig> list = this.template.query(sql.toString(), new Object[] {dataDate, type}, new BeanPropertyRowMapper<McdEcQuotaConfig>(McdEcQuotaConfig.class));
		return null == list || list.isEmpty() ? null : list.get(0);
	}

	/* 
	 * TODO
	 * @param dataDate
	 * @param type
	 * @return
	 * @see com.asiainfo.biapp.mcd.ec2b.dao.IMcdEcQuotaDao#queryUsed(java.lang.String, int)
	 */
	@Override
	public McdEcQuotaConfig queryUsed(String dataDate, int type) {

		StringBuilder sql = new StringBuilder();
		sql.append(" select * from mcd_ec_quota_used ")
			.append("where data_date=? and quota_type=?");
		logger.debug("查询已使用配额sql={}", sql.toString());
		logger.debug("查询已使用配额参数dataDate={}、type={}", dataDate, type);
		List<McdEcQuotaConfig> list = this.template.query(sql.toString(), new Object[] {dataDate, type}, new BeanPropertyRowMapper<McdEcQuotaConfig>(McdEcQuotaConfig.class));
		return null == list || list.isEmpty() ? null : list.get(0);
	}

	/* 
	 * TODO
	 * @param quota
	 * @see com.asiainfo.biapp.mcd.ec2b.dao.IMcdEcQuotaDao#saveUsed(com.asiainfo.biapp.mcd.ec2b.domain.McdEcQuotaConfig)
	 */
	@Override
	public synchronized void saveUsed(McdEcQuotaConfig quota) {

		McdEcQuotaConfig used = this.queryQuota(quota.getDataDate(), quota.getQuotaType());
		if (null == used) {
			StringBuilder sql = new StringBuilder();
			sql.append(" insert into mcd_ec_quota_used(data_date, send_num, quota_type) ")
				.append("values(?, ?, ?) ");
			logger.debug("保存已使用配额sql={}", sql.toString());
			logger.debug("保存已使用配额参数quota={}", quota);
			this.template.update(sql.toString(), new Object[] {quota.getDataDate(), quota.getSendNum(), quota.getQuotaType()});
		} else {
			this.incUsed(quota, quota.getSendNum());
		}
		
	}

	/* 
	 * TODO
	 * @param quota
	 * @see com.asiainfo.biapp.mcd.ec2b.dao.IMcdEcQuotaDao#updateUsed(com.asiainfo.biapp.mcd.ec2b.domain.McdEcQuotaConfig)
	 */
	@Override
	public void updateUsed(McdEcQuotaConfig quota) {
		
		StringBuilder sql = new StringBuilder();
		sql.append(" update mcd_ec_quota_used ")
			.append("set send_num=? ")
			.append("where data_date=? and quota_type=? ");
		logger.debug("更新已使用配额sql={}", sql.toString());
		logger.debug("更新已使用配额参数quota={}", quota);
		this.template.update(sql.toString(), new Object[] {quota.getSendNum(), quota.getDataDate(), quota.getQuotaType()});
	}

	/* 
	 * TODO
	 * @param quota
	 * @param count
	 * @see com.asiainfo.biapp.mcd.ec2b.dao.IMcdEcQuotaDao#incUsed(com.asiainfo.biapp.mcd.ec2b.domain.McdEcQuotaConfig, int)
	 */
	@Override
	public void incUsed(McdEcQuotaConfig quota, int count) {

		StringBuilder sql = new StringBuilder();
		sql.append(" update mcd_ec_quota_used ")
			.append("set send_num=send_num+? ")
			.append("where data_date=? and quota_type=? ");
		logger.debug("增加已使用配额sql={}", sql.toString());
		logger.debug("增加已使用配额参数quota={}", quota);
		this.template.update(sql.toString(), new Object[] {count, quota.getDataDate(), quota.getQuotaType()});
	}

	/* 
	 * TODO
	 * @param quota
	 * @param count
	 * @see com.asiainfo.biapp.mcd.ec2b.dao.IMcdEcQuotaDao#decUsed(com.asiainfo.biapp.mcd.ec2b.domain.McdEcQuotaConfig, int)
	 */
	@Override
	public void decUsed(McdEcQuotaConfig quota, int count) {

		StringBuilder sql = new StringBuilder();
		sql.append(" update mcd_ec_quota_used ")
			.append("set send_num=send_num-? ")
			.append("where data_date=? and quota_type=? ");
		logger.debug("减少已使用配额sql={}", sql.toString());
		logger.debug("减少已使用配额参数quota={}", quota);
		this.template.update(sql.toString(), new Object[] {count, quota.getDataDate(), quota.getQuotaType()});
	}
}
