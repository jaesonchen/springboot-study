package com.asiainfo.springboot.shiro.realm;

import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.LockedAccountException;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.util.ByteSource;
import org.springframework.beans.factory.annotation.Autowired;

import com.asiainfo.springboot.shiro.model.Permission;
import com.asiainfo.springboot.shiro.model.Role;
import com.asiainfo.springboot.shiro.model.User;
import com.asiainfo.springboot.shiro.service.UserService;

/**   
 * @Description: TODO
 * 
 * @author chenzq  
 * @date 2019年7月20日 下午6:00:32
 * @version V1.0
 * @Copyright: Copyright(c) 2019 jaesonchen.com Inc. All rights reserved. 
 */
public class UserRealm extends AuthorizingRealm {

    public static final int LOCKED = 1;
    
    @Autowired
    private UserService userService;
    
    // 每次请求时，如果已认证则读取主体的权限(角色/权限)，缓存配置主要用在这里，不需要每次都去读数据库查权限，因为权限通常很少改变
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
        
        System.out.println("doGetAuthorizationInfo");
        SimpleAuthorizationInfo authorizationInfo = new SimpleAuthorizationInfo();
        String username = (String) principals.getPrimaryPrincipal();
        User user = userService.findUserByName(username);
        for (Role role : user.getRoles()) {
            authorizationInfo.addRole(role.getRoleName());
            for (Permission permission : role.getPermissions()) {
                authorizationInfo.addStringPermission(permission.getPermName());
            }
        }
        return authorizationInfo;
    }

    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {
        
        System.out.println("doGetAuthenticationInfo");
        String username = (String) token.getPrincipal();
        User user = userService.findUserByName(username);
        if (user == null) {
            throw new UnknownAccountException();
        }
        if (LOCKED == user.getStatus()) {
            throw new LockedAccountException();
        }
        
        return new SimpleAuthenticationInfo(
                    user.getUsername(), 
                    user.getPassword(), 
                    ByteSource.Util.bytes(user.getCredentialsSalt()), 
                    getName());
    }
}