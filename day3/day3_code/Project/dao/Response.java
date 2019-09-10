package com.briup.java.Project.dao;

import java.io.IOException;
import java.io.PrintStream;

public interface Response {
    //组合得到响应行
    public String getResLine();
    //组合得到响应头
    public String getResHeader();
    //提取文件内容(响应体),发送到浏览器
    public void sendResBody() throws IOException;

    //整合所有的信息,然后发送到浏览器
    public void sendResresource() throws IOException, ClassNotFoundException, IllegalAccessException, InstantiationException;

    //获取通信流
    public PrintStream getPs();
}
