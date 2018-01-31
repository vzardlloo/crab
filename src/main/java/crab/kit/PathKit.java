package crab.kit;


/**
 * 路径工具类
 */
public class PathKit {

    /**
     * 对路径输入进行容错处理
     * @param path 路径
     * @return string
     */
    public static String fixPath(String path){
        if (path == null){
            return "/";
        }
        if (!path.startsWith("/")){
            path = "/" + path;
        }
        if (path.length() > 1 && path.endsWith("/")){
            path = path.substring(0,path.length() - 1);
        }
        //清除多余的“/”,比如www.google.com//search//test ==> www.google.com/search/test
        return path.replaceAll("[/]+","/");
    }






}
