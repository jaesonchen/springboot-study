package com.asiainfo.springboot.servlet;

import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.context.annotation.Configuration;

/**
 * @ServletComponentScan 扫描servlet注解
 * 
 * @author       zq
 * @date         2017年10月22日  下午5:34:18
 * Copyright: 	  北京亚信智慧数据科技有限公司
 */
@Configuration
@ServletComponentScan("com.asiainfo.springboot.servlet")
public class ServletConfiguration {

}
