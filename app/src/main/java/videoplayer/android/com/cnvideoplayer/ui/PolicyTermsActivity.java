package videoplayer.android.com.cnvideoplayer.ui;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;

import com.flyco.tablayout.SlidingTabLayout;
import com.flyco.tablayout.listener.OnTabSelectListener;
import com.google.gson.Gson;
import com.kymjs.rxvolley.RxVolley;
import com.kymjs.rxvolley.client.HttpCallback;
import com.kymjs.rxvolley.client.HttpParams;
import com.kymjs.rxvolley.http.VolleyError;

import java.util.ArrayList;

import videoplayer.android.com.cnvideoplayer.R;
import videoplayer.android.com.cnvideoplayer.base.BaseCompatActivity;
import videoplayer.android.com.cnvideoplayer.entity.BaseEntity;
import videoplayer.android.com.cnvideoplayer.entity.Share;
import videoplayer.android.com.cnvideoplayer.fragment.YSBKFragment;
import videoplayer.android.com.cnvideoplayer.fragment.ZCTKFragment;
import videoplayer.android.com.cnvideoplayer.utils.DisplayUtil;
import videoplayer.android.com.cnvideoplayer.utils.LogUtil;
import videoplayer.android.com.cnvideoplayer.utils.ShareUtils;
import videoplayer.android.com.cnvideoplayer.utils.StaticClass;
import videoplayer.android.com.cnvideoplayer.utils.ToastUtil;
import videoplayer.android.com.cnvideoplayer.view.Html5Webview;
import videoplayer.android.com.cnvideoplayer.view.StatusBarCompat;

public class PolicyTermsActivity extends BaseCompatActivity implements OnTabSelectListener {

    private SlidingTabLayout tl_3;
    private ViewPager vp;

    private final String[] mTitlesT = {"使用条款", "隐私权保护政策"};
    private ArrayList<Fragment> mFragments = new ArrayList<>();
    private MyPagerAdapter mAdapter;

    @Override
    protected String initTitle() {
        return "政策与条款";
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_policy_terms;
    }

    @Override
    public void initView() {
        StatusBarCompat.setStatusBarColor(this, ContextCompat.getColor(this, R.color.main_color));
        tl_3 = (SlidingTabLayout) this.findViewById(R.id.tl_3);
        vp = (ViewPager) this.findViewById(R.id.vp);
        tl_3.setOnTabSelectListener(this);
        ZCTKFragment  fragmentOne1 = new ZCTKFragment();
        Bundle bundle = new Bundle();
        bundle.putString("type", "0");
        //fragment保存参数，传入一个Bundle对象
        fragmentOne1.setArguments(bundle);
        mFragments.add(fragmentOne1);


        YSBKFragment fragmentOne2 = new YSBKFragment();
        Bundle bundle2 = new Bundle();
        bundle2.putString("type", "1");
        //fragment保存参数，传入一个Bundle对象
        fragmentOne2.setArguments(bundle2);
        mFragments.add(fragmentOne2);

        mAdapter = new MyPagerAdapter(getSupportFragmentManager());
        vp.setAdapter(mAdapter);
        tl_3.setOnTabSelectListener(this);
        tl_3.setTabWidth(DisplayUtil.px2dip(DisplayUtil.getScreenWidth(this) / 2));
        tl_3.setViewPager(vp);
    }

    @Override
    public void initData() {

    }

    @Override
    public void onClick(View v) {

    }



    @Override
    public void onTabSelect(int position) {

    }

    @Override
    public void onTabReselect(int position) {

    }


    private class MyPagerAdapter extends FragmentPagerAdapter {
        public MyPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public int getCount() {
            return mFragments.size();
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mTitlesT[position];
        }

        @Override
        public Fragment getItem(int position) {
            return mFragments.get(position);
        }
    }








}
