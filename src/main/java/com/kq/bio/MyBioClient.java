package com.kq.bio;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;

public class MyBioClient extends Thread{
    private Socket socket;

    public MyBioClient() {
//        this.socket = socket;

        try{
            socket = new Socket(InetAddress.getLocalHost(), 10000);
        }catch (Exception e){
            e.printStackTrace();
        }


    }

    @Override
    public void run(){
        try {
            PrintWriter out = new PrintWriter(socket.getOutputStream());
            for(int i=0;i<10;i++) {
                out.println("Hello World"+i);
                try {
                    Thread.sleep(2000l);
                }catch (Exception e) {
                    e.printStackTrace();
                }
            }
            out.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        for(int i=0;i<10000;i++) {
            new MyBioClient().start();
        }
    }

}
