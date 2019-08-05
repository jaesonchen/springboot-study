package com.asiainfo.springboot.mybatis.page;

/**   
 * @Description: TODO
 * 
 * @author chenzq  
 * @date 2019年5月11日 下午9:28:23
 * @version V1.0
 * @Copyright: Copyright(c) 2019 jaesonchen.com Inc. All rights reserved. 
 */
public class MyPageParameter {

    private int pageSize;
    private int pageNum;

    public MyPageParameter(int pageNum) {
        this.pageNum = pageNum;
    }
    public MyPageParameter(int pageNum, int pageSize) {
        this.pageNum = pageNum;
        this.pageSize = pageSize;
    }
    
    public int getPageSize() {
        return pageSize;
    }
    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }
    public int getPageNum() {
        return pageNum;
    }
    public void setPageNum(int pageNum) {
        this.pageNum = pageNum;
    }
    @Override
    public String toString() {
        return "MyPageParameter [pageSize=" + pageSize + ", pageNum=" + pageNum + "]";
    }
}
