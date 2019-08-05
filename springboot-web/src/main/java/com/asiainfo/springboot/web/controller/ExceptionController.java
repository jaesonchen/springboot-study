package com.asiainfo.springboot.web.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.asiainfo.springboot.web.exception.MyException;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

/**   
 * @Description: TODO
 * 
 * @author chenzq  
 * @date 2019年7月11日 下午12:23:38
 * @version V1.0
 * @Copyright: Copyright(c) 2019 jaesonchen.com Inc. All rights reserved. 
 */
@Api(value = "异常处理", tags = "异常处理模块")
@Controller
public class ExceptionController {

    @ApiOperation(value = "custom")
    @GetMapping("/custom")
    public String custom() {
        throw new RuntimeException("custom exception!");
    }
    
    // Exception 和其他服务器错误返回默认的/error
    @ApiOperation(value = "global")
    @GetMapping("/global")
    public String global() throws Exception {
        throw new Exception("global exception!");
    }
    
    @ApiOperation(value = "json")
    @GetMapping("/json")
    @ResponseBody
    public String json() throws MyException {
        throw new MyException("json exception!");
    }
}
