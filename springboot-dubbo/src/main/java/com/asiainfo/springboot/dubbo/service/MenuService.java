package com.asiainfo.springboot.dubbo.service;

import java.util.List;

import org.apache.dubbo.config.annotation.Service;

/**   
 * @Description: Menu 业务接口，消费端调用时设置Merge，可以合并多个group的实现
 * 
 * @author chenzq  
 * @date 2019年5月1日 下午6:42:24
 * @version V1.0
 * @Copyright: Copyright(c) 2019 jaesonchen.com Inc. All rights reserved. 
 */
@Service(group = "menu", version = "1.0")
public interface MenuService {
    
    List<String> getMenu();
}
