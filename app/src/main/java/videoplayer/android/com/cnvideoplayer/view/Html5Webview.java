package videoplayer.android.com.cnvideoplayer.view;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Message;
import android.support.annotation.RequiresApi;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.ConsoleMessage;
import android.webkit.JsPromptResult;
import android.webkit.JsResult;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebResourceResponse;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;

import org.greenrobot.eventbus.EventBus;

import videoplayer.android.com.cnvideoplayer.utils.LogUtil;

/**
 * Date: 2018/8/26
 * Author:
 * Email：
 * Des：
 */

public class Html5Webview extends WebView {
    private ProgressView progressView;//进度条
    private Context context;

    public Html5Webview(Context context) {
        this(context, null);
    }

    public Html5Webview(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public Html5Webview(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;
        init();
    }

    private void init() {
        //初始化进度条
        progressView = new ProgressView(context);
        progressView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, dp2px(context, 4)));
        progressView.setColor(Color.BLUE);
        progressView.setProgress(10);
        //把进度条加到Webview中
        addView(progressView);
        //初始化设置
        initWebSettings();
        setWebChromeClient(new MyWebCromeClient());
        setWebViewClient(new MyWebviewClient());
    }

    private void initWebSettings() {
        WebSettings settings = getSettings();
        //默认是false 设置true允许和js交互

        settings.setJavaScriptEnabled(true);
        //  WebSettings.LOAD_DEFAULT 如果本地缓存可用且没有过期则使用本地缓存，否加载网络数据 默认值
        //  WebSettings.LOAD_CACHE_ELSE_NETWORK 优先加载本地缓存数据，无论缓存是否过期
        //  WebSettings.LOAD_NO_CACHE  只加载网络数据，不加载本地缓存
        //  WebSettings.LOAD_CACHE_ONLY 只加载缓存数据，不加载网络数据
        //Tips:有网络可以使用LOAD_DEFAULT 没有网时用LOAD_CACHE_ELSE_NETWORK
        settings.setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);
        //开启 DOM storage API 功能 较大存储空间，使用简单
        settings.setDomStorageEnabled(true);
        //开启 Application Caches 功能 方便构建离线APP 不推荐使用
        settings.setAppCacheEnabled(true);
        final String cachePath = context.getApplicationContext().getDir("cache", Context.MODE_PRIVATE).getPath();
        settings.setAppCachePath(cachePath);
        settings.setAppCacheMaxSize(5 * 1024 * 1024);
        //设置数据库缓存路径 存储管理复杂数据 方便对数据进行增加、删除、修改、查询 不推荐使用
        settings.setDatabaseEnabled(true);
        final String dbPath = context.getApplicationContext().getDir("db", Context.MODE_PRIVATE).getPath();
        settings.setDatabasePath(dbPath);

        settings.setSupportZoom(true); // 支持缩放
        //主要用于平板，针对特定屏幕代码调整分辨率
        DisplayMetrics metrics = new DisplayMetrics();
        ((Activity )context).getWindowManager().getDefaultDisplay().getMetrics(metrics);
        int mDensity = metrics.densityDpi;
        if (mDensity == 240) {
            settings.setDefaultZoom(WebSettings.ZoomDensity.FAR);
        } else if (mDensity == 160) {
            settings.setDefaultZoom(WebSettings.ZoomDensity.MEDIUM);
        } else if (mDensity == 120) {
            settings.setDefaultZoom(WebSettings.ZoomDensity.CLOSE);
        } else if (mDensity == DisplayMetrics.DENSITY_XHIGH) {
            settings.setDefaultZoom(WebSettings.ZoomDensity.FAR);
        } else if (mDensity == DisplayMetrics.DENSITY_TV) {
            settings.setDefaultZoom(WebSettings.ZoomDensity.FAR);
        } else {
            settings.setDefaultZoom(WebSettings.ZoomDensity.MEDIUM);
        }

        /**
         * 用WebView显示图片，可使用这个参数 设置网页布局类型：
         * 1、LayoutAlgorithm.NARROW_COLUMNS ：适应内容大小
         * 2、LayoutAlgorithm.SINGLE_COLUMN:适应屏幕，内容将自动缩放
         */
        settings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);


        //支持插件
      //  settings.setPluginsEnabled(true);

//设置自适应屏幕，两者合用
        settings.setUseWideViewPort(true); //将图片调整到适合webview的大小
        settings.setLoadWithOverviewMode(true); // 缩放至屏幕的大小

//缩放操作
        settings.setSupportZoom(true); //支持缩放，默认为true。是下面那个的前提。
        settings.setBuiltInZoomControls(true); //设置内置的缩放控件。若为false，则该WebView不可缩放
        settings.setDisplayZoomControls(false); //隐藏原生的缩放控件

//其他细节操作
        settings.setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK); //关闭webview中缓存
        settings.setAllowFileAccess(true); //设置可以访问文件
        settings.setJavaScriptCanOpenWindowsAutomatically(true); //支持通过JS打开新窗口
        settings.setLoadsImagesAutomatically(true); //支持自动加载图片
        settings.setDefaultTextEncodingName("utf-8");//设置编码格式


    }


    private class MyWebCromeClient extends WebChromeClient {
        @Override
        public void onProgressChanged(WebView view, int newProgress) {
            if (newProgress == 100) {
                //加载完毕进度条消失
                progressView.setVisibility(View.GONE);
                if(imgView!=null){
                    imgView.setVisibility(View.VISIBLE);
                }
                EventBus.getDefault().post("EventBus_A");
            } else {
                //更新进度
                progressView.setProgress(newProgress);
            }
            super.onProgressChanged(view, newProgress);
        }

        @Override
        public void onReceivedTitle(WebView view, String title) {
            LogUtil.e("TTT", "title is " + title);
            super.onReceivedTitle(view, title);
        }

        @Override
        public boolean onJsAlert(WebView view, String url, String message, JsResult result) {
            return super.onJsAlert(view, url, message, result);
        }

        @Override
        public boolean onConsoleMessage(ConsoleMessage consoleMessage) {
            return super.onConsoleMessage(consoleMessage);
        }

        @Override
        public boolean onCreateWindow(WebView view, boolean isDialog, boolean isUserGesture, Message resultMsg) {
            return super.onCreateWindow(view, isDialog, isUserGesture, resultMsg);
        }

        @Override
        public boolean onJsBeforeUnload(WebView view, String url, String message, JsResult result) {
            return super.onJsBeforeUnload(view, url, message, result);
        }

        @Override
        public boolean onJsConfirm(WebView view, String url, String message, JsResult result) {
            return super.onJsConfirm(view, url, message, result);
        }

        @Override
        public boolean onJsPrompt(WebView view, String url, String message, String defaultValue, JsPromptResult result) {
            return super.onJsPrompt(view, url, message, defaultValue, result);
        }

        @Override
        public void onCloseWindow(WebView window) {
            super.onCloseWindow(window);
        }

        @Override
        public boolean onShowFileChooser(WebView webView, ValueCallback<Uri[]> filePathCallback, FileChooserParams fileChooserParams) {
            return super.onShowFileChooser(webView, filePathCallback, fileChooserParams);
        }
    }

    private class MyWebviewClient extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            LogUtil.e("TTT", "shouldOverrideUrlLoading 0");
            //该方法在Build.VERSION_CODES.LOLLIPOP以前有效，从Build.VERSION_CODES.LOLLIPOP起，建议使用shouldOverrideUrlLoading(WebView, WebResourceRequest)} instead
            //返回false，意味着请求过程里，不管有多少次的跳转请求（即新的请求地址），均交给webView自己处理，这也是此方法的默认处理
            //返回true，说明你自己想根据url，做新的跳转，比如在判断url符合条件的情况下，我想让webView加载http://ask.csdn.net/questions/178242

           /* if (url.toString().contains("sina.cn")){
                view.loadUrl("http://ask.csdn.net/questions/178242");
                return true;
            }

            return false;
            */


          /*  Uri uri = Uri.parse(url);
            String scheme = uri.getScheme();
            if (TextUtils.isEmpty(scheme)) return true;
            if (scheme.equals("nativeapi")) {
                //如定义nativeapi://showImg是用来查看大图，这里添加查看大图逻辑
                return true;
            } else if (scheme.equals("http") || scheme.equals("https")) {
                //处理http协议
                return false;
               *//* if (Uri.parse(url).getHost().equals("www.example.com")) {
                    // 内部网址，不拦截，用自己的webview加载
                    return false;
                } else {
                    //跳转外部浏览器
                    Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                    context.startActivity(intent);
                    return true;
                }*//*
            }
            return super.shouldOverrideUrlLoading(view, url);*/
            view.loadUrl(url);
            return true;
        }

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
            LogUtil.e("TTT", "shouldOverrideUrlLoading 1");
            //view.loadUrl(request.getUrl().toString());
            return false;

        }


        @Override
        public WebResourceResponse shouldInterceptRequest(WebView view, String url) {
            LogUtil.e("TTT", "shouldInterceptRequest 0 url is " + url);
            //回调发生在子线程中,不能直接进行UI操作
            return super.shouldInterceptRequest(view, url);
        }

        @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
        @Override
        public WebResourceResponse shouldInterceptRequest(WebView view, WebResourceRequest request) {
            LogUtil.e("TTT", "shouldInterceptRequest 1 request url is " + request.getUrl().toString());
            return super.shouldInterceptRequest(view, request);
        }

        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            LogUtil.e("TTT", "onPageStarted");
            super.onPageStarted(view, url, favicon);
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            LogUtil.e("TTT", "onPageFinished");
            //这个是一定要加上那个的,配合scrollView和WebView的height=wrap_content属性使用
            int w = View.MeasureSpec.makeMeasureSpec(0,
                    View.MeasureSpec.UNSPECIFIED);
            int h = View.MeasureSpec.makeMeasureSpec(0,
                    View.MeasureSpec.UNSPECIFIED);
            //重新测量
            view.measure(w, h);
            EventBus.getDefault().post("EventBus_B");
        }

        @Override
        public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
            LogUtil.e("TTT", "onPageFinished");
            super.onReceivedError(view, request, error);
        }
    }

    /**
     * dp转换成px
     *
     * @param context Context
     * @param dp      dp
     * @return px值
     */
    private int dp2px(Context context, float dp) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dp * scale + 0.5f);
    }
    ImageView imgView;
    public void setImgView(ImageView imgView){
        this.imgView=imgView;
    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int mExpandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2, MeasureSpec.AT_MOST);
        super.onMeasure(widthMeasureSpec, mExpandSpec);
    }
}