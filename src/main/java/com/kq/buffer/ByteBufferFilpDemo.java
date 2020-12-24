package com.kq.buffer;

import com.kq.util.ByteBufferUtil;

import java.nio.ByteBuffer;

/**
 * @author kq
 * @date 2020-12-23 19:07
 * @since 2020-0630
 */
public class ByteBufferFilpDemo {


    public static void main(String[] args) throws Exception{
        ByteBuffer byteBuffer = ByteBuffer.allocate(20);
        byteBuffer.put("12345".getBytes());

        resetByteBuffer(byteBuffer, 10);
        // 值不能超过limit
        byteBuffer.put("789ab".getBytes());

        System.out.println(ByteBufferUtil.toString(byteBuffer));

    }

    /**
     * position=0
     * @param byteBuffer
     * @param limit
     */
    private static void resetByteBuffer(final ByteBuffer byteBuffer, final int limit) {
        byteBuffer.flip(); // position=0
        byteBuffer.limit(limit);
    }

}
