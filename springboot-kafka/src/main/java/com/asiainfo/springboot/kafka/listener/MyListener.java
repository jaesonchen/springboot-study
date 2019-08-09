package com.asiainfo.springboot.kafka.listener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;

import com.asiainfo.springboot.kafka.model.Request;

/**   
 * @Description: TODO
 * 
 * @author chenzq  
 * @date 2019年8月7日 下午4:23:26
 * @version V1.0
 * @Copyright: Copyright(c) 2019 jaesonchen.com Inc. All rights reserved. 
 */
public class MyListener {

    final Logger logger = LoggerFactory.getLogger(getClass());
    
    @KafkaListener(topics = "${mylistener.topics}", groupId = "${mylistener.group-id}")
    public void processMessage1(Request message) {
        logger.info("message received: {}", message);
    }
    
    @KafkaListener(topics = "com.asiainfo.response")
    public void processMessage2(
            @Payload String message, 
            @Header(KafkaHeaders.RECEIVED_TOPIC) String topic, 
            @Header(KafkaHeaders.RECEIVED_PARTITION_ID) int partition, 
            @Header(KafkaHeaders.OFFSET) int offset, 
            @Header("code") byte[] code) {
        
        logger.info("topic: {}, partition: {}, offset: {}, message received: {}, code: {}", topic, partition, offset, message, new String(code));
    }
}
