package com.asiainfo.biapp.mcd.ec2b.dao;

import java.util.List;

import com.asiainfo.biapp.mcd.ec2b.domain.McdEcSendTask;

/**
 * @Description: TODO
 * 
 * @author       zq
 * @date         2017年10月23日  下午1:06:26
 * Copyright: 	  北京亚信智慧数据科技有限公司
 */
public interface IMcdEcSendTaskDao {

	void save(McdEcSendTask task);
	
	void update(McdEcSendTask task);
	
	void updateTaskStatus(String id, int status);
	
	List<McdEcSendTask> queryByStatus(int status);
	
	List<McdEcSendTask> queryByStatus(List<Integer> status);
	
	List<McdEcSendTask> queryByStatus(String campsegId, String taskId, String dataDate, List<Integer> status);
	
	List<String> querySendList(String tableName, int startIndex, int endIndex);
}
