package com.kq.nio.myserver;

import com.kq.util.ByteUtil;

import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.util.concurrent.TimeUnit;

/**
 * MyClient
 * 每次发送 休息200ms
 * @author kq
 * @date 2019-06-13
 */
public class MyClientSleep {

    public static void main(String[] args) throws Exception{
        send("localhost",8300);
//        send("192.168.3.102",8300);
    }

    private static void send(String host,int port) throws Exception{

        SocketChannel dubboClient = SocketChannel.open();
        dubboClient.connect(new InetSocketAddress(host, port));

        dubboClient.write(ByteBuffer.wrap("start".getBytes()));


        ByteBuffer response = ByteBuffer.allocate(1025);
        System.out.println("start response length="+response.position());
        dubboClient.read(response);
        System.out.println("end response length="+response.position());
        System.out.println("响应内容：");
        System.out.println(new String(response.array()));

        for(int i=1;i<=100;i++) {
            byte[] array = getSendBytes(i);
            Thread.sleep(100);
            dubboClient.write(ByteBuffer.wrap(array));
            dubboClient.socket().getOutputStream().flush();
        }

        TimeUnit.SECONDS.sleep(600);

    }

    private static byte[] getSendBytes(int i) {
        // 魔数，short类型 (类似 class文件里面的cafebabb) 数据的开头
        byte[] magicArray = ByteUtil.short2bytes((short) 0xdabb);

        String messgae = i+",i'm client!";
        byte[] mesBytes = messgae.getBytes();

        System.out.println("send data length="+mesBytes.length+",index="+i);

        byte[] length = ByteUtil.int2bytes(mesBytes.length);


        byte[] array = new byte[magicArray.length+length.length+mesBytes.length];

        // magicArray copy to array
        System.arraycopy(magicArray,0,array,0,magicArray.length);
        // length copy to array
        System.arraycopy(length,0,array,2,length.length);
        // mesBytes copy to array
        System.arraycopy(mesBytes,0,array,6,mesBytes.length);
        return array;
    }

}
