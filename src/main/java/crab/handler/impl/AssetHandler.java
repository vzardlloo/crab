package crab.handler.impl;


import crab.constant.CrabConst;
import crab.handler.HttpHandler;
import crab.http.HttpRequest;
import crab.http.HttpResponse;
import crab.http.HttpStatus;
import crab.kit.PathKit;


import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;

/**
 * 基于磁盘静态文件实现的Handler
 */
public class AssetHandler implements HttpHandler{



    private File rootFile;

    private String rootPath;

    public AssetHandler(File rootFile){
        this.rootFile = rootFile;
        this.rootPath = rootFile.getPath();
    }

    public AssetHandler(String rootPath){
        this(new File(rootPath));
    }

    @Override
    public HttpResponse handle(HttpRequest request) {
        String uri = request.getUri();
        try {
            uri = URLDecoder.decode(uri,"UTF-8");
        }catch (UnsupportedEncodingException e){
            uri = uri.replace("%20"," ");
        }
        System.out.println("Request URL > " + uri);

        uri = PathKit.fixPath(uri);

        if (uri.equals("/")){
            uri = "/" + CrabConst.DEFAULT_ROOTFILE;
        }

        File file = new File(rootFile,uri);
        if (file.exists() && !file.isDirectory()){
            try {
                if (rootPath == null){
                    rootPath = rootFile.getAbsolutePath();
                    if (rootPath.endsWith("/")  || rootPath.endsWith(".")){
                        rootPath = rootPath.substring(0,rootPath.length() -1);
                    }
                }
                //返回此抽象路径名的规范路径名字符串
                String requestPath = file.getCanonicalPath();
                if (requestPath.endsWith("/")){
                    requestPath = requestPath.substring(0,requestPath.length() - 1);
                }
                if (!requestPath.startsWith(rootPath)){
                    return new HttpResponse().reason(HttpStatus.BAD_REQUEST,HttpStatus.BAD_REQUEST.toString());
                }

                HttpResponse res = new HttpResponse(HttpStatus.OK,new FileInputStream(file));
                res.setLength(file.length());

                return res;

            }catch (IOException e){
                return new HttpResponse().reason(HttpStatus.INTERNAL_SERVER_ERROR,HttpStatus.INTERNAL_SERVER_ERROR.toString());
            }
        }
        return null;
    }
}
