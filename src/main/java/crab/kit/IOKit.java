package crab.kit;


import crab.kit.assist.ByteBufferBucket;

import java.io.IOException;
import java.io.InputStream;

public class IOKit {

    public static byte[] toByteArray(InputStream input) throws IOException {


        ByteBufferBucket os = new ByteBufferBucket();
        byte[] buf = new byte[1024];
        for (int n = input.read(buf); n != -1; n = input.read(buf)) {
            os.append(buf);
        }

        return os.toArray();
    }


}
