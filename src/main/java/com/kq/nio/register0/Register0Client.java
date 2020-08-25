package com.kq.nio.register0;

import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.time.LocalDateTime;
import java.util.Iterator;
import java.util.Random;
import java.util.Set;
import java.util.concurrent.atomic.AtomicLong;

/**
 * KeepAliveClient
 * 长连接客户端
 * @author kq
 * @date 2019-06-17
 */
public class Register0Client {

    private static AtomicLong atomicLong = new AtomicLong();

    public static void main(String[] args) throws Exception{
        SocketChannel socketChannel = SocketChannel.open();
        Selector selector = Selector.open();
        socketChannel.configureBlocking(false);
        socketChannel.register(selector, SelectionKey.OP_CONNECT);
        socketChannel.connect(new InetSocketAddress("localhost", Register0Server.port));
//        socketChannel.connect(new InetSocketAddress("192.168.3.107", KeepAliveServer.port));
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

                        String randomStr = random.nextInt(Integer.MAX_VALUE)+"";
                        ByteBuffer buffer = ByteBuffer.wrap(randomStr.getBytes());
                        socketChannel.write(buffer);

                        System.out.println(LocalDateTime.now()+",第"+atomicLong.incrementAndGet()+"次， sendData="+new String(buffer.array()));

//                    Thread.sleep(2000L);
                    Thread.sleep(2L);


                } else if (selectionKey.isReadable()) {// 可以开始读数据

                }

            }


        }




    }


}
