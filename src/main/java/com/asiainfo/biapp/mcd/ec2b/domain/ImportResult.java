package com.asiainfo.biapp.mcd.ec2b.domain;

import java.io.Serializable;

/**
 * @Description: TODO
 * 
 * @author       zq
 * @date         2017年10月23日  下午2:28:40
 * Copyright: 	  北京亚信智慧数据科技有限公司
 */
public class ImportResult implements Serializable {

	/** serialVersionUID */
	private static final long serialVersionUID = 1L;

	private String campsegId;
	private String taskId;
	private String dataDate;
	private String dataTable;
	private int total;
	private int baFilter;
	private int fqcFilter;
	
	public String getDataTable() {
		return dataTable;
	}
	public void setDataTable(String dataTable) {
		this.dataTable = dataTable;
	}
	public String getCampsegId() {
		return campsegId;
	}
	public void setCampsegId(String campsegId) {
		this.campsegId = campsegId;
	}
	public String getTaskId() {
		return taskId;
	}
	public void setTaskId(String taskId) {
		this.taskId = taskId;
	}
	public String getDataDate() {
		return dataDate;
	}
	public void setDataDate(String dataDate) {
		this.dataDate = dataDate;
	}
	public int getTotal() {
		return total;
	}
	public void setTotal(int total) {
		this.total = total;
	}
	public int getBaFilter() {
		return baFilter;
	}
	public void setBaFilter(int baFilter) {
		this.baFilter = baFilter;
	}
	public int getFqcFilter() {
		return fqcFilter;
	}
	public void setFqcFilter(int fqcFilter) {
		this.fqcFilter = fqcFilter;
	}
	@Override
	public String toString() {
		return "ImportResult [campsegId=" + campsegId + ", taskId=" + taskId + ", dataDate=" + dataDate + ", dataTable="
				+ dataTable + ", total=" + total + ", baFilter=" + baFilter + ", fqcFilter=" + fqcFilter + "]";
	}
}
