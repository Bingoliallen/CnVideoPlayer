package videoplayer.android.com.cnvideoplayer.entity;

/**
 * Date: 2018/8/29
 * Author:
 * Email：
 * Des：
 */

public class VideoLines {
    private  String name	;//名称
    private String path	;//地址
    private boolean isSelected;

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }
}
