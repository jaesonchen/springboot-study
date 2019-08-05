package com.asiainfo.springboot.security.image;

import org.springframework.security.core.AuthenticationException;

/**   
 * @Description: TODO
 * 
 * @author chenzq  
 * @date 2019年7月29日 下午5:00:41
 * @version V1.0
 * @Copyright: Copyright(c) 2019 jaesonchen.com Inc. All rights reserved. 
 */
public class ValidateException extends AuthenticationException {

    private static final long serialVersionUID = 1L;

    public ValidateException(String msg) {
        super(msg);
    }
}
