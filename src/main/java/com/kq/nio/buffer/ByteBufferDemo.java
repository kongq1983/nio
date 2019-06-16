package com.kq.nio.buffer;

import java.nio.ByteBuffer;

/**
 * ByteBufferDemo
 * 0<= mark <= position <= limit <= capacity
 * @author kq
 * @date 2019/6/17
 */
public class ByteBufferDemo {

    public static void main(String[] args) {
        byte[] array = new byte[] {1,2,3,4,5,6,7,8,9,10};

        ByteBuffer byteBuffer = ByteBuffer.wrap(array);

        //0
        System.out.println("position="+byteBuffer.position());
        //10
        System.out.println("limit="+byteBuffer.limit());
        //10
        System.out.println("capacity="+byteBuffer.capacity());

        byte[] readArray = new byte[5];
        byteBuffer.get(readArray);

        //5
        System.out.println("position="+byteBuffer.position());

//        limit = position;
//        position = 0;
//        mark = -1;
        byteBuffer.flip();

        //0
        System.out.println("position="+byteBuffer.position());
        //5
        System.out.println("limit="+byteBuffer.limit());


    }

}
