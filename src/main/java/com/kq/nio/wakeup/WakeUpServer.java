package com.kq.nio.wakeup;

import java.io.IOException;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.time.LocalDateTime;
import java.util.Iterator;
import java.util.concurrent.TimeUnit;

/**
 * @author kq
 * @date 2020-07-24 14:47
 * @since 2020-0630
 */
public class WakeUpServer {

    private Selector selector;
    private ServerSocketChannel serverSocketChannel;

    public WakeUpServer() throws Exception {
        this.init();
    }

    public void init() throws IOException {
        selector = Selector.open();
        serverSocketChannel = ServerSocketChannel.open();
        serverSocketChannel.configureBlocking(false);
    }

    public void start() throws Exception {
        while(true) {
            System.out.println(LocalDateTime.now()+"-------------------wait for select()");
            int selectSize = this.selector.select();
            System.out.println(LocalDateTime.now()+" after select() 得到selectSize="+selectSize);

            Iterator<SelectionKey> iter = this.selector.selectedKeys().iterator();

            while(iter.hasNext()) {
                SelectionKey key = iter.next();

                if(key.isAcceptable()) {
                    try {
                        this.accept(key);
                    }catch (Exception e){
                        e.printStackTrace();
                    }finally {
                        key.cancel();
                    }
                }

            }


        }
    }

    private void accept(SelectionKey key) throws Exception {
        ServerSocketChannel server = (ServerSocketChannel) key.channel();
        SocketChannel channel = server.accept(); //接收链接
        channel.configureBlocking(false);//设置为非阻塞
        System.out.println("开始注册读事件");
        channel.register(this.selector,SelectionKey.OP_READ);//为通道注册读事件
//        ByteBuffer outBuffer = ByteBuffer.wrap("hi , accept success!".getBytes());
//        channel.write(outBuffer); //写信息
//        this.sayHello(channel);
        System.out.println("----------aaaaaa-------------");
//        channel.register(this.selector, SelectionKey.OP_WRITE);//为通道注册读事件

    }


    public static void main(String[] args) throws Exception {
        WakeUpServer wakeUpServer = new WakeUpServer();


        Runnable wakeup = ()->{
            for(int i=0;i<10;i++) {
                try {
                    TimeUnit.SECONDS.sleep(5);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                System.out.println("start调用---------------- wake up");
                // 唤醒select
                wakeUpServer.selector.wakeup();
                System.out.println("after调用---------------- wake up");
            }
        };

        System.out.println("==========================================1");

        new Thread(wakeup).start();
        System.out.println("==========================================2");

        wakeUpServer.start();


    }

}
