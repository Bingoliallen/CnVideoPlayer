package videoplayer.android.com.cnvideoplayer.entity;

import java.io.Serializable;

/**
 * Date: 2018/11/8
 * Author:
 * Email：
 * Des：
 */

public class ActorEntity  implements Serializable {

    private static final long serialVersionUID = -7454736971346982705L;

    private String actorid;
    private String name;
    private String photo;
    private int favor;

    public String getActorid() {
        return actorid;
    }

    public void setActorid(String actorid) {
        this.actorid = actorid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public int getFavor() {
        return favor;
    }

    public void setFavor(int favor) {
        this.favor = favor;
    }
}
