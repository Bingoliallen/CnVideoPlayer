package videoplayer.android.com.cnvideoplayer.entity;

import java.util.List;

/**
 * Date: 2018/8/29
 * Author:
 * Email：
 * Des：
 */

public class VideoLinesEntity {
    private Result result;

    public Result getResult() {
        return result;
    }

    public void setResult(Result result) {
        this.result = result;
    }

    public class Result {
        List<VideoLines> items;

        public List<VideoLines> getItems() {
            return items;
        }

        public void setItems(List<VideoLines> items) {
            this.items = items;
        }
    }

}
