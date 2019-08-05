package com.asiainfo.springboot.myinitializer;

import java.util.EnumSet;

import javax.servlet.DispatcherType;
import javax.servlet.FilterRegistration;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;

/**   
 * @Description: TODO
 * 
 * @author chenzq  
 * @date 2019年7月8日 下午7:21:04
 * @version V1.0
 * @Copyright: Copyright(c) 2019 jaesonchen.com Inc. All rights reserved. 
 */
public class FilterInitializer implements MyWebAppInitializer {
    
    @Override
    public void onStartup(ServletContext servletContext) throws ServletException {
        
        // add filter
        FilterRegistration.Dynamic filter = servletContext.addFilter("myFilter", MyFilter.class);
        EnumSet<DispatcherType> dispatcherTypes = EnumSet.allOf(DispatcherType.class);
        dispatcherTypes.add(DispatcherType.REQUEST);
        dispatcherTypes.add(DispatcherType.FORWARD);
        filter.addMappingForUrlPatterns(dispatcherTypes, true, "/*");
        
        // add servlet
        //ServletRegistration.Dynamic servlet = servletContext.addServlet("dispatcherServlet", DispatcherServlet.class);
        //servlet.addMapping("/*");
        
        // add listener
        // servletContext.addListener(MyListener.class);
    }
}
