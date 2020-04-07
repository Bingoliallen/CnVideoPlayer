package videoplayer.android.com.cnvideoplayer.entity;

import java.util.List;

/**
 * Date: 2018/8/29
 * Author:
 * Email：
 * Des：
 */

public class VideoDetail {

    private String videoid;    //视频id
    private String name;    //视频名称
    private String pic;    //配图
    private String memo;    //简介
    private String releasets;    //发行时间
    private int favor;    //收藏标示
    private int views;    //播放次数
    private String path;
    private int type;
    private int free;
    private String freets;
    private List<QB> labels;

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getFree() {
        return free;
    }

    public void setFree(int free) {
        this.free = free;
    }

    public String getFreets() {
        return freets;
    }

    public void setFreets(String freets) {
        this.freets = freets;
    }

    public List<QB> getLabels() {
        return labels;
    }

    public void setLabels(List<QB> labels) {
        this.labels = labels;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getVideoid() {
        return videoid;
    }

    public void setVideoid(String videoid) {
        this.videoid = videoid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPic() {
        return pic;
    }

    public void setPic(String pic) {
        this.pic = pic;
    }

    public String getMemo() {
        return memo;
    }

    public void setMemo(String memo) {
        this.memo = memo;
    }

    public String getReleasets() {
        return releasets;
    }

    public void setReleasets(String releasets) {
        this.releasets = releasets;
    }

    public int getFavor() {
        return favor;
    }

    public void setFavor(int favor) {
        this.favor = favor;
    }

    public int getViews() {
        return views;
    }

    public void setViews(int views) {
        this.views = views;
    }
}
