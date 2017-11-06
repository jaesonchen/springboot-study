package com.asiainfo.biapp.mcd.ec2b.dao.impl;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import com.asiainfo.biapp.mcd.ec2b.dao.IMcdEcSendTaskDao;
import com.asiainfo.biapp.mcd.ec2b.domain.McdEcSendTask;

/**
 * TODO
 * 
 * @author       zq
 * @date         2017年10月26日  下午5:25:21
 * Copyright: 	  北京亚信智慧数据科技有限公司
 */
@Repository
public class McdEcSendTaskDaoImpl implements IMcdEcSendTaskDao {

	final Logger logger = LoggerFactory.getLogger(getClass());
	
	@Autowired
	private JdbcTemplate template;
	
	/* 
	 * TODO
	 * @param task
	 * @see com.asiainfo.biapp.mcd.ec2b.dao.IMcdEcSendTaskDao#save(com.asiainfo.biapp.mcd.ec2b.domain.McdEcSendTask)
	 */
	@Override
	public void save(McdEcSendTask task) {

		StringBuilder sql = new StringBuilder();
		sql.append(" insert into mcd_ec_send_task( ")
			.append("    id, campseg_id, campseg_pid, task_id, query_id, ")
			.append("    data_date, data_table, city_id, user_id, exec_content, ")
			.append("    start_index, end_index, total, send_num, success, failure, ")
			.append("    ba_filter, fqc_filter, cust_cycle, status, ")
			.append("    create_time, send_time) ")
			.append("values(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?) ");
		logger.debug("保存发送任务sql={}", sql.toString());
		logger.debug("保存发送任务参数task={}", task);
		this.template.update(sql.toString(), new Object[] {
				task.getId(), task.getCampsegId(), task.getCampsegPid(), task.getTaskId(), task.getQueryId(),
				task.getDataDate(), task.getDataTable(), task.getCityId(), task.getUserId(), task.getExecContent(), 
				task.getStartIndex(), task.getEndIndex(), task.getTotal(), task.getSendNum(), task.getSuccess(), task.getFailure(),
				task.getBaFilter(), task.getFqcFilter(), task.getCustCycle(), task.getStatus(), 
				task.getCreateTime(), task.getSendTime()});
	}

	/* 
	 * TODO
	 * @param task
	 * @see com.asiainfo.biapp.mcd.ec2b.dao.IMcdEcSendTaskDao#update(com.asiainfo.biapp.mcd.ec2b.domain.McdEcSendTask)
	 */
	@Override
	public void update(McdEcSendTask task) {

		StringBuilder sql = new StringBuilder();
		sql.append(" update mcd_ec_send_task set ")
			.append("    query_id=?, send_num=?, fqc_filter=?, status=?, send_time=? ")
			.append("where id=? ");
		logger.debug("更新发送任务sql={}", sql.toString());
		logger.debug("更新发送任务参数task={}", task);
		this.template.update(sql.toString(), new Object[] {
				task.getQueryId(), task.getSendNum(), task.getFqcFilter(), 
				task.getStatus(), task.getSendTime(), task.getId()});
	}

	/* 
	 * TODO
	 * @param id
	 * @param status
	 * @see com.asiainfo.biapp.mcd.ec2b.dao.IMcdEcSendTaskDao#updateTaskStatus(java.lang.String, int)
	 */
	@Override
	public void updateTaskStatus(String id, int status) {

		StringBuilder sql = new StringBuilder();
		sql.append(" update mcd_ec_send_task ")
			.append("set status=? ")
			.append("where id=? ");
		logger.debug("更新发送任务状态sql={}", sql.toString());
		logger.debug("更新发送任务状态参数id={}、status={}", id, status);
		this.template.update(sql.toString(), new Object[] {status, id});
	}

	/* 
	 * TODO
	 * @param status
	 * @return
	 * @see com.asiainfo.biapp.mcd.ec2b.dao.IMcdEcSendTaskDao#queryByStatus(int)
	 */
	@Override
	public List<McdEcSendTask> queryByStatus(int status) {
		
		StringBuilder sql = new StringBuilder();
		sql.append(" select * from mcd_ec_send_task ")
			.append("where status=? ");
		logger.debug("查询发送任务sql={}", sql.toString());
		logger.debug("查询发送任务参数status={}", status);
		return this.template.query(sql.toString(), new Object[] {status}, new BeanPropertyRowMapper<McdEcSendTask>(McdEcSendTask.class));
	}

	/* 
	 * TODO
	 * @param status
	 * @return
	 * @see com.asiainfo.biapp.mcd.ec2b.dao.IMcdEcSendTaskDao#queryByStatus(java.util.List)
	 */
	@Override
	public List<McdEcSendTask> queryByStatus(List<Integer> status) {

		StringBuilder sql = new StringBuilder();
		sql.append(" select * from mcd_ec_send_task ")
			.append("where status in ").append(this.getInSql(status, false));
		logger.debug("查询发送任务sql={}", sql.toString());
		logger.debug("查询发送任务参数status={}", status);
		return this.template.query(sql.toString(), new BeanPropertyRowMapper<McdEcSendTask>(McdEcSendTask.class));
	}
	
	/* 
	 * TODO
	 * @param campsegId
	 * @param taskId
	 * @param dataDate
	 * @param status
	 * @return
	 * @see com.asiainfo.biapp.mcd.ec2b.dao.IMcdEcSendTaskDao#queryByStatus(java.lang.String, java.lang.String, java.lang.String, java.util.List)
	 */
	@Override
	public List<McdEcSendTask> queryByStatus(String campsegId, String taskId, String dataDate, List<Integer> status) {

		StringBuilder sql = new StringBuilder();
		sql.append(" select * from mcd_ec_send_task ")
			.append("where campseg_id=? and task_id=? and data_date=? ")
			.append("and status in ").append(this.getInSql(status, false));
		logger.debug("查询发送任务sql={}", sql.toString());
		logger.debug("查询发送任务参数campsegId={}、taskId={}、dataDate={}、status={}", campsegId, taskId, dataDate, status);
		return this.template.query(sql.toString(), new Object[] {campsegId, taskId, dataDate}, 
				new BeanPropertyRowMapper<McdEcSendTask>(McdEcSendTask.class));
	}

	/* 
	 * TODO
	 * @param tableName
	 * @param startIndex
	 * @param endIndex
	 * @return
	 * @see com.asiainfo.biapp.mcd.ec2b.dao.IMcdEcSendTaskDao#querySendList(java.lang.String, int, int)
	 */
	@Override
	public List<String> querySendList(String tableName, int startIndex, int endIndex) {

		StringBuilder sql = new StringBuilder();
		sql.append(" select product_no from ").append(tableName).append(" ")
			.append("where data_index between ? and ? ");
		logger.debug("查询发送清单sql={}", sql.toString());
		logger.debug("查询发送清单参数tableName={}、startIndex={}、endIndex={}", tableName, startIndex, endIndex);
		return this.template.query(sql.toString(), new Object[] {startIndex, endIndex}, new RowMapper<String>() {
			@Override
			public String mapRow(ResultSet rs, int rowNum) throws SQLException {
				return rs.getString("product_no");
			}
		});
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
}
