package com.kq.nio.answerserver;

import com.kq.util.ByteBufferUtil;
import com.kq.util.Constant;

import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;

/**
 * @author kq
 * @date 2020-07-21 8:26
 * @since 2020-0630
 */
public class AnswerServer {

    private static AtomicLong atomicLong = new AtomicLong();

    public static final String[] answers = {"A","B","C","D"};

    public static final String RES_CONNECTION_OK = "OK";

    static Random random = new Random(3);

    private Selector selector;

    public AnswerServer(){
        try{
            this.init();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public void init() throws Exception{
        //打开1个io多路复用器
        selector = Selector.open();

        ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
        serverSocketChannel.configureBlocking(false);
        //绑定服务端口
        serverSocketChannel.socket().bind(new InetSocketAddress(Constant.SERVER.IP,Constant.SERVER.PORT));

        System.out.println("answer server linten port="+Constant.SERVER.PORT);

        //对于服务端来说，一定要先注册一个OP_ACCEPT事件用来响应客户端的请求连接
        serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);

    }

    public void start() throws Exception{

        while (true) {
            this.selector.select();
            System.out.println("the coming keys="+this.selector.selectedKeys());

            Iterator<SelectionKey> iterator = this.selector.selectedKeys().iterator();

            while(iterator.hasNext()) {
                SelectionKey key = iterator.next();
                iterator.remove();
                SocketChannel socketChannel = null;
                int type = 0;
                try {
                    if (key.isAcceptable()) {
                        //这里key.channel是ServerSocketChannel
                        type = SelectionKey.OP_ACCEPT;
                        this.accept(key);
                    } else if (key.isValid() && key.isReadable()) {
                        type = SelectionKey.OP_READ;
                        socketChannel = (SocketChannel) key.channel();
                        this.read(socketChannel, key);
                    }
                }catch (Exception e) {
                    key.cancel();
                    if(socketChannel!=null){
                        socketChannel.socket().close();
                        socketChannel.close();
                    }
                    System.out.println("exception at type="+type);
                    e.printStackTrace();
                }

            }

        }
    }

    private void accept(SelectionKey key) throws Exception {
        ServerSocketChannel serverSocketChannel = (ServerSocketChannel)key.channel();
        SocketChannel socketChannel = serverSocketChannel.accept();
        socketChannel.configureBlocking(false);
        System.out.println("开始注册读时间");

        socketChannel.register(this.selector,SelectionKey.OP_READ);

        this.sayAcceptOk(socketChannel);

    }

    private void sayAcceptOk(SocketChannel socketChannel) throws Exception{
        ByteBuffer byteBuffer = ByteBuffer.wrap(AnswerServer.RES_CONNECTION_OK.getBytes());
        System.out.println("AnswerServer发送连接成功!");
        socketChannel.write(byteBuffer);
    }

    private void read(SocketChannel channel,SelectionKey key) throws Exception {
        System.out.println(atomicLong.incrementAndGet()+",开始读数据 key="+key);
        // 先得到题目

        ByteBuffer buffer = ByteBuffer.allocate(50);

        while (channel.isOpen() && channel.read(buffer) != -1) {
            // 长连接情况下,需要手动判断数据有没有读取结束 (此处做一个简单的判断: 超过0字节就认为请求结束了)
            if (buffer.position() > 0) {
                System.out.println("数据读完了-----------------------position="+buffer.position());
                break;
            }
        }
        if (buffer.position() == 0) {
            System.out.println("没有数据了-----------------------");
            return; // 如果没数据了, 则不继续后面的处理
        }

        String questionId = ByteBufferUtil.toString(buffer);
        System.out.println("========read questionId :" + questionId);

        String answer = answers[random.nextInt(4)];

        String response = "questionId="+questionId+",answerId="+answer;

        ByteBuffer responseBuffer = ByteBuffer.wrap(response.getBytes());
        channel.write(responseBuffer);

    }

    public static void main(String[] args) throws Exception{
        new AnswerServer().start();
    }

}
