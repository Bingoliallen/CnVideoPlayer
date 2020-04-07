package videoplayer.android.com.cnvideoplayer.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import com.flyco.tablayout.SlidingTabLayout;
import com.flyco.tablayout.listener.OnTabSelectListener;
import java.util.ArrayList;
import butterknife.Bind;
import videoplayer.android.com.cnvideoplayer.R;
import videoplayer.android.com.cnvideoplayer.base.BaseFragment;
import videoplayer.android.com.cnvideoplayer.utils.DisplayUtil;
import videoplayer.android.com.cnvideoplayer.utils.LogUtil;
import videoplayer.android.com.cnvideoplayer.utils.ShareUtils;

/**
 * Date: 2018/11/5
 * Author:
 * Email：
 * Des：
 */

public class CollectMainFragment extends BaseFragment implements OnTabSelectListener {

    private ArrayList<Fragment> mFragments = new ArrayList<>();
  //  private List<Channel> items = new ArrayList<>();
  private final String[] mTitles = { "女优", "AV", "视频"};
    private MyPagerAdapter mAdapter;
    /**
     * 字体加粗,大写
     */
    @Bind(R.id.tl_3)
    SlidingTabLayout tabLayout_3;
    @Bind(R.id.vp)
    ViewPager vp;

    private String tokenCode;
    private String tokenInfo;

    @Override
    protected int getLayoutResource() {
        return R.layout.fragment_home_main;
    }

    @Override
    public void initPresenter() {
        tokenCode = ShareUtils.getString(getActivity(), "tokenCode", "");
        tokenInfo = ShareUtils.getString(getActivity(), "tokenInfo", "");
    }
    CollectActorFragment fragmentOne0;
    CollectFragment fragmentOne1;
    CollectFragment fragmentOne2;
    @Override
    protected void initView() {
        int i = 0;
        for (String title : mTitles) {
            if(i==0){
                fragmentOne0 = new CollectActorFragment();
                Bundle bundle = new Bundle();
                //0-影片；1-视频；2-演员
                // bundle.putString("channelid", title.getChannelid());
                if(i==0){
                    bundle.putInt("num",2 );
                }else if(i==1){
                    bundle.putInt("num",0 );
                }else if(i==2){
                    bundle.putInt("num",1 );
                }
                i++;
                //fragment保存参数，传入一个Bundle对象
                fragmentOne0.setArguments(bundle);
                mFragments.add(fragmentOne0);
            }else  if(i==1){
                fragmentOne1= new CollectFragment();
                Bundle bundle = new Bundle();
                //0-影片；1-视频；2-演员
                // bundle.putString("channelid", title.getChannelid());
                if(i==0){
                    bundle.putInt("num",2 );
                }else if(i==1){
                    bundle.putInt("num",0 );
                }else if(i==2){
                    bundle.putInt("num",1 );
                }
                i++;
                //fragment保存参数，传入一个Bundle对象
                fragmentOne1.setArguments(bundle);
                mFragments.add(fragmentOne1);
            }else  if(i==2){
                fragmentOne2= new CollectFragment();
                Bundle bundle = new Bundle();
                //0-影片；1-视频；2-演员
                // bundle.putString("channelid", title.getChannelid());
                if(i==0){
                    bundle.putInt("num",2 );
                }else if(i==1){
                    bundle.putInt("num",0 );
                }else if(i==2){
                    bundle.putInt("num",1 );
                }
                i++;
                //fragment保存参数，传入一个Bundle对象
                fragmentOne2.setArguments(bundle);
                mFragments.add(fragmentOne2);
            }


        }
        mAdapter = new MyPagerAdapter(getChildFragmentManager());
        vp.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                LogUtil.e("--------onPageSelected333------------:"+position);
                CollectMainFragment.this.position=position;
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        vp.setAdapter(mAdapter);
        tabLayout_3.setOnTabSelectListener(CollectMainFragment.this);
        tabLayout_3.setTabWidth(DisplayUtil.px2dip(DisplayUtil.getScreenWidth(getActivity()) / 3));
        tabLayout_3.setIndicatorWidth(70);
        tabLayout_3.setViewPager(vp);



    }
    private boolean isFirst = true;
    @Override
    public void onResume() {
        super.onResume();
        if (!isFirst) {
            isFirst = false;

        } else {
            isFirst = false;
        }

    }
    int position=0;

    public void autoRefresh(){
        if(position==0){
            fragmentOne0.autoRefresh();
        }else   if(position==1){
            fragmentOne1.autoRefresh();
        }else   if(position==2){
            fragmentOne2.autoRefresh();
        }
    }
    @Override
    public void onTabSelect(int position) {
        LogUtil.e("--------onTabSelect111------------:"+position);
    }

    @Override
    public void onTabReselect(int position) {
        LogUtil.e("--------onTabReselect222------------:"+position);
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
            return mTitles[position];
        }

        @Override
        public Fragment getItem(int position) {
            return mFragments.get(position);
        }
    }


}

