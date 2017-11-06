package com.asiainfo.biapp.mcd.ec2b.util;

import java.util.UUID;
import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * id生成工具类
 * 
 * @author       zq
 * @date         2017年10月24日  下午2:42:48
 * Copyright: 	  北京亚信智慧数据科技有限公司
 */
@Component
public class IdGenerator {

	@Value("${mcd.ec.sms.server:1}")
	private int server;
	
	public static String generatorUUID() {
		return UUID.randomUUID().toString().replace("-", "");
	}
	
	public synchronized String generateTimestampId() {
		
		StringBuilder sb = new StringBuilder();
		sb.append(System.currentTimeMillis())
			.append(String.format("%04d", server));
		
		try {
			TimeUnit.MILLISECONDS.sleep(1);
		} catch (Exception ex) {}
		return sb.toString();
	}
}
