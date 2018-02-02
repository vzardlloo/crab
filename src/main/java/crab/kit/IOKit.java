package crab.kit;


import crab.kit.assist.ByteBufferBucket;

import java.io.*;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

/**
 * IO工具类
 *
 * @author vzard
 */
public class IOKit {

    public static byte[] toByteArray(InputStream input) throws IOException {
        ByteBufferBucket os = new ByteBufferBucket();
        byte[] buf = new byte[1024];
        for (int n = input.read(buf); n != -1; n = input.read(buf)) {
            os.append(buf);
        }

        return os.toArray();
    }

    /**
     * 复制文件及文件夹
     *
     * @param src 源文件path
     * @param tar 目标文件path
     */
    public static void copyFile(String src, String tar) {
        File source = new File(src);
        File target = new File(tar);
        copyFile(source, target);
    }

    /**
     * 复制文件及文件夹
     *
     * @param source 源文件
     * @param target 目标文件
     */
    public static void copyFile(File source, File target) {
        if (source.isDirectory()) {
            if (!target.exists()) {
                target.mkdir();
            }
            String[] fileList = source.list();
            for (String file : fileList) {
                File srcFile = new File(source, file);
                File tarFile = new File(target, file);
                copyFile(srcFile.getPath(), tarFile.getPath());
            }
        } else {
            try {

                FileInputStream inputStream = new FileInputStream(source);
                FileChannel in = inputStream.getChannel();
                FileOutputStream outputStream = new FileOutputStream(target);
                FileChannel out = outputStream.getChannel();

                ByteBuffer buffer = ByteBuffer.allocate(16);
                while (in.read(buffer) != -1) {
                    buffer.flip();
                    out.write(buffer);
                    buffer.clear();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

}
