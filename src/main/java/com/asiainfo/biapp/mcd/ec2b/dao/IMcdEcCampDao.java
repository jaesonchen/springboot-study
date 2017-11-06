package com.asiainfo.biapp.mcd.ec2b.dao;

import java.util.List;

import com.asiainfo.biapp.mcd.ec2b.domain.ImportResult;
import com.asiainfo.biapp.mcd.ec2b.domain.InsertCondition;
import com.asiainfo.biapp.mcd.ec2b.domain.McdCampTaskDate;
import com.asiainfo.biapp.mcd.ec2b.domain.McdEcCampInfo;

/**
 * @Description: TODO
 * 
 * @author       zq
 * @date         2017年10月23日  下午1:15:27
 * Copyright: 	  北京亚信智慧数据科技有限公司
 */
public interface IMcdEcCampDao {
	
	McdEcCampInfo queryByEventId(String eventId);
	
	List<McdCampTaskDate> queryTaskDateByStatus(List<Integer> status);
	
	void updateTaskStatus(String campsegId, String taskId, int status);
	
	void updateTaskDateStatus(String taskId, String dataDate, int status);
	
	void createTable(String tableName);
	
	boolean existTable(String tableName);
	
	void removeAll(String tableName);
	
	ImportResult insertByCondition(String target, String source, List<InsertCondition> includeList, List<InsertCondition> excludeList);
}
