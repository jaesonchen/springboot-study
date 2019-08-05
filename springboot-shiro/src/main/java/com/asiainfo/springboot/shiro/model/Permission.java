package com.asiainfo.springboot.shiro.model;

import java.io.Serializable;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;

import com.asiainfo.springboot.shiro.util.PermissionType;

/**   
 * @Description: TODO
 * 
 * @author chenzq  
 * @date 2019年7月20日 下午5:42:20
 * @version V1.0
 * @Copyright: Copyright(c) 2019 jaesonchen.com Inc. All rights reserved. 
 */
@Entity
@Table(name = "permission")
public class Permission implements Serializable {

    private static final long serialVersionUID = 1L;
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false)
    private String permName;
    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private PermissionType permType;
    @Column
    private String description;
    @ManyToMany
    @JoinTable(name = "role_permission", 
        joinColumns = { @JoinColumn(name = "permission_id") }, 
        inverseJoinColumns = { @JoinColumn(name = "role_id") })
    private List<Role> roles;
    
    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public String getPermName() {
        return permName;
    }
    public void setPermName(String permName) {
        this.permName = permName;
    }
    public PermissionType getPermType() {
        return permType;
    }
    public void setPermType(PermissionType permType) {
        this.permType = permType;
    }
    public String getDescription() {
        return description;
    }
    public void setDescription(String description) {
        this.description = description;
    }
    public List<Role> getRoles() {
        return roles;
    }
    public void setRoles(List<Role> roles) {
        this.roles = roles;
    }
    @Override
    public String toString() {
        return "Permission [id=" + id + ", permName=" + permName + ", permType=" + permType + ", description="
                + description + ", roles=" + roles + "]";
    }
}
