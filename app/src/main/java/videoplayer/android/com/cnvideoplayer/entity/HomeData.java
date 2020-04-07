package videoplayer.android.com.cnvideoplayer.entity;

import java.util.List;

/**
 * Date: 2018/8/29
 * Author:
 * Email：
 * Des：
 */

public class HomeData extends BaseEntity{


    /**
     * error : null
     * errorCode : null
     * result : {"items":[],"banners":[]}
     */

    private ResultBean result;

    public ResultBean getResult() {
        return result;
    }

    public void setResult(ResultBean result) {
        this.result = result;
    }

    public static class ResultBean {
        private int indexFlag;
        private String indexUrl;
        private String notice;

        public String getNotice() {
            return notice;
        }

        public void setNotice(String notice) {
            this.notice = notice;
        }

        private List<VideoEntity> items;
        private List<BannerData> banners;

        public int getIndexFlag() {
            return indexFlag;
        }

        public void setIndexFlag(int indexFlag) {
            this.indexFlag = indexFlag;
        }

        public String getIndexUrl() {
            return indexUrl;
        }

        public void setIndexUrl(String indexUrl) {
            this.indexUrl = indexUrl;
        }

        public List<VideoEntity> getItems() {
            return items;
        }

        public void setItems(List<VideoEntity> items) {
            this.items = items;
        }

        public List<BannerData> getBanners() {
            return banners;
        }

        public void setBanners(List<BannerData> banners) {
            this.banners = banners;
        }
    }
}
