package videoplayer.android.com.cnvideoplayer.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;

import com.flyco.tablayout.SlidingTabLayout;
import com.flyco.tablayout.listener.OnTabSelectListener;
import com.google.gson.Gson;
import com.kymjs.rxvolley.RxVolley;
import com.kymjs.rxvolley.client.HttpCallback;
import com.kymjs.rxvolley.client.HttpParams;
import com.kymjs.rxvolley.http.VolleyError;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import videoplayer.android.com.cnvideoplayer.R;
import videoplayer.android.com.cnvideoplayer.base.BaseFragment;
import videoplayer.android.com.cnvideoplayer.entity.BaseEntity;
import videoplayer.android.com.cnvideoplayer.entity.Channel;
import videoplayer.android.com.cnvideoplayer.entity.ChannelData;
import videoplayer.android.com.cnvideoplayer.utils.DisplayUtil;
import videoplayer.android.com.cnvideoplayer.utils.LogUtil;
import videoplayer.android.com.cnvideoplayer.utils.ShareUtils;
import videoplayer.android.com.cnvideoplayer.utils.StaticClass;

/**
 * Date: 2018/11/5
 * Author:
 * Email：
 * Des：
 */

public class ClassifyMainFragment extends BaseFragment implements OnTabSelectListener {

    private ArrayList<Fragment> mFragments = new ArrayList<>();
  //  private List<Channel> items = new ArrayList<>();
    private MyPagerAdapter mAdapter;
    private final String[] mTitles = { "女优列表", "AV分类", "视频分类"};
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


    @Override
    protected void initView() {
        int i = 0;
        for (String title : mTitles) {
            if(i == 0){
                ActorFragment fragmentOne = new ActorFragment();
                //http://serpro/mobile/video/actors.html
                Bundle bundle = new Bundle();
                //  bundle.putString("channelid", title.getChannelid());
              //  bundle.putInt("num", i++);
                //fragment保存参数，传入一个Bundle对象
                fragmentOne.setArguments(bundle);
                mFragments.add(fragmentOne);

            }else if(i == 1){
                MovieFragment fragmentOne = new MovieFragment();
                //http://serpro/mobile/video/actors.html
                Bundle bundle = new Bundle();
                //  bundle.putString("channelid", title.getChannelid());
              // bundle.putInt("num", i++);
                //fragment保存参数，传入一个Bundle对象
                fragmentOne.setArguments(bundle);
                mFragments.add(fragmentOne);

            }else{
                NewVideoFragment fragmentOne = new NewVideoFragment();
                //http://serpro/mobile/video/actors.html
                Bundle bundle = new Bundle();
                //  bundle.putString("channelid", title.getChannelid());
              /*  if(i == 1){
                    bundle.putInt("num",0);
                }else if(i == 2){
                    bundle.putInt("num",1);
                }*/

                //fragment保存参数，传入一个Bundle对象
                fragmentOne.setArguments(bundle);
                mFragments.add(fragmentOne);
            }
            i++;
        }

        mAdapter = new MyPagerAdapter(getChildFragmentManager());
        vp.setAdapter(mAdapter);
        tabLayout_3.setOnTabSelectListener(ClassifyMainFragment.this);
        tabLayout_3.setTabWidth(DisplayUtil.px2dip(DisplayUtil.getScreenWidth(getActivity()) / 3));
        tabLayout_3.setIndicatorWidth(70);
        tabLayout_3.setViewPager(vp);



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
            return mTitles[position];
        }

        @Override
        public Fragment getItem(int position) {
            return mFragments.get(position);
        }
    }


}
