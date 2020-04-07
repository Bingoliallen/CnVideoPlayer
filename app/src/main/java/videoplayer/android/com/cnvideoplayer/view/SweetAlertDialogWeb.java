package videoplayer.android.com.cnvideoplayer.view;

import android.app.Dialog;
import android.content.Context;
import android.support.annotation.ColorInt;
import android.support.annotation.StringRes;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.TextView;
import videoplayer.android.com.cnvideoplayer.R;

/**
 * Date: 2018/10/31
 * Author:
 * Email：
 * Des：
 */

public class SweetAlertDialogWeb {

    private Context context;
    private int titleIds;
    private int mTitleColorId;
    private String title;
    private String message;
    private boolean hasCancle;
    private TextView mContentTitle;
    private WebView mContentMessage;


    private Dialog mDialog;
    private CharSequence mNegativeButtonText;
    private CharSequence mPositiveButtonText;
    private OnDialogClickListener mNegativeButtonListener;
    private OnDialogClickListener mPositiveButtonListener;
    private TextView mLeftTxt;
    private TextView mRightTxt;
    private View mCenterLine;
    private String msg;

    public SweetAlertDialogWeb(Builder builder) {
        this.context = builder.mContext;
        this.titleIds = builder.mTitleResId;
        this.mTitleColorId = builder.mTitleColorId;
        this.title = builder.mTitle;
        this.message = builder.mMessage;
        this.hasCancle = builder.mHasCancleable;
        this.mNegativeButtonText = builder.mNegativeButtonText;
        this.mPositiveButtonText = builder.mPositiveButtonText;
        this.mNegativeButtonListener = builder.mNegativeButtonListener;
        this.mPositiveButtonListener = builder.mPositiveButtonListener;
        this.initView();
    }

    /**
     * 初始化布局文件
     */
    private void initView() {
        View rootView = LayoutInflater.from(context).inflate(R.layout.layout_dialog_web, null);
        mContentTitle = (TextView) rootView.findViewById(R.id.tv_dialog_title);
        mContentMessage = (WebView) rootView.findViewById(R.id.mWebView);
        WebSettings webSettings = mContentMessage.getSettings();
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
        // message
        mContentMessage.loadUrl(message);
        mLeftTxt = (TextView) rootView.findViewById(R.id.dialog_left_txt);
        mRightTxt = (TextView) rootView.findViewById(R.id.dialog_right_txt);
        mCenterLine = rootView.findViewById(R.id.dialog_line);
        // 定义Dialog布局和参数
        mDialog = new Dialog(context, R.style.Sweet_Alert_Dialog);
        updateDialogUI();
        mDialog.setContentView(rootView);
        mDialog.setCanceledOnTouchOutside(false);

        mDialog.show();
    }


    private void updateDialogUI() {
        // title resId
        if (titleIds != 0) {
            mContentTitle.setVisibility(View.VISIBLE);
            mContentTitle.setText(titleIds);
        }
        if(mTitleColorId !=0){
            mContentTitle.setTextColor(mTitleColorId);
        }
        // title
        if (hasNull(title)) {
            mContentTitle.setVisibility(View.VISIBLE);
            mContentTitle.setText(title);
        }



        // 默认显示取消按钮 自定义字体
        if (hasNull(mNegativeButtonText) || hasCancle) {
            mLeftTxt.setVisibility(View.VISIBLE);
            mLeftTxt.setText(hasCancle ? "取消" : mNegativeButtonText);
            mLeftTxt.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mDialog != null)
                        mDialog.dismiss();
                    if (!hasCancle)
                        mNegativeButtonListener.onClick(mDialog, 0, "");
                }
            });
        }

        //左侧文字为空,
        if (!hasNull(mNegativeButtonText) && !hasCancle && hasNull(mPositiveButtonText)) {
            mLeftTxt.setVisibility(View.GONE);
            mCenterLine.setVisibility(View.GONE);
        }

        if (hasNull(mPositiveButtonText)) {
            mRightTxt.setVisibility(View.VISIBLE);
            mRightTxt.setText(mPositiveButtonText);
            mRightTxt.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if (mDialog != null)
                        mDialog.dismiss();
                    if (null != mPositiveButtonListener) {
                        mPositiveButtonListener.onClick(mDialog, 1, msg);
                    }
                }
            });
        }
    }


    public boolean hasNull(CharSequence msg) {
        return !TextUtils.isEmpty(msg);
    }

    public static class Builder {
        private Context mContext;
        private int mTitleResId;
        private int mTitleColorId;
        private String mTitle;
        private String mMessage;
        private boolean mHasCancleable = true;
        private CharSequence mNegativeButtonText;
        private CharSequence mPositiveButtonText;
        private OnDialogClickListener mNegativeButtonListener;
        private OnDialogClickListener mPositiveButtonListener;

        public Builder(Context context) {
            this.mContext = context;
        }

        public Builder setTitle(@StringRes int titleId) {
            this.mTitleResId = titleId;
            return this;
        }
        public Builder setTitleColor(@ColorInt int mTitleColorId) {
            this.mTitleColorId = mTitleColorId;
            return this;
        }
        public Builder setTitle(String title) {
            this.mTitle = title;
            return this;
        }

        public Builder setMessage(String message) {
            this.mMessage = message;
            return this;
        }

        public Builder setCancelable(boolean hasCancleable) {
            this.mHasCancleable = hasCancleable;
            return this;
        }

        public Builder setNegativeButton(CharSequence text, final OnDialogClickListener listener) {
            this.mNegativeButtonText = text;
            mNegativeButtonListener = listener;
            return this;
        }

        public Builder setPositiveButton(CharSequence text, final OnDialogClickListener listener) {
            this.mPositiveButtonText = text;
            this.mPositiveButtonListener = listener;
            return this;
        }

        public SweetAlertDialogWeb show() {
            return new SweetAlertDialogWeb(this);
        }
    }

    public interface OnDialogClickListener {
        void onClick(Dialog dialog, int which, String msg);
    }
}
