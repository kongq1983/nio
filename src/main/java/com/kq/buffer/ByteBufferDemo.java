package com.kq.buffer;

import com.kq.util.ByteBufferUtil;

import java.nio.ByteBuffer;
import java.util.concurrent.TimeUnit;

/**
 * @author kq
 * @date 2020-07-22 8:51
 * @since 2020-0630
 */
public class ByteBufferDemo {


    public static void main1(String[] args) {
        ByteBuffer byteBuffer = ByteBuffer.allocate(20);
        byteBuffer.putShort((short)100);
        byteBuffer.putShort((short)2);

        System.out.println(byteBuffer);

        ByteBuffer byteBuffer1 = ByteBuffer.wrap("1234567890".getBytes());
        ByteBuffer byteBuffer2 = ByteBuffer.wrap(byteBuffer1.array(),2,6);

        System.out.println("byteBuffer1="+byteBuffer1);
        System.out.println("byteBuffer2="+byteBuffer2+" length="+byteBuffer2.array().length);

        byteBuffer2.flip();
        System.out.println("byteBuffer2="+byteBuffer2+" length="+byteBuffer2.array().length);

        ByteBuffer byteBuffer3 = ByteBuffer.wrap("test filp".getBytes());
        System.out.println("filp-0 byteBuffer3="+byteBuffer3);
        byteBuffer3.flip();
        System.out.println("filp-1 byteBuffer3="+byteBuffer3);
        byteBuffer3.flip();
        System.out.println("filp-2 byteBuffer3="+byteBuffer3);

    }

    public static void main2(String[] args) throws Exception{
        ByteBuffer byteBuffer = ByteBuffer.allocate(20);
//        byteBuffer.put((byte)1);
//        byteBuffer.put((byte)2);
//        byteBuffer.put((byte)3);
        byteBuffer.put("123".getBytes());
        System.out.println("byteBuffer="+byteBuffer);
        //  同个hb的bytes数组,接着往下写数组(byteBuffer和byteBuffer1里面的hb是同个数组)
        ByteBuffer byteBuffer1 = byteBuffer.slice();
        System.out.println("byteBuffer1="+byteBuffer1);
        byteBuffer1.put("456".getBytes());
        System.out.println(ByteBufferUtil.toString(byteBuffer1));
        System.out.println(ByteBufferUtil.toString(byteBuffer));
        System.currentTimeMillis();
    }

    public static void main(String[] args) throws Exception{
        long start = System.currentTimeMillis();
        TimeUnit.SECONDS.sleep(5);
        long end = System.currentTimeMillis();

        System.out.println("diff="+(end-start));

    }

}
