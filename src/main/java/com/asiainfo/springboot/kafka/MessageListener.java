package com.asiainfo.springboot.kafka;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
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

    final Logger logger = LoggerFactory.getLogger(getClass());
    
    @Autowired
    KafkaTemplate<String, String> kafkaTemplate;
    
	@KafkaListener(topics="com.asiainfo.request")
    public void processMessage(String message) {
	    logger.info("message from topic(com.asiainfo.request): {}", message);
	    logger.info("after business logic, send to topici(com.asiainfo.response)!");
	    this.kafkaTemplate.send("com.asiainfo.response", message + "(from com.asiainfo.request)!");
	}
	
	@KafkaListener(topics="com.asiainfo.response")
    public void processMessage1(String message) {
		logger.info("message from topic(com.asiainfo.response): {}", message);
	}
}
