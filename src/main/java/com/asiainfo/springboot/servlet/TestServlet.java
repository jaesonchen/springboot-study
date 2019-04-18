package com.asiainfo.springboot.servlet;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * @WebServlet 配置servlet
 * 
 * @author       zq
 * @date         2017年10月22日  下午5:32:29
 * Copyright: 	  北京亚信智慧数据科技有限公司
 */
@WebServlet(urlPatterns = "/test")
public class TestServlet extends HttpServlet {
	
    /** serialVersionUID */
	private static final long serialVersionUID = 1L;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        response.setContentType("application/json");
        response.setCharacterEncoding("utf-8");
        PrintWriter writer = response.getWriter();
        writer.write("执行doGet方法成功!");
        writer.close();
    }
}
