package com.asiainfo.springboot.dubbo.service;

import java.util.ArrayList;
import java.util.List;

import org.apache.dubbo.config.annotation.Service;

/**   
 * @Description: TODO
 * 
 * @author chenzq  
 * @date 2019年5月1日 下午6:44:12
 * @version V1.0
 * @Copyright: Copyright(c) 2019 jaesonchen.com Inc. All rights reserved. 
 */
@Service(group = "subMenu", version = "1.0")
public class SubMenuServiceImpl implements MenuService {

    @Override
    public List<String> getMenu() {
        List<String> menus = new ArrayList<String>();
        menus.add("menu-1.1");
        menus.add("menu-1.2");
        menus.add("menu-2.1");
        menus.add("menu-2.2");
        return menus;
    }
}
