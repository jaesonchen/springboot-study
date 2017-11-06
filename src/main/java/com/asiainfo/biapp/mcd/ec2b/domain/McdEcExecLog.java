package com.asiainfo.biapp.mcd.ec2b.domain;

import java.io.Serializable;
import java.util.Date;

/**
 * @Description: TODO
 * 
 * @author       zq
 * @date         2017年10月23日  下午12:33:14
 * Copyright: 	  北京亚信智慧数据科技有限公司
 */
public class McdEcExecLog implements Serializable {

	/** serialVersionUID */
	private static final long serialVersionUID = 1L;

	private String taskId;
	private String campsegId;
	private String campsegPid;
	private String dataDate;
	private String dataTable;
	private String cityId;
	private String userId;
	private int startIndex;
	private int endIndex;
	private int total;
	private int baFilter;
	private int fqcFilter;
	private int success;
	private int failure;
	private int sendNum;
	private int stage;
	private int status;
	private Date createTime;
	
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
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
	public String getCityId() {
		return cityId;
	}
	public void setCityId(String cityId) {
		this.cityId = cityId;
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
	public int getStartIndex() {
		return startIndex;
	}
	public void setStartIndex(int startIndex) {
		this.startIndex = startIndex;
	}
	public int getEndIndex() {
		return endIndex;
	}
	public void setEndIndex(int endIndex) {
		this.endIndex = endIndex;
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
	public int getSuccess() {
		return success;
	}
	public void setSuccess(int success) {
		this.success = success;
	}
	public int getFailure() {
		return failure;
	}
	public void setFailure(int failure) {
		this.failure = failure;
	}
	public int getSendNum() {
		return sendNum;
	}
	public void setSendNum(int sendNum) {
		this.sendNum = sendNum;
	}
	public int getStage() {
		return stage;
	}
	public void setStage(int stage) {
		this.stage = stage;
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
	@Override
	public String toString() {
		return "McdEcExecLog [taskId=" + taskId + ", campsegId=" + campsegId + ", campsegPid=" + campsegPid
				+ ", dataDate=" + dataDate + ", dataTable=" + dataTable + ", cityId=" + cityId + ", userId=" + userId
				+ ", startIndex=" + startIndex + ", endIndex=" + endIndex + ", total=" + total + ", baFilter="
				+ baFilter + ", fqcFilter=" + fqcFilter + ", success=" + success + ", failure=" + failure + ", sendNum="
				+ sendNum + ", stage=" + stage + ", status=" + status + ", createTime=" + createTime + "]";
	}
}
