package videoplayer.android.com.cnvideoplayer.entity;

import java.util.List;

/**
 * Date: 2018/11/8
 * Author:
 * Email：
 * Des：
 */

public class MovieListEntity extends BaseEntity{


    private Result result;

    public Result getResult() {
        return result;
    }

    public void setResult(Result result) {
        this.result = result;
    }

    public class Result {
        private List<Movie> items;

        public List<Movie> getItems() {
            return items;
        }

        public void setItems(List<Movie> item) {
            this.items = item;
        }
    }
}
