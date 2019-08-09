package com.asiainfo.springboot.kafka.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.asiainfo.springboot.kafka.listener.MyListener;

/**   
 * @Description: TODO
 * 
 * @author chenzq  
 * @date 2019年8月7日 下午4:22:48
 * @version V1.0
 * @Copyright: Copyright(c) 2019 jaesonchen.com Inc. All rights reserved. 
 */
@Configuration
public class KafkaConfig {

    @Bean
    public MyListener myListener() {
        return new MyListener();
    }
}
