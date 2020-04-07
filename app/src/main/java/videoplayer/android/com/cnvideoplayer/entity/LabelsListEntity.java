package videoplayer.android.com.cnvideoplayer.entity;

import java.util.List;

/**
 * Date: 2018/11/9
 * Author:
 * Email：
 * Des：
 */

public class LabelsListEntity extends BaseEntity{
    private Result result;

    public Result getResult() {
        return result;
    }

    public void setResult(Result result) {
        this.result = result;
    }

    public class Result {
        private List<QB> items;

        public List<QB> getItems() {
            return items;
        }

        public void setItems(List<QB> item) {
            this.items = item;
        }
    }
}
