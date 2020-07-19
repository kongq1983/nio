package com.kq.nio.backlog;

import java.net.InetAddress;
import java.net.Socket;

public class BacklogClient {

    public static void main1(String[] args) throws Exception{
        for(int i=0;i<1000;i++) {
            Socket socket = new Socket("localhost", BacklogServerChanel.PORT);
            socket.close();
            System.out.println("客户端连接个数为:"+(i+1));
        }
    }

    public static void main(String[] args) throws Exception{

        Runnable r = () ->{
            Socket socket = null;
            try {
//                socket = new Socket("localhost", BacklogServerChanel.PORT);
                socket = new Socket(InetAddress.getLocalHost(), BacklogServerChanel.PORT);
                System.out.println(Thread.currentThread().getName()+"");
                Thread.sleep(1000);
                socket.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        };

        for(int i=0;i<100;i++) {
            Thread t = new Thread(r);
            t.start();
        }


//        for(int i=0;i<1000;i++) {
//            Socket socket = new Socket("localhost",BacklogServer.PORT);
//            socket.close();
//            System.out.println("客户端连接个数为:"+(i+1));
//        }
    }

}
