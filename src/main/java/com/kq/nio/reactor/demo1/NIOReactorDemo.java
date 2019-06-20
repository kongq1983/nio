package com.kq.nio.reactor.demo1;


import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.ServerSocketChannel;


/**
 * NIOReactorDemo
 *
 * @author kq
 * @date 2019-06-20
 */
public class NIOReactorDemo {

    private ServerSocketChannel serverSocketChannel;
    public static SubReactor[] subReactors = new SubReactor[16];// 负责处理IO读和写
    private MainReactor mainReactor;// 负责接收客户端的连接


    /**
     * 初始化mainReactor和subReactor
     * @throws IOException
     */
    public void newGroup() throws IOException {
        mainReactor = new MainReactor();

        for (int i = 0; i < subReactors.length; i++) {
            subReactors[i] = new SubReactor();
        }
    }


    /**
     * 初始化服务端channel并注册到mainReactor中，并且启动mainReactor
     * @throws IOException
     */
    public void initAndRegister() throws IOException {
        serverSocketChannel = ServerSocketChannel.open();
        serverSocketChannel.configureBlocking(false);
        mainReactor.register(serverSocketChannel);
        mainReactor.start();
    }


    /**
     * 绑定端口，启动服务
     * @throws IOException
     */
    private void bind() throws IOException {
        serverSocketChannel.bind(new InetSocketAddress(9000));
        System.out.println("服务启动，监听9090端口");
    }



    public static void main(String[] args) throws IOException {
        NIOReactorDemo nioReactorDemo = new NIOReactorDemo();
        nioReactorDemo.newGroup();
        nioReactorDemo.initAndRegister();
        nioReactorDemo.bind();

    }





}
