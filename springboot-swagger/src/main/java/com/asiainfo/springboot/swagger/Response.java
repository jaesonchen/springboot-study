package com.asiainfo.springboot.swagger;

import java.io.Serializable;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**   
 * @Description: TODO
 * 
 * @author chenzq  
 * @date 2019年6月25日 下午3:14:24
 * @version V1.0
 * @Copyright: Copyright(c) 2019 jaesonchen.com Inc. All rights reserved. 
 */
@ApiModel(description = "响应模型")
public class Response implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "结果编码")
    private String code;
    @ApiModelProperty(value = "结果描述")
    private String msg;
    @ApiModelProperty(value = "返回数据")
    private Object data;
    
    public Response(String code, String msg) {
        this(code, msg, null);
    }
    public Response(String code, String msg, Object data) {
        this.code = code;
        this.msg = msg;
        this.data = data;
    }

    public String getCode() {
        return code;
    }
    public void setCode(String code) {
        this.code = code;
    }
    public String getMsg() {
        return msg;
    }
    public void setMsg(String msg) {
        this.msg = msg;
    }
    public Object getData() {
        return data;
    }
    public void setData(Object data) {
        this.data = data;
    }
    public static Response success(String msg) {
        return new Response("200", msg);
    }
    public static Response failure(String code, String msg) {
        return new Response(code, msg);
    }
    public static Response success(Object data) {
        return new Response("200", "success", data);
    }
    
    @Override
    public String toString() {
        return "Response [code=" + code + ", msg=" + msg + ", data=" + data + "]";
    }
}
