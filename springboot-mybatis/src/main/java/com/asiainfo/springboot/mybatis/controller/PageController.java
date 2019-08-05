package com.asiainfo.springboot.mybatis.controller;

import java.util.List;

import org.apache.ibatis.session.RowBounds;
import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.asiainfo.springboot.mybatis.dao.UserDao;
import com.asiainfo.springboot.mybatis.model.User;
import com.asiainfo.springboot.mybatis.page.MyPage;
import com.asiainfo.springboot.mybatis.page.MyPageHelper;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

/**   
 * @Description: TODO
 * 
 * @author chenzq  
 * @date 2019年7月18日 下午5:00:18
 * @version V1.0
 * @Copyright: Copyright(c) 2019 jaesonchen.com Inc. All rights reserved. 
 */
@Api(value = "分页", tags = "分页模块")
@RestController
@RequestMapping("/page")
public class PageController {

    @Autowired
    private UserDao userDao;
    
    @Autowired
    private SqlSession session;
    
/*    @ApiOperation(value = "分页", notes = "pagehelper分页")
    @GetMapping
    public PageInfo<User> page(@RequestParam int pageNum) {
        PageHelper.startPage(pageNum, 10);
        List<User> list = this.userDao.findAll();
        //用PageInfo对结果进行包装
        return new PageInfo<>(list);
    }*/
    
    @ApiOperation(value = "简单", notes = "简单分页")
    @GetMapping
    public List<User> list() {
        MyPageHelper.startPage(1, 5);
        return (MyPage<User>) this.userDao.findAll();
    }
    
    @ApiOperation(value = "嵌套", notes = "嵌套查询分页")
    @GetMapping("/bound")
    public List<User> rowBounds() {
        return session.selectList("com.asiainfo.springboot.mybatis.dao.UserDao.findAll", null, new RowBounds(0, 5));
    }
}
