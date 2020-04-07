package videoplayer.android.com.cnvideoplayer.entity;

import java.util.List;

/**
 * Date: 2018/8/29
 * Author:
 * Email：
 * Des：
 */

public class VideoDetailEntity {


    private Result result;

    public Result getResult() {
        return result;
    }

    public void setResult(Result result) {
        this.result = result;
    }

    public class Result {
        private VideoDetail item;
        private List<Actors> actors;
        private List<VideoEntity> recommends;

        public VideoDetail getItem() {
            return item;
        }

        public void setItem(VideoDetail item) {
            this.item = item;
        }

        public List<Actors> getActors() {
            return actors;
        }

        public void setActors(List<Actors> actors) {
            this.actors = actors;
        }

        public List<VideoEntity> getRecommends() {
            return recommends;
        }

        public void setRecommends(List<VideoEntity> recommends) {
            this.recommends = recommends;
        }
    }
}
