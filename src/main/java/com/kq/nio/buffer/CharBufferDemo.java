package com.kq.nio.buffer;

import java.nio.CharBuffer;

/**
 * CharBufferDemo
 *
 * @author kq
 * @date 2019/6/17
 */
public class CharBufferDemo {

    public static void main(String[] args) {
        char[] array = new char[]{'a','b','c','d','e'};
        CharBuffer buffer = CharBuffer.wrap(array);

        // 5 5
        System.out.println("A capacity="+buffer.capacity()+" limit="+buffer.limit());

        buffer.limit(3);
        System.out.println();
        // 5 3
        System.out.println("B capacity="+buffer.capacity()+" limit="+buffer.limit()+" position="+buffer.position());

        buffer.put(0,'o');
        buffer.put(1,'p');
        buffer.put(2,'q');
        buffer.put(3,'r');  //  此位置是不可读 不可写  上面设置了limit(3) 这里会报错
        buffer.put(4,'s');
        buffer.put(5,'t');
        buffer.put(6,'u');

    }

}
