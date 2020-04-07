package videoplayer.android.com.cnvideoplayer.utils;
import android.util.Log;

/**
 * 描述：    Log封装类
 */
public class LogUtil {

    public static final boolean DEBUG = true;

    public static final String TAG = "LogUtil";

    public static void d(String text){
        if (DEBUG){
           d(TAG,text);
        }
    }

    public static void i(String text){
        if (DEBUG){
            i(TAG,text);
        }
    }

    public static void w(String text){
        if (DEBUG){
            w(TAG,text);
        }
    }

    public static void e(String text){
        if (DEBUG){
           e(TAG,text);
        }
    }
    /**
     * 截断输出日志
     * @param msg
     */
    public static void e(String tag, String msg) {
        if (tag == null || tag.length() == 0
                || msg == null || msg.length() == 0)
            return;

        int segmentSize = 3 * 1024;
        long length = msg.length();
        if (length <= segmentSize ) {// 长度小于等于限制直接打印
            Log.e(tag, msg);
        }else {
            while (msg.length() > segmentSize ) {// 循环分段打印日志
                String logContent = msg.substring(0, segmentSize );
                msg = msg.replace(logContent, "");
                Log.e(tag, logContent);
            }
            Log.e(tag, msg);// 打印剩余日志
        }
    }

    /**
     * 截断输出日志
     * @param msg
     */
    public static void i(String tag, String msg) {
        if (tag == null || tag.length() == 0
                || msg == null || msg.length() == 0)
            return;

        int segmentSize = 2 * 1024;
        long length = msg.length();
        if (length <= segmentSize ) {// 长度小于等于限制直接打印
            Log.i(tag, msg);
        }else {
            while (msg.length() > segmentSize ) {// 循环分段打印日志
                String logContent = msg.substring(0, segmentSize );
                msg = msg.replace(logContent, "-----");
                Log.i(tag, logContent);
            }
            Log.i(tag, msg);// 打印剩余日志
        }
    }

    /**
     * 截断输出日志
     * @param msg
     */
    public static void d(String tag, String msg) {
        if (tag == null || tag.length() == 0
                || msg == null || msg.length() == 0)
            return;

        int segmentSize = 3 * 1024;
        long length = msg.length();
        if (length <= segmentSize ) {// 长度小于等于限制直接打印
            Log.d(tag, msg);
        }else {
            while (msg.length() > segmentSize ) {// 循环分段打印日志
                String logContent = msg.substring(0, segmentSize );
                msg = msg.replace(logContent, "");
                Log.d(tag, logContent);
            }
            Log.d(tag, msg);// 打印剩余日志
        }
    }


    /**
     * 截断输出日志
     * @param msg
     */
    public static void w(String tag, String msg) {
        if (tag == null || tag.length() == 0
                || msg == null || msg.length() == 0)
            return;

        int segmentSize = 3 * 1024;
        long length = msg.length();
        if (length <= segmentSize ) {// 长度小于等于限制直接打印
            Log.w(tag, msg);
        }else {
            while (msg.length() > segmentSize ) {// 循环分段打印日志
                String logContent = msg.substring(0, segmentSize );
                msg = msg.replace(logContent, "");
                Log.w(tag, logContent);
            }
            Log.w(tag, msg);// 打印剩余日志
        }
    }

}
