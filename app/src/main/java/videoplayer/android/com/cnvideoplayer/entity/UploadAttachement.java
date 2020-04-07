package videoplayer.android.com.cnvideoplayer.entity;

/**
 * Date: 2018/8/30
 * Author:
 * Email：
 * Des：
 */

public class UploadAttachement {

    private String oldfilename;//原文件名
    private String size;//文件大小
    private String httppath;//	文件路径
    private String state;//	上传结果
    private String msg;//上传失败的原因

    public String getOldfilename() {
        return oldfilename;
    }

    public void setOldfilename(String oldfilename) {
        this.oldfilename = oldfilename;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public String getHttppath() {
        return httppath;
    }

    public void setHttppath(String httppath) {
        this.httppath = httppath;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
