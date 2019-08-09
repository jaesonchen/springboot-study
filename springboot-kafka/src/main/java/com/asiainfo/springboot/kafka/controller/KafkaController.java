package com.asiainfo.springboot.kafka.controller;

import java.util.Date;

import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.header.internals.RecordHeader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.util.concurrent.ListenableFutureCallback;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import com.asiainfo.springboot.kafka.model.Request;

/**   
 * @Description: TODO
 * 
 * @author chenzq  
 * @date 2019年8月7日 下午4:23:48
 * @version V1.0
 * @Copyright: Copyright(c) 2019 jaesonchen.com Inc. All rights reserved. 
 */
@RestController
public class KafkaController {

    final Logger logger = LoggerFactory.getLogger(getClass());
    
    @Value("${mylistener.topics}")
    private String topic;
    
    @Autowired
    KafkaTemplate<Object, Object> kafkaTemplate;
    
    @GetMapping("/send")
    public Object produce() throws Exception {
        Request message = new Request(1001L, "jaeson", new Date());
        ListenableFuture<SendResult<Object, Object>> future = kafkaTemplate.send(topic, message);
        future.addCallback(new ListenableFutureCallback<SendResult<Object, Object>>() {
            @Override
            public void onSuccess(SendResult<Object, Object> result) {
                logger.info("message: {} send success!", message);
            }
            @Override
            public void onFailure(Throwable ex) {
                logger.error("message send error!", ex);
            }
        });
        return "success";
    }
    
    @GetMapping("/send/{topic}/{message}")
    public Object produce(@PathVariable("topic") String topic, @PathVariable("message") String message) {
        ProducerRecord<Object, Object> record = new ProducerRecord<>(topic, message);
        record.headers().add(new RecordHeader("code", "1234".getBytes()));
        kafkaTemplate.send(record);
        return "success";
    }
}
