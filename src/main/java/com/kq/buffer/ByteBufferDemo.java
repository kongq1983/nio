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

    }

}
