package com.asiainfo.springboot.shiro.model;

import java.io.Serializable;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;

/**   
 * @Description: TODO
 * 
 * @author chenzq  
 * @date 2019年7月20日 下午5:41:56
 * @version V1.0
 * @Copyright: Copyright(c) 2019 jaesonchen.com Inc. All rights reserved. 
 */
@Entity
@Table(name = "role")
public class Role implements Serializable {

    private static final long serialVersionUID = 1L;
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false)
    private String roleName;
    @Column
    private String description;
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "role_permission", 
        joinColumns = { @JoinColumn(name = "role_id") }, 
        inverseJoinColumns = { @JoinColumn(name = "permission_id") })
    private List<Permission> permissions;
    
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
    public List<Permission> getPermissions() {
        return permissions;
    }
    public void setPermissions(List<Permission> permissions) {
        this.permissions = permissions;
    }
    public String getDescription() {
        return description;
    }
    public void setDescription(String description) {
        this.description = description;
    }
    @Override
    public String toString() {
        return "Role [id=" + id + ", roleName=" + roleName + ", description=" + description + ", permissions="
                + permissions + "]";
    }
}
