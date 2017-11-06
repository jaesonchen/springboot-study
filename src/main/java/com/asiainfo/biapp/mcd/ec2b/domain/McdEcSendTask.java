package com.asiainfo.biapp.mcd.ec2b.domain;

import java.io.Serializable;
import java.util.Date;

/**
 * @Description: TODO
 * 
 * @author       zq
 * @date         2017年10月23日  下午12:32:24
 * Copyright: 	  北京亚信智慧数据科技有限公司
 */
public class McdEcSendTask implements Serializable {

	/** serialVersionUID */
	private static final long serialVersionUID = 1L;

	private String id;
	private String taskId;
	private String campsegId;
	private String campsegPid;
	private String queryId;
	private String dataTable;
	private String dataDate;
	private String cityId;
	private String userId;
	private String execContent;
	private int startIndex;
	private int endIndex;
	private int total;
	private int sendNum;
	private int success;
	private int failure;
	private int baFilter;
	private int fqcFilter;
	private int custCycle;
	private int status;
	private Date createTime;
	private Date sendTime;
	
	public String getExecContent() {
		return execContent;
	}
	public void setExecContent(String execContent) {
		this.execContent = execContent;
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
	public String getQueryId() {
		return queryId;
	}
	public void setQueryId(String queryId) {
		this.queryId = queryId;
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
	public int getSendNum() {
		return sendNum;
	}
	public void setSendNum(int sendNum) {
		this.sendNum = sendNum;
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
	public Date getSendTime() {
		return sendTime;
	}
	public void setSendTime(Date sendTime) {
		this.sendTime = sendTime;
	}
	@Override
	public String toString() {
		return "McdEcSendTask [id=" + id + ", taskId=" + taskId + ", campsegId=" + campsegId + ", queryId=" + queryId
				+ ", dataTable=" + dataTable + ", dataDate=" + dataDate + ", cityId=" + cityId + ", userId=" + userId
				+ ", startIndex=" + startIndex + ", endIndex=" + endIndex + ", total=" + total + ", sendNum=" + sendNum
				+ ", success=" + success + ", failure=" + failure + ", baFilter=" + baFilter + ", fqcFilter="
				+ fqcFilter + ", custCycle=" + custCycle + ", status=" + status + ", createTime=" + createTime
				+ ", sendTime=" + sendTime + "]";
	}
}
