package com.asiainfo.springboot.shiro.util;

import org.apache.shiro.crypto.RandomNumberGenerator;
import org.apache.shiro.crypto.SecureRandomNumberGenerator;
import org.apache.shiro.crypto.hash.SimpleHash;
import org.apache.shiro.util.ByteSource;

import com.asiainfo.springboot.shiro.model.User;

/**   
 * @Description: TODO
 * 
 * @author chenzq  
 * @date 2019年7月20日 下午5:35:45
 * @version V1.0
 * @Copyright: Copyright(c) 2019 jaesonchen.com Inc. All rights reserved. 
 */
public class PasswordHelper {
    
    public static final String ALGORITHM_NAME = "md5";  // 基础散列算法
    public static final int HASH_ITERATIONS = 2;        // 自定义散列次数
    
    private static final RandomNumberGenerator randomNumberGenerator = new SecureRandomNumberGenerator();
    
    // 随机字符串作为salt因子
    public static void encryptPassword(User user) {
        user.setSalt(randomNumberGenerator.nextBytes().toHex());
        String newPassword = new SimpleHash(ALGORITHM_NAME, user.getPassword(), 
                ByteSource.Util.bytes(user.getCredentialsSalt()), HASH_ITERATIONS).toHex();
        user.setPassword(newPassword);
    }
}