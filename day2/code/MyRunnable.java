package com.briup.java.Project.server;

import com.briup.java.Project.util.ProperFinder;

import java.io.*;
import java.net.Socket;

/**
 * \* Created with IntelliJ IDEA.
 * \* User: yfl
 * \* Date: 2019/8/29
 * \* To change this template use File | Settings | File Templates.
 * \* Description:分离多线程处理客户端(浏览器)的请求和给浏览器发送响应
 * \
 */
public class MyRunnable implements Runnable{
    //属性 resourcePath    errorFile   welFile  socket
    private String resourcePath;
    private String errorFile;
    private String welFile;
    private Socket socket;

    //在构造器中初始化
    public MyRunnable(Socket socket){
        this.socket = socket;
        resourcePath = ProperFinder.getValue("resourcePath");
        errorFile = ProperFinder.getValue("errorFile");
        welFile = ProperFinder.getValue("welFile");

    }

    @Override
    public void run() {

        //拿到url, getUrl();
        String url = getUrl();
        System.out.println("url:" + url);

        //发送响应, sendResponse();
        sendResponse(url);

        try {
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void sendResponse(String url) {
        //准备流发送响应
        PrintStream ps = null;
        try {
            ps = new PrintStream(socket.getOutputStream());

        } catch (IOException e) {
            e.printStackTrace();
        }
        //资源目录
        File file = new File(resourcePath, url);

        //判断请求资源在服务器中是否存在
        boolean flag = file.exists();
        //System.out.println(flag);

        //根据判断的结果发送对应的响应
        String responseLine = null; //响应状态行
        if (flag){
            responseLine = "HTTP/1.1 200 OK";
            //如果为"/",返回欢迎页面
            if ("/".equals(url)) {
                file = new File(resourcePath, "welcom.html");
            }
        }else {
            responseLine = "HTTP/1.1 404 NotFound";
            file = new File(resourcePath,"error.html");
        }

        //根据协议发送响应
        //发送状态行
        ps.println(responseLine);
        //发送空行
        ps.println();
        //发送响应正文
        BufferedInputStream bis = null;
        try {
            bis = new BufferedInputStream(new FileInputStream(file));

            int len;
            byte[] bytes2 = new byte[1024];
            while ((len =bis.read(bytes2))!=-1){
//           String s = new String(bytes2, 0, len);
//          ps.println(s);
                ps.write(bytes2,0,len);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private String getUrl() {
        //接收客户端的数据
        BufferedReader br = null;
        String url = null;
        try {
            br = new BufferedReader(
                    new InputStreamReader(socket.getInputStream()));
            //接收请求行
            String str = br.readLine();

            //解析请求行
            String[] arr = str.split(" ");
            url = arr[1];

        } catch (IOException e) {
            e.printStackTrace();
        }
        return url;
    }
}