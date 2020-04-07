package videoplayer.android.com.cnvideoplayer.entity;

import java.util.List;

/**
 * Date: 2018/11/9
 * Author:
 * Email：
 * Des：
 */

public class Programme extends BaseEntity {

    private Result result;

    public Result getResult() {
        return result;
    }

    public void setResult(Result result) {
        this.result = result;
    }

    public class Result {
        private List<ResultItem> items;

        public List<ResultItem> getItems() {
            return items;
        }

        public void setItems(List<ResultItem> items) {
            this.items = items;
        }
    }

    public class ResultItem {
        private String name;
        private String time;
        private String status;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getTime() {
            return time;
        }

        public void setTime(String time) {
            this.time = time;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }
    }


}
