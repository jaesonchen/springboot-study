package com.asiainfo.springboot.jdbc.config;

import java.util.HashMap;
import java.util.Map;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.annotation.Order;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;

import com.asiainfo.springboot.jdbc.route.DataSourceType;
import com.asiainfo.springboot.jdbc.route.MyRoutingDataSource;

/**   
 * @Description: TODO
 * 
 * @author chenzq  
 * @date 2019年7月12日 下午12:56:02
 * @version V1.0
 * @Copyright: Copyright(c) 2019 jaesonchen.com Inc. All rights reserved. 
 */
@Configuration
public class DataSourceConfig {

    @Primary
    @Bean("dataSource")
    @Qualifier("dataSource")
    @ConfigurationProperties(prefix = "spring.datasource.primary")
    public DataSource dataSource() {
        return DataSourceBuilder.create().build();
    }

    @Bean("secondaryDataSource")
    @Qualifier("secondaryDataSource")
    @ConfigurationProperties(prefix = "spring.datasource.secondary")
    public DataSource secondaryDataSource() {
        return DataSourceBuilder.create().build();
    }

    @Bean("routeDataSource")
    @Qualifier("routeDataSource")
    public DataSource myRoutingDataSource(@Qualifier("dataSource") DataSource masterDataSource,
                                          @Qualifier("secondaryDataSource") DataSource slaveDataSource) {
        
        Map<Object, Object> dataSources = new HashMap<>();
        dataSources.put(DataSourceType.MASTER, masterDataSource);
        dataSources.put(DataSourceType.SLAVE, slaveDataSource);
        MyRoutingDataSource myRoutingDataSource = new MyRoutingDataSource();
        myRoutingDataSource.setDefaultTargetDataSource(masterDataSource);
        myRoutingDataSource.setTargetDataSources(dataSources);
        return myRoutingDataSource;
    }
    
    @Primary
    @Bean("jdbcTemplate")
    public JdbcTemplate jdbcTemplate(@Qualifier("routeDataSource") DataSource dataSource) {
        return new JdbcTemplate(dataSource);
    }
    
    @Order(1)
    @Bean("transactionManager")
    public PlatformTransactionManager transactionManager(@Qualifier("routeDataSource") DataSource dataSource) {
        return new DataSourceTransactionManager(dataSource);
    }
}
