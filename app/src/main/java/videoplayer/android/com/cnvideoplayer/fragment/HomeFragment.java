package videoplayer.android.com.cnvideoplayer.fragment;


import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.gongwen.marqueen.SimpleMF;
import com.gongwen.marqueen.SimpleMarqueeView;
import com.google.gson.Gson;
import com.kymjs.rxvolley.RxVolley;
import com.kymjs.rxvolley.client.HttpCallback;
import com.kymjs.rxvolley.client.HttpParams;
import com.kymjs.rxvolley.http.VolleyError;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.youth.banner.Banner;
import com.youth.banner.BannerConfig;
import com.youth.banner.Transformer;
import com.youth.banner.listener.OnBannerListener;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.Bind;
import q.rorbin.badgeview.DisplayUtil;
import videoplayer.android.com.cnvideoplayer.MainActivity;
import videoplayer.android.com.cnvideoplayer.R;
import videoplayer.android.com.cnvideoplayer.adpater.ArticleListAdapter;
import videoplayer.android.com.cnvideoplayer.base.BaseFragment;
import videoplayer.android.com.cnvideoplayer.entity.BannerData;
import videoplayer.android.com.cnvideoplayer.entity.BaseEntity;
import videoplayer.android.com.cnvideoplayer.entity.HomeData;
import videoplayer.android.com.cnvideoplayer.entity.VideoEntity;
import videoplayer.android.com.cnvideoplayer.entity.VideoListEntity;
import videoplayer.android.com.cnvideoplayer.ui.AcountBindingActivity;
import videoplayer.android.com.cnvideoplayer.ui.SearchResultActivity;
import videoplayer.android.com.cnvideoplayer.ui.SortListActivity;
import videoplayer.android.com.cnvideoplayer.ui.VideoDetailActivity;
import videoplayer.android.com.cnvideoplayer.ui.VipActivity;
import videoplayer.android.com.cnvideoplayer.ui.WebVideoDetailActivity;
import videoplayer.android.com.cnvideoplayer.utils.GlideImageLoader;
import videoplayer.android.com.cnvideoplayer.utils.LogUtil;
import videoplayer.android.com.cnvideoplayer.utils.ShareUtils;
import videoplayer.android.com.cnvideoplayer.utils.StaticClass;
import videoplayer.android.com.cnvideoplayer.utils.TimeUtils;
import videoplayer.android.com.cnvideoplayer.utils.ToastUtil;
import videoplayer.android.com.cnvideoplayer.view.GridSpacingItemDecoration;
import videoplayer.android.com.cnvideoplayer.view.SpacesItemDecoration;
import videoplayer.android.com.cnvideoplayer.view.SweetAlertDialog;
import videoplayer.android.com.cnvideoplayer.view.SweetAlertDialogWeb;

import static android.support.v7.widget.DividerItemDecoration.VERTICAL;

/**
 * Date: 2018/8/15
 * Author:
 * Email：
 * Des：
 */

public class HomeFragment extends BaseFragment implements ArticleListAdapter.OnItemClickListener {
    @Bind(R.id.normal_view)
    SmartRefreshLayout mRefreshLayout;
    @Bind(R.id.main_pager_recycler_view)
    RecyclerView mRecyclerView;
    @Bind(R.id.tvSX)
    TextView tvSX;

    @Bind(R.id.edSearch)
    TextView edSearch;

    private ArticleListAdapter mAdapter;
    private List<VideoEntity> mDataList = new ArrayList<>();
    private List<String> bannerIdList = new ArrayList<>();
    Banner mBanner;
    TextView bannerTitle;
    LinearLayout mHeaderGroup;


    private String tokenCode;
    private String tokenInfo;

    private String type="0";
    private String channelid;
    private int num;

    @Override
    protected int getLayoutResource() {
        return R.layout.fragment_home;
    }

    @Override
    public void initPresenter() {

    }

    @Override
    protected void initView() {
        num = getArguments().getInt("num",-1);
        channelid = getArguments().getString("channelid");
        tokenCode = ShareUtils.getString(getActivity(), "tokenCode", "");
        tokenInfo = ShareUtils.getString(getActivity(), "tokenInfo", "");
        tvSX.requestFocus();
        initRecyclerView();
        setRefresh();

        mRefreshLayout.autoRefresh();


        edSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(SearchResultActivity.class);
            }
        });
        tvSX.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              //  MainActivity mainActivity = (MainActivity) getActivity();
               // mainActivity.setChangeTab(2);

                SortListActivity.startActivity(getActivity(),0);
              //  EventBus.getDefault().post("EventBus_A");
            }
        });


    }
    LinearLayout simpleMarqueeView1;
    SimpleMarqueeView<String> simpleMarqueeView;
    private void initRecyclerView() {

        mAdapter = new ArticleListAdapter(getActivity(), mDataList);
        mAdapter.setOnItemClickListener(this);

        //   int spanCount = 3;//跟布局里面的spanCount属性是一致的
        //   int spacing = DisplayUtil.px2dp(getActivity(),10);//每一个矩形的间距
        //   boolean includeEdge = false;//如果设置成false那边缘地带就没有间距
        //设置每个item间距
        mRecyclerView.setLayoutManager(new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL));
        // mRecyclerView.addItemDecoration(new SpacesItemDecoration(spacing ,  spanCount));
        mRecyclerView.setHasFixedSize(true);
        //add head banner
        mHeaderGroup = ((LinearLayout) LayoutInflater.from(getActivity()).inflate(R.layout.head_banner, null));
        simpleMarqueeView1=(LinearLayout)   mHeaderGroup.findViewById(R.id.simpleMarqueeView1);
        simpleMarqueeView =(SimpleMarqueeView) mHeaderGroup.findViewById(R.id.simpleMarqueeView);
        if(num==0){
            simpleMarqueeView1.setVisibility(View.VISIBLE);
            final List<String> datas = Arrays.asList("");
            SimpleMF<String> marqueeFactory = new SimpleMF(getActivity());
            marqueeFactory.setData(datas);
            simpleMarqueeView.setMarqueeFactory(marqueeFactory);
            simpleMarqueeView.startFlipping();
        }


        mBanner = mHeaderGroup.findViewById(R.id.head_banner);
        bannerTitle = (TextView) mHeaderGroup.findViewById(R.id.bannerTitle);
        bannerTitle.setVisibility(View.VISIBLE);
        bannerTitle.setTextColor(getActivity().getResources().getColor(R.color.text_color));

        mAdapter.setHeaderView(mHeaderGroup);

        mRecyclerView.setAdapter(mAdapter);


    }

    public void showBannerData(List<BannerData> bannerDataList) {
        final List<String> mBannerTitleList = new ArrayList<>();
        final List<String> bannerImageList = new ArrayList<>();
        final List<Integer> bannerType = new ArrayList<>();
        final List<String> bannerUrl = new ArrayList<>();
        bannerIdList = new ArrayList<>();
        for (BannerData bannerData : bannerDataList) {
            bannerUrl.add(bannerData.getUrl());
            bannerType.add(bannerData.getType());
            mBannerTitleList.add(bannerData.getObjname());
            bannerImageList.add(bannerData.getImage());
            bannerIdList.add(bannerData.getObjid());
        }
        //设置样式,默认为:Banner.NOT_INDICATOR(不显示指示器和标题)
        //可选样式如下:
        //1. Banner.CIRCLE_INDICATOR    显示圆形指示器
        //2. Banner.NUM_INDICATOR   显示数字指示器
        //3. Banner.NUM_INDICATOR_TITLE 显示数字指示器和标题
        //4. Banner.CIRCLE_INDICATOR_TITLE  显示圆形指示器和标题


        //设置banner样式
        mBanner.setBannerStyle(BannerConfig.NUM_INDICATOR_TITLE);
        //设置图片加载器
        mBanner.setImageLoader(new GlideImageLoader());
        //设置图片集合
        mBanner.setImages(bannerImageList);
        //设置banner动画效果
        mBanner.setBannerAnimation(Transformer.DepthPage);
        //设置标题集合（当banner样式有显示title时）
        mBanner.setBannerTitles(mBannerTitleList);
        //设置自动轮播，默认为true
        mBanner.isAutoPlay(true);
        //设置轮播时间
        mBanner.setDelayTime(5000);
        //设置指示器位置（当banner模式中有指示器时）
        mBanner.setIndicatorGravity(BannerConfig.CENTER);

        mBanner.setOnBannerListener(new OnBannerListener() {
            @Override
            public void OnBannerClick(int position) {
                if (!TextUtils.isEmpty(bannerIdList.get(position))) {
                    if(bannerType.get(position)==1){
                        WebVideoDetailActivity.startActivity(getActivity(), bannerUrl.get(position), mBannerTitleList.get(position));
                    }else{
                        VideoDetailActivity.startActivity(getActivity(), bannerIdList.get(position));
                    }

                }

            }
        });
        //banner设置方法全部调用完毕时最后调用
        mBanner.start();

    }


    private void setRefresh() {
        mRefreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(RefreshLayout refreshLayout) {
                pageNo = 1;
                getBannerData();
                getViedosData(false);


                refreshLayout.finishRefresh(1000);
            }
        });
        mRefreshLayout.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(RefreshLayout refreshLayout) {

              //  mDataList.add(new VideoEntity());
              //  mAdapter.notifyDataSetChanged();

                //loadMore();
                refreshLayout.finishLoadMore(1000);
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        if (mBanner != null) {
            mBanner.startAutoPlay();
        }
    }

    boolean isFisrt=true;
    @Override
    public void onStop() {
        super.onStop();
        if (mBanner != null) {
            mBanner.stopAutoPlay();
        }
    }

    @Override
    public void onItemClick(int position, VideoEntity data) {
        VideoDetailActivity.startActivity(getActivity(), data.getVideoid());
    }


    private void getBannerData() {
        //http://serpro/mobile/index.html
        HttpParams post_params = new HttpParams();
        // Map<String, String> params = new HashMap<String, String>();
        post_params.putHeaders("tokenCode", tokenCode);
        post_params.putHeaders("tokenInfo", tokenInfo);
        post_params.putHeaders("Content-Type", "application/json");
        // post_params.putJsonParams(new Gson().toJson(params));


        RxVolley.jsonPost(StaticClass.domain1 + "/index.html", post_params, new HttpCallback() {
            @Override
            public void onSuccess(String t) {

                LogUtil.i("*************getBannerData***sq****:" + t);
                if (!TextUtils.isEmpty(t)) {
                    try {
                        BaseEntity mBaseEntity = new Gson().fromJson(t, BaseEntity.class);
                        if (mBaseEntity != null) {
                            if (mBaseEntity.getStatus() == 100) {
                                HomeData mHomeData = new Gson().fromJson(t, HomeData.class);
                                if (mHomeData != null && mHomeData.getResult() != null) {
                                    if (mHomeData.getResult().getBanners() != null) {

                                        showBannerData(mHomeData.getResult().getBanners());
                                    } else {
                                        List<BannerData> bannerDataList = new ArrayList<>();
                                        bannerDataList.add(new BannerData());
                                        showBannerData(bannerDataList);
                                    }

                                    if(num==0){

                                        final List<String> datas = Arrays.asList(mHomeData.getResult().getNotice());
                                        SimpleMF<String> marqueeFactory = new SimpleMF(getActivity());
                                        marqueeFactory.setData(datas);
                                        simpleMarqueeView.setMarqueeFactory(marqueeFactory);
                                        simpleMarqueeView.startFlipping();

                                        if(isFisrt==true){

                                            if(mHomeData.getResult().getIndexFlag()==1  ){
                                                showOpenShow();
                                               /* new SweetAlertDialogWeb.Builder(getActivity())
                                                        .setMessage(mHomeData.getResult().getIndexUrl())
                                                        .setCancelable(false)
                                                        .setPositiveButton("确定", new SweetAlertDialogWeb.OnDialogClickListener() {
                                                            @Override
                                                            public void onClick(Dialog dialog, int which , String msg) {
                                                                showOpenShow();

                                                            }
                                                        }).show();*/

                                            }else{
                                                showOpenShow();
                                            }
                                            isFisrt=false;
                                        }



                                    }
                                    /*if (mHomeData.getResult().getItems() != null) {
                                        mDataList.clear();
                                        mDataList.addAll(mHomeData.getResult().getItems());
                                        mAdapter.notifyDataSetChanged();
                                    }*/

                                } else {
                                    ToastUtil.showToast(getActivity(), "暂无数据");
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
                LogUtil.i("onFailure...." + error.getMessage());
                ToastUtil.showToast(getActivity(), "请求失败");
            }

        });
    }

    private void showOpenShow() {

        int exdays= ShareUtils.getInt(getActivity(), "exdays",0);
        int usertype= ShareUtils.getInt(getActivity(), "usertype",0);  // 0-普通用户；1-试用会员；2-正式会员
        String extime = ShareUtils.getString(getActivity(), "extime", "");
        if(usertype==2){
            if(!TextUtils.isEmpty(extime) && TimeUtils.isAfter(
                    TimeUtils.getDateTimeForStr(TimeUtils.DF_YYYY_MM_DD_HH_MM_SS,extime),TimeUtils.getCurrentDate(TimeUtils.DF_YYYY_MM_DD_HH_MM_SS))){

                new AlertDialog.Builder(getActivity())
                        .setMessage("您的VIP会员到期时间剩余"+ exdays+"天！")
                        .setPositiveButton("立即续费", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                String email = ShareUtils.getString(getActivity(), "email", "");
                                String mobile = ShareUtils.getString(getActivity(), "mobile", "");
                                if(!TextUtils.isEmpty(email) || !TextUtils.isEmpty(mobile)){
                                    Intent mIntent=new Intent(getActivity(), VipActivity.class);
                                    mIntent.putExtra("mode",0);
                                    startActivityForResult(mIntent, 1105);
                                }else{
                                    ToastUtil.showToast(getActivity(),"您还没绑定任何账号，请先绑定！");
                                    Intent mIntent=new Intent(getActivity(), AcountBindingActivity.class);
                                    mIntent.putExtra("mode",0);
                                    startActivityForResult(mIntent, 1106);
                                }

                            }
                        }).setNegativeButton("取消", null).show();
            }else{//gq
                new AlertDialog.Builder(getActivity())
                        .setMessage("您的会员已过期，成为VIP，内容无限看！")
                        .setPositiveButton("立即续费", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                String email = ShareUtils.getString(getActivity(), "email", "");
                                String mobile = ShareUtils.getString(getActivity(), "mobile", "");
                                if(!TextUtils.isEmpty(email) || !TextUtils.isEmpty(mobile)){
                                    Intent mIntent=new Intent(getActivity(), VipActivity.class);
                                    mIntent.putExtra("mode",0);
                                    startActivityForResult(mIntent, 1105);
                                }else{
                                    ToastUtil.showToast(getActivity(),"您还没绑定任何账号，请先绑定！");
                                    Intent mIntent=new Intent(getActivity(), AcountBindingActivity.class);
                                    mIntent.putExtra("mode",0);
                                    startActivityForResult(mIntent, 1106);
                                }
                            }
                        }).setNegativeButton("取消", null).show();
            }

        }else if(usertype==1){
            new AlertDialog.Builder(getActivity())
                    .setMessage("您是试用会员，可以免费试看一天！")
                    .setPositiveButton("VIP会员，内容无限看", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            String email = ShareUtils.getString(getActivity(), "email", "");
                            String mobile = ShareUtils.getString(getActivity(), "mobile", "");
                            if(!TextUtils.isEmpty(email) || !TextUtils.isEmpty(mobile)){
                                Intent mIntent=new Intent(getActivity(), VipActivity.class);
                                mIntent.putExtra("mode",0);
                                startActivityForResult(mIntent, 1105);
                            }else{
                                ToastUtil.showToast(getActivity(),"您还没绑定任何账号，请先绑定！");
                                Intent mIntent=new Intent(getActivity(), AcountBindingActivity.class);
                                mIntent.putExtra("mode",0);
                                startActivityForResult(mIntent, 1106);
                            }
                        }
                    }).setNegativeButton("取消", null).show();

        }else {
            new AlertDialog.Builder(getActivity())
                    .setMessage("您还不是会员，成为会员，内容无限看！")
                    .setPositiveButton("立即激活", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            String email = ShareUtils.getString(getActivity(), "email", "");
                            String mobile = ShareUtils.getString(getActivity(), "mobile", "");
                            if(!TextUtils.isEmpty(email) || !TextUtils.isEmpty(mobile)){
                                Intent mIntent=new Intent(getActivity(), VipActivity.class);
                                mIntent.putExtra("mode",0);
                                startActivityForResult(mIntent, 1105);
                            }else{
                                ToastUtil.showToast(getActivity(),"您还没绑定任何账号，请先绑定！");
                                Intent mIntent=new Intent(getActivity(), AcountBindingActivity.class);
                                mIntent.putExtra("mode",0);
                                startActivityForResult(mIntent, 1106);
                            }
                        }
                    }).setNegativeButton("取消", null).show();
        }



    }


    private int pageNo = 1;
    private int pageSize = 12;
    private void loadMore() {
        pageNo++;
        getViedosData(true);
    }

    private void getViedosData(final boolean isMore) {
        //http://serpro/mobile/video/list.html

        HttpParams post_params = new HttpParams();

        post_params.putHeaders("tokenCode", tokenCode);
        post_params.putHeaders("tokenInfo", tokenInfo);
        post_params.putHeaders("Content-Type", "application/json");
         Map<String, String> params = new HashMap<String, String>();
        params.put("key", "");
        params.put("actorid", "");
        params.put("sortsid", "");
        params.put("order", "");
        params.put("pageNo", "" + pageNo);
        params.put("pageSize", "" + pageSize);

        params.put("type", type);
        params.put("channelid", channelid);
        post_params.putJsonParams(new Gson().toJson(params));


        RxVolley.jsonPost(StaticClass.domain1 + "/video/list.html", post_params, new HttpCallback() {
            @Override
            public void onSuccess(String t) {

                LogUtil.i("*************getViedosData**list*sq****:" + t);
                if (!TextUtils.isEmpty(t)) {
                    try {
                        BaseEntity mBaseEntity = new Gson().fromJson(t, BaseEntity.class);
                        if (mBaseEntity != null) {
                            if (mBaseEntity.getStatus() == 100) {
                                VideoListEntity mVideoList = new Gson().fromJson(t, VideoListEntity.class);
                                if (mVideoList != null && mVideoList.getResult() != null) {
                                    if (mVideoList.getResult().getItems() != null) {
                                        if (!isMore) {
                                            //刷新
                                            mDataList.clear();
                                            mDataList.addAll(mVideoList.getResult().getItems());
                                            mAdapter.notifyDataSetChanged();

                                        } else {
                                            //更多
                                            mDataList.addAll(mVideoList.getResult().getItems());
                                            mAdapter.notifyDataSetChanged();
                                        }

                                    }
                                }

                            } else {
                                ToastUtil.showToast(getActivity(), "获取视频列表失败");
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
                LogUtil.i("onFailure...." + error.getMessage());
                ToastUtil.showToast(getActivity(), "请求失败");
            }

        });
    }

}

