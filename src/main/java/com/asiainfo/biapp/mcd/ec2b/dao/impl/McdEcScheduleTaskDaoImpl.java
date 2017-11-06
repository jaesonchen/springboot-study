package com.asiainfo.biapp.mcd.ec2b.dao.impl;

import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.asiainfo.biapp.mcd.ec2b.dao.IMcdEcScheduleTaskDao;
import com.asiainfo.biapp.mcd.ec2b.domain.McdEcScheduleTask;

/**
 * TODO
 * 
 * @author       zq
 * @date         2017年10月26日  下午5:25:03
 * Copyright: 	  北京亚信智慧数据科技有限公司
 */
@Repository
public class McdEcScheduleTaskDaoImpl implements IMcdEcScheduleTaskDao {

	final Logger logger = LoggerFactory.getLogger(getClass());
	
	@Autowired
	private JdbcTemplate template;
	
	/* 
	 * TODO
	 * @param task
	 * @see com.asiainfo.biapp.mcd.ec2b.dao.IMcdEcScheduleTaskDao#save(com.asiainfo.biapp.mcd.ec2b.domain.McdEcScheduleTask)
	 */
	@Override
	public void save(McdEcScheduleTask task) {

		StringBuilder sql = new StringBuilder();
		sql.append(" insert into mcd_ec_schedule_task( ")
			.append("	id, campseg_id, campseg_pid, task_id, data_date, ")
			.append("   data_table, city_id, user_id, cep_event_id, exec_content, ")
			.append("   current_index, total, ba_filter, fqc_filter, ")
			.append("   cust_cycle, status, create_time, schedule_time) ")
			.append("values(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?) ");
		logger.debug("保存调度任务sql={}", sql.toString());
		logger.debug("保存调度任务参数schedule={}", task);
		this.template.update(sql.toString(), new Object[] {
				task.getId(), task.getCampsegId(), task.getCampsegPid(), task.getTaskId(), task.getDataDate(),
				task.getDataTable(), task.getCityId(), task.getUserId(), task.getCepEventId(), task.getExecContent(), 
				task.getCurrentIndex(), task.getTotal(), task.getBaFilter(), task.getFqcFilter(),
				task.getCustCycle(), task.getStatus(), task.getCreateTime(), task.getScheduleTime()});
	}

	/* 
	 * TODO
	 * @param id
	 * @param status
	 * @see com.asiainfo.biapp.mcd.ec2b.dao.IMcdEcScheduleTaskDao#updateScheduleStatus(java.lang.String, int)
	 */
	@Override
	public void updateScheduleStatus(String id, int status) {

		StringBuilder sql = new StringBuilder();
		sql.append(" update mcd_ec_schedule_task ")
			.append("set status=?, schedule_time=? ")
			.append("where id=? ");
		logger.debug("保存调度任务状态sql={}", sql.toString());
		logger.debug("保存调度任务状态参数id={}、status={}", id, status);
		this.template.update(sql.toString(), new Object[] {status, new Date(), id});
	}

	/* 
	 * TODO
	 * @param id
	 * @param index
	 * @see com.asiainfo.biapp.mcd.ec2b.dao.IMcdEcScheduleTaskDao#updateCurrentIndex(java.lang.String, int)
	 */
	@Override
	public void updateCurrentIndex(String id, int index) {
		
		StringBuilder sql = new StringBuilder();
		sql.append(" update mcd_ec_schedule_task ")
			.append("set current_index=? ")
			.append("where id=? ");
		logger.debug("保存调度任务当前坐标sql={}", sql.toString());
		logger.debug("保存调度任务当前坐标参数id={}、index={}", id, index);
		this.template.update(sql.toString(), new Object[] {index, id});
	}

	/* 
	 * TODO
	 * @param status
	 * @return
	 * @see com.asiainfo.biapp.mcd.ec2b.dao.IMcdEcScheduleTaskDao#queryByStatus(int)
	 */
	@Override
	public List<McdEcScheduleTask> queryByStatus(int status) {

		StringBuilder sql = new StringBuilder();
		sql.append(" select * from mcd_ec_schedule_task ")
			.append("where status=? ");
		logger.debug("查询调度任务sql={}", sql.toString());
		logger.debug("查询调度任务参数status={}", status);
		return this.template.query(sql.toString(), new Object[] {status}, new BeanPropertyRowMapper<McdEcScheduleTask>(McdEcScheduleTask.class));
	}

	/* 
	 * TODO
	 * @param campsegId
	 * @param taskId
	 * @param dataDate
	 * @return
	 * @see com.asiainfo.biapp.mcd.ec2b.dao.IMcdEcScheduleTaskDao#queryScheduleTask(java.lang.String, java.lang.String, java.lang.String)
	 */
	@Override
	public McdEcScheduleTask queryScheduleTask(String campsegId, String taskId, String dataDate) {
		
		StringBuilder sql = new StringBuilder();
		sql.append(" select * from mcd_ec_schedule_task ")
			.append("where campseg_id=? and task_id=? and data_date=? ");
		logger.debug("查询调度任务sql={}", sql.toString());
		logger.debug("查询调度任务参数campsegId={}、taskId={}、dataDate={}", campsegId, taskId, dataDate);
		List<McdEcScheduleTask> list = this.template.query(sql.toString(), 
				new Object[] {campsegId, taskId, dataDate}, 
				new BeanPropertyRowMapper<McdEcScheduleTask>(McdEcScheduleTask.class));
		return null == list ? null : list.get(0);
	}
}
