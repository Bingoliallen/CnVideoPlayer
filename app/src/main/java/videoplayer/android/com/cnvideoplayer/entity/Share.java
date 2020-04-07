package videoplayer.android.com.cnvideoplayer.entity;

/**
 * Date: 2018/8/29
 * Author:
 * Email：
 * Des：
 */

public class Share extends BaseEntity {

    private Result result;

    public Result getResult() {
        return result;
    }

    public void setResult(Result result) {
        this.result = result;
    }

    public class Result {
        private String link;//app下载地址
        private String url;//webview地址

        public String getLink() {
            return link;
        }

        public void setLink(String link) {
            this.link = link;
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }
    }
}
