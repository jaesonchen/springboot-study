package com.asiainfo.springboot.properties;

/**   
 * @Description: TODO
 * 
 * @author chenzq  
 * @date 2019年7月9日 下午4:47:01
 * @version V1.0
 * @Copyright: Copyright(c) 2019 jaesonchen.com Inc. All rights reserved. 
 */
public class PoolProperties {

    private String id;
    private long timeout;
    private int maxIdle;
    private int minIdle;
    private int total;
    
    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }
    public long getTimeout() {
        return timeout;
    }
    public void setTimeout(long timeout) {
        this.timeout = timeout;
    }
    public int getMaxIdle() {
        return maxIdle;
    }
    public void setMaxIdle(int maxIdle) {
        this.maxIdle = maxIdle;
    }
    public int getMinIdle() {
        return minIdle;
    }
    public void setMinIdle(int minIdle) {
        this.minIdle = minIdle;
    }
    public int getTotal() {
        return total;
    }
    public void setTotal(int total) {
        this.total = total;
    }
    @Override
    public String toString() {
        return "PoolProperties [id=" + id + ", timeout=" + timeout + ", maxIdle=" + maxIdle + ", minIdle=" + minIdle
                + ", total=" + total + "]";
    }
}
