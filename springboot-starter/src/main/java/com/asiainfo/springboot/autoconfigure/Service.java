package com.asiainfo.springboot.autoconfigure;

/**   
 * @Description: TODO
 * 
 * @author chenzq  
 * @date 2019年7月8日 下午2:31:58
 * @version V1.0
 * @Copyright: Copyright(c) 2019 jaesonchen.com Inc. All rights reserved. 
 */
public class Service {
    
    private String welcome;
    private Repository repository;
    
    public void setWelcome(String welcome) {
        this.welcome = welcome;
    }
    public void setRepository(Repository repository) {
        this.repository = repository;
    }

    public String hello(long id) {
        return welcome + " " + repository.getById(id);
    }
}
