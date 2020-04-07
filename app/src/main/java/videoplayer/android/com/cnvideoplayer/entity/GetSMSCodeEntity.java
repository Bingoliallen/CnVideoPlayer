package videoplayer.android.com.cnvideoplayer.entity;

/**
 * Date: 2018/8/27
 * Author:
 * Email：
 * Des：
 */

public class GetSMSCodeEntity extends BaseEntity{

    /**
     * error : null
     * errorCode : null
     * result : {"smscode":"1234"}
     */

    private ResultBean result;


    public ResultBean getResult() {
        return result;
    }

    public void setResult(ResultBean result) {
        this.result = result;
    }

    public static class ResultBean {
        /**
         * smscode : 1234
         */

        private String smscode;

        public String getSmscode() {
            return smscode;
        }

        public void setSmscode(String smscode) {
            this.smscode = smscode;
        }
    }
}
