package com.kq.nio.myserver1;

import com.kq.nio.backlog.BacklogServerChanel;

import java.net.Socket;
import java.util.concurrent.atomic.AtomicInteger;

public class MySocketClient {

    public static void main1(String[] args) throws Exception{
        for(int i=0;i<1000;i++) {
            Socket socket = new Socket("localhost", MyServer1.PORT);
            socket.close();
            System.out.println("客户端连接个数为:"+(i+1));
        }
    }

    public static void main(String[] args) throws Exception{

        AtomicInteger atomicLong = new AtomicInteger();

        Runnable r = () ->{
            Socket socket = null;
            int index = atomicLong.incrementAndGet();
            try {
//                socket = new Socket("localhost", BacklogServerChanel.PORT);
                socket = new Socket("localhost", MyServer1.PORT);
//                socket = new Socket(InetAddress.getLocalHost(), BacklogServerChanel.PORT);
                System.out.println(Thread.currentThread().getName()+"");
                socket.getOutputStream().write(index);
//                Thread.sleep(60000);
                socket.close();
            } catch (Exception e) {
                System.out.println("index="+index+e.getMessage());
                e.printStackTrace();
            }
        };

        for(int i=0;i<1;i++) {
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
