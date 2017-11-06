package com.asiainfo.biapp.mcd.ec2b.domain;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * @Description: TODO
 * 
 * @author       zq
 * @date         2017年10月23日  下午3:51:05
 * Copyright: 	  北京亚信智慧数据科技有限公司
 */
public class FilterResult implements Serializable {

	/** serialVersionUID */
	private static final long serialVersionUID = 1L;

	private List<String> query = new ArrayList<>();
	private List<String> success = new ArrayList<>();
	private List<String> failure = new ArrayList<>();
	
	public List<String> getQuery() {
		return query;
	}
	public void setQuery(List<String> query) {
		this.query = query;
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
}
