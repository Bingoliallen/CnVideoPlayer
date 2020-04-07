package videoplayer.android.com.cnvideoplayer.base;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.ColorRes;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.umeng.analytics.MobclickAgent;

import butterknife.ButterKnife;
import videoplayer.android.com.cnvideoplayer.R;
import videoplayer.android.com.cnvideoplayer.utils.MIUIUtil;

/**
 * Date: 2018/8/15
 * Author:
 * Email：
 * Des：
 */

public  abstract class BaseCompatActivity extends AppCompatActivity implements View.OnClickListener{

    protected BaseApplication abApplication = null;
    private ActionBar supportActionBar;
    private TextView tvTitle;
    ProgressDialog mProgressDialog;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);
        // 无标题
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        // 设置竖屏
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        abApplication = (BaseApplication) getApplication();
        setContentView(getLayoutId());
        initToolbar();
        initView();
        initData();

    }

    private void initToolbar() {
        // 适配MIUI6
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            if (Build.MANUFACTURER.equals("Xiaomi")) {
                MIUIUtil.setStatusBarIconDark(this);
            }
        }
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        if (toolbar != null) {
            toolbar.setTitle("");

            tvTitle = new TextView(this);
            tvTitle.setSingleLine();
            tvTitle.setEllipsize(TextUtils.TruncateAt.END);
            tvTitle.setGravity(Gravity.CENTER);
            tvTitle.setTextSize(18);// 20sp
            tvTitle.setTextColor(getResources().getColor(R.color.white));
            tvTitle.setText(initTitle());
            Toolbar.LayoutParams layoutParams = new Toolbar.LayoutParams(Toolbar.LayoutParams.WRAP_CONTENT, Toolbar.LayoutParams.WRAP_CONTENT, Gravity.CENTER);
            toolbar.addView(tvTitle, layoutParams);

            setSupportActionBar(toolbar);
            supportActionBar = getSupportActionBar();
            if (supportActionBar != null) {
                supportActionBar.setDisplayHomeAsUpEnabled(true);
                //   supportActionBar.setHomeAsUpIndicator(R.mipmap.ic_arrow_back);
            }
            setThemeColor(toolbar,R.color.colorPrimary, R.color.colorPrimaryDark);
            toolbar.setNavigationOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    finish();
                }
            });
        }
    }
    private void setThemeColor(Toolbar mToolbar,int colorPrimary, int colorPrimaryDark) {
        mToolbar.setBackgroundResource(colorPrimary);
        mToolbar.setTitleTextColor(ContextCompat.getColor(this, android.R.color.white));

        if (Build.VERSION.SDK_INT >= 21) {
            getWindow().setStatusBarColor(ContextCompat.getColor(this, colorPrimaryDark));
        }
        if (Build.VERSION.SDK_INT >= 23) {
            Window window = getWindow();
            int systemUiVisibility = window.getDecorView().getSystemUiVisibility();
            systemUiVisibility &= ~View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR;
            window.getDecorView().setSystemUiVisibility(systemUiVisibility);
        }
    }

    public  void setTitleFocusable(){
        if (tvTitle != null) {
            tvTitle.setFocusable(true);
            tvTitle.setFocusableInTouchMode(true);
            tvTitle.requestFocus();
        }

    }
    /**
     * 设置标题
     *
     * @param title
     */
    public void setTitle(String title) {
        if (tvTitle != null) {
            tvTitle.setText(title);
        }
    }

    public void setTitleColorRes(@ColorRes int colorRes) {
        if (tvTitle != null) {
            tvTitle.setTextColor(getResources().getColor(colorRes));
        }
    }

    //初始化标题
    protected abstract String initTitle();

    //获取布局文件
    public abstract int getLayoutId();

    //初始化view
    public abstract void initView();

    //初始化数据
    public abstract void initData();

    public void setThemeColor(int colorPrimaryDark) {

        if (Build.VERSION.SDK_INT >= 21) {
            getWindow().setStatusBarColor(ContextCompat.getColor(this, colorPrimaryDark));
        }
        if (Build.VERSION.SDK_INT >= 23) {
            Window window = getWindow();
            int systemUiVisibility = window.getDecorView().getSystemUiVisibility();
            systemUiVisibility &= ~View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR;
            window.getDecorView().setSystemUiVisibility(systemUiVisibility);
        }
    }

    public int getScreenWidth() {
        WindowManager wm = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics dm = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(dm);
        int width = dm.widthPixels;         // 屏幕宽度（像素）
        int height = dm.heightPixels;       // 屏幕高度（像素）
        float density = dm.density;         // 屏幕密度（0.75 / 1.0 / 1.5）
        int densityDpi = dm.densityDpi;     // 屏幕密度dpi（120 / 160 / 240）
        // 屏幕宽度算法:屏幕宽度（像素）/屏幕密度
        int screenWidth = (int) (width / density);  // 屏幕宽度(dp)
        int screenHeight = (int) (height / density);// 屏幕高度(dp)
        return width;
    }
    public int getScreenHeight() {
        WindowManager wm = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics dm = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(dm);
        int width = dm.widthPixels;         // 屏幕宽度（像素）
        int height = dm.heightPixels;       // 屏幕高度（像素）
        float density = dm.density;         // 屏幕密度（0.75 / 1.0 / 1.5）
        int densityDpi = dm.densityDpi;     // 屏幕密度dpi（120 / 160 / 240）
        // 屏幕宽度算法:屏幕宽度（像素）/屏幕密度
        int screenWidth = (int) (width / density);  // 屏幕宽度(dp)
        int screenHeight = (int) (height / density);// 屏幕高度(dp)
        return height;
    }
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_HOME) {
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }


    public void showProgress(String msg) {
        if (mProgressDialog == null) mProgressDialog = new ProgressDialog(this);
        mProgressDialog.setMessage(msg);
        if (!mProgressDialog.isShowing())
            mProgressDialog.show();
    }

    public void dismissProgress() {
        if (mProgressDialog != null && mProgressDialog.isShowing())
            mProgressDialog.dismiss();
    }

    public void setProgressCancelable(boolean isCancel) {
        if (mProgressDialog == null)
            mProgressDialog = new ProgressDialog(this);
        mProgressDialog.setCancelable(isCancel);
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();

        ButterKnife.unbind(this);

    }

    public void onResume() {
        super.onResume();
        MobclickAgent.onResume(this);
    }
    @Override
    public void onPause() {
        super.onPause();
        MobclickAgent.onPause(this);
    }


}

