package com.asiainfo.springboot.web.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.asiainfo.springboot.web.model.Response;
import com.asiainfo.springboot.web.view.Views;
import com.fasterxml.jackson.annotation.JsonView;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

/**   
 * @Description: json视图管理
 * 
 * @author chenzq  
 * @date 2019年7月10日 下午7:58:00
 * @version V1.0
 * @Copyright: Copyright(c) 2019 jaesonchen.com Inc. All rights reserved. 
 */
@Api(value = "视图管理", tags = "视图管理模块")
@RestController
public class ViewsController {
    
    @ApiOperation(value = "公共", notes = "公共视图")
    @JsonView(Views.Public.class)
    @GetMapping("/views/public")
    public Response viewPublic() {
        return new Response(1001, "chenzq", "1234");
    }
    
    @ApiOperation(value = "私有", notes = "私有视图")
    @JsonView(Views.Private.class)
    @GetMapping("/views/private")
    public Response viewPrivate() {
        return new Response(1001, "chenzq", "1234");
    }
}
