package crab.handler.impl;


import crab.Crab;
import crab.constant.CrabConst;
import crab.handler.HttpHandler;
import crab.http.HttpRequest;
import crab.http.HttpResponse;
import crab.http.HttpStatus;
import crab.kit.IOKit;
import crab.kit.PathKit;

import java.io.*;
import java.net.URLDecoder;
import java.util.List;
import java.util.stream.Stream;

/**
 * 基于磁盘静态文件实现的Handler
 */
public class AssetHandler implements HttpHandler {


    private File rootFile;

    private String rootPath;

    public AssetHandler(File rootFile) {
        this.rootFile = rootFile;
        this.rootPath = rootFile.getPath();
    }

    public AssetHandler(String rootPath) {
        this(new File(rootPath));
    }

    @Override
    public HttpResponse handle(HttpRequest request) {
        String uri = request.getUri();
        try {
            uri = URLDecoder.decode(uri, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            uri = uri.replace("%20", " ");
        }
        System.out.println("Request URL > " + uri);

        uri = PathKit.fixPath(uri);

        if (uri.equals("/")) {
            uri = "/" + CrabConst.DEFAULT_ROOTFILE;
        }
        //处理rootPath
        if (rootPath == null) {
            rootPath = rootFile.getAbsolutePath();
            if (rootPath.endsWith("/") || rootPath.endsWith(".")) {
                rootPath = rootPath.substring(0, rootPath.length() - 1);
            }
        }
        //初始化web服务器工作空间
        initWorkSpace(rootPath);


        File file = new File(rootFile, uri);

        if (file.exists() && !file.isDirectory()) {
            try {

                //返回此抽象路径名的规范路径名字符串
                String requestPath = file.getCanonicalPath();
                if (requestPath.endsWith("/")) {
                    requestPath = requestPath.substring(0, requestPath.length() - 1);
                }
                if (!requestPath.startsWith(rootPath)) {
                    return new HttpResponse().reason(HttpStatus.BAD_REQUEST, HttpStatus.BAD_REQUEST.toString());
                }

                HttpResponse res = new HttpResponse(HttpStatus.OK, new FileInputStream(file));
                res.setLength(file.length());

                return res;

            } catch (IOException e) {
                return new HttpResponse().reason(HttpStatus.INTERNAL_SERVER_ERROR, HttpStatus.INTERNAL_SERVER_ERROR.toString());
            }
        }
        if (!file.exists()) {
            return new HttpResponse().reason(HttpStatus.NOT_FOUND, HttpStatus.NOT_FOUND.toString());
        }
        return null;
    }

    private static void initWorkSpace(String rootPath) {
        System.out.println("Crab's WorkSpace is at :: " + rootPath);
        String resourcePath = "";
        if (Crab.isTest() == true) {
            resourcePath = Crab.class.getResource("/workspace").getPath();
            IOKit.copyFile(resourcePath, rootPath);
        } else {
            File rootfile = new File(rootPath);
            rootfile.mkdirs();
            List<InputStream> streamList = IOKit.getInputStreamList("/workspace/crab.gif", "/workspace/favicon.ico", "/workspace/index.html");
            IOKit.copyJarFile(streamList.get(0), new File(rootPath + "/crab.gif"));
            IOKit.copyJarFile(streamList.get(1), new File(rootPath + "/favicon.ico"));
            IOKit.copyJarFile(streamList.get(2), new File(rootPath + "/index.html"));
        }
    }

    public static void main(String[] args) {
        initWorkSpace("D:/teds");
    }

}
