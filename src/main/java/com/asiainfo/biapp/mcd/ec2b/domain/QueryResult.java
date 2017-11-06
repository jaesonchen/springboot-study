package com.asiainfo.biapp.mcd.ec2b.domain;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * @Description: TODO
 * 
 * @author       zq
 * @date         2017年10月25日  下午3:59:20
 * Copyright: 	  北京亚信智慧数据科技有限公司
 */
public class QueryResult implements Serializable {

	/** serialVersionUID */
	private static final long serialVersionUID = 1L;

	private String queryId;
	private List<String> success = new ArrayList<String>();
	private List<String> failure = new ArrayList<String>();
	private int result;
	
	public String getQueryId() {
		return queryId;
	}
	public void setQueryId(String queryId) {
		this.queryId = queryId;
	}
	public List<String> getSuccess() {
		return success;
	}
	public void setSuccess(List<String> success) {
		this.success = success;
	}
	public List<String> getFailure() {
		return failure;
	}
	public void setFailure(List<String> failure) {
		this.failure = failure;
	}
	public int getResult() {
		return result;
	}
	public void setResult(int result) {
		this.result = result;
	}
	@Override
	public String toString() {
		return "QueryResult [queryId=" + queryId + ", success=" + success + ", failure=" + failure + ", result="
				+ result + "]";
	}
}
