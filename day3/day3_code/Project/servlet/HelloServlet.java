package com.briup.java.Project.servlet;

import com.briup.java.Project.dao.Request;
import com.briup.java.Project.dao.Response;

import java.io.PrintStream;

/**
 * \* Created with IntelliJ IDEA.
 * \* User: yfl
 * \* Date: 2019/8/29
 * \* To change this template use File | Settings | File Templates.
 * \* Description:
 * \
 */
public class HelloServlet implements Servlet{
    @Override
    public void service(Request req, Response res) {
        PrintStream ps = res.getPs();
        //发送响应到浏览器
        //1.发送响应行
        String resLine = res.getResLine();
        System.out.println("响应行为:" + resLine);
        ps.println(resLine);
        //2.发送响应头
        String resHeader = res.getResHeader();
        System.out.println("响应头为:" + resHeader);
        ps.println(resHeader);
        //3.发送空行
        ps.println();
        //4.发送响应体
        ps.println("<html>");
        ps.println("<head>");
        ps.println("</head>");
        ps.println("<body>");
        ps.println("<h1>");
        ps.println("hello world");
        ps.println("</h1>");
        ps.println("</body>");
        ps.println("</html>");
    }
}