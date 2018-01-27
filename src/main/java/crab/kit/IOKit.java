package crab.kit;


import crab.kit.assist.ByteBufferBucket;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
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

    public static void copyFile(String source, String target) {
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
