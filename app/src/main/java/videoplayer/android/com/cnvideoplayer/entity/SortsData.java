package videoplayer.android.com.cnvideoplayer.entity;

import java.util.List;

/**
 * Date: 2018/8/29
 * Author:
 * Email：
 * Des：
 */

public class SortsData {

    private String order;
    private String sortsid;//	分类id
    private String name;   //	名称
    private String levels; //分类级别
    private String status;  //	状态
    private String seq;    //	排序
    private List<SortsData> children;

    public List<SortsData> getChildren() {
        return children;
    }

    public void setChildren(List<SortsData> children) {
        this.children = children;
    }

    private boolean isSelected=false;

    public String getOrder() {
        return order;
    }

    public void setOrder(String order) {
        this.order = order;
    }

    /**
     * levels : null
     * icon : null
     * seq : null
     * status : null
     * children : null
     * type : null
     */

    private String icon;
    private String type;

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }

    public String getSortsid() {
        return sortsid;
    }

    public void setSortsid(String sortsid) {
        this.sortsid = sortsid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLevels() {
        return levels;
    }

    public void setLevels(String levels) {
        this.levels = levels;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getSeq() {
        return seq;
    }

    public void setSeq(String seq) {
        this.seq = seq;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }


    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
