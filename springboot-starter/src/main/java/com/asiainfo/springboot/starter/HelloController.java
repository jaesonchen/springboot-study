package com.asiainfo.springboot.starter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.asiainfo.springboot.autoconfigure.Service;

/**   
 * @Description: TODO
 * 
 * @author chenzq  
 * @date 2019年7月8日 下午6:09:40
 * @version V1.0
 * @Copyright: Copyright(c) 2019 jaesonchen.com Inc. All rights reserved. 
 */
@RestController
public class HelloController {

    @Autowired
    private Service service;
    
    @GetMapping("/hello/{id}")
    public String hello(@PathVariable long id) {
        return service.hello(id);
    }
    
    @RequestMapping("/hello")
    public String index() {
        return "Hello World";
    }
}
