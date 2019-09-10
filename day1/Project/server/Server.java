package com.briup.java.Project.server;

import java.io.*;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * \* Created with IntelliJ IDEA.
 * \* User: yfl
 * \* Date: 2019/8/28
 * \* To change this template use File | Settings | File Templates.
 * \* Description:服务器,获取浏览器的请求并解析
 *                  //创建服务器,监听一个端口
 *                  //接收客户端的连接,并判断socket,给出IP等信息
 *  *               //创建流,接收客户端的数据
 *                  //拿到请求行并解析   请求行:Method Request-URI HTTP-Version CRLF
 *                  //解析请求行,获取url
 *                  //根据url判断请求的资源在服务器中是否存在
 *                  //准备将文件发送给客户端的流
 *                        //如果存在,进一步判断资源路径是否为空
 *                            //是:发送欢迎页
 *                            //否:发送请求的文件
 *
 *                        //如果不存在
 *                            发送error,404 NotFound
 *
 *                  //发送响应给浏览器   HTTP响应也是由三个部分组成， 响应状态行、消息报头、响应正文
 *                    //1.发送响应状态行
 *                    //2.发送响应体,可以为空
 *                    //空行
 *                    //3.发送响应正文,即请求的资源内容
 *                  //关闭资源
 * \
 */
public class Server {
    public static void main(String[] args) throws IOException {
        //创建服务器,监听一个端口
        ServerSocket server = new ServerSocket(9999);
        System.out.println("服务器已经启动,服务器端口号为:" + 9999);
        //接收客户端的连接,并判断socket,给出IP等信息
        Socket socket = server.accept();
        if (socket!=null){
            System.out.println("客户端已经连接...Address: "  + InetAddress.getLocalHost());
        }

        //接收客户端的数据
        BufferedReader br = new BufferedReader(
                new InputStreamReader(socket.getInputStream()));

        //接收请求行
        String str = br.readLine();
        System.out.println("请求行:" + str);

        //接收客户端发送的数据
        String line = null;

        while (!"".equals(line = br.readLine())){
            System.out.println(line);
        }

        //解析请求行
        String[] arr = str.split(" ");
        System.out.println("url为: " + arr[1]);

        //准备流发送响应
        PrintStream ps = new PrintStream(socket.getOutputStream());
        //资源目录
        String resourcepath = "E:\\BD1904\\src\\com\\briup\\java\\Project\\resource";
        File file = new File(resourcepath, arr[1]);

        //判断请求资源在服务器中是否存在
        boolean flag = file.exists();
        System.out.println(flag);

        //根据判断的结果发送对应的响应
        String responseLine = null; //响应状态行
        if (flag){
            responseLine = "HTTP/1.1 200 OK";
            //如果为"/",返回欢迎页面
            if ("/".equals(arr[1])) {
                file = new File(resourcepath, "welcom.html");
            }
        }else {
            responseLine = "HTTP/1.1 404 NotFound";
            file = new File(resourcepath,"error.html");
        }

        //根据协议发送响应
        //发送状态行
        ps.println(responseLine);
        //发送空行
        ps.println();
        //发送响应正文
        BufferedInputStream bis = new BufferedInputStream(new FileInputStream(file));
        int len;
        byte[] bytes2 = new byte[1024];
        while ((len =bis.read(bytes2))!=-1){
//           String s = new String(bytes2, 0, len);
//          ps.println(s);
            ps.write(bytes2,0,len);
        }

        bis.close();
        ps.close();
        br.close();
        socket.close();
        server.close();

    }
}