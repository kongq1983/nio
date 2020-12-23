package com.kq.mmap;

import java.io.*;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;

/**
 * @author kq
 * @date 2020-12-22 18:08
 * @since 2020-0630
 */
public class TransToFileDemo {

    static String prefix = "c:";

    public static void main(String[] args) {
        int index = 1;
        if(args!=null && args.length>0) {
            try {
                index = Integer.parseInt(args[0]);
            }catch (Exception e) {
                e.printStackTrace();
            }
        }


        System.out.println("index="+index);
        long start = System.currentTimeMillis();
        fileTransfer(index);
        System.out.println("fileTransfer spent time : "+(System.currentTimeMillis()-start));

        start = System.currentTimeMillis();
        copy(index);
        System.out.println("copy spent time : "+(System.currentTimeMillis()-start));

        start = System.currentTimeMillis();
        mappedCopy(index);
        System.out.println("mappedCopy spent time : "+(System.currentTimeMillis()-start));


    }

    /**
     * 基于FileChannel transferTo transferFrom 方法文件复制
     */
    public static void fileTransfer(int index){

//        String prefix = "";

        for(int i=0;i<index;i++) {

            try (
                    RandomAccessFile afile = new RandomAccessFile(prefix+"/srcdemo/hello.txt", "rw");
                    RandomAccessFile bfile = new RandomAccessFile(prefix+"/todemo1/to"+i+".txt", "rw");
                    FileChannel ac = afile.getChannel();
                    FileChannel bc = bfile.getChannel()) {
                long position = 0;
                long count = ac.size();
                //            bc.transferFrom(ac, position, count);
                ac.transferTo(position, count, bc);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


    public static void copy(int index){

        for(int i=0;i<index;i++) {
            File file = new File(prefix+"/srcdemo/hello.txt");
            File writeFile = new File(prefix+"/todemo2/bio"+i+".txt");
            try (

                    BufferedReader bufferedReader = new BufferedReader(new FileReader(file));
                    BufferedWriter writer = new BufferedWriter(new FileWriter(writeFile));

            ) {
                String result = null;
                while((result=bufferedReader.readLine())!=null)  {
//                    System.out.println("result="+result);
                    writer.write(result);
                }
                writer.flush();
            } catch (Exception e) {
                e.printStackTrace();
            }

        }

//
    }


    /**
     * 基于MappedFileChannle的文件复制
     * 文件锁
     */
    public static void mappedCopy(int num){


        for(int index=0;index<num;index++) {
            RandomAccessFile afile = null;
            RandomAccessFile bfile = null;
            FileChannel fc = null;
            FileChannel fcb = null;
            try {
                afile = new RandomAccessFile(prefix + "/srcdemo/hello.txt", "rw");
                fc = afile.getChannel();
                long length = fc.size();
//            FileLock fileLock = fc.tryLock(0, length, true);//true共享锁 false 独占锁 从开始 锁定全部内容 如果获取不到锁会返回null
//            if(null != fileLock) {
                MappedByteBuffer mbb = fc.map(FileChannel.MapMode.READ_WRITE, 0, length);
                byte[] fbo = new byte[(int) length];
                mbb.get(fbo);
//                System.out.println(new String(fbo, "UTF-8"));
                bfile = new RandomAccessFile(prefix + "/todemo3/mapped"+index+".txt", "rw");
                fcb = bfile.getChannel();
                MappedByteBuffer mbbb = fcb.map(FileChannel.MapMode.READ_WRITE, 0, length);

                for (int i = 0; i < length; i++) {
                    mbbb.put(i, fbo[i]);
                }
                mbbb.flip();
                mbbb.force();
//            }
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                try {
                    fc.close();
                    fcb.close();
                    afile.close();
                    bfile.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

}
