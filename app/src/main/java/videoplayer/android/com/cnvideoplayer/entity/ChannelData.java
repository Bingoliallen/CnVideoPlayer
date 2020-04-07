package videoplayer.android.com.cnvideoplayer.entity;

import java.util.List;

/**
 * Date: 2018/10/29
 * Author:
 * Email：
 * Des：
 */

public class ChannelData extends BaseEntity {

    private Result result;

    public Result getResult() {
        return result;
    }

    public void setResult(Result result) {
        this.result = result;
    }

    public class Result {

        private List<Channel> items;

        public List<Channel> getItems() {
            return items;
        }

        public void setItems(List<Channel> items) {
            this.items = items;
        }
    }

}
