package com.asiainfo.springboot.web.schedule;

import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;

/**   
 * @Description: spring调度任务
 * 
 * @author chenzq  
 * @date 2019年7月13日 下午2:43:58
 * @version V1.0
 * @Copyright: Copyright(c) 2019 jaesonchen.com Inc. All rights reserved. 
 */
//@Component
public class ScheduleTask {

    final Logger logger = LoggerFactory.getLogger(getClass());
    
    // cron表达式
    @Scheduled(cron = "0/10 * * * * *")
    public void cron() {
        logger.info("cron execute...");
    }
    
    // 调用时间间隔
    @Scheduled(fixedRateString = "${service.schedule.fix-rate.ms}")
    public void fixRate() {
        try {
            logger.info("fixRate start...");
            TimeUnit.SECONDS.sleep(1);
            logger.info("fixRate end...");
        } catch (InterruptedException e) {
            // ignore
        }
    }
    
    // 上一次结束到下一次开始执行的时间间隔， initialDelay 第一次调用的时间点
    @Scheduled(initialDelay = 1000, fixedDelay = 5000)
    public void fixDelay() {
        try {
            logger.info("fixDelay start...");
            TimeUnit.SECONDS.sleep(1);
            logger.info("fixDelay end...");
        } catch (InterruptedException e) {
            // ignore
        }
    }
}
