package com.briup.java.Project.dao;



import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

/**
 * \* Created with IntelliJ IDEA.
 * \* User: yfl
 * \* Date: 2019/8/29
 * \* To change this template use File | Settings | File Templates.
 * \* Description:
 * \
 */
public class RequestImp implements Request{
    //属性
    private String method;
    //请求的资源
    private String url;
    //Http协议版本
    private String protocol;
    //请求头版本
    private Map<String,String> headMap;
    //请求 提交信息
    private Map<String,String> bodyMap;
    //服务器与客户端通信的套接字
    private Socket socket;

    //构造方法
    public RequestImp(Socket socket){
        this.socket = socket;
        headMap = new HashMap<String, String>();
        bodyMap = new HashMap<String, String>();

        getInfos();
    }

    private void getInfos() {
        //准备包装流
        try {

            //第一部分,拆分请求体
            BufferedReader br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            //读取请求行
            String reqLine = br.readLine();
            //解决浏览器重复刷新的问题
            if (reqLine == null){
                return;
            }else {
                String[] larr = reqLine.split(" ");
                //赋值
                method = larr[0];
                url = larr[1];
                protocol = larr[2];

                System.out.println("**************************11111111*********************************");
            }
            //第二部分,拆分请求头
            String hstr = null;
            while (!"".equals((hstr = br.readLine()))){
                System.out.println("请求头: " + hstr);
                String[] harr = hstr.split(": ");
                if (harr.length == 2){
                    headMap.put(harr[0],harr[1]);
                }
            }
            System.out.println("**************************222222222222222222*********************************");

            //第三部分,拆分请求体
            if (br.ready()){
                char[] chars = new char[1024];
                int len = br.read(chars);
                String bstr = new String(chars, 0, len);
                System.out.println(bstr);
                splitBody(bstr);

            }else {  //从url中拆分请求体  "/a.txt?name=zhangsan&code=188"
                String[] arr = url.split("[?]");
                if (arr.length == 1){
                    return;
                }
                //拆分arr[1]
                splitBody(arr[1]);
                url = arr[0];
            }


        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void splitBody(String bstr) {
        String[] barr = bstr.split("&");
        //遍历拆分拿到的每一部分请求体
        for(String s : barr){
            String[] arr = s.split("=");
            if (arr.length!=2){
                bodyMap.put(arr[0],"");
            }else {
                bodyMap.put(arr[0],arr[1]);
            }
        }
    }

    @Override
    public String getMethod() {
        return method;
    }

    @Override
    public String getUrl() {
        return url;
    }

    @Override
    public String getProtocol() {
        return protocol;
    }

    @Override
    public Map<String, String> getReqHead() {
        return headMap;
    }

    @Override
    public Map<String, String> getReqBody() {
        return bodyMap;
    }
}