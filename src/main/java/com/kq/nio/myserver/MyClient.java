package com.kq.nio.myserver;

import com.kq.util.ByteUtil;

import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.util.concurrent.TimeUnit;

/**
 * MyClient
 * 发送不休息
 * @author kq
 * @date 2019-06-13
 */
public class MyClient {

    public static void main(String[] args) throws Exception{
        send("localhost",8300);
//        send("192.168.3.102",8300);
    }

    private static void send(String host,int port) throws Exception{

        SocketChannel dubboClient = SocketChannel.open();
        dubboClient.connect(new InetSocketAddress(host, port));

        dubboClient.write(ByteBuffer.wrap("start".getBytes()));

//        String bodyStr = getBody();
//
//        byte[] body = bodyStr.getBytes();
//
//        // 发送数据 - header段
//        byte[] header = new byte[16];






//        System.arraycopy(magicArray, 0, header, 0, 2);
//        // 标志：请求/响应， 以及body数据的序列化方式
//        header[2] = (byte) 0xC6;
//        // 响应状态码
//        header[3] = 0x00;
//        // messageId(8B)，每次请求的唯一ID
//        byte[] messageId = ByteUtil.long2bytes(1);
//        System.arraycopy(messageId, 0, header, 4, 8);
//        // bodyLength(4B)，后面的内容长度
//        byte[] bodyLength = ByteUtil.int2bytes(body.length);
//        System.arraycopy(bodyLength, 0, header, 12, 4);
//
//        // 拼装请求报文
//        byte[] request = new byte[body.length + header.length];
//        System.arraycopy(header, 0, request, 0, header.length);
//        System.arraycopy(body, 0, request, 16, body.length);
//
//
//        boolean isOpen = dubboClient.isOpen();      // 测试SocketChannel是否为open状态
//        boolean isConnected = dubboClient.isConnected();    //测试SocketChannel是否已经被连接
//        boolean isConnectionPending = dubboClient.isConnectionPending();    //测试SocketChannel是否正在进行连接
//        boolean finishConnect = dubboClient.finishConnect();    //校验正在进行套接字连接的SocketChannel是否已经完成连接
//
//        System.out.println("isOpen="+isOpen);
//        System.out.println("isConnected="+isConnected);
//        System.out.println("isConnectionPending="+isConnectionPending);
//        System.out.println("finishConnect="+finishConnect);




        ByteBuffer response = ByteBuffer.allocate(1025);
        System.out.println("start response length="+response.position());
        dubboClient.read(response);
        System.out.println("end response length="+response.position());
        System.out.println("响应内容：");
        System.out.println(new String(response.array()));

        for(int i=1;i<=100;i++) {
            byte[] array = getSendBytes(i);
//            Thread.sleep(100);
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
