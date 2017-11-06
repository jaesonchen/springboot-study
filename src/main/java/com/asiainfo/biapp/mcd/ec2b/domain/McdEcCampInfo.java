package com.asiainfo.biapp.mcd.ec2b.domain;

import java.io.Serializable;

/**
 * TODO
 * 
 * @author       zq
 * @date         2017年11月3日  下午4:28:48
 * Copyright: 	  北京亚信智慧数据科技有限公司
 */
public class McdEcCampInfo implements Serializable {

	/** serialVersionUID */
	private static final long serialVersionUID = 1L;
	
	private String campsegId;
	private String campsegPid;
	private String execContent;
	private String cepEventId;
	private String cityId;
	private int execStatus;
	private int ifHaveVar;
	
	public String getCityId() {
		return cityId;
	}
	public void setCityId(String cityId) {
		this.cityId = cityId;
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
	public int getExecStatus() {
		return execStatus;
	}
	public void setExecStatus(int execStatus) {
		this.execStatus = execStatus;
	}
	public int getIfHaveVar() {
		return ifHaveVar;
	}
	public void setIfHaveVar(int ifHaveVar) {
		this.ifHaveVar = ifHaveVar;
	}
	@Override
	public String toString() {
		return "McdEcCampInfo [campsegId=" + campsegId + ", campsegPid=" + campsegPid + ", execContent=" + execContent
				+ ", cepEventId=" + cepEventId + ", cityId=" + cityId + ", execStatus=" + execStatus + ", ifHaveVar="
				+ ifHaveVar + "]";
	}
}
