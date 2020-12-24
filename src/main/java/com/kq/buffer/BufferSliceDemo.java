package com.kq.buffer;

import java.nio.ByteBuffer;

public class BufferSliceDemo {

    public static void main1(String[] args) {
        byte[] bytes = {1,2,3,4,5,6,7,8};

        ByteBuffer byteBuffer = ByteBuffer.wrap(bytes);
        byteBuffer.position(5);
        // 底层还是同个byte数组
        ByteBuffer byteBuffer1 = byteBuffer.slice();

        System.out.println("byteBuffer.position="+byteBuffer.position()+",byteBuffer.catacity="+byteBuffer.position()+",byteBuffer.limit="+byteBuffer.limit());
        System.out.println("byteBuffer1.position="+byteBuffer1.position()+",byteBuffer1.catacity="+byteBuffer1.position()+",byteBuffer1.limit="+byteBuffer1.limit());

        byteBuffer1.put(0,(byte)111);

        byte[] b1 = byteBuffer.array();
        byte[] b2 = byteBuffer1.array();

        for(int i=0;i<b1.length;i++){
            System.out.print(b1[i]+" ");
        }
        System.out.println();

        for(int i=0;i<b2.length;i++){
            System.out.print(b2[i]+" ");
        }
    }

    public static void main(String[] args) {
        byte[] bytes = {1,2,3,4,5,6,7,8};
        ByteBuffer byteBuffer = ByteBuffer.wrap(bytes);
        byteBuffer.position(5);
        //position=0  limit or capacity = (limit-position)
        ByteBuffer byteBuffer1 = byteBuffer.slice();
        byteBuffer1.put((byte)66);
        System.out.println("byteBuffer.position="+byteBuffer.position()+",byteBuffer.catacity="+byteBuffer.position()+",byteBuffer.limit="+byteBuffer.limit());
        System.out.println("byteBuffer1.position="+byteBuffer1.position()+",byteBuffer1.catacity="+byteBuffer1.position()+",byteBuffer1.limit="+byteBuffer1.limit());


        for(int i=0;i<byteBuffer.array().length;i++){
            System.out.print(byteBuffer.array()[i]+" ");
        }
    }

}
