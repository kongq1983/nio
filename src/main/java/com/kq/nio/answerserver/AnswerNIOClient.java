package com.kq.nio.answerserver;

import com.kq.common.MyRunnable;
import com.kq.util.ByteBufferUtil;
import com.kq.util.Constant;

import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;

/**
 * @author kq
 * @date 2020-07-21 14:00
 * @since 2020-0630
 */
public class AnswerNIOClient implements MyRunnable {

    private Selector selector;
    SocketChannel socketChannel;
    private AtomicLong atomicLong = new AtomicLong();

    public AnswerNIOClient() throws Exception {
        this.init();
    }


    @Override
    public void init() throws Exception {
        selector = Selector.open();
        socketChannel = SocketChannel.open();
        socketChannel.configureBlocking(false);
//        System.out.println("end init method");
    }

    @Override
    public void start() throws Exception {
//        System.out.println("call start method");
        try {
            this.doConnect();
//            System.out.println("after call doConnect method");
        }catch (Exception e){
            System.out.println("连接失败!");
            e.printStackTrace();
            System.exit(-1);
        }


        while (true) {
            selector.select();
            try {

                Iterator<SelectionKey> iterator = this.selector.selectedKeys().iterator();

                while (iterator.hasNext()) {
                    SelectionKey key = iterator.next();
                    iterator.remove();

                    SocketChannel sc = (SocketChannel) key.channel();

                    System.out.println("isTrue=" + (sc==socketChannel));
                    System.out.println("socketChannel=" + socketChannel);

                    if(key.isConnectable()) {
                        if (sc.finishConnect()) {
                                System.out.println("==========finishConnect==================================");
                            sc.register(selector, SelectionKey.OP_READ);
                            this.doWrite(socketChannel);
                        } else {
                            //连接失败 进程退出
                            System.exit(1);
                        }

                    } else if (key.isValid() && key.isReadable()) {
                        this.doRead(sc, key);
                    } else if (key.isValid() && key.isWritable()) {
                        this.doWrite(sc);
                    }

                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }

    private void doConnect() throws Exception {
        //通过ip和端口号连接到服务器
        boolean connected = this.socketChannel.connect(new InetSocketAddress(Constant.SERVER.IP,Constant.SERVER.PORT));
        System.out.println("doConnect="+connected);
        if(connected){
            //向多路复用器注册可读事件
            socketChannel.register(this.selector,SelectionKey.OP_READ);
        } else {
            //若连接服务器失败,则向多路复用器注册连接事件
            socketChannel.register(this.selector, SelectionKey.OP_CONNECT);
        }

    }

    private void doRead(SocketChannel socketChannel,SelectionKey key) throws Exception {
        ByteBuffer readBuffer = ByteBuffer.allocate(1024);
        socketChannel.read(readBuffer);
        String content = ByteBufferUtil.toString(readBuffer);
        System.out.println("服务器响应！ 问题及答案:"+content);

        socketChannel.register(this.selector,SelectionKey.OP_WRITE);

    }


    private void doWrite(SocketChannel socketChannel) throws Exception {
        Long questionId = getQuestionId();

        ByteBuffer byteBuffer = ByteBuffer.wrap(String.valueOf(questionId).getBytes());
        socketChannel.write(byteBuffer);
        System.out.println("AnswerNIOClient 发送查询答案！ questionId="+questionId);

        socketChannel.register(this.selector,SelectionKey.OP_READ);

    }


    private Long getQuestionId() throws Exception{
        TimeUnit.SECONDS.sleep(3);
        Long questionId = atomicLong.incrementAndGet();
        return questionId;


    }

    public static void main(String[] args) throws Exception{
        new AnswerNIOClient().start();
    }

}
