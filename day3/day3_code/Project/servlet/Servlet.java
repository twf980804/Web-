package com.briup.java.Project.servlet;


import com.briup.java.Project.dao.Request;
import com.briup.java.Project.dao.Response;

public interface Servlet {
    //类对外公共方法
    //该方法内部,有可能会用到请求的信息,
    // 也有可能会用到响应的信息
    public void service(Request req, Response res);
}