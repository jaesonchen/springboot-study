package com.asiainfo.springboot.schedule;

import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * TODO
 * 
 * @author       zq
 * @date         2017年11月6日  下午4:04:40
 * Copyright: 	  北京亚信智慧数据科技有限公司
 */
@Component
public class ScheduleTask {

	final Logger logger = LoggerFactory.getLogger(getClass());
	
	@Scheduled(cron = "0/10 * * * * *")
	public void cron() {
		logger.info("cron execute...");
	}
	
	@Scheduled(fixedRateString="${fixRate.ms}")
	public void fixRate() {
		try {
			logger.info("fixRate start...");
			TimeUnit.SECONDS.sleep(1);
			logger.info("fixRate end...");
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	@Scheduled(fixedDelay=5000)
	public void fixDelay() {
		try {
			logger.info("fixDelay start...");
			TimeUnit.SECONDS.sleep(1);
			logger.info("fixDelay end...");
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
}
