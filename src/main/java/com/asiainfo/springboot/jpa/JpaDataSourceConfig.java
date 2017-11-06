package com.asiainfo.springboot.jpa;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.jdbc.DataSourceBuilder;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @Description: TODO
 * 
 * @author       zq
 * @date         2017年10月20日  下午3:44:51
 * Copyright: 	  北京亚信智慧数据科技有限公司
 */
@Configuration
public class JpaDataSourceConfig {

	@Bean(name = "dataSource")
	@Qualifier("dataSource")
    @ConfigurationProperties(prefix="spring.datasource")
    public DataSource dataSource() {
        return DataSourceBuilder.create().build();
    }
}
