package videoplayer.android.com.cnvideoplayer.entity;

import java.io.Serializable;

/**
 * Date: 2018/8/25
 * Author:
 * Email：
 * Des：
 */

public class VideoEntity implements Serializable{

    private static final long serialVersionUID = 1127982543241489165L;

    private String videoid;//视频id
    private String name;// 视频名称
    private String pic;  // 配图
    private int favor; // 收藏标示   1-已收藏；0-未收藏
    private int views;  // 播放次数
    private int free;
    private String memo;



    private boolean isSelected=false;
    public VideoEntity(boolean isSelected) {
        this.isSelected = isSelected;
    }
    public VideoEntity() {
    }

    public String getMemo() {
        return memo;
    }

    public void setMemo(String memo) {
        this.memo = memo;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
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

    public int getFree() {
        return free;
    }

    public void setFree(int free) {
        this.free = free;
    }
}
