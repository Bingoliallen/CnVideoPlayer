package videoplayer.android.com.cnvideoplayer.entity;

/**
 * Date: 2018/8/17
 * Author:
 * Email：
 * Des：
 */

public class BannerData {

    private String image;
    private String objid;
    private String objname;
    private String advertisingid;
    private int seq;
    private int type;
    private String url;

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public int getSeq() {
        return seq;
    }

    public void setSeq(int seq) {
        this.seq = seq;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getAdvertisingid() {
        return advertisingid;
    }

    public void setAdvertisingid(String advertisingid) {
        this.advertisingid = advertisingid;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getObjid() {
        return objid;
    }

    public void setObjid(String objid) {
        this.objid = objid;
    }

    public String getObjname() {
        return objname;
    }

    public void setObjname(String objname) {
        this.objname = objname;
    }
}
