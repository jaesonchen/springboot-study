package com.asiainfo.springboot.shiro.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.asiainfo.springboot.shiro.model.Permission;

/**   
 * @Description: TODO
 * 
 * @author chenzq  
 * @date 2019年7月20日 下午10:35:25
 * @version V1.0
 * @Copyright: Copyright(c) 2019 jaesonchen.com Inc. All rights reserved. 
 */
@Repository
public interface PermissionRepository extends JpaRepository<Permission, Long> {

}
