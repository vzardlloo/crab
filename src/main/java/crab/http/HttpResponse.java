package crab.http;

import blade.kit.IOKit;
import blade.kit.StringKit;
import blade.kit.io.CharStreams;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * http响应对象
 * @author vzard
 */
public class HttpResponse {

    private String version = "HTTP/1.1";
    private HttpStatus status = HttpStatus.OK;
    private String reason = "";
    private Map<String,String> headers = new LinkedHashMap<>();
    private InputStream response;
    private String body;
    private long length = 0;

    public HttpResponse(){

    }

    public HttpResponse(HttpStatus status,String body){
        this.status = status;
        this.body = body;
        this.length = body.length();
    }

    public HttpResponse(HttpStatus status,InputStream inputStream){
        this.status = status;
        this.response = inputStream;
        try {
            this.length = inputStream.available();
        }catch (IOException e){
            e.printStackTrace();
        }
    }

    public void addDefaultHeaders(){
        headers.put(HttpHeader.DATE,new Date().toString());
        headers.put(HttpHeader.SERVER,"crab");
        headers.put(HttpHeader.CONTENT_LENGTH,length+"");
        headers.put(HttpHeader.CONNECTION,"close");
    }

    public long getLength(){
        return length;
    }

    public void setLength(long length){
        this.length = length;
    }

    public HttpStatus getStatus() {
        return status;
    }

    public int getStatusCode() {
        return status.getCode();
    }

    public void setStatus(HttpStatus status) {
        this.status = status;
    }

    public String getHeader(String header) {
        return headers.get(header);
    }


    public String getReason() {
        return reason;
    }

    public HttpResponse reason(String reason) {
        this.reason = reason;
        return this;
    }

    public HttpResponse reason(HttpStatus httpStatus, String reason) {
        this.status = httpStatus;
        this.reason = reason;
        return this;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public void setHeader(String key, String value) {
        headers.put(key, value);
    }

    public String getVersion() {
        return version;
    }

    public Map<String, String> getHeaders() {
        return headers;
    }

    public InputStream getResponse() {
        return response;
    }

    public byte[] bytes(){
        try {
            if (StringKit.isNotBlank(body)){
                return body.getBytes("UTF-8");
            }
        }catch (UnsupportedEncodingException e){
            e.printStackTrace();
        }

        if (null != response){
            try {
                return IOKit.toByteArray(response);
            }catch (IOException e){

            }
        }
        return null;
    }

    public String body(){
        if (StringKit.isNotBlank(body)){
            return body;
        }
        if (null != response){
            try {
                return CharStreams.toString(new InputStreamReader(response,"UTF-8"));
            }catch (IOException e){
                e.printStackTrace();
            }
        }
        return null;
    }



}
