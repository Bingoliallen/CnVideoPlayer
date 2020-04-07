package videoplayer.android.com.cnvideoplayer.utils;

import android.os.Environment;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;

/**
 * Date: 2018/9/1
 * Author:
 * Email：
 * Des：
 */

public class DownloadFileTool {

    // 获得存储卡的路径
    private static String sd_path = Environment.getExternalStorageDirectory() + "/";
    private static String filePath = sd_path + "MyFileDirTest/";
    private static String saveFileAllName = filePath + "TTQ.mp4";

    public static void doDownloadThread(String tokenCode, String tokenInfo, String urlStr, String mJson) {
        new Thread(new DownloadFileThread(tokenCode,tokenInfo,urlStr,mJson)).start();
    }


    private static class DownloadFileThread implements Runnable {
        String tokenCode;
        String tokenInfo;
        String urlStr;
        String mJson;

        public DownloadFileThread(String tokenCode, String tokenInfo, String urlStr, String mJson) {
            this.tokenCode = tokenCode;
            this.tokenInfo = tokenInfo;
            this.urlStr = urlStr;
            this.mJson = mJson;
        }

        @Override
        public void run() {
            FileOutputStream fileOutputStream = null;
            InputStream inputStream = null;
            //
            try {
                URL url = new URL(urlStr);
                //获取连接
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("POST");
                connection.setConnectTimeout(5000);
                connection.setRequestProperty("Connection", "Keep-Alive");
                connection.setRequestProperty("Charset", "UTF-8");
                connection.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
                connection.setRequestProperty("tokenCode", tokenCode);
                connection.setRequestProperty("tokenInfo", tokenInfo);
                connection.setRequestProperty("Content-Length", String.valueOf(mJson.length()));
                connection.setDoOutput(true);
                connection.setDoInput(true);
                connection.setUseCaches(false);

                OutputStream out = connection.getOutputStream();
                out.write(mJson.getBytes());
                out.flush();
                out.close();

                //打开连接
                connection.connect();
                //获取内容长度
                int contentLength = connection.getContentLength();
                LogUtil.e("----contentLength----:"+contentLength);

                File file = new File(filePath);
                // 判断文件目录是否存在
                if (!file.exists()) {
                    file.mkdir();
                }
                //file.mkdirs();

                //输入流
                inputStream = connection.getInputStream();

                File myFile = new File(saveFileAllName);
                //输出流
                fileOutputStream = new FileOutputStream(myFile);

                byte[] bytes = new byte[1024];
                // int  index=0;
                long totalReaded = 0;
                int temp_Len;
                while ((temp_Len = inputStream.read(bytes)) != -1) {
                    // bytes[index]= (byte) temp_Len;
                    // index++;
                    totalReaded += temp_Len;
                    LogUtil.e("run: totalReaded:" + totalReaded);
                    long progress = totalReaded * 100 / contentLength;
                    LogUtil.e( "run: progress:" + progress);
                    fileOutputStream.write(bytes, 0, temp_Len);

                }
              /*  byte[] bytes = new byte[1024];
                while (inputStream.read(bytes)!=-1){
                    fileOutputStream.write(bytes);
                }*/

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (ProtocolException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                try {
                    if (fileOutputStream != null) {
                        fileOutputStream.close();
                    }
                    if (inputStream != null) {
                        inputStream.close();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

        }
    }

}
