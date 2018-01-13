package crab.http;



import lombok.Getter;
import lombok.Setter;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

@Getter
@Setter
public class HttpCookie {

    private final static String RFC1123_PATTERN = "EEE,dd MMM yyy HH:mm:ss z";

    private final static TimeZone GMT_ZONE = TimeZone.getTimeZone("GMT");

    private static DateFormat RFC_DATEFORMAT = new SimpleDateFormat(RFC1123_PATTERN);

    static {
        RFC_DATEFORMAT.setTimeZone(GMT_ZONE);
    }

    private String name;

    private String value;
    //过期时间
    private long expireTime = -1;

    private String path;

    private String domain;

    private boolean secure;

    public HttpCookie(String name,String value){
        this.name = name;
        this.value = value;
    }

    public boolean hasExpired(){
        return expireTime > System.currentTimeMillis();
    }

    public boolean isSecure(){
        return secure;
    }

    public String toHeader(){
        StringBuilder header = new StringBuilder();
        header.append(name).append('=').append(value);
        if (domain != null){
            header.append("; ").append("Domain=").append(domain);
        }
        if (path != null){
            header.append("; ").append("Path=").append(path);
        }
        if (expireTime > -1){
            header.append("; ").append("Expires=").append(RFC_DATEFORMAT.format(new Date(expireTime)));
        }
        if (secure){
            header.append("; ").append("Secure");
        }
        return header.toString();
    }

    @Override
    public String toString() {
        return name + '=' + value;
    }
}
