package com.asiainfo.springboot.web.controller;

import java.io.Writer;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.asiainfo.springboot.web.model.User;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.Example;
import io.swagger.annotations.ExampleProperty;

/**   
 * @Description: contoller方法测试
 * 
 * @author chenzq  
 * @date 2019年7月10日 下午7:30:03
 * @version V1.0
 * @Copyright: Copyright(c) 2019 jaesonchen.com Inc. All rights reserved. 
 */
@Api(value = "Controller测试", tags = "Controller测试")
@Controller
public class HelloController {
    
    final Logger logger = LoggerFactory.getLogger(getClass());

    // Model / ModelMap / Map传递值到视图
    @ApiOperation(value = "model", notes = "Model传递值到视图")
    @RequestMapping(value = "/hello", method = RequestMethod.GET)
    public String hello(Model model) {

        logger.info("hello() is executed!");
        model.addAttribute("title", "World");
        model.addAttribute("desc", "Spring mvc Hello world example!");
        return "hello";
    }
    
    // 路径变量
    @ApiOperation(value = "变量", notes = "路径变量传递")
    @ApiImplicitParam(name = "name", value = "名称", required = true, dataType = "string", paramType = "path")
    @RequestMapping(value = "/hello/{name:.+}", method = RequestMethod.GET)
    public ModelAndView hello(@PathVariable("name") String name) {

        logger.info("hello() is executed - $name {}", name);
        ModelAndView model = new ModelAndView("hello");
        model.addObject("title", name);
        model.addObject("desc", "Spring mvc Hello world example!");
        return model;
    }
    
    // required=false时，如果request没有传入相应的参数，则会将null赋予age，此时会报错并要求使用包装类型Integer
    @ApiOperation(value = "默认值", notes = "请求参数设置默认值")
    @ApiImplicitParam(name = "age", value = "年龄", required = false, dataType = "string", paramType = "query")
    @RequestMapping(value = "/default", method = RequestMethod.GET)
    @ResponseBody
    public Map<String, Object> defaultValue(@RequestParam(value = "age", required = true, defaultValue = "20") int age) {
        
        logger.info("defaultValue() is executed - $age {}", age);
        Map<String, Object> map = new HashMap<>();
        map.put("age", age);
        return map;
    }
    
    // 传递参数数组
    @ApiOperation(value = "数组", notes = "接收参数数组")
    @ApiImplicitParam(name = "role", value = "角色", required = true, allowMultiple = true, dataType = "string", paramType = "query")
    @RequestMapping(value = "/array", method = RequestMethod.GET)
    @ResponseBody
    public Map<String, Object> array(@RequestParam("role") String[] roleList) {
        logger.info("array() is executed - $role {}", Arrays.toString(roleList));
        Map<String, Object> map = new HashMap<>();
        map.put("role", roleList);
        return map;
    }
    
    // Map接收参数
    @ApiOperation(value = "map", notes = "map接收多个参数")
    @ApiImplicitParam(name = "map", value = "用户信息map", required = true, dataType = "Map", paramType = "body", 
        examples = @Example({@ExampleProperty(value = "{'id': 0, 'name':'string'}", mediaType = "application/json")}))
    @RequestMapping(value = "/map", method = RequestMethod.POST)
    @ResponseBody
    public Map<String, Object> map(@RequestBody Map<String, Object> map) {
        logger.info("map() is executed - $map {}", Arrays.toString(map.entrySet().toArray()));
        return map;
    }

    // 直接写response流
    @ApiOperation(value = "写流", notes = "写response流")
    @RequestMapping(value = "/void", method = RequestMethod.GET)
    public void writer(Writer writer) throws Exception {
        logger.info("writer() is executed!");
        writer.write("Hello World!!");
        writer.flush();
    }
    
    // 指定request/response的content-type
    @ApiOperation(value = "consume", notes = "设置content-type")
    @RequestMapping(value = "/consume", method = RequestMethod.POST, consumes="application/json", produces="application/json")
    @ResponseBody
    public User consume(@RequestBody User user) {
        logger.info("consume() is executed!");
        user.setId(1234);
        return user;
    }
    
    // @RequestHeader 获取Request header值
    // @CookieValue 获取cookie值
    @ApiOperation(value = "header", notes = "读取header内容")
    @ApiImplicitParams({
        @ApiImplicitParam(name = "Accept-Encoding", value = "编码", required = true, dataType = "string", example = "gzip", paramType = "header"), 
        @ApiImplicitParam(name = "Accept", value = "内容协商类型", required = true, dataType = "string", example = "application/json", paramType = "header")})
    @RequestMapping(value = "/header", method = RequestMethod.POST)
    @ResponseBody
    public Map<String, Object> header(@RequestHeader("Accept-Encoding") String encoding, 
            @RequestHeader("Accept") String[] accept,
            @ApiParam(hidden = true) @CookieValue(value = "JSESSIONID", defaultValue = "") String sessionId) {
        
        logger.info("\nAccept-Encoding={}\nAccept={}\nJSESSIONID={}", encoding, Arrays.toString(accept), sessionId);
        Map<String, Object> map = new HashMap<>();
        map.put("encoding", encoding);
        map.put("accept", accept);
        map.put("sessionId", sessionId);
        return map;
    }
    
    // forward: 转发请求到 /hello上
    @ApiOperation(value = "forward", notes = "转发请求")
    @RequestMapping(value = "/forward", method = RequestMethod.GET)
    public String forward() {
        return "forward:/hello";
    }
    
    // redirect: 重定向到/hello，client重启发起请求
    @ApiOperation(value = "redirect", notes = "重定向请求")
    @RequestMapping(value = "/redirect", method = RequestMethod.GET)
    public ModelAndView redirect() {
        return new ModelAndView("redirect:/hello");
    }
}
