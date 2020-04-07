package videoplayer.android.com.cnvideoplayer.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.util.Collection;
import java.util.Iterator;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Date: 2018/8/15
 * Author:
 * Email：
 * Des：
 */

public class StringUtil {
    public StringUtil() {
    }

    public static String parseEmpty(String str) {
        if(str == null || "null".equals(str.trim())) {
            str = "";
        }

        return str.trim();
    }

    public static boolean isEmpty(String str) {
        return str == null || str.trim().length() == 0;
    }

    public static boolean isNotEmpty(String str) {
        return !isEmpty(str);
    }

    public static int chineseLength(String str) {
        int valueLength = 0;
        String chinese = "[Α-￥]";
        if(!isEmpty(str)) {
            for(int i = 0; i < str.length(); ++i) {
                String temp = str.substring(i, i + 1);
                if(temp.matches(chinese)) {
                    valueLength += 2;
                }
            }
        }

        return valueLength;
    }

    public static int strLength(String str) {
        int valueLength = 0;
        String chinese = "[Α-￥]";
        if(!isEmpty(str)) {
            for(int i = 0; i < str.length(); ++i) {
                String temp = str.substring(i, i + 1);
                if(temp.matches(chinese)) {
                    valueLength += 2;
                } else {
                    ++valueLength;
                }
            }
        }

        return valueLength;
    }

    public static int subStringLength(String str, int maxL) {
        int currentIndex = 0;
        int valueLength = 0;
        String chinese = "[Α-￥]";

        for(int i = 0; i < str.length(); ++i) {
            String temp = str.substring(i, i + 1);
            if(temp.matches(chinese)) {
                valueLength += 2;
            } else {
                ++valueLength;
            }

            if(valueLength >= maxL) {
                currentIndex = i;
                break;
            }
        }

        return currentIndex;
    }

    public static Boolean isMobileNo(String str) {
        Boolean isMobileNo = Boolean.valueOf(false);

        try {
            Pattern p = Pattern.compile("^((13[0-9])|(15[^4,\\D])|(18[0,5-9])|(17[0,5-9]))\\d{8}$");
            Matcher m = p.matcher(str);
            isMobileNo = Boolean.valueOf(m.matches());
        } catch (Exception var4) {
            var4.printStackTrace();
        }

        return isMobileNo;
    }

    public static Boolean isNumberLetter(String str) {
        Boolean isNoLetter = Boolean.valueOf(false);
        String expr = "^[A-Za-z0-9]+$";
        if(str.matches(expr)) {
            isNoLetter = Boolean.valueOf(true);
        }

        return isNoLetter;
    }

    public static Boolean isNumber(String str) {
        Boolean isNumber = Boolean.valueOf(false);
        String expr = "^[0-9]+$";
        if(str.matches(expr)) {
            isNumber = Boolean.valueOf(true);
        }

        return isNumber;
    }

    public static Boolean isEmail(String str) {
        Boolean isEmail = Boolean.valueOf(false);
        String expr = "^([a-z0-9A-Z]+[-|\\.]?)+[a-z0-9A-Z]@([a-z0-9A-Z]+(-[a-z0-9A-Z]+)?\\.)+[a-zA-Z]{2,}$";
        if(str.matches(expr)) {
            isEmail = Boolean.valueOf(true);
        }

        return isEmail;
    }

    public static Boolean isChinese(String str) {
        Boolean isChinese = Boolean.valueOf(true);
        String chinese = "[Α-￥]";
        if(!isEmpty(str)) {
            for(int i = 0; i < str.length(); ++i) {
                String temp = str.substring(i, i + 1);
                if(!temp.matches(chinese)) {
                    isChinese = Boolean.valueOf(false);
                }
            }
        }

        return isChinese;
    }

    public static Boolean isContainChinese(String str) {
        Boolean isChinese = Boolean.valueOf(false);
        String chinese = "[Α-￥]";
        if(!isEmpty(str)) {
            for(int i = 0; i < str.length(); ++i) {
                String temp = str.substring(i, i + 1);
                if(temp.matches(chinese)) {
                    isChinese = Boolean.valueOf(true);
                }
            }
        }

        return isChinese;
    }

    public static String convertStreamToString(InputStream is) {
        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        StringBuilder sb = new StringBuilder();
        String line = null;

        try {
            while((line = reader.readLine()) != null) {
                sb.append(line + "\n");
            }

            if(sb.indexOf("\n") != -1 && sb.lastIndexOf("\n") == sb.length() - 1) {
                sb.delete(sb.lastIndexOf("\n"), sb.lastIndexOf("\n") + 1);
            }
        } catch (IOException var13) {
            var13.printStackTrace();
        } finally {
            try {
                is.close();
            } catch (IOException var12) {
                var12.printStackTrace();
            }

        }

        return sb.toString();
    }

    public static String dateTimeFormat(String dateTime) {
        StringBuilder sb = new StringBuilder();

        try {
            if(isEmpty(dateTime)) {
                return null;
            }

            String[] dateAndTime = dateTime.split(" ");
            if(dateAndTime.length > 0) {
                String[] var3 = dateAndTime;
                int var4 = dateAndTime.length;

                for(int var5 = 0; var5 < var4; ++var5) {
                    String str = var3[var5];
                    String[] date;
                    int i;
                    String str1;
                    if(str.indexOf("-") != -1) {
                        date = str.split("-");

                        for(i = 0; i < date.length; ++i) {
                            str1 = date[i];
                            sb.append(strFormat2(str1));
                            if(i < date.length - 1) {
                                sb.append("-");
                            }
                        }
                    } else if(str.indexOf(":") != -1) {
                        sb.append(" ");
                        date = str.split(":");

                        for(i = 0; i < date.length; ++i) {
                            str1 = date[i];
                            sb.append(strFormat2(str1));
                            if(i < date.length - 1) {
                                sb.append(":");
                            }
                        }
                    }
                }
            }
        } catch (Exception var10) {
            var10.printStackTrace();
            return null;
        }

        return sb.toString();
    }

    public static String strFormat2(String str) {
        try {
            if(str.length() <= 1) {
                str = "0" + str;
            }
        } catch (Exception var2) {
            var2.printStackTrace();
        }

        return str;
    }

    public static String cutString(String str, int length) {
        return cutString(str, length, "");
    }

    public static String cutString(String str, int length, String dot) {
        int strBLen = strlen(str, "GBK");
        if(strBLen <= length) {
            return str;
        } else {
            int temp = 0;
            StringBuffer sb = new StringBuffer(length);
            char[] ch = str.toCharArray();
            char[] var7 = ch;
            int var8 = ch.length;

            for(int var9 = 0; var9 < var8; ++var9) {
                char c = var7[var9];
                sb.append(c);
                if(c > 256) {
                    temp += 2;
                } else {
                    ++temp;
                }

                if(temp >= length) {
                    if(dot != null) {
                        sb.append(dot);
                    }
                    break;
                }
            }

            return sb.toString();
        }
    }

    public static String cutStringFromChar(String str1, String str2, int offset) {
        if(isEmpty(str1)) {
            return "";
        } else {
            int start = str1.indexOf(str2);
            return start != -1 && str1.length() > start + offset?str1.substring(start + offset):"";
        }
    }

    public static int strlen(String str, String charset) {
        if(str != null && str.length() != 0) {
            int length = 0;

            try {
                length = str.getBytes(charset).length;
            } catch (Exception var4) {
                var4.printStackTrace();
            }

            return length;
        } else {
            return 0;
        }
    }

    public static String getSizeDesc(long size) {
        String suffix = "B";
        if(size >= 1024L) {
            suffix = "K";
            size >>= 10;
            if(size >= 1024L) {
                suffix = "M";
                size >>= 10;
                if(size >= 1024L) {
                    suffix = "G";
                    size >>= 10;
                }
            }
        }

        return size + suffix;
    }

    public static long ip2int(String ip) {
        ip = ip.replace(".", ",");
        String[] items = ip.split(",");
        return Long.valueOf(items[0]).longValue() << 24 | Long.valueOf(items[1]).longValue() << 16 | Long.valueOf(items[2]).longValue() << 8 | Long.valueOf(items[3]).longValue();
    }

    public static String join(Object[] array, String separator) {
        return array == null?null:join(array, separator, 0, array.length);
    }

    public static String join(Object[] array, String separator, int startIndex, int endIndex) {
        if(array == null) {
            return null;
        } else {
            if(separator == null) {
                separator = "";
            }

            int bufSize = endIndex - startIndex;
            if(bufSize <= 0) {
                return "";
            } else {
                bufSize *= (array[startIndex] == null?16:array[startIndex].toString().length()) + separator.length();
                StringBuffer buf = new StringBuffer(bufSize);

                for(int i = startIndex; i < endIndex; ++i) {
                    if(i > startIndex) {
                        buf.append(separator);
                    }

                    if(array[i] != null) {
                        buf.append(array[i]);
                    }
                }

                return buf.toString();
            }
        }
    }

    public static String join(Collection collection, String separator) {
        return collection == null?null:join(collection.iterator(), separator);
    }

    public static String join(Iterator iterator, String separator) {
        if(iterator == null) {
            return null;
        } else if(!iterator.hasNext()) {
            return "";
        } else {
            Object first = iterator.next();
            if(!iterator.hasNext()) {
                return first == null?null:first.toString();
            } else {
                StringBuffer buf = new StringBuffer(256);
                if(first != null) {
                    buf.append(first);
                }

                while(iterator.hasNext()) {
                    if(separator != null) {
                        buf.append(separator);
                    }

                    Object obj = iterator.next();
                    if(obj != null) {
                        buf.append(obj);
                    }
                }

                return buf.toString();
            }
        }
    }

    public static int getInt(String intStr, int intDef) {
        int result = intDef;

        try {
            result = Integer.parseInt(intStr);
        } catch (Exception var4) {
            ;
        }

        return result;
    }

    public static String getIsNullStr(String str) {
        return "null".equals(str)?"":str;
    }

    public static String getString(String str, String strDef) {
        return isEmpty(str)?strDef:str;
    }

    public static int getInt(String intStr) {
        return getInt(intStr, 0);
    }

    public static double getDouble(String dbstr, double dbDef) {
        double result = dbDef;

        try {
            result = Double.parseDouble(dbstr);
        } catch (Exception var6) {
            ;
        }

        return result;
    }

    public static double getDouble(String dbstr) {
        return getDouble(dbstr, 0.0D);
    }

    public static float getFloat(String flstr, float flDef) {
        float result = flDef;

        try {
            result = Float.parseFloat(flstr);
        } catch (Exception var4) {
            ;
        }

        return result;
    }

    public static float getFloat(String flstr) {
        return getFloat(flstr, 0.0F);
    }

    public static long getLong(String longstr, long longDef) {
        long result = longDef;

        try {
            result = Long.parseLong(longstr);
        } catch (Exception var6) {
            ;
        }

        return result;
    }

    public static long getLong(String longstr) {
        return getLong(longstr, 0L);
    }

    public static BigDecimal getBigDecimal(String bigDecimalStr) {
        return getBigDecimal(bigDecimalStr, BigDecimal.ZERO);
    }

    public static BigDecimal getBigDecimal(String bigDecimalStr, BigDecimal bigDecimalDef) {
        BigDecimal bigDecimal = bigDecimalDef;

        try {
            bigDecimal = new BigDecimal(bigDecimalStr);
        } catch (Exception var4) {
            ;
        }

        return bigDecimal;
    }

    public static boolean getBoolean(String booleanstr, boolean booleanDef) {
        boolean result = booleanDef;

        try {
            result = Boolean.parseBoolean(booleanstr);
        } catch (Exception var4) {
            ;
        }

        return result;
    }

    public static boolean getBoolean(String booleanstr) {
        return getBoolean(booleanstr, false);
    }

    public static String rightPad(String str, int size, char padChar) {
        if(str == null) {
            return null;
        } else {
            int pads = size - str.length();
            return pads <= 0?str:(pads > 8192?rightPad(str, size, String.valueOf(padChar)):str.concat(padding(pads, padChar)));
        }
    }

    private static String padding(int repeat, char padChar) throws IndexOutOfBoundsException {
        if(repeat < 0) {
            throw new IndexOutOfBoundsException("Cannot pad a negative amount: " + repeat);
        } else {
            char[] buf = new char[repeat];

            for(int i = 0; i < buf.length; ++i) {
                buf[i] = padChar;
            }

            return new String(buf);
        }
    }

    public static String rightPad(String str, int size, String padStr) {
        if(str == null) {
            return null;
        } else {
            if(isEmpty(padStr)) {
                padStr = " ";
            }

            int padLen = padStr.length();
            int strLen = str.length();
            int pads = size - strLen;
            if(pads <= 0) {
                return str;
            } else if(padLen == 1 && pads <= 8192) {
                return rightPad(str, size, padStr.charAt(0));
            } else if(pads == padLen) {
                return str.concat(padStr);
            } else if(pads < padLen) {
                return str.concat(padStr.substring(0, pads));
            } else {
                char[] padding = new char[pads];
                char[] padChars = padStr.toCharArray();

                for(int i = 0; i < pads; ++i) {
                    padding[i] = padChars[i % padLen];
                }

                return str.concat(new String(padding));
            }
        }
    }

    public static String leftPad(String str, int size) {
        return leftPad(str, size, ' ');
    }

    public static String leftPad(String str, int size, char padChar) {
        if(str == null) {
            return null;
        } else {
            int pads = size - str.length();
            return pads <= 0?str:(pads > 8192?leftPad(str, size, String.valueOf(padChar)):padding(pads, padChar).concat(str));
        }
    }

    public static String leftPad(String str, int size, String padStr) {
        if(str == null) {
            return null;
        } else {
            if(isEmpty(padStr)) {
                padStr = " ";
            }

            int padLen = padStr.length();
            int strLen = str.length();
            int pads = size - strLen;
            if(pads <= 0) {
                return str;
            } else if(padLen == 1 && pads <= 8192) {
                return leftPad(str, size, padStr.charAt(0));
            } else if(pads == padLen) {
                return padStr.concat(str);
            } else if(pads < padLen) {
                return padStr.substring(0, pads).concat(str);
            } else {
                char[] padding = new char[pads];
                char[] padChars = padStr.toCharArray();

                for(int i = 0; i < pads; ++i) {
                    padding[i] = padChars[i % padLen];
                }

                return (new String(padding)).concat(str);
            }
        }
    }

    public static void main(String[] args) {
        System.out.println(dateTimeFormat("2012-3-2 12:2:20"));
    }
}
