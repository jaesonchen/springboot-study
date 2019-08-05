package com.asiainfo.springboot.jdbc.route;

/**
 * 
 * @Description: ThreadLocal 保存上下文数据源类型
 * 
 * @author chenzq
 * @date 2019年7月12日 下午1:35:41
 * @version V1.0
 * @Copyright: Copyright(c) 2019 jaesonchen.com Inc. All rights reserved.
 */
public class DataSourceHolder {

    public static final ThreadLocal<DataSourceType> HOLDER = new ThreadLocal<DataSourceType>();

    public static void setDataSource(DataSourceType name) {
        HOLDER.set(name);
    }

    public static DataSourceType getDataSouce() {
        return HOLDER.get();
    }

    public static void clear() {
        HOLDER.remove();
    }
}
