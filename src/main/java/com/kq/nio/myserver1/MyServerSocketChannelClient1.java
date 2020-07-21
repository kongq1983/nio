package com.kq.nio.myserver1;

import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.util.concurrent.TimeUnit;

/**
 * @author kq
 * @date 2020-07-20 17:55
 * @since 2020-0630
 */
public class MyServerSocketChannelClient1 {

    public static void main(String[] args) throws Exception{
        SocketChannel socketChannel = SocketChannel.open();
        socketChannel.connect(new InetSocketAddress("localhost",MyServer1.PORT));

        ByteBuffer byteBuffer = ByteBuffer.allocate(5);
        byteBuffer.put("12345".getBytes());
        socketChannel.write(byteBuffer);

//        byteBuffer = ByteBuffer.allocate(200);
//        byteBuffer.put("2".getBytes());
//        socketChannel.write(byteBuffer);
//
//        byteBuffer = ByteBuffer.allocate(200);
//        byteBuffer.put("3".getBytes());
//        socketChannel.write(byteBuffer);

        System.out.println("---------------------send over");

        TimeUnit.SECONDS.sleep(200);

        socketChannel.socket().close();
        socketChannel.close();

    }

}
