package com.kq.file;

import java.io.Closeable;

public class AutoCloseDemo {

    static class DbReader implements AutoCloseable {
        @Override
        public void close() throws Exception {
            System.out.println("DbReader Auto Close .");
        }

        public void open(){
            System.out.println("DbReader open it.");
        }

    }


    public static void main(String[] args) {

        try(
                DbReader reader = new DbReader();
        ) {
            reader.open();
        }catch (Exception e) {
            e.printStackTrace();
        }

    }

}
