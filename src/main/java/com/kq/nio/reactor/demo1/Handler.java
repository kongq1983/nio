package com.kq.nio.reactor.demo1;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Handler
 *
 * @author kq
 * @date 2019-06-20
 */
public class Handler {

    /**
     * 处理业务操作的线程
     */
    private static ExecutorService workPool = Executors.newCachedThreadPool();

    public Handler(SocketChannel socketChannel) throws IOException {

        System.out.println(Thread.currentThread().getName()+" 开始处理业务! "+socketChannel);

        ByteBuffer requestBuffer = ByteBuffer.allocate(1024);

        while (socketChannel.isOpen() && socketChannel.read(requestBuffer) != -1) {
            // 长连接情况下,需要手动判断数据有没有读取结束 (此处做一个简单的判断: 超过0字节就认为请求结束了)
            if (requestBuffer.position() > 0) break;
        }
        if (requestBuffer.position() == 0) return; // 如果没数据了, 则不继续后面的处理

        requestBuffer.flip();

        byte[] content = new byte[requestBuffer.remaining()];
        requestBuffer.get(content);
        System.out.println(new String(content));
        System.out.println("收到数据,来自：" + socketChannel.getRemoteAddress());
        // TODO 业务操作 数据库 接口调用等等
        workPool.execute(new Runnable() {
            @Override
            public void run() {
                // 处理业务
            }
        });

        // 响应结果 200
        String response = "HTTP/1.1 200 OK\r\n" +
                "Content-Length: 11\r\n\r\n" +
                "Hello World";
        ByteBuffer buffer = ByteBuffer.wrap(response.getBytes());
        while (buffer.hasRemaining()) {
            socketChannel.write(buffer);
        }


    }


}
