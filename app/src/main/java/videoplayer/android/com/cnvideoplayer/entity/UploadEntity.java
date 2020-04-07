package videoplayer.android.com.cnvideoplayer.entity;

import java.util.List;

/**
 * Date: 2018/8/30
 * Author:
 * Email：
 * Des：
 */

public class UploadEntity {

    private Result result;

    public Result getResult() {
        return result;
    }

    public void setResult(Result result) {
        this.result = result;
    }

    public class Result {
        private List<UploadAttachement> items;

        public List<UploadAttachement> getItems() {
            return items;
        }

        public void setItems(List<UploadAttachement> items) {
            this.items = items;
        }
    }
}
