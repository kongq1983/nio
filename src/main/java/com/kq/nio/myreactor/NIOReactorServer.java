package com.kq.nio.myreactor;

import com.kq.util.Constant;

import java.net.InetSocketAddress;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;

/**
 * @author kq
 * @date 2020-07-23 14:57
 * @since 2020-0630
 */
public class NIOReactorServer {

    private Selector selector;
    private ServerSocketChannel serverSocketChannel;
    private AcceptIOThread ioThread = new AcceptIOThread();
    private AcceptThread acceptThread = new AcceptThread(ioThread);


    public NIOReactorServer() throws Exception{
        this.init();
    }

    public void init() throws Exception {
        selector = Selector.open();
        serverSocketChannel = ServerSocketChannel.open();
        // 非阻塞
        serverSocketChannel.configureBlocking(false);
        // 服务器绑定端口
        serverSocketChannel.bind(new InetSocketAddress(Constant.SERVER.IP,Constant.SERVER.PORT),100);
        System.out.println("NIOReactorServer Listen Port :" +Constant.SERVER.PORT);

        acceptThread.register(this.serverSocketChannel);

    }

    public void start(){
        this.acceptThread.start();
        this.ioThread.start();
    }

    public static void main(String[] args) throws Exception{
        new NIOReactorServer().start();
    }



}
