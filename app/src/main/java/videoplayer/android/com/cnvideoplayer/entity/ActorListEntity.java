package videoplayer.android.com.cnvideoplayer.entity;

import java.util.List;

/**
 * Date: 2018/11/8
 * Author:
 * Email：
 * Des：
 */

public class ActorListEntity extends BaseEntity{

    private Datas result;

    public Datas getResult() {
        return result;
    }

    public void setResult(Datas result) {
        this.result = result;
    }

    public class Datas {
        private List<ActorEntity > items;

        public List<ActorEntity> getItems() {
            return items;
        }

        public void setItems(List<ActorEntity> items) {
            this.items = items;
        }
    }



}
