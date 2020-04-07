package videoplayer.android.com.cnvideoplayer.entity;

import java.util.List;

/**
 * Date: 2018/8/25
 * Author:
 * Email：
 * Des：
 */

public class VideoListEntity extends BaseEntity{

    private Datas result;

    public Datas getResult() {
        return result;
    }

    public void setResult(Datas result) {
        this.result = result;
    }

    public class Datas {
        private List<VideoEntity > items;

        public List<VideoEntity> getItems() {
            return items;
        }

        public void setItems(List<VideoEntity> items) {
            this.items = items;
        }
    }


}
