package com.kq.test;

import java.nio.channels.SelectionKey;

/**
 * @author kq
 * @date 2020-08-11 11:38
 * @since 2020-0630
 */
public class SelectionKeyTest {

    public static void main(String[] args) {
        System.out.println("OP_READ="+SelectionKey.OP_READ);
        System.out.println("OP_ACCEPT="+SelectionKey.OP_ACCEPT);
        System.out.println("OP_WRITE="+SelectionKey.OP_WRITE);
        System.out.println("OP_CONNECT="+SelectionKey.OP_CONNECT);

    }

}
