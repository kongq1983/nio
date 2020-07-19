package com.kq.nio.buffer;

import org.apache.commons.lang3.StringUtils;

import java.nio.CharBuffer;

public class CharBufferTest {

    public static void main(String[] args) {
        CharBuffer buffer = CharBuffer.allocate(20);
        String str = "welcome to you!";
        for (int i=0;i<str.length();i++) {
            String s = str.substring(i,i+1);
//            System.out.println(str.substring(i,i+1));
            buffer.put(s); //1个1个put放到buffer

        }
       buffer.flip(); // position设置为0

        while (buffer.hasRemaining()){ // position < limit
            char s = buffer.get(); //1个1个读出来
            System.out.println(s);
        }

        System.out.println("-----------------------------------------");
    }

}
