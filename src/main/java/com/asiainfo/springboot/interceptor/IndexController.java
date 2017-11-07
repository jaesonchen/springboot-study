package com.asiainfo.springboot.interceptor;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * 
 * @Description: TODO
 * 
 * @author       zq
 * @date         2017年10月22日  下午10:46:35
 * Copyright: 	  北京亚信智慧数据科技有限公司
 */
@Controller
public class IndexController {

    @RequestMapping(value = "/login_view", method = RequestMethod.GET)
    public String loginView() {
        return "login";
    }

    @RequestMapping(value = "/index", method = RequestMethod.GET)
    public String index() {
        return "index";
    }
}
