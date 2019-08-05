package com.asiainfo.springboot.myinitializer;

import java.util.ServiceLoader;

import javax.servlet.ServletContainerInitializer;

/**   
 * @Description: TODO
 * 
 * @author chenzq  
 * @date 2019年7月8日 下午9:47:21
 * @version V1.0
 * @Copyright: Copyright(c) 2019 jaesonchen.com Inc. All rights reserved. 
 */
public class Application {

    public static void main(String[] args) {
        
        ServiceLoader<ServletContainerInitializer> loaders = ServiceLoader.load(ServletContainerInitializer.class);
        for (ServletContainerInitializer service : loaders) {
            System.out.println(service.getClass());
        }
    }
}
