package com.asiainfo.springboot.profile;

import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

/**   
 * @Description: @Profile spring.profiles.active注入bean
 * 
 * @author chenzq  
 * @date 2019年7月13日 下午2:30:24
 * @version V1.0
 * @Copyright: Copyright(c) 2019 jaesonchen.com Inc. All rights reserved. 
 */
@Component
@Profile("dev")
public class ProfileBean {

    private String profile = "dev";

    public String getProfile() {
        return profile;
    }
    public void setProfile(String profile) {
        this.profile = profile;
    }
    @Override
    public String toString() {
        return "ProfileBean [profile=" + profile + "]";
    }
}
