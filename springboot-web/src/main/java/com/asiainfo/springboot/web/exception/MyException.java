package com.asiainfo.springboot.web.exception;

/**   
 * @Description: TODO
 * 
 * @author chenzq  
 * @date 2019年7月10日 下午6:39:16
 * @version V1.0
 * @Copyright: Copyright(c) 2019 jaesonchen.com Inc. All rights reserved. 
 */
public class MyException extends Exception {

    private static final long serialVersionUID = 1L;

    public MyException(String message) {
        super(message);
    }
}
