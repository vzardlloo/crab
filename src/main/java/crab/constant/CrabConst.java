package crab.constant;


import java.nio.charset.Charset;
import java.nio.charset.CharsetEncoder;

public class CrabConst {

    /**
     * 默认编码
     */
    public static final Charset CHARSET = Charset.forName("UTF-8");

    /**
     * 编码器
     */
    public static final CharsetEncoder ENCODER = CHARSET.newEncoder();

    /**
     * 默认主页文件
     */
    public static String DEFAULT_ROOTFILE = "index.html";


}
