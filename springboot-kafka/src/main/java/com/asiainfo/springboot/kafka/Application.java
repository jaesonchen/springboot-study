package com.asiainfo.springboot.kafka;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**   
 * @Description: KafkaAutoConfiguration 自动配置kafka消费容器、KafkaTemplate，默认配置为 ConcurrentKafkaListenerContainerFactory，并自动激活 @EnableKafka
 * 
 *               @EnableKafka 自动注册bean里标注 @KafkaListener 的方法为容器消费监听器
 *               @KafkaListener 标注一个方法为指定topic的消费者
 * 
 * @author chenzq  
 * @date 2019年7月15日 下午2:37:54
 * @version V1.0
 * @Copyright: Copyright(c) 2019 jaesonchen.com Inc. All rights reserved. 
 */
@SpringBootApplication
public class Application {
    
    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
}
