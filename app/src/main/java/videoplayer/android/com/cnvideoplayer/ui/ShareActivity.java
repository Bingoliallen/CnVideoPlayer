package videoplayer.android.com.cnvideoplayer.ui;


import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;

import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.kymjs.rxvolley.RxVolley;
import com.kymjs.rxvolley.client.HttpCallback;
import com.kymjs.rxvolley.client.HttpParams;
import com.kymjs.rxvolley.http.VolleyError;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

import videoplayer.android.com.cnvideoplayer.R;
import videoplayer.android.com.cnvideoplayer.base.BaseCompatActivity;
import videoplayer.android.com.cnvideoplayer.entity.BaseEntity;
import videoplayer.android.com.cnvideoplayer.entity.Share;
import videoplayer.android.com.cnvideoplayer.utils.LogUtil;
import videoplayer.android.com.cnvideoplayer.utils.QRCodeUtil;
import videoplayer.android.com.cnvideoplayer.utils.ShareUtils;
import videoplayer.android.com.cnvideoplayer.utils.StaticClass;
import videoplayer.android.com.cnvideoplayer.utils.ToastUtil;
import videoplayer.android.com.cnvideoplayer.view.Html5Webview;
import videoplayer.android.com.cnvideoplayer.view.StatusBarCompat;

public class ShareActivity extends BaseCompatActivity {

    private WebView webView;
    private TextView tv_userno;
    private ImageView iv_QRCode;
    private String tokenCode;
    private String tokenInfo;
    private String userno;

    private String getUrl;
    private String link;

    private Button shareTo;
    private Bitmap mBitmap;

    @Override
    protected String initTitle() {
        return "分享推荐";
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_share;
    }

    @Override
    public void initView() {
        EventBus.getDefault().register(this);
        StatusBarCompat.setStatusBarColor(this, ContextCompat.getColor(this, R.color.main_color));
        tokenCode = ShareUtils.getString(ShareActivity.this, "tokenCode", "");
        tokenInfo = ShareUtils.getString(ShareActivity.this, "tokenInfo", "");
        userno = ShareUtils.getString(ShareActivity.this, "userno", "");

        iv_QRCode = (ImageView) this.findViewById(R.id.iv_QRCode);
        tv_userno = (TextView) this.findViewById(R.id.tv_userno);
        webView = (WebView) this.findViewById(R.id.webview);
        shareTo = (Button) this.findViewById(R.id.shareTo);
        shareTo.setOnClickListener(this);

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

        tv_userno.setText("我的邀请码："+ userno);

    }

    @Override
    public void initData() {
        getData();

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.shareTo:
                // commit();
                localshare(ShareActivity.this,mBitmap,"shareQRC");
                break;
        }
    }

    public void loadUrl() {
        if (webView != null) {
           // webView.setImgView(iv_QRCode);
            webView.loadUrl(getUrl);
        }
    }


    private Bitmap drawableToBitamp(Drawable drawable) {
        if (drawable == null) {
            return null;
        }
        Bitmap bitmap = null;
        BitmapDrawable bd = (BitmapDrawable) drawable;
        return bitmap = bd.getBitmap();
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

    private void getData() {
        //http://serpro/mobile/user/share.html
        HttpParams post_params = new HttpParams();
        post_params.putHeaders("tokenCode", tokenCode);
        post_params.putHeaders("tokenInfo", tokenInfo);
        post_params.putHeaders("Content-Type", "application/json");
        showProgress("正在加载...");
        RxVolley.jsonPost(StaticClass.domain + "/share.html", post_params, new HttpCallback() {
            @Override
            public void onSuccess(String t) {
                dismissProgress();
                LogUtil.i("*************share***sq****:" + t);
                if (!TextUtils.isEmpty(t)) {
                    try {
                        BaseEntity mBaseEntity = new Gson().fromJson(t, BaseEntity.class);
                        if (mBaseEntity != null) {
                            if (mBaseEntity.getStatus() == 100) {
                                Share mShare = new Gson().fromJson(t, Share.class);
                                if (mShare != null && mShare.getResult() != null) {
                                    link = mShare.getResult().getLink();
                                    mBitmap = QRCodeUtil.createQRCodeBitmap(link, 330, null);
                                    iv_QRCode.setImageBitmap(mBitmap);

                                    getUrl = mShare.getResult().getUrl();
                                    loadUrl();


                                }

                            } else {
                                ToastUtil.showToast(ShareActivity.this, "获取失败");
                            }

                        } else {
                            ToastUtil.showToast(ShareActivity.this, "返回数据异常");
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        ToastUtil.showToast(ShareActivity.this, "返回数据异常");
                    }

                } else {
                    ToastUtil.showToast(ShareActivity.this, "返回数据异常");
                }

            }

            @Override
            public void onFailure(VolleyError error) {
                dismissProgress();
                LogUtil.i("onFailure...." + error.getMessage());
                ToastUtil.showToast(ShareActivity.this, "请求失败");
            }

        });
    }

    //消息推送通知收到事件
    @Subscribe
    public void onEvent(String name) {

        if (name.equals("EventBus_A") || name.equals("EventBus_B")) {
            iv_QRCode.setVisibility(View.VISIBLE);

        } else {

        }
    }


    // Bitmap以文件File形式保存在本地
    private Uri saveBitmap(Bitmap bm, String picName) {
        try {
            String dir = Environment.getExternalStorageDirectory().getAbsolutePath() + "/CVPShare/" + picName + ".jpg";
            File f = new File(dir);
            if (!f.exists()) {
                f.getParentFile().mkdirs();
                f.createNewFile();
            }
            FileOutputStream out = new FileOutputStream(f);
            bm.compress(Bitmap.CompressFormat.JPEG, 80, out);
            out.flush();
            out.close();
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                Uri tempUri = FileProvider.getUriForFile(ShareActivity.this, "com.ansen.checkupdate.fileprovider", f);
                return tempUri;
            }else{
                Uri uri = Uri.fromFile(f);
                return uri;
            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void localshare(Context context, Bitmap bgimg0, String pic) {
           /* 分享图片
            */

        if (bgimg0 == null) {
            ToastUtil.showToast(this, "分享文件不存在");
            return;
        }
        Uri mUri = saveBitmap(bgimg0, pic);
        if (mUri == null) {
            ToastUtil.showToast(this, "分享文件不存在");
            return;
        }
        Intent share_intent = new Intent();
        share_intent.setAction(Intent.ACTION_SEND);//设置分享行为
        share_intent.setType("image/"); //设置分享内容的类型
        share_intent.putExtra(Intent.EXTRA_STREAM, mUri);
        share_intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION | Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
        //创建分享的Dialog
        share_intent = Intent.createChooser(share_intent, "分享图片");
        context.startActivity(share_intent);
    }


    public static void originalShareImage(Context context, ArrayList<File> files) {
        Intent share_intent = new Intent();
        ArrayList<Uri> imageUris = new ArrayList<Uri>();
        Uri uri;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            for (File f : files) {
                Uri imageContentUri = getImageContentUri(context, f);
                imageUris.add(imageContentUri);
            }
        } else {
            for (File f : files) {
                imageUris.add(Uri.fromFile(f));
            }

        }
        share_intent.setAction(Intent.ACTION_SEND_MULTIPLE);//设置分享行为
        share_intent.setType("image/png");//设置分享内容的类型
        share_intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION | Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
        share_intent.putParcelableArrayListExtra(Intent.EXTRA_STREAM, imageUris);
        context.startActivity(Intent.createChooser(share_intent, "Share"));

    }

    /**
     *
     * @param context
     * @param imageFile
     * @return content Uri
     */
    public static Uri getImageContentUri(Context context, File imageFile) {
        String filePath = imageFile.getAbsolutePath();
        Cursor cursor = context.getContentResolver().query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                new String[]{MediaStore.Images.Media._ID}, MediaStore.Images.Media.DATA + "=? ",
                new String[]{filePath}, null);
        if (cursor != null && cursor.moveToFirst()) {
            int id = cursor.getInt(cursor.getColumnIndex(MediaStore.MediaColumns._ID));
            Uri baseUri = Uri.parse("content://media/external/images/media");
            return Uri.withAppendedPath(baseUri, "" + id);
        } else {
            if (imageFile.exists()) {
                ContentValues values = new ContentValues();
                values.put(MediaStore.Images.Media.DATA, filePath);
                return context.getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
            } else {
                return null;
            }
        }
    }



}
