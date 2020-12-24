package com.kq.file.file;

import com.kq.util.FileUtil;

import java.io.IOException;
import java.io.RandomAccessFile;

/**
 * @author kq
 * @date 2020-12-24 10:04
 * @since 2020-0630
 */
public class RandomAccessFileAppend {

    public static void main(String[] args) throws Exception{

        String path = "D:\\myprotocol";
        FileUtil.ensureDirOK(path);

        // 只替换前2行
        try (RandomAccessFile raf  = new RandomAccessFile(path+"\\demo1.txt","rw")){

            raf.write("first line\r\n".getBytes());
            raf.write("second line\r\n".getBytes());
        }catch (IOException e) {
            e.printStackTrace();
        }

        // 从最后1行开始追加
        try (RandomAccessFile raf  = new RandomAccessFile(path+"\\demo1.txt","rw")){
            //指向文件最后
            raf.seek(raf.length());
            raf.write("last line\r\n".getBytes());
        }catch (IOException e) {
            e.printStackTrace();
        }

    }

}
