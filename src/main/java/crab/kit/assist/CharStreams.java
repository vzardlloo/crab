package crab.kit.assist;


import crab.kit.AssertKit;

import java.io.IOException;
import java.nio.CharBuffer;

public class CharStreams {

    public static long copy(Readable from, Appendable to) throws IOException {
        AssertKit.checkNotNull(from);
        AssertKit.checkNotNull(to);
        CharBuffer buffer = CharBuffer.allocate(2048);
        long total = 0L;
        while (from.read(buffer) != -1) {
            buffer.flip();
            to.append(buffer);
            total += (long) buffer.remaining();
            buffer.clear();
        }
        return total;
    }

    public static StringBuilder toStringBuilder(Readable readable) throws IOException {

        StringBuilder sb = new StringBuilder();
        copy(readable, sb);
        return sb;
    }

    public static String toString(Readable readable) throws IOException {
        return toStringBuilder(readable).toString();
    }


}
