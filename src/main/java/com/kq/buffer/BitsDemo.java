package com.kq.buffer;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

/**
 * @author kq
 * @date 2020-07-22 9:55
 * @since 2020-0630
 */
public class BitsDemo {

    public static void main(String[] args) {
        // 0x01020304  十进制:100 401 404
        int num = 100401404;

        System.out.println("二进制="+Integer.toBinaryString(num));

        ByteBuffer byteBuffer = ByteBuffer.allocate(100);
//        byteBuffer.order(ByteOrder.BIG_ENDIAN);
//        byteBuffer.order(ByteOrder.LITTLE_ENDIAN);
        byteBuffer.putInt(num);

        System.out.println(byteBuffer);

        System.out.println((short) 0xdabb);

    }

}
