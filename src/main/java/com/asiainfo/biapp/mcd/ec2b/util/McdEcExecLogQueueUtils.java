package com.asiainfo.biapp.mcd.ec2b.util;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import com.asiainfo.biapp.mcd.ec2b.domain.McdEcExecLog;

/**
 * @Description: 执行日志队列
 * 
 * @author       zq
 * @date         2017年10月24日  上午9:33:19
 * Copyright: 	  北京亚信智慧数据科技有限公司
 */
public class McdEcExecLogQueueUtils {

	private final BlockingQueue<McdEcExecLog> queue = new LinkedBlockingQueue<>();
	private McdEcExecLogQueueUtils() {}
	private static class McdEcExecLogQueueUtilsHolder {
		static McdEcExecLogQueueUtils instance = new McdEcExecLogQueueUtils();
	}
	
	public static McdEcExecLogQueueUtils getInstance() {
		return McdEcExecLogQueueUtilsHolder.instance;
	}
	
	public BlockingQueue<McdEcExecLog> queue() {
		return this.queue;
	}
	
	public void offer(McdEcExecLog log) {
		this.queue.offer(log);
	}
	
	public McdEcExecLog poll() {
		return this.queue.poll();
	}
}
