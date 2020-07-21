package com.kq.nio.backlog;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @author kq
 * @date 2020-07-20 15:30
 * @since 2020-0630
 */
public class BacklogServer1 {
    static LinkedBlockingQueue blockingQueue = new LinkedBlockingQueue();
    static ThreadPoolExecutor poolExecutor = new ThreadPoolExecutor(2,2,60,TimeUnit.SECONDS,blockingQueue);

    public static void main(String[] args) throws Exception {

        int backlog = 5;
        int index = 0;
        ServerSocket serversocket = new ServerSocket(BacklogServerChanel.PORT, backlog);
        while (true) {
            System.out.println("启动服务端......");

            Socket socket = serversocket.accept();
            System.out.println("第"+(index++)+"个，有客户端连上服务端, 客户端信息如下：" + socket.getInetAddress() + " : " + socket.getPort() + ".");
//            poolExecutor.execute(new BacklogServerChanel.SocketThread(socket,index++));


            syncInvoke(socket);

        }
    }


    public static void syncInvoke(Socket socket) throws Exception{
        BufferedReader in = null;
        PrintWriter out = null;
        try {
            int i;
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            out = new PrintWriter(socket.getOutputStream(), true);
            do {
                char[] c = new char[1024];
                i = in.read(c);
                System.out.println("服务端收到信息: " + new String(c, 0, i));
            } while (i == -1);
            out.close();
            in.close();
            socket.close();
            System.out.println("关闭服务端......");
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            try {
                socket.close();
            }catch (Exception e){

            }
        }
    }

    static class SocketThread implements Runnable {

        Socket socket;
        int index;

        public SocketThread(Socket socket,int index){
            this.socket = socket;
            this.index = index;
        }

        @Override
        public void run() {

            BufferedReader in = null;
            PrintWriter out = null;
            try {
                int i;
                in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                out = new PrintWriter(socket.getOutputStream(), true);
                do {
                    char[] c = new char[1024];
                    i = in.read(c);
                    System.out.println("服务端收到信息: " + new String(c, 0, i));
                } while (i == -1);
                out.close();
                in.close();
                socket.close();
                System.out.println("关闭服务端......");
                TimeUnit.SECONDS.sleep(60);

            }catch (Exception e){
                e.printStackTrace();
            }finally {
                try {
                    socket.close();
                }catch (Exception e){

                }
            }

        }
    }


}
