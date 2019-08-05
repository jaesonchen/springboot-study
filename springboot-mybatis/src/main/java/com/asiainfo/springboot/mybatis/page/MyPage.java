package com.asiainfo.springboot.mybatis.page;

import java.util.ArrayList;
import java.util.List;

/**   
 * @Description: TODO
 * 
 * @author chenzq  
 * @date 2019年7月18日 下午4:39:27
 * @version V1.0
 * @Copyright: Copyright(c) 2019 jaesonchen.com Inc. All rights reserved. 
 */
public class MyPage<E> extends ArrayList<E> {

    private static final long serialVersionUID = 1L;
    
    private List<E> list;
    private int total;
    private int pageNum;
    private int pageSize;
    private int pages;
    
    public MyPage(List<E> list, int total, int defautPageSize) {
        super(list);
        this.list = list;
        this.total = total;
        MyPageParameter param = MyPageHelper.getMyPageParameter();
        this.pageSize = param.getPageSize() > 0 ? param.getPageSize() : defautPageSize;
        this.pages = total % pageSize == 0 ? total / pageSize : (total / pageSize + 1);
        this.pageNum = total == 0 ? 0 : param.getPageNum() <= 0 ? 1 : (param.getPageNum() > pages ? pages : param.getPageNum());
    }
    public List<E> getList() {
        return list;
    }
    public void setList(List<E> list) {
        this.list = list;
    }
    public int getTotal() {
        return total;
    }
    public void setTotal(int total) {
        this.total = total;
    }
    public int getPageNum() {
        return pageNum;
    }
    public void setPageNum(int pageNum) {
        this.pageNum = pageNum;
    }
    public int getPageSize() {
        return pageSize;
    }
    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }
    public int getPages() {
        return pages;
    }
    public void setPages(int pages) {
        this.pages = pages;
    }
    @Override
    public String toString() {
        return "MyPage [list=" + list + ", total=" + total + ", pageNum=" + pageNum + ", pageSize=" + pageSize
                + ", pages=" + pages + "]";
    }
}
