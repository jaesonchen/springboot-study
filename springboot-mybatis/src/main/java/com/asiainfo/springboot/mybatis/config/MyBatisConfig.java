package com.asiainfo.springboot.mybatis.config;

import java.util.Properties;

import org.apache.ibatis.mapping.VendorDatabaseIdProvider;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingClass;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.asiainfo.springboot.mybatis.interceptor.MyInterceptor;
import com.asiainfo.springboot.mybatis.page.MyPageInterceptor;

/**   
 * @Description: @MapperScan 扫描Dao目录以生成Mapper代理。没有指定@MapperScan时，需要在Dao上标注@Mapper，springboot会自动扫描@Mapper注解
 *               @MapperScan("com.asiainfo.springboot.mybatis.dao")
 * 
 * @author chenzq  
 * @date 2019年7月16日 下午7:12:59
 * @version V1.0
 * @Copyright: Copyright(c) 2019 jaesonchen.com Inc. All rights reserved. 
 */
@Configuration
//@MapperScan("com.asiainfo.springboot.mybatis.dao")
public class MyBatisConfig {

    @Bean
    public MyInterceptor myInterceptor() {
        MyInterceptor interceptor = new MyInterceptor();
        Properties properties = new Properties(); 
        properties.setProperty("welcome", "hello world");
        interceptor.setProperties(properties);
        return interceptor;
    }
    
    @Bean
    @ConditionalOnMissingClass({ "com.github.pagehelper.autoconfigure.PageHelperAutoConfiguration" })
    public MyPageInterceptor myPageInterceptor() {
        MyPageInterceptor interceptor = new MyPageInterceptor();
        Properties properties = new Properties(); 
        properties.setProperty("pigeSize", "10");
        properties.setProperty("dbType", "mysql");
        interceptor.setProperties(properties);
        return interceptor;
    }
    
    @Bean
    public VendorDatabaseIdProvider databaseIdProvider() {
        VendorDatabaseIdProvider provider = new VendorDatabaseIdProvider();
        Properties prop = new Properties();
        prop.put("Oracle", "oracle");
        prop.put("MySQL", "mysql");
        provider.setProperties(prop);
        return provider;
    }
    
    // 手动配置
    /*@Bean
    public SqlSessionFactoryBean sqlSessionFactory(DataSource dataSource, VendorDatabaseIdProvider databaseIdProvider) throws Exception {
        SqlSessionFactoryBean factory = new SqlSessionFactoryBean();
        factory.setDataSource(dataSource);
        factory.setDatabaseIdProvider(databaseIdProvider);
        factory.setTypeAliasesPackage("com.asiainfo.springboot.mybatis.model");
        factory.setMapperLocations(new PathMatchingResourcePatternResolver().getResources("classpath:mapper/*.xml"));
        factory.setConfigLocation(new ClassPathResource("mybatis-config.xml"));
        return factory;
    }*/
}
