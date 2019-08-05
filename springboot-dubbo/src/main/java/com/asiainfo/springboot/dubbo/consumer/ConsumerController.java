package com.asiainfo.springboot.dubbo.consumer;

import java.util.List;

import org.apache.dubbo.config.annotation.Reference;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import com.asiainfo.springboot.dubbo.model.User;
import com.asiainfo.springboot.dubbo.service.MenuService;
import com.asiainfo.springboot.dubbo.service.UserService;

/**   
 * @Description: method 配置在parameters里
 * 
 * @author chenzq  
 * @date 2019年7月14日 上午11:16:55
 * @version V1.0
 * @Copyright: Copyright(c) 2019 jaesonchen.com Inc. All rights reserved. 
 */
@RestController
public class ConsumerController {

    //mock = "com.asiainfo.springboot.dubbo.service.UserServiceMock" / mock = "true"
    @Reference(check = false, version = "*", parameters = { "check.timeout", "1000", 
            "check.mock", "com.asiainfo.springboot.dubbo.service.UserServiceMock" })
    private UserService userService;
    
    @Reference(group = "*", version = "1.0", parameters = { "getMenu.merger", "true" })
    private MenuService menuService;
    
    @GetMapping("/users")
    public List<User> findAll() {
        return this.userService.findAll();
    }
    
    @GetMapping("/users/{id}")
    public User getById(@PathVariable long id) {
        return this.userService.getById(id);
    }
    
    @GetMapping("/users/check")
    public Boolean check() {
        return this.userService.check();
    }
    
    @GetMapping("/menu")
    public List<String> menu() {
        return this.menuService.getMenu();
    }
}
