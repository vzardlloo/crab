package crab.kit;


import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.HashMap;
import java.util.Map;
import java.util.StringTokenizer;

public class HttpKit {

    /**
     * 解析请求中的数据
     *
     * @param data 请求中的数据
     * @return 名称：对象的Map
     */
    public static Map<String,Object> parseData(String data){
        Map<String,Object> ret = new HashMap<>();
        String[] split = data.split("&");
        for (String s : split){
            int idx = s.indexOf('=');
            try {
                if (idx != -1){
                    ret.put(URLDecoder.decode(s.substring(0,idx),"UTF-8"),URLDecoder.decode(s.substring(idx + 1),"UTF-8"));
                }else {
                    ret.put(URLDecoder.decode(s,"UTF-8"),"ture");
                }
            }catch (UnsupportedEncodingException e){
                e.printStackTrace();
            }
        }

        return ret;
    }


    /**
     * 将字符串以‘-’为分隔符格式化为首字母大写
     * 例如：<p>abc-def-ghj ==> Abc-Def-Ghj</p>
     * @param header 字符串
     * @return 处理后的字符串
     */
    public static String capitalizeHeader(String header){
        StringTokenizer st  = new StringTokenizer(header,"-");
        StringBuilder out = new StringBuilder();
        while (st.hasMoreTokens()){
            String l = st.nextToken();
            out.append(Character.toUpperCase(l.charAt(0)));
            if (l.length() > 1){
                out.append(l.substring(1));
            }
            if (st.hasMoreTokens()){
                out.append('-');
            }
        }
        return out.toString();
    }






}
