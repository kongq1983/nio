package com.kq.nio.keepalive;

import org.apache.commons.lang3.RandomStringUtils;

import java.net.InetSocketAddress;
import java.nio.Buffer;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Random;
import java.util.Set;

/**
 * KeepAliveClient
 * 长连接客户端
 * @author kq
 * @date 2019-06-17
 */
public class KeepAliveClient {

    public static void main(String[] args) throws Exception{
        SocketChannel socketChannel = SocketChannel.open();
        Selector selector = Selector.open();
        socketChannel.configureBlocking(false);
        socketChannel.register(selector, SelectionKey.OP_CONNECT);
        socketChannel.connect(new InetSocketAddress("192.168.3.107", KeepAliveServer.port));
//        socketChannel.connect(new InetSocketAddress("localhost", KeepAliveServer.port));

        Random random = new Random();

        while (true) {
            selector.select();
            Set<SelectionKey> selectionKeys = selector.selectedKeys();
            Iterator<SelectionKey> iterator = selectionKeys.iterator();

            while (iterator.hasNext()) {
                SelectionKey selectionKey = iterator.next();
                iterator.remove();

                if (selectionKey.isConnectable()) {
                    if (socketChannel.finishConnect()) { // 完成连接
                        System.out.println("连接成功-" + socketChannel);
                        selectionKey.interestOps(SelectionKey.OP_WRITE);

                    }

                } else if (selectionKey.isWritable()) {// 可以开始写数据

                    int foreach = random.nextInt(2);

                    for(int i=0;i<foreach;i++) {
                        ByteBuffer sendBuffer = ByteBuffer.allocate(10);

                        byte[] bs = new byte[]{1,2,3,4,5,6,7,8,9,0};

//                        String data = RandomStringUtils.random(10);
                        sendBuffer.put(bs);

                        ByteBuffer buffer = ByteBuffer.wrap("12345678901".getBytes());

//                        socketChannel.write(sendBuffer);
                        socketChannel.write(buffer);

                        System.out.println("sendData="+new String(bs));

                    }

                    Thread.sleep(2000l);


                } else if (selectionKey.isReadable()) {// 可以开始读数据

                }

            }


        }




    }


}
