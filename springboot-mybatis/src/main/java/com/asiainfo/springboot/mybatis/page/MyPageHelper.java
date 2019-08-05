package com.asiainfo.springboot.mybatis.page;

/**   
 * @Description: TODO
 * 
 * @author chenzq  
 * @date 2019年5月11日 下午9:26:11
 * @version V1.0
 * @Copyright: Copyright(c) 2019 jaesonchen.com Inc. All rights reserved. 
 */
public class MyPageHelper {

    public static final ThreadLocal<MyPageParameter> param = new ThreadLocal<>();
    
    public static void startPage(int pageNum) {
        param.set(new MyPageParameter(pageNum));
    }
    public static void startPage(int pageNum, int pageSize) {
        param.set(new MyPageParameter(pageNum, pageSize));
    }
    public static void clearPage() {
        param.set(null);
    }
    public static boolean isInPage() {
        return param.get() != null;
    }
    public static MyPageParameter getMyPageParameter() {
        return param.get();
    }
}
