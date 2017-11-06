package com.asiainfo.biapp.mcd.ec2b.domain;

import java.io.Serializable;

/**
 * @Description: TODO
 * 
 * @author       zq
 * @date         2017年10月23日  下午1:29:20
 * Copyright: 	  北京亚信智慧数据科技有限公司
 */
public class McdCampTaskDate implements Serializable {

	/** serialVersionUID */
	private static final long serialVersionUID = 1L;

	private String taskId;
	private String campsegId;
	private String campsegPid;
	private String cityId;
	private String execContent;
	private String custgroupTableName;
	private String dataDate;
	private String userId;
	private String cepEventId;
	private int custgroupNum;
	private int execStatus;
	private int custCycle;
	private int ifHaveVar;
	
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
	public String getExecContent() {
		return execContent;
	}
	public void setExecContent(String execContent) {
		this.execContent = execContent;
	}
	public String getCustgroupTableName() {
		return custgroupTableName;
	}
	public void setCustgroupTableName(String custgroupTableName) {
		this.custgroupTableName = custgroupTableName;
	}
	public String getDataDate() {
		return dataDate;
	}
	public void setDataDate(String dataDate) {
		this.dataDate = dataDate;
	}
	public int getCustgroupNum() {
		return custgroupNum;
	}
	public void setCustgroupNum(int custgroupNum) {
		this.custgroupNum = custgroupNum;
	}
	public int getExecStatus() {
		return execStatus;
	}
	public void setExecStatus(int execStatus) {
		this.execStatus = execStatus;
	}
	public int getCustCycle() {
		return custCycle;
	}
	public void setCustCycle(int custCycle) {
		this.custCycle = custCycle;
	}
	public int getIfHaveVar() {
		return ifHaveVar;
	}
	public void setIfHaveVar(int ifHaveVar) {
		this.ifHaveVar = ifHaveVar;
	}
	@Override
	public String toString() {
		return "McdCampTaskDate [taskId=" + taskId + ", campsegId=" + campsegId + ", campsegPid=" + campsegPid
				+ ", cityId=" + cityId + ", execContent=" + execContent + ", custgroupTableName=" + custgroupTableName
				+ ", dataDate=" + dataDate + ", userId=" + userId + ", cepEventId=" + cepEventId + ", custgroupNum="
				+ custgroupNum + ", execStatus=" + execStatus + ", custCycle=" + custCycle + ", ifHaveVar=" + ifHaveVar
				+ "]";
	}
}
