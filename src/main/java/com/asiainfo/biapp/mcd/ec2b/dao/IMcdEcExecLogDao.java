package com.asiainfo.biapp.mcd.ec2b.dao;

import java.util.List;

import com.asiainfo.biapp.mcd.ec2b.domain.McdEcExecLog;

/**
 * @Description: TODO
 * 
 * @author       zq
 * @date         2017年10月23日  下午1:14:28
 * Copyright: 	  北京亚信智慧数据科技有限公司
 */
public interface IMcdEcExecLogDao {

	void save(McdEcExecLog log);
	
	void save(List<McdEcExecLog> list);
}
