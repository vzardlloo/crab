package crab.http;

import crab.kit.HttpKit;
import crab.kit.PlatformKit;
import lombok.Getter;
import lombok.Setter;

import java.util.*;

/**
 * http请求对象
 * 主要就是根据请求url结合http协议组装一个httpRequest
 * @author vzard
 */
@Getter
@Setter
public class HttpRequest {

    private String raw;
    private String uri;
    private HttpMethod method;
    private HttpSession session;
    private String queryString;
    private String data;
    private Map<String,Object> getData;
    private Map<String,Object> postData;
    private Map<String,HttpCookie> cookies;

    private Map<String,String> headers = new HashMap<>();

    public HttpRequest(HttpSession session){
        this.session = session;
        parse();
    }

    //parse the request string
    private void parse(){

        this.raw = session.line();

        //parse the first line
        StringTokenizer tokenizer = new StringTokenizer(raw);
        String methodName = tokenizer.nextToken().toUpperCase();
        method = HttpMethod.valueOf(methodName);

        String uri = tokenizer.nextToken();
        this.uri = uri;

        int questionIdx = uri.indexOf('?');
        if (questionIdx != -1){
            String queryString = uri.substring(questionIdx+1);
            this.setQueryString(queryString);
            this.setGetData(HttpKit.parseData(queryString));

            uri = uri.substring(0,questionIdx);
            this.setUri(uri);
        }

        //parse the headers

        String[] lines = raw.split("\r\n");
            //start with index 1
            for (int i = 1; i < lines.length; i++) {
                String[] keyVal = lines[i].split(":",2);
                headers.put(keyVal[0],keyVal[1]);
            }


        //set cookies
        if (headers.containsKey(HttpHeader.COOKIE)){
            List<HttpCookie> cookies = new LinkedList<>();
            StringTokenizer token = new StringTokenizer(headers.get(HttpHeader.COOKIE),";");
            while (token.hasMoreTokens()){
                String tok = token.nextToken();
                int eqIdx = tok.indexOf('=');
                if(eqIdx == -1){
                    // invalid cookie
                    continue;
                }
                String key = tok.substring(0,eqIdx);
                String value = tok.substring(eqIdx+1);

                cookies.add(new HttpCookie(key,value));
            }
        }
    }

    public void setCookies(List<HttpCookie> cookies){
        Map<String, HttpCookie> map = new HashMap<>();
        for (HttpCookie cookie : cookies){
            map.put(cookie.getName(),cookie);
        }
        this.cookies = map;
    }

    @Override
    protected void finalize(){
        if (postData != null){
            for (Object value : postData.values()){
                if (value instanceof HttpFileUpload){
                    HttpFileUpload u = (HttpFileUpload) value;
                    //when can't delete the file normally,delete it when exit JVM
                    if (!u.getTempFile().delete()){
                        u.getTempFile().deleteOnExit();
                    }
                }
            }
        }
    }
}
