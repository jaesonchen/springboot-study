package com.asiainfo.springboot.kafka;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

/**
 * TODO
 * 
 * @author       zq
 * @date         2017年11月2日  下午3:57:26
 * Copyright: 	  北京亚信智慧数据科技有限公司
 */
@Component
public class MessageListener {

	@KafkaListener(topics="com.asiainfo.bdx.cgf.request")
    public void processMessage(String message) {
		System.out.println(message);
	}
	
	@KafkaListener(topics="com.asiainfo.bdx.cgf.response")
    public void processMessage1(String message) {
		System.out.println("response:" + message);
	}
}
