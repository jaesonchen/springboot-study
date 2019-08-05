package com.asiainfo.springboot.dubbo.service;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.apache.dubbo.config.annotation.Service;

import com.asiainfo.springboot.dubbo.model.User;

/**   
 * @Description: TODO
 * 
 * @author chenzq  
 * @date 2019年7月14日 上午10:07:15
 * @version V1.0
 * @Copyright: Copyright(c) 2019 jaesonchen.com Inc. All rights reserved. 
 */
@Service(filter = "legacy-block", loadbalance = "roundrobin", cluster="failfast", timeout = 6000, retries = 0, version = "1.0")
// parameters = {"getById.timeout", "3000", "findAll.timeout", "5000"}
public class UserServiceImpl implements UserService {

    @Override
    public User getById(Long id) {
        return new User(id, "chenzq", "1234");
    }

    @Override
    public List<User> findAll() {
        return Arrays.asList(new User(1001L, "chenzq", "1234"), new User(1002L, "jaeson", "1234"), new User(1003L, "jaesonchen", "1234"));
    }

    @Override
    public boolean check() {
        try {
            TimeUnit.SECONDS.sleep(3);
        } catch (InterruptedException e) {
            // ignore
        }
        return true;
    }
}
