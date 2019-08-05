package com.asiainfo.springboot.jdbc.route;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;

/**
 * 
 * @Description: 数据源路由
 * 
 * @author chenzq
 * @date 2019年7月12日 下午1:36:26
 * @version V1.0
 * @Copyright: Copyright(c) 2019 jaesonchen.com Inc. All rights reserved.
 */
public class MyRoutingDataSource extends AbstractRoutingDataSource {

    final Logger logger = LoggerFactory.getLogger(getClass());
    
    @Override
    protected Object determineCurrentLookupKey() {
        logger.info("LookupKey {}", DataSourceHolder.getDataSouce());
        return DataSourceHolder.getDataSouce();
    }
}
