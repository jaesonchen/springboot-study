package com.asiainfo.springboot.myinitializer;

import java.lang.reflect.Modifier;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import javax.servlet.ServletContainerInitializer;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.HandlesTypes;

/**
 * @Description:  servlet3.0 使用spi提供了自动加载的接口，不需要在web.xml中配置。
 * - 在web容器启动时为第三方组件提供了做初始化的工作的机会，例如注册servlet或者filter等，servlet规范中通过ServletContainerInitializer实现此功能。
 * - 每个框架要使用ServletContainerInitializer就必须在对应的jar包的META-INF/services目录下创建一个名为javax.servlet.ServletContainerInitializer的文件，
 * - 文件内容指定具体的ServletContainerInitializer实现类。那么，当web容器启动时就会运行这个初始化器做一些组件内的初始化工作。
 * - 一起使用的还有HandlesTypes注解，通过HandlesTypes可以将感兴趣的一些类注入到ServletContainerInitializer的onStartup方法作为参数传入。
 * 
 * @author chenzq  
 * @date 2019年7月8日 下午7:17:04
 * @version V1.0
 * @Copyright: Copyright(c) 2019 jaesonchen.com Inc. All rights reserved.
 */
@HandlesTypes(MyWebAppInitializer.class)
public class MyServletContainerInitializer implements ServletContainerInitializer {

    @Override
    public void onStartup(Set<Class<?>> webAppInitializerClasses, ServletContext servletContext) throws ServletException {
        
        servletContext.log("MyServletContainerInitializer startup!");
        List<MyWebAppInitializer> initializers = new LinkedList<MyWebAppInitializer>();
        if (webAppInitializerClasses != null) {
            for (Class<?> waiClass : webAppInitializerClasses) {
                // Be defensive: Some servlet containers provide us with invalid classes,
                // no matter what @HandlesTypes says...
                if (!waiClass.isInterface() && !Modifier.isAbstract(waiClass.getModifiers()) 
                        && MyWebAppInitializer.class.isAssignableFrom(waiClass)) {
                    try {
                        initializers.add((MyWebAppInitializer) waiClass.newInstance());
                    } catch (Throwable ex) {
                        throw new ServletException("Failed to instantiate MyWebAppInitializer class!", ex);
                    }
                }
            }
        }

        if (initializers.isEmpty()) {
            servletContext.log("No MyWebAppInitializer types detected on classpath");
            return;
        }

        servletContext.log("MyWebAppInitializer detected on classpath: " + initializers);
        for (MyWebAppInitializer initializer : initializers) {
            initializer.onStartup(servletContext);
        }
    }
}
