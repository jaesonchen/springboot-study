package com.asiainfo.biapp.mcd.ec2b.util;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * TODO
 * 
 * @author       zq
 * @date         2017年11月3日  下午5:44:01
 * Copyright: 	  北京亚信智慧数据科技有限公司
 */
public class ThreadPoolUtils {

	private final ExecutorService service;
	private ThreadPoolUtils(int coreSize, int waiter) {
		service = new ThreadPoolExecutor(coreSize, coreSize,
                0L, TimeUnit.MILLISECONDS,
                new LinkedBlockingQueue<Runnable>(waiter),
                Executors.defaultThreadFactory());
	}
	static class ThreadPoolUtilsHolder {
		static ThreadPoolUtils instance = new ThreadPoolUtils(10, 1000);
	}
	public static ExecutorService getInstance() {
		return ThreadPoolUtilsHolder.instance.service;
	}
	public static void shutdown(ExecutorService service) {
		try {
			if (null != service) {
				service.shutdown();
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}
}
