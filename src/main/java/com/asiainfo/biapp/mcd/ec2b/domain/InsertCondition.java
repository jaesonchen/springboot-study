package com.asiainfo.biapp.mcd.ec2b.domain;

import java.io.Serializable;

/**
 * TODO
 * 
 * @author       zq
 * @date         2017年11月6日  上午10:33:01
 * Copyright: 	  北京亚信智慧数据科技有限公司
 */
public class InsertCondition implements Serializable {

	/** serialVersionUID */
	private static final long serialVersionUID = 1L;

	private String table;
	private String column;
	public InsertCondition() {}
	public InsertCondition(String table, String column) {
		this.table = table;
		this.column = column;
	}
	public String getTable() {
		return table;
	}
	public void setTable(String table) {
		this.table = table;
	}
	public String getColumn() {
		return column;
	}
	public void setColumn(String column) {
		this.column = column;
	}
	@Override
	public String toString() {
		return "InsertCondition [table=" + table + ", column=" + column + "]";
	}
}
