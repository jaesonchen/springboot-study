package com.asiainfo.springboot.mybatis.model;

import java.io.Serializable;

import com.asiainfo.springboot.mybatis.enums.RoleType;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**   
 * @Description: TODO
 * 
 * @author chenzq  
 * @date 2019年7月16日 下午5:11:11
 * @version V1.0
 * @Copyright: Copyright(c) 2019 jaesonchen.com Inc. All rights reserved. 
 */
@JsonIgnoreProperties(value = {"handler"})
public class Role implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long id;
    private String roleName;
    private RoleType roleType;
    
    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public String getRoleName() {
        return roleName;
    }
    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }
    public RoleType getRoleType() {
        return roleType;
    }
    public void setRoleType(RoleType roleType) {
        this.roleType = roleType;
    }
    @Override
    public String toString() {
        return "Role [id=" + id + ", roleName=" + roleName + ", roleType=" + roleType + "]";
    }
}
