package videoplayer.android.com.cnvideoplayer.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Date: 2018/9/20
 * Author:
 * Email：
 * Des：
 */

public class TimeUtils {
    /**
     * 日期格式：yyyy-MM-dd HH:mm:ss
     **/
    public static final String DF_YYYY_MM_DD_HH_MM_SS = "yyyy-MM-dd HH:mm:ss";

    /**
     * 日期格式：yyyy-MM-dd HH:mm
     **/
    public static final String DF_YYYY_MM_DD_HH_MM = "yyyy-MM-dd HH:mm";

    /**
     * 日期格式：yyyy-MM-dd
     **/
    public static final String DF_YYYY_MM_DD = "yyyy-MM-dd";


    /**
     * 日期格式：yyyy/MM/dd
     **/
    public static final String DF_YYYY_MM_DD1 = "yyyy/MM/dd";

    /**
     * 日期格式：HH:mm:ss
     **/
    public static final String DF_HH_MM_SS = "HH:mm:ss";

    /**
     * 日期格式：HH:mm
     **/
    public static final String DF_HH_MM = "HH:mm";

    /**
     * 获取当前时间
     *
     * @param timeFormat 时间格式，如：yyyy-MM-dd HH:mm:ss yyyy年MM月dd日 HH:mm:ss yyyy年MM月dd日
     *                   hh时mm分ss秒 yyyy-MM-dd EE HH-mm-ss MM-dd HH:mm:ss yyyy-MM-dd
     *                   HH-mm-ss-SSS …… 当传入的格式不正确 会抛出非法参数异常
     * @return
     */
    public static String getCurrentTime(String timeFormat) {
        Date date = new Date();
        SimpleDateFormat df = new SimpleDateFormat(timeFormat);
        String time = df.format(date);
        return time;
    }

    /**
     * 获取当前Date
     *
     * @param timeFormat 时间格式，如：yyyy-MM-dd HH:mm:ss yyyy年MM月dd日 HH:mm:ss yyyy年MM月dd日
     *                   hh时mm分ss秒 yyyy-MM-dd EE HH-mm-ss MM-dd HH:mm:ss yyyy-MM-dd
     *                   HH-mm-ss-SSS …… 当传入的格式不正确 会抛出非法参数异常
     * @return
     */
    public static Date getCurrentDate(String timeFormat) {
        return getDateTimeForStr(timeFormat, getCurrentTime(timeFormat));
    }


    /**
     * 将时间挫转换成时间
     *
     * @param longTime   需要转换的long类型的时间
     * @param timeFormat
     * @return
     */
    public static String getTimeForLongTime(String timeFormat, long longTime) {
        SimpleDateFormat df = new SimpleDateFormat(timeFormat);
        String time = df.format(longTime);
        return time;
    }

    /**
     * 将时间转换成时间挫
     *
     * @param timeFormat 要转换时间的格式，如：yyyy-MM-dd HH:mm
     * @param strDate    要转换的时间，格式必须是第一个参数，如："2016-5-10 21:08"
     * @return 转换后的long类型的时间，如果返回0，则转换失败
     */
    public static long getLongTimeForStr(String timeFormat, String strDate) {
        SimpleDateFormat sdf = new SimpleDateFormat(timeFormat);
        Date dt;
        try {
            dt = sdf.parse(strDate);
            return dt.getTime();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return 0L;
    }


    /**
     * 将时间转换成Date类型
     *
     * @param timeFormat 要转换时间的格式，如：yyyy-MM-dd HH:mm
     * @param strDate    要转换的时间，格式必须是第一个参数，如："2016-5-10 21:08"
     * @return 转换后的Date类型的时间，如果返回null，则转换失败
     */
    public static Date getDateTimeForStr(String timeFormat, String strDate) {
        SimpleDateFormat sdf = new SimpleDateFormat(timeFormat);
        // sdf.applyPattern(timeFormat);
        try {
            return sdf.parse(strDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }


    /**
     * 判断原日期是否在目标日期之前
     */
    public static boolean isBefore(Date src, Date dst) {
        return src.before(dst);
    }

    /**
     * 判断原日期是否在目标日期之后
     */
    public static boolean isAfter(Date src, Date dst) {
        return src.after(dst);
    }

}
