package crab.kit;


public class PlatformKit {

    private static String getPlatformName(){
        return System.getProperty("os.name");
    }

    public static Boolean isTargetPlatfrom(String name){
        String platfromName = getPlatformName().toLowerCase();
        if (platfromName.startsWith(name)){
            return true;
        }
        return false;
    }

    public static Boolean isWindow(){
        return isTargetPlatfrom("windows");
    }

    public static Boolean isLinux(){
        return isTargetPlatfrom("linux");
    }

    public static Boolean isMac(){
        return isTargetPlatfrom("mac");
    }



}
