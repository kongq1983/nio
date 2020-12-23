package com.kq.file;

import java.io.File;

/**
 * 創建目錄
 * @author kq
 * @date 2020-12-23 10:37
 * @since 2020-0630
 */
public class FileDemo {

    public static void main(String[] args) {
        String path = "c:\\a\\b\\c\\d";
        ensureDirOK(path);
    }

    public static void ensureDirOK(final String dirName) {
        if (dirName != null) {
            File f = new File(dirName);
            if (!f.exists()) {
                boolean result = f.mkdirs();
                System.out.println(dirName + " mkdir " + (result ? "OK" : "Failed"));
            }
        }
    }

}
