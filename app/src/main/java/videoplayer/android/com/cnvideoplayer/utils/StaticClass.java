package videoplayer.android.com.cnvideoplayer.utils;

/**
 * Date: 2018/8/23
 * Author:
 * Email：
 * Des：
 */

public class StaticClass {
    //http://119.23.27.57     //http://mikanmovie.info/

    //   public static final String defaultIP="2yi2646026.51mypc.cn";//   /mobile
    //  public static final String defaultIP="119.23.27.57/video";
   //  public static final String defaultIP="kc6666.net";
     public static String defaultIP="kc6666.net:8080";
    // public static final String defaultIP = "mikanmovie.info";

    public static void init( String portIP) {
       defaultIP=portIP;
    }

    public static String domain = "http://" + defaultIP + "/mobile/user"; //域名
    public static String domain1 = "http://" + defaultIP + "/mobile";
    //http://119.23.27.57/video/mobile/user/login.html
    //http://serpro/mobile/user/login.html

    public static String getURL1(String ip) {
        return "http://" + ip + "/mobile/user";
    }

    public static String getURL2(String ip) {
        return "http://" + ip + "/mobile";
    }
}
