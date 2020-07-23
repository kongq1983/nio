package com.kq.nio.protocol.simple;

import com.kq.common.MyRunnable;
import com.kq.util.ByteUtil;
import com.kq.util.Constant;

import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/** 1条多1条多的数据发送
 * 演示客户端发多次数据(多个包组成1个完整的包)
 * @author kq
 * @date 2020-07-22 14:44
 * @since 2020-0630
 */
public class SimpleSplitProtocolClient implements MyRunnable {

    private Selector selector;
    private SocketChannel socketChannel;

    private AtomicInteger atomicInteger = new AtomicInteger(6);

    private Random random = new Random();

    public SimpleSplitProtocolClient() throws Exception{
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
                            System.out.println("发送数据出错!"+e.getMessage());
                            key.cancel();
                            sc.socket().close();
                            sc.close();
                            e.printStackTrace();
                        }
                    }

                }


            }catch (Exception e) {
                e.printStackTrace();
            }
        }

    }

    private ByteBuffer getByteBuffer() {

        // 魔数，short类型 (类似 class文件里面的cafebabb) 数据的开头
        byte[] magicArray = ByteUtil.short2bytes((short) 0xdabb);//see java.nio.Bits


        String messgae = "i'm client! , the id="+atomicInteger.incrementAndGet();
        byte[] mesBytes = messgae.getBytes();
        // 2+4+
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

        return byteBuffer;

    }


    private int sendSize = 0;
    /** 现在发送到第几个字节数 */
    private int nowIndex = 0;
    /** 最大可发送字节数 */
    private int endIndex = 0;

    /** 发送ByteBuffer */
    private ByteBuffer sendByteBuffer = this.getByteBufferJoin();

    /**
     * 多个合并成1个
     * @return
     */
    private ByteBuffer getByteBufferJoin(){

        List<ByteBuffer> list = new ArrayList<>();

        int length = 0;
        for(int i=0;i<10;i++) {
            ByteBuffer byteBuffer = this.getByteBuffer();
            length+=byteBuffer.capacity();
            list.add(byteBuffer);
        }
        ByteBuffer joinByteBuffer = ByteBuffer.allocate(length);

        for(ByteBuffer byteBuffer : list) {
            joinByteBuffer.put(byteBuffer.array());
        }

        endIndex = length;

        joinByteBuffer.flip();

        return joinByteBuffer;

    }

    private int times = 0;

    /**
     * 正常发送
     * @param sc
     * @throws Exception
     */
    private void doWrite(SocketChannel sc) throws Exception{

        System.out.println("sendByteBuffer="+sendByteBuffer);

        times++;

        int next = random.nextInt(15);

        if(this.endIndex-this.nowIndex>15){
            if(times%2==0){
                this.sendSize = 12;
            } else {
                this.sendSize = 6;
            }
        } else {
            this.sendSize = this.endIndex-this.nowIndex;
        }

        byte[] data = new byte[sendSize];

        System.out.println("nowIndex="+nowIndex+" sendSize="+sendSize);
//        this.sendByteBuffer.get(data,this.nowIndex,this.sendSize);

//        ByteBuffer byteBuffer = ByteBuffer.wrap(data);
        ByteBuffer byteBuffer = ByteBuffer.wrap(sendByteBuffer.array(),nowIndex,sendSize);
        System.out.println("本次发送ByteBuffer"+byteBuffer +" 长度:"+sendSize);
        sc.write(byteBuffer);

        TimeUnit.SECONDS.sleep(3);

        this.nowIndex = this.nowIndex+this.sendSize;

        // 每隔3s发送1次
        sc.register(selector, SelectionKey.OP_WRITE);

    }

    public static void main(String[] args) throws Exception{
        new SimpleSplitProtocolClient().start();

    }

}
