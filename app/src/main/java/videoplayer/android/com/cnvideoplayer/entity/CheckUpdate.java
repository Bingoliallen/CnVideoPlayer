package videoplayer.android.com.cnvideoplayer.entity;

/**
 * Date: 2018/11/1
 * Author:
 * Email：
 * Des：
 */

public class CheckUpdate {

    private Result result;

    public Result getResult() {
        return result;
    }

    public void setResult(Result result) {
        this.result = result;
    }

    public class Result {
        /**
         * versionCode : 1
         * versionName : 1.0.0
         * content : 1.新增抢单功能#2.性能优化
         * mustUpdate : true
         * url : apk download url
         */

        private int lastVersion;
        private int minVersion;

        private String lastVersionName;
        private String content;
        private int force;
        private String updateUrl;

        public int getMinVersion() {
            return minVersion;
        }

        public void setMinVersion(int minVersion) {
            this.minVersion = minVersion;
        }

        public int getVersionCode() {
            return lastVersion;
        }

        public void setVersionCode(int versionCode) {
            this.lastVersion = versionCode;
        }

        public String getVersionName() {
            return lastVersionName;
        }

        public void setVersionName(String versionName) {
            this.lastVersionName = versionName;
        }

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }

        public int isMustUpdate() {
            return force;
        }

        public void setMustUpdate(int mustUpdate) {
            this.force = mustUpdate;
        }

        public String getUrl() {
            return updateUrl;
        }

        public void setUrl(String url) {
            this.updateUrl = url;
        }
    }

}
