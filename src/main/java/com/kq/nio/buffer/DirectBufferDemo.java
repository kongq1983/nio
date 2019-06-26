package com.kq.nio.buffer;

import java.nio.ByteBuffer;

/**
 * DirectBufferDemo
 *
 * @author kq
 * @date 2019/6/25
 */
public class DirectBufferDemo {

    public static void main(String[] args) {
        ByteBuffer byteBuffer = ByteBuffer.allocateDirect(100);
        ByteBuffer byteBuffer1 = ByteBuffer.allocate(100);
        // TRUE
        System.out.println("byteBuffer isDirect = "+byteBuffer.isDirect());
        // FALSE
        System.out.println("byteBuffer1 isDirect = "+byteBuffer1.isDirect());
    }

}
