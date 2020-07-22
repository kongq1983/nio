package com.kq.nio.protocol.simple;

import com.kq.common.MyRunnable;
import com.kq.util.ByteUtil;
import com.kq.util.Constant;

import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author kq
 * @date 2020-07-22 14:44
 * @since 2020-0630
 */
public class SimpleProtocolClient implements MyRunnable {

    private Selector selector;
    private SocketChannel socketChannel;

    private AtomicInteger atomicInteger = new AtomicInteger();

    public SimpleProtocolClient() throws Exception{
        this.init();
    }

    @Override
    public void init() throws Exception {
        selector = Selector.open();
        socketChannel = SocketChannel.open();
        socketChannel.configureBlocking(false);


    }

    private void doConnect() throws Exception{
        boolean connected = socketChannel.connect(new InetSocketAddress(Constant.SERVER.IP,Constant.SERVER.PORT));

        if(!connected) {
            //若连接服务器失败,则向多路复用器注册连接事件
            socketChannel.register(selector, SelectionKey.OP_CONNECT);
        }

    }

    @Override
    public void start() throws Exception {

        this.doConnect();

        while (true) {
            try{
                this.selector.select();
                Iterator<SelectionKey> keys = this.selector.selectedKeys().iterator();

                while(keys.hasNext()) {
                    SelectionKey key = keys.next();
                    keys.remove();

                    SocketChannel sc = (SocketChannel) key.channel();

                    if (key.isConnectable()) {
                        if(sc.finishConnect()) {
                            sc.register(selector, SelectionKey.OP_WRITE);
                        }
                    }else if(key.isWritable()) {
                        try{
                            this.doWrite(sc);
                        }catch (Exception e){
                            e.printStackTrace();
                            key.cancel();;
                            sc.socket().close();
                            sc.close();
                        }
                    }

                }


            }catch (Exception e) {
                e.printStackTrace();
            }
        }

    }


    /**
     * 正常发送
     * @param sc
     * @throws Exception
     */
    private void doWrite(SocketChannel sc) throws Exception{
        // 魔数，short类型 (类似 class文件里面的cafebabb) 数据的开头
        byte[] magicArray = ByteUtil.short2bytes((short) 0xdabb);//see java.nio.Bits


        String messgae = "i'm client! , the id="+atomicInteger.incrementAndGet();
        byte[] mesBytes = messgae.getBytes();
//
        int length = magicArray.length+mesBytes.length+4;
        ByteBuffer byteBuffer = ByteBuffer.allocate(length);
//        int length = magicArray.length;
        //协议头
        byteBuffer.put(magicArray);
        //body长度
        byteBuffer.putInt(mesBytes.length);
        //body
        byteBuffer.put(mesBytes);

//        System.out.println("start byteBuffer="+byteBuffer);
        // 注意: allocatey一定要filp 否则服务端，接收不到OP_READ事件   warp没有这个问题
        byteBuffer.flip();
//        byteBuffer.put(mesBytes);
//        System.out.println("end byteBuffer="+byteBuffer);
//        System.out.println(" byteBuffer="+ByteBuffer.wrap(mesBytes));

        sc.write(byteBuffer);


        System.out.println("客户端发送:"+messgae);

        TimeUnit.SECONDS.sleep(3);
        // 每隔3s发送1次
        sc.register(selector, SelectionKey.OP_WRITE);

    }

    public static void main(String[] args) throws Exception{
        new SimpleProtocolClient().start();
    }

}
