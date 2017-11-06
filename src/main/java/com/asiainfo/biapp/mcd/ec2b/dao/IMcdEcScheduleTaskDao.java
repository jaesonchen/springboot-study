package com.asiainfo.biapp.mcd.ec2b.dao;

import java.util.List;

import com.asiainfo.biapp.mcd.ec2b.domain.McdEcScheduleTask;

/**
 * @Description: TODO
 * 
 * @author       zq
 * @date         2017年10月23日  下午1:12:53
 * Copyright: 	  北京亚信智慧数据科技有限公司
 */
public interface IMcdEcScheduleTaskDao {

	void save(McdEcScheduleTask task);
	
	void updateScheduleStatus(String id, int status);
	
	void updateCurrentIndex(String id, int index);
	
	List<McdEcScheduleTask> queryByStatus(int status);
	
	McdEcScheduleTask queryScheduleTask(String campsegId, String taskId, String dataDate);
}
