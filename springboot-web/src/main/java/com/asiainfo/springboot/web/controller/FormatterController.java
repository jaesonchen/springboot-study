package com.asiainfo.springboot.web.controller;

import java.util.Date;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.asiainfo.springboot.web.model.PhoneNumber;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;

/**   
 * @Description: TODO
 * 
 * @author chenzq  
 * @date 2019年7月11日 下午1:54:46
 * @version V1.0
 * @Copyright: Copyright(c) 2019 jaesonchen.com Inc. All rights reserved. 
 */
@Api(value = "格式化", tags = "格式化测试")
@RestController
public class FormatterController {

    @ApiOperation(value = "日期", notes = "日期格式化")
    @ApiImplicitParam(name = "date", value = "日期", required = true, dataType = "string", example = "2019-07-11 13:59:10", paramType = "query")
    @GetMapping("/date")
    public Date date(@RequestParam Date date) {
        return date;
    }
    
    @ApiOperation(value = "号码", notes = "号码格式化")
    @ApiImplicitParam(name = "phone", value = "日期", required = true, dataType = "string", example = "010-12345678", paramType = "query")
    @GetMapping("/phone")
    public PhoneNumber phone(@RequestParam PhoneNumber phone) {
        return phone;
    }
}
