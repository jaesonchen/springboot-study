package com.asiainfo.biapp.mcd.ec2b.service;

import java.util.List;

import com.asiainfo.biapp.mcd.ec2b.domain.McdEcExecLog;

/**
 * 执行日志服务类，用于批量或单条执行日志的入库
 * 
 * @author       zq
 * @date         2017年10月24日  上午9:39:11
 * Copyright: 	  北京亚信智慧数据科技有限公司
 */
public interface IMcdEcExecLogService {

	void save(McdEcExecLog log);
	
	void save(List<McdEcExecLog> list);
}
