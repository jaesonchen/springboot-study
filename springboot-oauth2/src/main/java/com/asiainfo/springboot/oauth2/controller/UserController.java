package com.asiainfo.springboot.oauth2.controller;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**   
 * @Description: TODO
 * 
 * @author chenzq  
 * @date 2019年7月30日 下午2:43:11
 * @version V1.0
 * @Copyright: Copyright(c) 2019 jaesonchen.com Inc. All rights reserved. 
 */
@RestController
public class UserController {

    @GetMapping("/api")
    public Object index(@AuthenticationPrincipal Authentication authentication) {
        return authentication;
    }
    
    @GetMapping("/list")
    public Object home() {
        return "hello world";
    }
}
