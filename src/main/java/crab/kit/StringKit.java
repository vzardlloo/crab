package crab.kit;


import crab.kit.assist.CharStreams;

import java.io.IOException;

public class StringKit {

    /**
     * 判断一个字符串是否是由空格组成，不是则返回true
     *
     * @param str
     * @return
     */
    public static boolean isNotBlank(String str) {
        if (str != null && str.length() != 0) {
            for (int i = 0; i < str.length(); i++) {
                if (!Character.isWhitespace(str.charAt(i))) {
                    return true;
                }
            }
            return false;
        } else {
            return false;
        }
    }

    public static String toString(Readable r) throws IOException {
        return CharStreams.toString(r);
    }


}
