package com.asiainfo.springboot.kafka;

import java.util.Map;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.kafka.KafkaProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.config.KafkaListenerContainerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.kafka.listener.ConcurrentMessageListenerContainer;

/**
 * TODO
 * 
 * @author       zq
 * @date         2017年11月3日  上午9:45:32
 * Copyright: 	  北京亚信智慧数据科技有限公司
 */
@Configuration
@EnableKafka
public class KafkaConfig {
	
	@Bean
	@Primary
	@Qualifier("kafkaProperties")
	@ConfigurationProperties(prefix = "spring.kafka")
	public KafkaProperties kafkaProperties() {
		return new KafkaProperties();
	}
	
	@Bean
	@Qualifier("consumerFactory")
	public DefaultKafkaConsumerFactory<String, String> consumerFactory(@Qualifier("kafkaProperties") KafkaProperties properties) {
		return new DefaultKafkaConsumerFactory<String, String>(properties.buildConsumerProperties());
	}

	@Bean
    public KafkaListenerContainerFactory<ConcurrentMessageListenerContainer<String, String>> kafkaListenerContainerFactory(
    		@Qualifier("kafkaProperties") KafkaProperties properties, 
    		@Qualifier("consumerFactory") DefaultKafkaConsumerFactory<String, String> consumerFactory) {
		
        ConcurrentKafkaListenerContainerFactory<String, String> factory = new ConcurrentKafkaListenerContainerFactory<String, String>();
        factory.setConsumerFactory(consumerFactory);
        factory.setConcurrency(properties.getListener().getConcurrency());
        factory.getContainerProperties().setPollTimeout(properties.getListener().getPollTimeout());
        return factory;
    }
	
	@Bean
	public KafkaTemplate<String, String> createTemplate(@Qualifier("kafkaProperties") KafkaProperties properties) {
		
		Map<String, Object> props = properties.buildProducerProperties();
		ProducerFactory<String, String> pf = new DefaultKafkaProducerFactory<String, String>(props);
		return new KafkaTemplate<String, String>(pf);
	}
}
