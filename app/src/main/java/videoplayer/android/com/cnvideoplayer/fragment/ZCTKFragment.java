package videoplayer.android.com.cnvideoplayer.fragment;

import android.text.TextUtils;
import android.view.KeyEvent;
import android.webkit.WebSettings;
import android.webkit.WebView;

import com.google.gson.Gson;
import com.kymjs.rxvolley.RxVolley;
import com.kymjs.rxvolley.client.HttpCallback;
import com.kymjs.rxvolley.client.HttpParams;
import com.kymjs.rxvolley.http.VolleyError;

import butterknife.Bind;
import videoplayer.android.com.cnvideoplayer.R;
import videoplayer.android.com.cnvideoplayer.base.BaseFragment;
import videoplayer.android.com.cnvideoplayer.entity.BaseEntity;
import videoplayer.android.com.cnvideoplayer.entity.Share;
import videoplayer.android.com.cnvideoplayer.ui.PolicyTermsActivity;
import videoplayer.android.com.cnvideoplayer.utils.LogUtil;
import videoplayer.android.com.cnvideoplayer.utils.ShareUtils;
import videoplayer.android.com.cnvideoplayer.utils.StaticClass;
import videoplayer.android.com.cnvideoplayer.utils.ToastUtil;

/**
 * Date: 2018/11/10
 * Author:
 * Email：
 * Des：
 */

public class ZCTKFragment extends BaseFragment{

    @Bind(R.id.webview)
    WebView webView;
    private String getUrl;
    private String tokenCode;
    private String tokenInfo;


    @Override
    protected int getLayoutResource() {
        return R.layout.fragment_webview;
    }

    @Override
    public void initPresenter() {
        tokenCode = ShareUtils.getString(getActivity(), "tokenCode", "");
        tokenInfo = ShareUtils.getString(getActivity(), "tokenInfo", "");
    }

    @Override
    protected void initView() {

        WebSettings webSettings = webView.getSettings();
        //如果访问的页面中要与Javascript交互，则webview必须设置支持Javascript
        webSettings.setJavaScriptEnabled(true);
        // 若加载的 html 里有JS 在执行动画等操作，会造成资源浪费（CPU、电量）
        // 在 onStop 和 onResume 里分别把 setJavaScriptEnabled() 给设置成 false 和 true 即可
        //支持插件
        //  webSettings.setPluginsEnabled(true);

        //设置自适应屏幕，两者合用
        webSettings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);

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



        getData();
    }

    public void loadUrl() {
        if (webView != null) {
            webView.loadUrl(getUrl);
        }
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
/*
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
   */


    private void getData() {
        //http://serpro/mobile/user/policy.html
        HttpParams post_params = new HttpParams();
        post_params.putHeaders("tokenCode", tokenCode);
        post_params.putHeaders("tokenInfo", tokenInfo);
        post_params.putHeaders("Content-Type", "application/json");
        startProgressDialog("正在加载...");
        RxVolley.jsonPost(StaticClass.domain + "/policy.html", post_params, new HttpCallback() {
            @Override
            public void onSuccess(String t) {
                stopProgressDialog();
                LogUtil.i("*************policy***sq****:" + t);
                if (!TextUtils.isEmpty(t)) {
                    try {
                        BaseEntity mBaseEntity = new Gson().fromJson(t, BaseEntity.class);
                        if (mBaseEntity != null) {
                            if (mBaseEntity.getStatus() == 100) {
                                Share mShare = new Gson().fromJson(t, Share.class);
                                if (mShare != null && mShare.getResult() != null) {

                                    getUrl= mShare.getResult().getUrl();
                                    loadUrl();

                                }

                            } else {
                                ToastUtil.showToast(getActivity(), "获取失败");
                            }

                        } else {
                            ToastUtil.showToast(getActivity(), "返回数据异常");
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        ToastUtil.showToast(getActivity(), "返回数据异常");
                    }

                } else {
                    ToastUtil.showToast(getActivity(), "返回数据异常");
                }

            }

            @Override
            public void onFailure(VolleyError error) {
                stopProgressDialog();
                LogUtil.i("onFailure...." + error.getMessage());
                ToastUtil.showToast(getActivity(), "请求失败");
            }

        });
    }



}
