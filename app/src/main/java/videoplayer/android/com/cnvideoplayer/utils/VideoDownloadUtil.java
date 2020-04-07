package videoplayer.android.com.cnvideoplayer.utils;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.logging.Level;
import java.util.logging.Logger;


/**
 * Date: 2018/9/1
 * Author:
 * Email：
 * Des：
 */

public class VideoDownloadUtil {

    private static final Logger LOGGER = Logger.getLogger(VideoDownloadUtil.class.getSimpleName());

    /**
     * 下载文件到本地
     *
     * @param urlString 被下载的文件地址
     * @param filename  本地文件名
     * @param timeout   超时时间毫秒
     * @throws Exception 各种异常
     */

    @SuppressWarnings("finally")
    public static boolean download( String tokenCode,String tokenInfo, String urlString, String mJson, String filename, int timeout) {
        boolean ret = false;
        File file = new File(filename);
        try {
            if (file.exists()) {
                ret = true;
            } else {
                // 构造URL
                URL url = new URL(urlString);
                // 打开连接
                HttpURLConnection con = (HttpURLConnection) url.openConnection();
                con.setConnectTimeout(timeout);
                con.setReadTimeout(timeout);
                con.setRequestProperty("tokenCode", tokenCode);
                con.setRequestProperty("tokenInfo", tokenInfo);
                con.setRequestMethod("POST");
                con.setDoOutput(true);
                con.setDoInput(true);
                con.setUseCaches(false);
                con.setRequestProperty("Connection", "Keep-Alive");
                con.setRequestProperty("Charset", "UTF-8");
                con.setRequestProperty("Content-Length", String.valueOf(mJson.length()));
                // 设置文件类型:
                con.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
                // 设置接收类型否则返回415错误
                con.setRequestProperty("accept", "*/*");//此处为暴力方法设置接受所有类型，以此来防范返回415

                OutputStream out = con.getOutputStream();
                out.write(mJson.getBytes());
                out.flush();
                out.close();

                if (con.getResponseCode() == 200) {
                    // 输入流
                    // int contentLength = con.getContentLength();
                    InputStream is = con.getInputStream();
                    // 1K的数据缓冲
                    byte[] bs = new byte[1024];
                    // 读取到的数据长度
                    int len;
                    // 输出的文件流

                    File file2 = new File(file.getParent());
                    file2.mkdirs();
                    if (file.isDirectory()) {

                    } else {
                        file.createNewFile();//创建文件
                    }
                    OutputStream os = new FileOutputStream(file);
                    // 开始读取
                    while ((len = is.read(bs)) != -1) {
                        os.write(bs, 0, len);
                    }
                    // 完毕，关闭所有链接
                    os.close();
                    is.close();
                    ret = true;
                }


            }
        } catch (IOException e) {
            e.printStackTrace();
            file.delete();
            ret = false;
            LOGGER.log(Level.ALL, "[VideoUtil:download]:\n" + " VIDEO URL：" + urlString + " \n NEW FILENAME:" + filename + " DOWNLOAD FAILED!! ");
        } finally {
            return ret;
        }
    }


    /**
     * 从网络Url中下载文件
     *
     * @param urlStr
     * @param fileName
     * @param savePath
     * @throws IOException
     */
    public static void downLoadFromUrl(String tokenCode,String tokenInfo, String urlStr,String mJson,  String fileName, String savePath) throws IOException {
        URL url = new URL(urlStr);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        //设置超时间为3秒
        conn.setConnectTimeout(6 * 1000);
        //防止屏蔽程序抓取而返回403错误
        conn.setRequestProperty("User-Agent", "Mozilla/4.0 (compatible; MSIE 5.0; Windows NT; DigExt)");
        conn.setRequestProperty("tokenCode", tokenCode);
        conn.setRequestProperty("tokenInfo", tokenInfo);
        conn.setRequestMethod("POST");
        conn.setDoOutput(true);
        conn.setDoInput(true);
        conn.setUseCaches(false);
        conn.setRequestProperty("Connection", "Keep-Alive");
        conn.setRequestProperty("Charset", "UTF-8");
        conn.setRequestProperty("Content-Length", String.valueOf(mJson.length()));
        // 设置文件类型:
        conn.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
        // 设置接收类型否则返回415错误
        conn.setRequestProperty("accept", "*/*");//此处为暴力方法设置接受所有类型，以此来防范返回415

        OutputStream out = conn.getOutputStream();
        out.write(mJson.getBytes());
        out.flush();
        out.close();

        //获取内容长度
        int contentLength = conn.getContentLength();
        LogUtil.e("----contentLength----:"+contentLength);
        //得到输入流
        InputStream inputStream = conn.getInputStream();
        //获取自己数组
        byte[] getData = readInputStream(inputStream);

        //文件保存位置
        File saveDir = new File(savePath);
        if (!saveDir.exists()) {
            saveDir.mkdir();
        }
        File file = new File(saveDir + File.separator + fileName);
        FileOutputStream fos = new FileOutputStream(file);
        fos.write(getData);
        if (fos != null) {
            fos.close();
        }
        if (inputStream != null) {
            inputStream.close();
        }
        System.out.println("info:" + url + " download success");
    }

    /**
     * 从输入流中获取字节数组
     *
     * @param inputStream
     * @return
     * @throws IOException
     */
    public static byte[] readInputStream(InputStream inputStream) throws IOException {
        byte[] buffer = new byte[1024];
        int len = 0;
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        while ((len = inputStream.read(buffer)) != -1) {
            bos.write(buffer, 0, len);
        }
        bos.close();
        return bos.toByteArray();
    }



}
