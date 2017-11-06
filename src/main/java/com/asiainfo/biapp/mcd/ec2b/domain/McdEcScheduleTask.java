package com.asiainfo.biapp.mcd.ec2b.domain;

import java.io.Serializable;
import java.util.Date;

/**
 * @Description: TODO
 * 
 * @author       zq
 * @date         2017年10月23日  下午12:32:44
 * Copyright: 	  北京亚信智慧数据科技有限公司
 */
public class McdEcScheduleTask implements Serializable {

	/** serialVersionUID */
	private static final long serialVersionUID = 1L;

	private String id;
	private String taskId;
	private String campsegId;
	private String campsegPid;
	private String dataDate;
	private String dataTable;
	private String cityId;
	private String userId;
	private String cepEventId;
	private String execContent;
	private int currentIndex;
	private int total;
	private int baFilter;
	private int fqcFilter;
	private int custCycle;
	private int status;
	private Date createTime;
	private Date scheduleTime;
	
	public String getExecContent() {
		return execContent;
	}
	public void setExecContent(String execContent) {
		this.execContent = execContent;
	}
	public String getCepEventId() {
		return cepEventId;
	}
	public void setCepEventId(String cepEventId) {
		this.cepEventId = cepEventId;
	}
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getTaskId() {
		return taskId;
	}
	public void setTaskId(String taskId) {
		this.taskId = taskId;
	}
	public String getCampsegId() {
		return campsegId;
	}
	public void setCampsegId(String campsegId) {
		this.campsegId = campsegId;
	}
	public String getCampsegPid() {
		return campsegPid;
	}
	public void setCampsegPid(String campsegPid) {
		this.campsegPid = campsegPid;
	}
	public String getDataDate() {
		return dataDate;
	}
	public void setDataDate(String dataDate) {
		this.dataDate = dataDate;
	}
	public String getDataTable() {
		return dataTable;
	}
	public void setDataTable(String dataTable) {
		this.dataTable = dataTable;
	}
	public String getCityId() {
		return cityId;
	}
	public void setCityId(String cityId) {
		this.cityId = cityId;
	}
	public int getCurrentIndex() {
		return currentIndex;
	}
	public void setCurrentIndex(int currentIndex) {
		this.currentIndex = currentIndex;
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
	public int getStatus() {
		return status;
	}
	public void setStatus(int status) {
		this.status = status;
	}
	public Date getCreateTime() {
		return createTime;
	}
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
	public int getCustCycle() {
		return custCycle;
	}
	public void setCustCycle(int custCycle) {
		this.custCycle = custCycle;
	}
	public Date getScheduleTime() {
		return scheduleTime;
	}
	public void setScheduleTime(Date scheduleTime) {
		this.scheduleTime = scheduleTime;
	}
	@Override
	public String toString() {
		return "McdEcScheduleTask [id=" + id + ", taskId=" + taskId + ", campsegId=" + campsegId + ", campsegPid="
				+ campsegPid + ", dataDate=" + dataDate + ", dataTable=" + dataTable + ", cityId=" + cityId
				+ ", userId=" + userId + ", cepEventId=" + cepEventId + ", execContent=" + execContent
				+ ", currentIndex=" + currentIndex + ", total=" + total + ", baFilter=" + baFilter + ", fqcFilter="
				+ fqcFilter + ", custCycle=" + custCycle + ", status=" + status + ", createTime=" + createTime
				+ ", scheduleTime=" + scheduleTime + "]";
	}
}
