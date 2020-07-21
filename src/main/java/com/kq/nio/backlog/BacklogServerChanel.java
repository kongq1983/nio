package com.kq.nio.backlog;

import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.channels.ServerSocketChannel;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;

public class BacklogServerChanel {

    public static final int PORT = 8083;
    static LinkedBlockingQueue blockingQueue = new LinkedBlockingQueue();
    static ThreadPoolExecutor poolExecutor = new ThreadPoolExecutor(2,2,60,TimeUnit.SECONDS,blockingQueue);

    static AtomicLong atomicLong = new AtomicLong();

    public static void main(String[] args) throws Exception{
        ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
        serverSocketChannel.configureBlocking(true);
        //backlog来限制客户端连接数量
        serverSocketChannel.bind(new InetSocketAddress("localhost",PORT),30);

        ServerSocket serverSocket = serverSocketChannel.socket();
        Thread.sleep(5000);

        boolean isRun = true;

        int index = 0;
        while(isRun) {
            System.out.println("wait for connection : "+atomicLong.incrementAndGet());
            Socket socket = serverSocket.accept();

            poolExecutor.execute(new SocketThread(socket,index++));

//            socket.close();
        }

        Thread.sleep(8000);

        serverSocket.close();
        serverSocketChannel.close();

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
            System.out.println("index="+index+" is coming");
            try {
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
