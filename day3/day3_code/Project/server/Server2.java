package com.briup.java.Project.server;

import com.briup.java.Project.util.ProperFinder;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * \* Created with IntelliJ IDEA.
 * \* User: yfl
 * \* Date: 2019/8/29
 * \* To change this template use File | Settings | File Templates.
 * \* Description:多线程服务器的实现
 *              //1.创建服务器
 * \
 */
public class Server2 {
    private int server_port;//端口


    public Server2(){
        server_port = Integer.parseInt(
                ProperFinder.getValue("server_port"));//对端口做初始化

        try {
            receive();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //和客户端做交互的方法
  private void receive() throws IOException {
      //1.创建服务器
      ServerSocket server = new ServerSocket(server_port);
      while (true) {
          Socket socket = server.accept();
          new Thread(new MyRunnable(socket)).start();

      }
  }


    public static void main(String[] args) {
        Server2 server2 = new Server2();

    }
}