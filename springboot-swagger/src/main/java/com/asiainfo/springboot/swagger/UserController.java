package com.asiainfo.springboot.swagger;

import java.util.Arrays;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import springfox.documentation.annotations.ApiIgnore;

/**   
 * @Description: TODO
 * 
 * @author chenzq  
 * @date 2019年7月7日 下午6:29:46
 * @version V1.0
 * @Copyright: Copyright(c) 2019 jaesonchen.com Inc. All rights reserved. 
 */
@Api(value = "用户管理", tags = "用户管理模块")
@RequestMapping("/users")
@RestController
public class UserController {

    final Logger logger = LoggerFactory.getLogger(getClass());
    
    final Map<Long, User> users = new ConcurrentHashMap<>();
    final String TOKEN = "123456";
    final AtomicLong atomic = new AtomicLong(0);
    
    @ApiIgnore
    @GetMapping("/hello")
    public ResponseEntity<String> hello() {
        logger.info("hello world!");
        return new ResponseEntity<>("hello world!", HttpStatus.OK);
    }
    
    @ApiOperation(value = "用户列表", notes = "返回用户列表")
    @GetMapping
    public ResponseEntity<Map<Long, User>> list() {
        logger.info("list: {}", users);
        return new ResponseEntity<>(users, HttpStatus.OK);
    }
    
    // allowMultiple = true 传递参数数组
    @ApiOperation(value = "角色数组", notes = "返回角色数组")
    @ApiImplicitParam(name = "role", value = "角色", required = true, allowMultiple = true, dataType = "string", paramType = "query")
    @GetMapping("/array")
    public ResponseEntity<String> array(@RequestParam("role") String[] roleList) {
        logger.info("array args: {}", Arrays.toString(roleList));
        return new ResponseEntity<>(Arrays.toString(roleList), HttpStatus.OK);
    }
    
    @ApiOperation(value = "读取", notes = "返回用户信息")
    @ApiImplicitParam(name = "id", value = "用户id", required = true, dataType = "long", example = "1", paramType = "path")
    @GetMapping("/{id}")
    public ResponseEntity<Response> getById(@PathVariable long id) {
        logger.info("get user, id: {}", id);
        if (users.containsKey(id)) {
            return new ResponseEntity<>(Response.success(users.get(id)), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(Response.failure("404", "user(id=" + id + ") not found!"), HttpStatus.OK);
        }
    }
    
    // example 显示样例值，在非string类型时，如果不写example，后台会报""类型转换错误
    @ApiOperation(value = "删除", notes = "删除用户")
    @ApiImplicitParams({
        @ApiImplicitParam(name = "token", value = "token字符串", required = true, dataType = "string", example = "123456", paramType = "header"), 
        @ApiImplicitParam(name = "id", value = "用户id", required = true, dataType = "long", example = "1", paramType = "path")})
    @DeleteMapping("/{id}")
    public ResponseEntity<Response> deleteById(@PathVariable long id, @RequestHeader String token) {
        logger.info("delete user, id: {}", id);
        if (!TOKEN.equals(token)) {
            return new ResponseEntity<>(Response.failure("401", "token invalid!"), HttpStatus.UNAUTHORIZED);
        } else {
            if (users.containsKey(id)) {
                User user = users.remove(id);
                if (null != user) {
                    return new ResponseEntity<>(Response.success("remove: " + user), HttpStatus.OK);
                }
            }
            return new ResponseEntity<>(Response.failure("404", "user(id=" + id + ") not found!"), HttpStatus.OK);
        }
    }
    
    @ApiOperation(value = "保存", notes = "新增用户")
    @ApiImplicitParams({
        @ApiImplicitParam(name = "token", value = "token字符串", required = true, dataType = "string", example = "123456", paramType = "header"), 
        @ApiImplicitParam(name = "user", value = "json用户信息", required = true, dataType = "User", paramType = "body")})
    @PostMapping
    public ResponseEntity<Response> save(@RequestBody User user, @RequestHeader String token) {
        logger.info("save user, user: {}", user);
        if (!TOKEN.equals(token)) {
            return new ResponseEntity<>(Response.failure("401", "token invalid!"), HttpStatus.UNAUTHORIZED);
        } else {
            user.setId(atomic.incrementAndGet());
            users.put(user.getId(), user);
            return new ResponseEntity<>(Response.success(user), HttpStatus.OK);
        }
    }
    
    @ApiOperation(value = "保存2", notes = "新增用户")
    @ApiImplicitParams({
        @ApiImplicitParam(name = "token", value = "token字符串", required = true, dataType = "string", example = "123456", paramType = "header"), 
        @ApiImplicitParam(name = "id", value = "用户id", required = false, dataType = "string", paramType = "query"), 
        @ApiImplicitParam(name = "username", value = "用户名", required = true, dataType = "string", paramType = "query"), 
        @ApiImplicitParam(name = "password", value = "密码", required = true, dataType = "string", paramType = "query")})
    @PostMapping("/save")
    public ResponseEntity<Response> save2(User user, @RequestHeader String token) {
        logger.info("save user, user: {}", user);
        if (!TOKEN.equals(token)) {
            return new ResponseEntity<>(Response.failure("401", "token invalid!"), HttpStatus.UNAUTHORIZED);
        } else {
            user.setId(atomic.incrementAndGet());
            users.put(user.getId(), user);
            return new ResponseEntity<>(Response.success(user), HttpStatus.OK);
        }
    }
    
    @ApiOperation(value = "更新", notes = "更新用户")
    @ApiImplicitParams({
        @ApiImplicitParam(name = "token", value = "token字符串", required = true, dataType = "string", example = "123456", paramType = "header"), 
        @ApiImplicitParam(name = "id", value = "用户id", required = true, dataType = "long", example = "1", paramType = "path"), 
        @ApiImplicitParam(name = "password", value = "用户密码", required = true, dataType = "string", paramType = "query")})
    @PutMapping("/{id}")
    public ResponseEntity<Response> update(@PathVariable long id, @RequestParam String password, @RequestHeader String token) {
        logger.info("update user, password: {}", password);
        if (!TOKEN.equals(token)) {
            return new ResponseEntity<>(Response.failure("401", "token invalid!"), HttpStatus.UNAUTHORIZED);
        } else {
            if (users.containsKey(id)) {
                User user = users.get(id);
                user.setPassword(password);
                return new ResponseEntity<>(Response.success("update password: " + password), HttpStatus.OK);
            } else {
                return new ResponseEntity<>(Response.success("user(id=" + id + ") not found!"), HttpStatus.OK);
            }
        }
    }
}
