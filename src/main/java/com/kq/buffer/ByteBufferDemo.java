package com.kq.buffer;

import java.nio.ByteBuffer;

/**
 * @author kq
 * @date 2020-07-22 8:51
 * @since 2020-0630
 */
public class ByteBufferDemo {


    public static void main(String[] args) {
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

}
