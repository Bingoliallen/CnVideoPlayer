package videoplayer.android.com.cnvideoplayer.entity;

import java.util.List;

/**
 * Date: 2018/8/29
 * Author:
 * Email：
 * Des：
 */

public class SortsList extends BaseEntity{


    /**
     * error : null
     * errorCode : null
     * result : {"items":[]}
     */

    private ResultBean result;

    public ResultBean getResult() {
        return result;
    }

    public void setResult(ResultBean result) {
        this.result = result;
    }

    public static class ResultBean {
        private List<SortsData> items;

        public List<SortsData> getItems() {
            return items;
        }

        public void setItems(List<SortsData> items) {
            this.items = items;
        }
    }
}
