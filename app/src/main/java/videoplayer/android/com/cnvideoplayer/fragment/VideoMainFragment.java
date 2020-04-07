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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
 * Date: 2018/10/29
 * Author:
 * Email：
 * Des：
 */

public class VideoMainFragment extends BaseFragment implements OnTabSelectListener {

    private ArrayList<Fragment> mFragments = new ArrayList<>();
    private List<Channel> items=new ArrayList<>();
    private MyPagerAdapter mAdapter;
    /** 字体加粗,大写 */
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
        //http://serpro/mobile/video/channel.html

        HttpParams post_params = new HttpParams();
        post_params.putHeaders("tokenCode", tokenCode);
        post_params.putHeaders("tokenInfo", tokenInfo);
        post_params.putHeaders("Content-Type", "application/json");
        Map<String, String> params = new HashMap<String, String>();
        params.put("vtype", "1");
        post_params.putJsonParams(new Gson().toJson(params));


        RxVolley.jsonPost(StaticClass.domain1 + "/video/channel.html", post_params, new HttpCallback() {
            @Override
            public void onSuccess(String t) {
                LogUtil.i("**********视频***video**channel*sq****:" + t);

                if (!TextUtils.isEmpty(t)) {
                    try {
                        BaseEntity mBaseEntity = new Gson().fromJson(t, BaseEntity.class);
                        if (mBaseEntity != null) {
                            if (mBaseEntity.getStatus() == 100) {
                                ChannelData mUserInfo = new Gson().fromJson(t, ChannelData.class);
                                if (mUserInfo != null && mUserInfo.getResult() != null && mUserInfo.getResult().getItems()!=null) {
                                    items.clear();
                                    items.addAll(mUserInfo.getResult().getItems());

                                    for (Channel title : items) {
                                        VideoFragment fragmentOne = new VideoFragment();
                                        Bundle bundle = new Bundle();
                                        bundle.putString("channelid", title.getChannelid());
                                        //fragment保存参数，传入一个Bundle对象
                                        fragmentOne.setArguments(bundle);
                                        mFragments.add(fragmentOne);

                                    }
                                    mAdapter = new MyPagerAdapter(getChildFragmentManager());
                                    vp.setAdapter(mAdapter);
                                    tabLayout_3.setOnTabSelectListener(VideoMainFragment.this);
                                    tabLayout_3.setTabWidth(DisplayUtil.px2dip(DisplayUtil.getScreenWidth(getActivity())/4));
                                    tabLayout_3.setViewPager(vp);
                                } else {

                                }

                            } else {

                            }

                        } else {

                        }
                    } catch (Exception e) {
                        e.printStackTrace();

                    }
                }
            }

            @Override
            public void onFailure(VolleyError error) {
                LogUtil.i("onFailure...." + error.getMessage());

            }

        });



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
            return items.get(position).getName();
        }

        @Override
        public Fragment getItem(int position) {
            return mFragments.get(position);
        }
    }


}

