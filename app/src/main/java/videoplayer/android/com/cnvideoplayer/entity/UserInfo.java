package videoplayer.android.com.cnvideoplayer.entity;

/**
 * Date: 2018/8/29
 * Author:
 * Email：
 * Des：
 */

public class UserInfo extends BaseEntity {
    private Result result;

    public Result getResult() {
        return result;
    }

    public void setResult(Result result) {
        this.result = result;
    }

    public class Result {
        private String  userid;
        private String  nickname;
        private String  mobile;
        private String  portrait;
        private String  url;

        private String  tokenCode;
        private String  tokenInfo;
        private String  userno;
        private int  vip;
        private String  extime;
        private String  balance;
        private int  vipstatus;
        private String email;


        private int usertype;
        private int exdays;

        public int getUsertype() {
            return usertype;
        }

        public void setUsertype(int usertype) {
            this.usertype = usertype;
        }

        public int getExdays() {
            return exdays;
        }

        public void setExdays(int exdays) {
            this.exdays = exdays;
        }

        //private int type;

       /* public int getType() {
            return type;
        }

        public void setType(int type) {
            this.type = type;
        }*/

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }

        public String getBalance() {
            return balance;
        }

        public void setBalance(String balance) {
            this.balance = balance;
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }

        public int getVipstatus() {
            return vipstatus;
        }

        public void setVipstatus(int vipstatus) {
            this.vipstatus = vipstatus;
        }

        public String getUserid() {
            return userid;
        }

        public void setUserid(String userid) {
            this.userid = userid;
        }

        public String getNickname() {
            return nickname;
        }

        public void setNickname(String nickname) {
            this.nickname = nickname;
        }

        public String getMobile() {
            return mobile;
        }

        public void setMobile(String mobile) {
            this.mobile = mobile;
        }

        public String getPortrait() {
            return portrait;
        }

        public void setPortrait(String portrait) {
            this.portrait = portrait;
        }

        public String getTokenCode() {
            return tokenCode;
        }

        public void setTokenCode(String tokenCode) {
            this.tokenCode = tokenCode;
        }

        public String getTokenInfo() {
            return tokenInfo;
        }

        public void setTokenInfo(String tokenInfo) {
            this.tokenInfo = tokenInfo;
        }

        public String getUserno() {
            return userno;
        }

        public void setUserno(String userno) {
            this.userno = userno;
        }

        public int getVip() {
            return vip;
        }

        public void setVip(int vip) {
            this.vip = vip;
        }

        public String getExtime() {
            return extime;
        }

        public void setExtime(String extime) {
            this.extime = extime;
        }
    }


}
