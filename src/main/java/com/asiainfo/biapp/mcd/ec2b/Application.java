package com.asiainfo.biapp.mcd.ec2b;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.asiainfo.biapp.mcd.ec2b.service.IMcdEcScheduleService;
import com.asiainfo.biapp.mcd.ec2b.service.IMcdEcSendService;
import com.asiainfo.biapp.mcd.ec2b.service.IMcdEcSplitService;
import com.asiainfo.biapp.mcd.ec2b.thread.McdEcQueryThread;
import com.asiainfo.biapp.mcd.ec2b.thread.McdEcScheduleThread;
import com.asiainfo.biapp.mcd.ec2b.thread.McdEcSendThread;
import com.asiainfo.biapp.mcd.ec2b.thread.McdEcSplitThread;

/**
 * @Description: TODO
 * 
 * @author       zq
 * @date         2017年10月20日  下午3:56:46
 * Copyright: 	  北京亚信智慧数据科技有限公司
 */
@SpringBootApplication
@ComponentScan("com.asiainfo.biapp.mcd")
@RestController
@EnableScheduling
public class Application {
	
	@Autowired
	IMcdEcScheduleService schedule;
	
	@Autowired
	IMcdEcSplitService split;
	
	@Autowired
	IMcdEcSendService send;
	
	@RequestMapping("/schedule")
	String schedule() {
		new McdEcScheduleThread(schedule).run();
		return "running";
	}

	@RequestMapping("/split")
	String split() {
		new McdEcSplitThread(split).run();
		return "running";
	}
	
	@RequestMapping("/send")
	String send() {
		new McdEcSendThread(send).run();
		return "running";
	}

	@RequestMapping("/query")
	String query() {
		new McdEcQueryThread(send).run();
		return "running";
	}
	
	/** 
	 * @Description: TODO
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}
}
