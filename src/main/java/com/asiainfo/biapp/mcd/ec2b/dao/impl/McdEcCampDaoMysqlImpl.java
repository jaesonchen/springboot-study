package com.asiainfo.biapp.mcd.ec2b.dao.impl;

import java.util.List;

import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Repository;

import com.asiainfo.biapp.mcd.ec2b.domain.ImportResult;
import com.asiainfo.biapp.mcd.ec2b.domain.InsertCondition;

/**
 * TODO
 * 
 * @author       zq
 * @date         2017年11月6日  上午10:56:30
 * Copyright: 	  北京亚信智慧数据科技有限公司
 */
@Repository
@Profile("mysql")
public class McdEcCampDaoMysqlImpl extends McdEcCampDaoImpl {

	@Override
	public ImportResult insertByCondition(String target, String source, List<InsertCondition> includeList,
			List<InsertCondition> excludeList) {

		//TODO mysql的rownum实现
		StringBuilder sql = new StringBuilder();
		sql.append(" insert into ").append(target).append("(product_no, data_date, data_index) ")
			.append("select product_no, data_date, rownum from ")
			.append("(select @rownum := @rownum+1 AS rownum,").append(source).append(".* ")
	 		.append("from (select @rownum:=0) rownum_alias, ").append(source).append(") a ")
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
}
