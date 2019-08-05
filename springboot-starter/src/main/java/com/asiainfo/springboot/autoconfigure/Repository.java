package com.asiainfo.springboot.autoconfigure;

/**   
 * @Description: TODO
 * 
 * @author chenzq  
 * @date 2019年7月8日 下午2:50:29
 * @version V1.0
 * @Copyright: Copyright(c) 2019 jaesonchen.com Inc. All rights reserved. 
 */
public class Repository {

    public String getById(long id) {
        return "guest(" + id + ")";
    }
}
