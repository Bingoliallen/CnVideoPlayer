package videoplayer.android.com.cnvideoplayer.entity;

import java.util.List;

/**
 * Date: 2018/11/8
 * Author:
 * Email：
 * Des：
 */

public class CollectListEntity extends BaseEntity{
    private Datas result;

    public Datas getResult() {
        return result;
    }

    public void setResult(Datas result) {
        this.result = result;
    }

    public class Datas {
        private List<Collect > items;

        public List<Collect> getItems() {
            return items;
        }

        public void setItems(List<Collect> items) {
            this.items = items;
        }
    }
}
