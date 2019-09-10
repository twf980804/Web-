package com.briup.java.Project.dao;

import java.util.Map;

public interface Request {
    //获取请求方法
    public String getMethod();
    //获取url
    public String getUrl();
    //获取协议版本
    public String getProtocol();
    //获取请求头
    public Map<String,String> getReqHead();
    //获取请求体
    public Map<String,String> getReqBody();
}
