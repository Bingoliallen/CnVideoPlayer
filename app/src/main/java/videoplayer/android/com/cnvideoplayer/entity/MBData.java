package videoplayer.android.com.cnvideoplayer.entity;

import java.util.List;

/**
 * Date: 2018/10/29
 * Author:
 * Email：
 * Des：
 */

public class MBData extends BaseEntity{
    private ResultBean result;

    public ResultBean getResult() {
        return result;
    }

    public void setResult(ResultBean result) {
        this.result = result;
    }

    public static class ResultBean {
        private List<MB> items;

        public List<MB> getItems() {
            return items;
        }

        public void setItems(List<MB> items) {
            this.items = items;
        }
    }
}
