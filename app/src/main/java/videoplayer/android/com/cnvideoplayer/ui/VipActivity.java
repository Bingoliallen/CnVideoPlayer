package videoplayer.android.com.cnvideoplayer.ui;

import android.content.Intent;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.LinearLayout;

import com.google.gson.Gson;
import com.kymjs.rxvolley.RxVolley;
import com.kymjs.rxvolley.client.HttpCallback;
import com.kymjs.rxvolley.client.HttpParams;
import com.kymjs.rxvolley.http.VolleyError;
import com.xgr.easypay.EasyPay;
import com.xgr.easypay.alipay.AliPay;
import com.xgr.easypay.alipay.AlipayInfoImpli;
import com.xgr.easypay.callback.IPayCallback;
import com.xgr.easypay.wxpay.WXPay;
import com.xgr.easypay.wxpay.WXPayInfoImpli;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import videoplayer.android.com.cnvideoplayer.R;
import videoplayer.android.com.cnvideoplayer.base.BaseCompatActivity;
import videoplayer.android.com.cnvideoplayer.entity.AlipayParam;
import videoplayer.android.com.cnvideoplayer.entity.BaseEntity;
import videoplayer.android.com.cnvideoplayer.entity.WxpayParam;
import videoplayer.android.com.cnvideoplayer.utils.LogUtil;
import videoplayer.android.com.cnvideoplayer.utils.ShareUtils;
import videoplayer.android.com.cnvideoplayer.utils.StaticClass;
import videoplayer.android.com.cnvideoplayer.utils.ToastUtil;
import videoplayer.android.com.cnvideoplayer.view.Html5Webview;
import videoplayer.android.com.cnvideoplayer.view.StatusBarCompat;

public class VipActivity extends BaseCompatActivity {
    private WebView webView;
    private LinearLayout payZFB;
    private LinearLayout payWX;
    private String tokenCode;
    private String tokenInfo;

    private int mode;
    @Override
    protected String initTitle() {
        return "开通VIP";
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_vip;
    }

    @Override
    public void initView() {
        mode=getIntent().getIntExtra("mode",-1);
        EventBus.getDefault().register(this);
        StatusBarCompat.setStatusBarColor(this, ContextCompat.getColor(this, R.color.main_color));
        tokenCode = ShareUtils.getString(VipActivity.this, "tokenCode", "");
        tokenInfo = ShareUtils.getString(VipActivity.this, "tokenInfo", "");

        webView = (WebView) this.findViewById(R.id.webview);
        WebSettings webSettings = webView.getSettings();
        //如果访问的页面中要与Javascript交互，则webview必须设置支持Javascript
        webSettings.setJavaScriptEnabled(true);
       // 若加载的 html 里有JS 在执行动画等操作，会造成资源浪费（CPU、电量）
       // 在 onStop 和 onResume 里分别把 setJavaScriptEnabled() 给设置成 false 和 true 即可
       //支持插件
      //  webSettings.setPluginsEnabled(true);
        webSettings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);

       //设置自适应屏幕，两者合用
        webSettings.setUseWideViewPort(true); //将图片调整到适合webview的大小
        webSettings.setLoadWithOverviewMode(true); // 缩放至屏幕的大小

        //缩放操作
        webSettings.setSupportZoom(true); //支持缩放，默认为true。是下面那个的前提。
        webSettings.setBuiltInZoomControls(true); //设置内置的缩放控件。若为false，则该WebView不可缩放
        webSettings.setDisplayZoomControls(false); //隐藏原生的缩放控件

        //其他细节操作
        webSettings.setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK); //关闭webview中缓存
        webSettings.setAllowFileAccess(true); //设置可以访问文件
        webSettings.setJavaScriptCanOpenWindowsAutomatically(true); //支持通过JS打开新窗口
        webSettings.setLoadsImagesAutomatically(true); //支持自动加载图片


        payZFB = (LinearLayout) this.findViewById(R.id.payZFB);
        payWX = (LinearLayout) this.findViewById(R.id.payWX);
        payZFB.setOnClickListener(this);
        payWX.setOnClickListener(this);
        loadUrl();
    }

    @Override
    public void initData() {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.payZFB:
                subpay();
               // alipayparam();
                break;
            case R.id.payWX:
                Intent mmIntent= new Intent(VipActivity.this, JHMActivity.class);
                mmIntent.putExtra("mode",mode);
                startActivityForResult(mmIntent, 1101);
                // wxpayparam();
                break;
        }
    }

    public void loadUrl() {
        if (webView != null) {
            webView.loadUrl(getUrl());
        }
    }

    public String getUrl() {
        String url = ShareUtils.getString(VipActivity.this, "url", "");
        return url;
    }


    @Override
    public void onPause() {
        webView.onPause();
        webView.pauseTimers();
        super.onPause();
    }

    @Override
    public void onResume() {
        webView.onResume();
        webView.resumeTimers();
        super.onResume();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        // Check if the key event was the Back button and if there's history
        if ((keyCode == KeyEvent.KEYCODE_BACK) && webView.canGoBack()) {
            webView.goBack();
            return true;
        }
        // If it wasn't the Back key or there's no web page history, bubble up to the default
        // system behavior (probably exit the activity)
        return super.onKeyDown(keyCode, event);
    }

    //微信支付
    private void wxpay(String wxAppId, WXPayInfoImpli wxPayInfoImpli) {
        //实例化微信支付策略
        //   String wxAppId = "";
        WXPay wxPay = WXPay.getInstance(this, wxAppId);
        //构造微信订单实体。一般都是由服务端直接返回。
        //  WXPayInfoImpli wxPayInfoImpli = new WXPayInfoImpli();
        // wxPayInfoImpli.setTimestamp("");
        // wxPayInfoImpli.setSign("");
        //   wxPayInfoImpli.setPrepayId("");
        //   wxPayInfoImpli.setPartnerid("");
        //  wxPayInfoImpli.setAppid("");
        //  wxPayInfoImpli.setNonceStr("");
        //  wxPayInfoImpli.setPackageValue("");
        //策略场景类调起支付方法开始支付，以及接收回调。
        EasyPay.pay(wxPay, this, wxPayInfoImpli, new IPayCallback() {
            @Override
            public void success() {
                ToastUtil.showToast(VipActivity.this, "支付成功");
            }

            @Override
            public void failed() {
                ToastUtil.showToast(VipActivity.this, "支付失败");
            }

            @Override
            public void cancel() {
                ToastUtil.showToast(VipActivity.this, "支付取消");
            }
        });
    }

    private void alipay() {
        //实例化支付宝支付策略
        AliPay aliPay = new AliPay();
        //构造支付宝订单实体。一般都是由服务端直接返回。
        AlipayInfoImpli alipayInfoImpli = new AlipayInfoImpli();
        alipayInfoImpli.setOrderInfo("");
        //策略场景类调起支付方法开始支付，以及接收回调。
        EasyPay.pay(aliPay, this, alipayInfoImpli, new IPayCallback() {
            @Override
            public void success() {
                ToastUtil.showToast(VipActivity.this, "支付成功");
            }

            @Override
            public void failed() {
                ToastUtil.showToast(VipActivity.this, "支付失败");
            }

            @Override
            public void cancel() {
                ToastUtil.showToast(VipActivity.this, "支付取消");
            }
        });
    }

    //http://serpro/mobile/user/subpay.html
    private void subpay() {
        //http://serpro/mobile/user/subpay.html
        HttpParams post_params = new HttpParams();
        post_params.putHeaders("tokenCode", tokenCode);
        post_params.putHeaders("tokenInfo", tokenInfo);
        post_params.putHeaders("Content-Type", "application/json");
        showProgress("正在加载...");
        RxVolley.jsonPost(StaticClass.domain + "/subpay.html", post_params, new HttpCallback() {
            @Override
            public void onSuccess(String t) {
                dismissProgress();
                LogUtil.i("*************subpay********sq****:" + t);
                if (!TextUtils.isEmpty(t)) {
                    try {
                        BaseEntity mBaseEntity = new Gson().fromJson(t, BaseEntity.class);
                        if (mBaseEntity != null) {
                            if (mBaseEntity.getStatus() == 100) {
                               ToastUtil.showToast(VipActivity.this, "请求成功");
                                EventBus.getDefault().post("EventBus_PAY");
                            } else {
                                ToastUtil.showToast(VipActivity.this, "请求失败");
                            }

                        } else {
                            ToastUtil.showToast(VipActivity.this, "返回数据异常");
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        ToastUtil.showToast(VipActivity.this, "返回数据异常");
                    }

                } else {
                    ToastUtil.showToast(VipActivity.this, "返回数据异常");
                }

            }

            @Override
            public void onFailure(VolleyError error) {
                dismissProgress();
                LogUtil.i("onFailure...." + error.getMessage());
                ToastUtil.showToast(VipActivity.this, "请求失败");
            }

        });

    }





    private void alipayparam() {
        //http://hostname[:port]/mobile/user/alipayparam.html
        HttpParams post_params = new HttpParams();
        post_params.putHeaders("tokenCode", tokenCode);
        post_params.putHeaders("tokenInfo", tokenInfo);
        post_params.putHeaders("Content-Type", "application/json");
        showProgress("正在加载...");
        RxVolley.jsonPost(StaticClass.domain + "/alipayparam.html", post_params, new HttpCallback() {
            @Override
            public void onSuccess(String t) {
                dismissProgress();
                LogUtil.i("*************alipayparam********sq****:" + t);
                if (!TextUtils.isEmpty(t)) {
                    try {
                        BaseEntity mBaseEntity = new Gson().fromJson(t, BaseEntity.class);
                        if (mBaseEntity != null) {
                            if (mBaseEntity.getStatus() == 100) {
                                AlipayParam mAlipayParam = new Gson().fromJson(t, AlipayParam.class);
                                if (mAlipayParam != null && mAlipayParam.getResult() != null) {
                                    alipay();
                                }

                            } else {
                                ToastUtil.showToast(VipActivity.this, "获取失败");
                            }

                        } else {
                            ToastUtil.showToast(VipActivity.this, "返回数据异常");
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        ToastUtil.showToast(VipActivity.this, "返回数据异常");
                    }

                } else {
                    ToastUtil.showToast(VipActivity.this, "返回数据异常");
                }

            }

            @Override
            public void onFailure(VolleyError error) {
                dismissProgress();
                LogUtil.i("onFailure...." + error.getMessage());
                ToastUtil.showToast(VipActivity.this, "请求失败");
            }

        });

    }

    private void wxpayparam() {
        //http://hostname[:port]/mobile/user/wxpayparam.html
        HttpParams post_params = new HttpParams();
        post_params.putHeaders("tokenCode", tokenCode);
        post_params.putHeaders("tokenInfo", tokenInfo);
        post_params.putHeaders("Content-Type", "application/json");
        showProgress("正在加载...");
        RxVolley.jsonPost(StaticClass.domain + "/wxpayparam.html", post_params, new HttpCallback() {
            @Override
            public void onSuccess(String t) {
                dismissProgress();
                LogUtil.i("*************wxpayparam********sq****:" + t);
                if (!TextUtils.isEmpty(t)) {
                    try {
                        BaseEntity mBaseEntity = new Gson().fromJson(t, BaseEntity.class);
                        if (mBaseEntity != null) {
                            if (mBaseEntity.getStatus() == 100) {
                                WxpayParam mWxpayParam = new Gson().fromJson(t, WxpayParam.class);
                                if (mWxpayParam != null && mWxpayParam.getResult() != null) {

                                    WXPayInfoImpli wxPayInfoImpli = new WXPayInfoImpli();
                                    wxPayInfoImpli.setTimestamp(mWxpayParam.getResult().getTimestamp());
                                    wxPayInfoImpli.setSign(mWxpayParam.getResult().getSign());
                                    wxPayInfoImpli.setPrepayId(mWxpayParam.getResult().getPrepayid());
                                    wxPayInfoImpli.setPartnerid(mWxpayParam.getResult().getPartnerid());
                                    wxPayInfoImpli.setAppid(mWxpayParam.getResult().getAppid());
                                    wxPayInfoImpli.setNonceStr(mWxpayParam.getResult().getNoncestr());
                                    wxPayInfoImpli.setPackageValue(mWxpayParam.getResult().getPackagestr());

                                    wxpay("", wxPayInfoImpli);
                                }

                            } else {
                                ToastUtil.showToast(VipActivity.this, "获取失败");
                            }

                        } else {
                            ToastUtil.showToast(VipActivity.this, "返回数据异常");
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        ToastUtil.showToast(VipActivity.this, "返回数据异常");
                    }

                } else {
                    ToastUtil.showToast(VipActivity.this, "返回数据异常");
                }

            }

            @Override
            public void onFailure(VolleyError error) {
                dismissProgress();
                LogUtil.i("onFailure...." + error.getMessage());
                ToastUtil.showToast(VipActivity.this, "请求失败");
            }

        });
    }

    //消息推送通知收到事件
    @Subscribe
    public void onEvent(String name) {

        if (name.equals("EventBus_A")) {
            findViewById(R.id.rLbottom).setVisibility(View.VISIBLE);

        } else  if (name.equals("EventBus_JHM")) {
            finish();

        } else{

        }
    }


}
