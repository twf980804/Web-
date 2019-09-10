package com.briup.java.Project.dao;

import com.briup.java.Project.servlet.Servlet;
import com.briup.java.Project.util.MimeFinder;
import com.briup.java.Project.util.ProperFinder;
import com.briup.java.Project.util.ServletFinder;
import com.briup.java.Project.util.StatusFinder;

import java.io.*;
import java.net.Socket;

/**
 * \* Created with IntelliJ IDEA.
 * \* User: yfl
 * \* Date: 2019/8/29
 * \* To change this template use File | Settings | File Templates.
 * \* Description:
 * \
 */
public class ResponseImp implements Response{
    //属性
    //和浏览器通信的socket
    private Socket socket;
    //浏览器申请的资源名
    private String url;
    //发送数据给浏览器所使用的流对象
    private PrintStream ps;

    private String resourcePath;
    private String errorFile;
    private  String welFile;

    private String dynClassName;//动态资源名

    private Request req;

    //构造器
    public ResponseImp(Socket socket,Request req)  {
        this.socket = socket;
        this.req=req;
        //this.url = url;
        url = req.getUrl();

        resourcePath = ProperFinder.getValue("resourcePath");
        errorFile = ProperFinder.getValue("errorFile");
        String welFile = ProperFinder.getValue("welFile");

        try {
            ps = new PrintStream(socket.getOutputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }

        File file = new File(resourcePath,url);
    }

    //组合生成响应行
    @Override
    public String getResLine() {
        if (dynClassName!=null){
            return "HTTP/1.1 200 OK";
        }
        //响应协议
        String resLine = "HTTP/1.1";
        //响应状态码
        int StatusCode = getStatusCode(url);
        //响应状态码的描述信息
        String statusMsg = StatusFinder.getStatusMsg(StatusCode + "");

        return resLine + " " + StatusCode + " " + statusMsg;
    }

    private int getStatusCode(String url2) {

        File file = new File(resourcePath, url2);
        if (file.exists()){
            if ("/".equals(url2))
                url = ProperFinder.getValue("welFile");
            System.out.println(url);
            return 200;

        }
        //url = errorFile;
        url = ProperFinder.getValue("errorFile");
        System.out.println(url);
        return 404;

    }

    //生成响应头
    @Override
    public String getResHeader() {
        if (dynClassName!=null){
            return "Content-Type: " + MimeFinder.getMime("html");
        }
        //根据url获取文件后缀名
        String type = null;
        String[] arr = url.split("[.]");
        String endname = arr[arr.length - 1];
        //根据后缀名获取文件传输类型
        type = MimeFinder.getMime(endname);
        return "Content-Type: " + type;

    }


    //发送响应体
    @Override
    public void sendResBody() throws IOException {
        File file = new File(resourcePath,url);
        BufferedInputStream bis = new BufferedInputStream(new FileInputStream(file));
        byte[] bytes = new byte[1024];
        int len;
        while ((len = bis.read(bytes))!=-1){
            ps.write(bytes,0,len);
        }
        bis.close();
    }

    //发送响应消息
    @Override
    public void sendResresource() throws IOException, ClassNotFoundException, IllegalAccessException, InstantiationException {
        //判断是否为动态资源,如果是:执行下面代码
        if (isDynamicClass()){
            //利用反射实例化类对象
            Class clazz = Class.forName(dynClassName);
            System.out.println("--------------------------------" + dynClassName);
            Servlet s = (Servlet)clazz.newInstance();

            //调用service方法,service中实现将动态页面发送给浏览器
            System.out.println("============================"+req);
            s.service(this.req,this);


        }else {
            //如果不是,执行原来的代码
            //发送响应行
            String resLine = getResLine();
            System.out.println(resLine);
            ps.println(resLine);
            //发送响应头
            String resHeader = getResHeader();
            System.out.println(resHeader);
            ps.println(resHeader);
            //发送空行
            ps.println();
            ps.flush();
            //发送响应体
            sendResBody();
        }

    }

    private boolean isDynamicClass() {
        //1.判断文件是否存在
        File file = new File(resourcePath, url);
        if (file.exists())
            return false;
        //2.判断是否以.action结尾
        if (url.endsWith(".action") == false)
            return false;
        //3.分割请求的url拿到urlName,判断请求资源是否在配置文件内
        String[] arr = url.split("[.]");
            //System.out.println(arr[0]);
        //login.servlet.action
        if (arr.length != 2)
            return false;
        //       arr[0] = /loginServlet
        String urlName = arr[0].substring(1);
        //System.out.println(urlName);
        //从配置文件中提取类的全包名
        dynClassName = ServletFinder.getServlet(urlName);
         //System.out.println(dynClassName);
        if (dynClassName == null)
            return false;
        return true;
    }

    @Override
    public PrintStream getPs() {
        return this.ps;
    }
}