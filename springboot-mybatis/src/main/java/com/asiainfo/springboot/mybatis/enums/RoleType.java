package com.asiainfo.springboot.mybatis.enums;

/**   
 * @Description: TODO
 * 
 * @author chenzq  
 * @date 2019年7月16日 下午5:20:50
 * @version V1.0
 * @Copyright: Copyright(c) 2019 jaesonchen.com Inc. All rights reserved. 
 */
public enum RoleType implements EnumValue<String> {

    FUNC("101", "功能权限"), DATA("102", "数据权限");
    
    private String code;
    private String desc;
    private RoleType(String code, String desc) {
        this.code = code;
        this.desc = desc;
    }
    public String getCode() {
        return code;
    }
    public String getDesc() {
        return desc;
    }
    @Override
    public String getValue() {
        return code;
    }
}
