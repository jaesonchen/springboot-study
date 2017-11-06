package com.asiainfo.biapp.mcd.ec2b.dao.impl;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.asiainfo.biapp.mcd.ec2b.dao.IMcdEcCampDao;
import com.asiainfo.biapp.mcd.ec2b.domain.ImportResult;
import com.asiainfo.biapp.mcd.ec2b.domain.InsertCondition;
import com.asiainfo.biapp.mcd.ec2b.domain.McdCampTaskDate;
import com.asiainfo.biapp.mcd.ec2b.domain.McdEcCampInfo;
import com.asiainfo.biapp.mcd.ec2b.util.EcConstants;

/**
 * TODO
 * 
 * @author       zq
 * @date         2017年10月26日  上午11:22:54
 * Copyright: 	  北京亚信智慧数据科技有限公司
 */
@Repository
@Profile("oracle")
public class McdEcCampDaoImpl implements IMcdEcCampDao {

	final Logger logger = LoggerFactory.getLogger(getClass());
	
	@Autowired
	protected JdbcTemplate template;
	
	/* 
	 * TODO
	 * @param status
	 * @return
	 * @see com.asiainfo.biapp.mcd.ec2b.dao.IMcdEcCampDao#queryTaskDateByStatus(java.util.List)
	 */
	@Override
	public List<McdCampTaskDate> queryTaskDateByStatus(List<Integer> status) {
		
		StringBuilder sql = new StringBuilder();
		sql.append(" select ")
			.append("    mctd.task_id, mctd.data_date, mctd.exec_status, ")
			.append("    mct.campseg_id, mct.cycle_type as cust_cycle, ")
			.append("    mccl.cep_event_id, mccl.exec_content, mccl.if_have_var, ")
			.append("    mcd.campseg_pid, mcd.create_userid as user_id, mcd.city_id, ")
			.append("    tab.list_table_name as custgroup_table_name ")
			.append("from mcd_camp_task_date mctd ")
			.append("join mcd_camp_task mct on mctd.task_id=mct.task_id ")
			.append("join mcd_camp_channel_list mccl on mct.campseg_id=mccl.campseg_id and mct.channel_id=mccl.channel_id ")
			.append("join mcd_camp_def mcd on mct.campseg_id=mcd.campseg_id ")
			.append("join mcd_camp_custgroup_list cust on mcd.campseg_id=cust.campseg_id ")
			.append("join mcd_custgroup_tab_list tab on cust.custgroup_id=tab.custom_group_id and tab.data_date=mctd.data_date ")
			.append("where mct.channel_id='901' and mctd.exec_status in ").append(this.getInSql(status, false))
			.append("order by mctd.task_id, mctd.data_date ");
		logger.debug("查询task_date任务sql={}", sql.toString());
		logger.debug("查询task_date任务参数status={}", status);
		return this.template.query(sql.toString(), new BeanPropertyRowMapper<McdCampTaskDate>(McdCampTaskDate.class));
	}
	
	//参数列表转in sql
	protected String getInSql(List<? extends Object> list, boolean isStr) {
		
		StringBuilder sql = new StringBuilder("(");
		for (int i = 0; i < list.size(); i++) {
			sql.append(isStr ? "'" : "");
			sql.append(String.valueOf(list.get(i)));
			sql.append(isStr ? "'" : "");
			sql.append(i == (list.size() - 1) ? "" : ", ");
		}
		sql.append(")");
		return sql.toString();
	}
	
	/* 
	 * TODO
	 * @param campsegId
	 * @param taskId
	 * @param status
	 * @see com.asiainfo.biapp.mcd.ec2b.dao.IMcdEcCampDao#updateTaskStatus(java.lang.String, java.lang.String, int)
	 */
	@Override
	public void updateTaskStatus(String campsegId, String taskId, int status) {

		StringBuilder sql = new StringBuilder();
		sql.append(" update mcd_camp_task ")
			.append("set exec_status=? ")
			.append("where campseg_id=? and task_id=? ");
		logger.debug("更新camp_task任务状态sql={}", sql.toString());
		logger.debug("更新camp_task任务状态参数campsegId={}、taskId={}、status={}", campsegId, taskId, status);
		this.template.update(sql.toString(), new Object[] {status, campsegId, taskId});
	}

	/* 
	 * TODO
	 * @param taskId
	 * @param dataDate
	 * @param status
	 * @see com.asiainfo.biapp.mcd.ec2b.dao.IMcdEcCampDao#updateTaskDateStatus(java.lang.String, java.lang.String, int)
	 */
	@Override
	public void updateTaskDateStatus(String taskId, String dataDate, int status) {

		StringBuilder sql = new StringBuilder();
		sql.append(" update mcd_camp_task_date ")
			.append("set exec_status=? ")
			.append("where task_id=? and data_date=? ");
		logger.debug("更新task_date任务状态sql={}", sql.toString());
		logger.debug("更新task_date任务状态参数dataDate={}、taskId={}、status={}", dataDate, taskId, status);
		this.template.update(sql.toString(), new Object[] {status, taskId, dataDate});
	}

	/* 
	 * TODO
	 * @param tableName
	 * @see com.asiainfo.biapp.mcd.ec2b.dao.IMcdEcCampDao#createTable(java.lang.String)
	 */
	@Override
	public void createTable(String tableName) {
		this.template.execute("create table " + tableName 
				+ " as select * from " + EcConstants.EC_DATA_TABLE_TEMPLATE + " where 1=2");
	}

	/* 
	 * TODO
	 * @param tableName
	 * @return
	 * @see com.asiainfo.biapp.mcd.ec2b.dao.IMcdEcCampDao#existTable(java.lang.String)
	 */
	@Override
	public boolean existTable(String tableName) {

		try {
			this.template.execute("select * from " + tableName);
			return true;
		} catch (Exception ex) {}
		return false;
	}

	/* 
	 * TODO
	 * @param tableName
	 * @see com.asiainfo.biapp.mcd.ec2b.dao.IMcdEcCampDao#removeAll(java.lang.String)
	 */
	@Override
	public void removeAll(String tableName) {
		this.template.execute("TRUNCATE TABLE " + tableName);
	}

	/* 
	 * TODO
	 * @param target
	 * @param source
	 * @param includeList
	 * @param excludeList
	 * @return
	 * @see com.asiainfo.biapp.mcd.ec2b.dao.IMcdEcCampDao#insertByCondition(java.lang.String, java.lang.String, java.util.List, java.util.List)
	 */
	@Override
	public ImportResult insertByCondition(String target, String source, List<InsertCondition> includeList,
			List<InsertCondition> excludeList) {
		
		//TODO mysql的rownum实现
		StringBuilder sql = new StringBuilder();
		sql.append(" insert into ").append(target).append("(product_no, data_date, data_index) ")
			.append("select product_no, data_date, rownum from ").append(source).append(" ")
			.append("where 1=1 ");
		//include 白名单
		if (null != includeList && includeList.size() > 0) {
			for (InsertCondition tab : includeList) {
				sql.append(" and product_no in (select ").append(tab.getColumn())
					.append(" from ").append(tab.getTable()).append(") ");
			}
		}
		//exclude 黑名单
		if (null != excludeList && excludeList.size() > 0) {
			for (InsertCondition tab : excludeList) {
				sql.append(" and product_no not in (select ").append(tab.getColumn())
					.append(" from ").append(tab.getTable()).append(") ");
			}
		}
		logger.debug("保存清单表sql={}", sql.toString());
		logger.debug("保存清单表参数source={}、target={}、includeList={}、excludeList={}", source, target, includeList, excludeList);
		this.template.execute(sql.toString());

		ImportResult result = new ImportResult();
		result.setTotal(this.template.queryForObject("select count(*) from " + target, Integer.class));
		return result;
	}

	/* 
	 * TODO
	 * @param eventId
	 * @return
	 * @see com.asiainfo.biapp.mcd.ec2b.dao.IMcdEcCampDao#queryByEventId(java.lang.String)
	 */
	@Override
	public McdEcCampInfo queryByEventId(String eventId) {
		
		StringBuilder sql = new StringBuilder();
		sql.append(" select ")
			.append("    mccl.cep_event_id, mccl.exec_content, mccl.if_have_var, ")
			.append("    mcd.campseg_id, mcd.campseg_pid, mcd.campseg_id, mcd.city_id, mcd.campseg_stat_id as exec_status ")
			.append("from mcd_camp_channel_list mccl ")
			.append("join mcd_camp_def mcd on mccl.campseg_id=mcd.campseg_id ")
			.append("where mccl.cep_event_id=? ");
		logger.debug("查询camp_def活动sql={}", sql.toString());
		logger.debug("查询camp_def活动参数eventId={}", eventId);
		List<McdEcCampInfo> list = this.template.query(sql.toString(), new Object[] {eventId}, 
				new BeanPropertyRowMapper<McdEcCampInfo>(McdEcCampInfo.class));
		return null == list || list.isEmpty() ? null : list.get(0);
	}
}
