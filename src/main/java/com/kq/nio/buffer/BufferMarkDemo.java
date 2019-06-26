package com.kq.nio.buffer;

import java.nio.ByteBuffer;

/**
 * BufferMarkDemo
 *
 * @author kq
 * @date 2019/6/25
 */
public class BufferMarkDemo {

    public static void main(String[] args) {
        byte[] bytes = new byte[] {1,2,3};
        ByteBuffer byteBuffer = ByteBuffer.wrap(bytes);

        System.out.println("capacity="+byteBuffer.capacity());
        // position=0
        System.out.println("position="+byteBuffer.position());
        System.out.println();

        byteBuffer.position(1);
        byteBuffer.mark();  //在位置1 设置mark
        // position=1
        System.out.println("position="+byteBuffer.position());

        byteBuffer.position(2);  //改变位置

        byteBuffer.reset();  //位置重置

        System.out.println();
        // position=1
        System.out.println("position="+byteBuffer.position());


    }

}
