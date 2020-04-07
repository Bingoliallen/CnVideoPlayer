package videoplayer.android.com.cnvideoplayer;

import android.Manifest;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Toast;

import com.flyco.tablayout.CommonTabLayout;
import com.flyco.tablayout.listener.CustomTabEntity;
import com.flyco.tablayout.listener.OnTabSelectListener;
import com.google.gson.Gson;
import com.kymjs.rxvolley.RxVolley;
import com.kymjs.rxvolley.client.HttpCallback;
import com.kymjs.rxvolley.client.HttpParams;
import com.kymjs.rxvolley.http.VolleyError;
import com.shuyu.gsyvideoplayer.utils.Debuger;
import com.umeng.analytics.MobclickAgent;

import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;
import permissions.dispatcher.PermissionUtils;
import videoplayer.android.com.cnvideoplayer.base.AppConstant;
import videoplayer.android.com.cnvideoplayer.entity.BaseEntity;
import videoplayer.android.com.cnvideoplayer.entity.TabEntity;
import videoplayer.android.com.cnvideoplayer.fragment.ClassifyFragment;
import videoplayer.android.com.cnvideoplayer.fragment.ClassifyMainFragment;
import videoplayer.android.com.cnvideoplayer.fragment.CollectFragment;
import videoplayer.android.com.cnvideoplayer.fragment.CollectMainFragment;
import videoplayer.android.com.cnvideoplayer.fragment.HomeMainFragment;
import videoplayer.android.com.cnvideoplayer.fragment.MineFragment;
import videoplayer.android.com.cnvideoplayer.fragment.VideoMainFragment;
import videoplayer.android.com.cnvideoplayer.ui.VipActivity;
import videoplayer.android.com.cnvideoplayer.utils.LogUtil;
import videoplayer.android.com.cnvideoplayer.utils.ShareUtils;
import videoplayer.android.com.cnvideoplayer.utils.StaticClass;
import videoplayer.android.com.cnvideoplayer.utils.TimeUtils;
import videoplayer.android.com.cnvideoplayer.view.StatusBarCompat;

public class MainActivity extends AppCompatActivity {
    /**
     * 入口
     * @param activity
     */
    public static void startAction(Activity activity){
        Intent intent = new Intent(activity, MainActivity.class);
        activity.startActivity(intent);
        activity.overridePendingTransition(R.anim.fade_in,R.anim.fade_out);
    }

    @Bind(R.id.tab_layout)
    CommonTabLayout tabLayout;

    private String[] mTitles = {"首页", "视频", "分类","收藏","我的"};
    private int[] mIconUnselectIds = {
            R.mipmap.home_sy,R.mipmap.video_sy,R.mipmap.home_c,R.mipmap.home_icon_sc,R.mipmap.home_mine};
    private int[] mIconSelectIds = {
            R.mipmap.home_sy_p,R.mipmap.video_sy_p,R.mipmap.home_c_p, R.mipmap.home_icon_sc_p,R.mipmap.home_mine_p};
    private ArrayList<CustomTabEntity> mTabEntities = new ArrayList<>();
    private static int tabLayoutHeight;
    private HomeMainFragment mHomeMainFragment;
    private VideoMainFragment mVideoMainFragment;

    private ClassifyMainFragment mClassifyMainFragment;
    private CollectMainFragment mCollectMainFragment;
    private MineFragment mMineFragment;
    final String[] permissions = {Manifest.permission.WRITE_EXTERNAL_STORAGE};
    private long lastClickTime = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        StatusBarCompat.setStatusBarColor(this, ContextCompat.getColor(this,R.color.main_color));

        Debuger.disable();
        boolean hadPermission = PermissionUtils.hasSelfPermissions(this, permissions);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && !hadPermission) {
            String[] permissions = {Manifest.permission.WRITE_EXTERNAL_STORAGE};
            requestPermissions(permissions, 1110);
        }
        //初始化菜单
        initTab();
        //初始化frament
        initFragment(savedInstanceState);
        tabLayout.measure(0,0);
        tabLayoutHeight=tabLayout.getMeasuredHeight();


    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        //奔溃前保存位置
        LogUtil.e("onSaveInstanceState进来了1");
        if (tabLayout != null) {
            LogUtil.e("onSaveInstanceState进来了2");
            outState.putInt(AppConstant.HOME_CURRENT_TAB_POSITION, tabLayout.getCurrentTab());
        }
    }
    /**
     * 初始化tab
     */
    private void initTab() {
        for (int i = 0; i < mTitles.length; i++) {
            mTabEntities.add(new TabEntity(mTitles[i], mIconSelectIds[i], mIconUnselectIds[i]));
        }
        tabLayout.setTabData(mTabEntities);
        //点击监听
        tabLayout.setOnTabSelectListener(new OnTabSelectListener() {
            @Override
            public void onTabSelect(int position) {
                SwitchTo(position);
            }
            @Override
            public void onTabReselect(int position) {
            }
        });
    }

    /**
     * 初始化碎片
     */
    private void initFragment(Bundle savedInstanceState) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        int currentTabPosition = 0;
        if (savedInstanceState != null) {
            mHomeMainFragment = (HomeMainFragment) getSupportFragmentManager().findFragmentByTag("mHomeMainFragment");
            mVideoMainFragment = (VideoMainFragment) getSupportFragmentManager().findFragmentByTag("mVideoMainFragment");
            mClassifyMainFragment = (ClassifyMainFragment) getSupportFragmentManager().findFragmentByTag("mClassifyMainFragment");
            mCollectMainFragment = (CollectMainFragment) getSupportFragmentManager().findFragmentByTag("mCollectMainFragment");
            mMineFragment = (MineFragment) getSupportFragmentManager().findFragmentByTag("mMineFragment");
            currentTabPosition = savedInstanceState.getInt(AppConstant.HOME_CURRENT_TAB_POSITION);
        } else {
            mHomeMainFragment = new HomeMainFragment();
            mVideoMainFragment = new VideoMainFragment();
            mClassifyMainFragment = new ClassifyMainFragment();
            mCollectMainFragment = new CollectMainFragment();
            mMineFragment = new MineFragment();

            transaction.add(R.id.fl_body, mHomeMainFragment, "mHomeMainFragment");
            transaction.add(R.id.fl_body, mVideoMainFragment, "mVideoMainFragment");

            transaction.add(R.id.fl_body, mClassifyMainFragment, "mClassifyMainFragment");
            transaction.add(R.id.fl_body, mCollectMainFragment, "mCollectMainFragment");
            transaction.add(R.id.fl_body, mMineFragment, "mMineFragment");
        }
        transaction.commit();
        SwitchTo(currentTabPosition);
        tabLayout.setCurrentTab(currentTabPosition);
    }

    /**
     * 切换
     */
    public void SwitchTo(int position) {
        LogUtil.d("主页菜单position" + position);
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        switch (position) {
            //首页
            case 0:
                transaction.hide(mVideoMainFragment);
                transaction.hide(mClassifyMainFragment);
                transaction.hide(mCollectMainFragment);
                transaction.hide(mMineFragment);
                transaction.show(mHomeMainFragment);
                transaction.commitAllowingStateLoss();
                break;
            //视频
            case 1:
                transaction.hide(mHomeMainFragment);
                transaction.hide(mClassifyMainFragment);
                transaction.hide(mCollectMainFragment);
                transaction.hide(mMineFragment);
                transaction.show(mVideoMainFragment);
                transaction.commitAllowingStateLoss();
                break;

            //分类
            case 2:
                transaction.hide(mHomeMainFragment);
                transaction.hide(mVideoMainFragment);
                transaction.hide(mCollectMainFragment);
                transaction.hide(mMineFragment);
                transaction.show(mClassifyMainFragment);
                transaction.commitAllowingStateLoss();
                break;
            //收藏
            case 3:
                transaction.hide(mHomeMainFragment);
                transaction.hide(mVideoMainFragment);
                transaction.hide(mClassifyMainFragment);
                transaction.hide(mMineFragment);
                transaction.show(mCollectMainFragment);
                transaction.commitAllowingStateLoss();
                mCollectMainFragment.autoRefresh();
                break;
            //我的
            case 4:
                transaction.hide(mHomeMainFragment);
                transaction.hide(mVideoMainFragment);
                transaction.hide(mClassifyMainFragment);
                transaction.hide(mCollectMainFragment);
                transaction.show(mMineFragment);
                transaction.commitAllowingStateLoss();
                break;
            default:
                break;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        MobclickAgent.onPageStart("");
        MobclickAgent.onResume(this);
    }



    @Override
    protected void onStop() {
        super.onStop();
        MobclickAgent.onPageEnd("");
        MobclickAgent.onPause(this);
        LogUtil.e("---------onStop----");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        setStatus();
        LogUtil.e("---------onDestroy----");
        ButterKnife.unbind(this);

    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        boolean sdPermissionResult = PermissionUtils.verifyPermissions(grantResults);
        if (!sdPermissionResult) {
            Toast.makeText(this, "没获取到sd卡权限", Toast.LENGTH_LONG).show();
        }
    }

    /**
     * 监听返回键
     *
     * @param keyCode
     * @param event
     * @return
     */
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        LogUtil.e("---------onKeyDown----"+keyCode);
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if ((System.currentTimeMillis() - lastClickTime) > 2000) {
                Toast.makeText(this, "再按一次退出程序", Toast.LENGTH_SHORT).show();
                lastClickTime = System.currentTimeMillis();
            } else {
                finish();
            }
            return false;
        }else if(keyCode == KeyEvent.KEYCODE_HOME){

        }
        return super.onKeyDown(keyCode, event);
    }


    //消息推送通知收到事件
    @Subscribe
    public void onEvent(String name) {

        if (name.equals("EventBus_A")){
            SwitchTo(1);
        }
    }


    public void setChangeTab(int pos){
        SwitchTo(pos);
        tabLayout.setCurrentTab(pos);
    }


    private void setStatus(){
        //http://serpro/mobile/user/status.html
        String tokenCode = ShareUtils.getString(MainActivity.this, "tokenCode", "");
        String tokenInfo = ShareUtils.getString(MainActivity.this, "tokenInfo", "");
        HttpParams post_params = new HttpParams();
        post_params.putHeaders("tokenCode", tokenCode);
        post_params.putHeaders("tokenInfo", tokenInfo);
        post_params.putHeaders("Content-Type", "application/json");
        Map<String, String> params = new HashMap<String, String>();
        params.put("status", ""+0);
        post_params.putJsonParams(new Gson().toJson(params));

        RxVolley.jsonPost(StaticClass.domain + "/status.html", post_params, new HttpCallback() {
            @Override
            public void onSuccess(String t) {

                LogUtil.i("*********0****status***sq****:" + t);
                if (!TextUtils.isEmpty(t)) {
                    try {
                        BaseEntity mBaseEntity = new Gson().fromJson(t, BaseEntity.class);
                        if (mBaseEntity != null) {
                            if (mBaseEntity.getStatus() == 100) {


                            } else {
                                //   ToastUtil.showToast(S.this, "获取失败");
                            }

                        } else {
                            //  ToastUtil.showToast(PolicyTermsActivity.this, "返回数据异常");
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        // ToastUtil.showToast(PolicyTermsActivity.this, "返回数据异常");
                    }

                } else {
                    //  ToastUtil.showToast(PolicyTermsActivity.this, "返回数据异常");
                }

            }

            @Override
            public void onFailure(VolleyError error) {

                LogUtil.i("onFailure...." + error.getMessage());
                // ToastUtil.showToast(PolicyTermsActivity.this, "请求失败");
            }

        });
    }




}
