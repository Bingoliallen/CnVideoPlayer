package videoplayer.android.com.cnvideoplayer.entity;

import com.google.gson.annotations.SerializedName;

/**
 * Date: 2018/8/24
 * Author:
 * Email：
 * Des：
 */

public class LoginEntity extends BaseEntity {
    private Result result;

    public Result getResult() {
        return result;
    }

    public void setResult(Result result) {
        this.result = result;
    }

    public class Result {


        private String nickname;
        private String mobile;
        private String portrait;
        private String tokenCode;//
        private String tokenInfo;//
        private String userno;
        private int vip;//0-一般会员；1-VIP会员
        private String extime;
        private String balance;
        private String email;
        private int usertype;
        private int exdays;



        /**
         * wxname : null
         * wxportrait : null
         * code : null
         * auths : null
         * createtime : null
         * userid : 2c92d678657c00b701658363acbb0001
         * portrait : null
         * logindevice : null
         * recommend : null
         * vipstatus : 1
         * smscode : null
         * shopid : null
         * openid : null
         * integral : null
         * role : null
         * password : null
         * name : null
         * state : null
         * type : 0
         */

        private String wxname;
        private String wxportrait;
        private String code;
        private String auths;
        private String createtime;
        private String userid;
        private String url;

        private String logindevice;
        private String recommend;
        private int vipstatus;
        private String smscode;
        private String shopid;
        private String openid;
        private String integral;
        private String role;
        private String password;
        private String name;
        private String state;
      //  private int type;

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

        public String getWxname() {
            return wxname;
        }

        public void setWxname(String wxname) {
            this.wxname = wxname;
        }

        public String getWxportrait() {
            return wxportrait;
        }

        public void setWxportrait(String wxportrait) {
            this.wxportrait = wxportrait;
        }

        public String getCode() {
            return code;
        }

        public void setCode(String code) {
            this.code = code;
        }

        public String getAuths() {
            return auths;
        }

        public void setAuths(String auths) {
            this.auths = auths;
        }

        public Object getCreatetime() {
            return createtime;
        }

        public void setCreatetime(String createtime) {
            this.createtime = createtime;
        }

        public String getUserid() {
            return userid;
        }

        public void setUserid(String userid) {
            this.userid = userid;
        }


        public String getLogindevice() {
            return logindevice;
        }

        public void setLogindevice(String logindevice) {
            this.logindevice = logindevice;
        }

        public String getRecommend() {
            return recommend;
        }

        public void setRecommend(String recommend) {
            this.recommend = recommend;
        }

        public int getVipstatus() {
            return vipstatus;
        }

        public void setVipstatus(int vipstatus) {
            this.vipstatus = vipstatus;
        }

        public String getSmscode() {
            return smscode;
        }

        public void setSmscode(String smscode) {
            this.smscode = smscode;
        }

        public String getShopid() {
            return shopid;
        }

        public void setShopid(String shopid) {
            this.shopid = shopid;
        }

        public String getOpenid() {
            return openid;
        }

        public void setOpenid(String openid) {
            this.openid = openid;
        }

        public String getIntegral() {
            return integral;
        }

        public void setIntegral(String integral) {
            this.integral = integral;
        }

        public String getRole() {
            return role;
        }

        public void setRole(String role) {
            this.role = role;
        }

        public String getPassword() {
            return password;
        }

        public void setPassword(String password) {
            this.password = password;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getState() {
            return state;
        }

        public void setState(String state) {
            this.state = state;
        }

       /* public int getType() {
            return type;
        }

        public void setType(int type) {
            this.type = type;
        }*/
    }
}
