package videoplayer.android.com.cnvideoplayer.entity;

import java.util.List;

/**
 * Date: 2018/10/30
 * Author:
 * Email：
 * Des：
 */

public class HotData extends BaseEntity {
    private ResultBean result;

    public ResultBean getResult() {
        return result;
    }

    public void setResult(ResultBean result) {
        this.result = result;
    }

    public static class ResultBean {
        private List<Video> movie;
        private List<Video> video;

        public List<Video> getMovie() {
            return movie;
        }

        public void setMovie(List<Video> movie) {
            this.movie = movie;
        }

        public List<Video> getVideo() {
            return video;
        }

        public void setVideo(List<Video> video) {
            this.video = video;
        }
    }
}
