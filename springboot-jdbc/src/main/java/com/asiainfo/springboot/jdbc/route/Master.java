package com.asiainfo.springboot.jdbc.route;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**   
 * @Description: 主数据源注解，用于某些特定情况下，写完需要立即读时，需要把对应的读方法标记为@Master
 * 
 * @author chenzq  
 * @date 2019年7月12日 下午1:42:35
 * @version V1.0
 * @Copyright: Copyright(c) 2019 jaesonchen.com Inc. All rights reserved. 
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface Master {

}
