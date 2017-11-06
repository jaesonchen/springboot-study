package com.asiainfo.biapp.mcd.ec2b.domain;

import java.io.Serializable;

/**
 * TODO
 * 
 * @author       zq
 * @date         2017年11月2日  下午2:19:15
 * Copyright: 	  北京亚信智慧数据科技有限公司
 */
public class McdEcQuotaConfig implements Serializable {

	/** serialVersionUID */
	private static final long serialVersionUID = 1L;

	private String dataDate;
	private int sendNum;
	private int quotaNum;
	private int quotaType;
	
	public int getSendNum() {
		return sendNum;
	}
	public void setSendNum(int sendNum) {
		this.sendNum = sendNum;
	}
	public int getQuotaType() {
		return quotaType;
	}
	public void setQuotaType(int quotaType) {
		this.quotaType = quotaType;
	}
	public String getDataDate() {
		return dataDate;
	}
	public void setDataDate(String dataDate) {
		this.dataDate = dataDate;
	}
	public int getQuotaNum() {
		return quotaNum;
	}
	public void setQuotaNum(int quotaNum) {
		this.quotaNum = quotaNum;
	}
	@Override
	public String toString() {
		return "McdEcQuota [dataDate=" + dataDate + ", sendNum=" + sendNum + ", quotaNum=" + quotaNum + ", quotaType="
				+ quotaType + "]";
	}
}
