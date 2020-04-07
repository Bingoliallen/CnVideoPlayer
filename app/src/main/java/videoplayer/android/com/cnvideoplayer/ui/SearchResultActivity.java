package videoplayer.android.com.cnvideoplayer.ui;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.flyco.tablayout.SlidingTabLayout;
import com.flyco.tablayout.listener.OnTabSelectListener;
import com.google.gson.Gson;
import com.kymjs.rxvolley.RxVolley;
import com.kymjs.rxvolley.client.HttpCallback;
import com.kymjs.rxvolley.client.HttpParams;
import com.kymjs.rxvolley.http.VolleyError;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import videoplayer.android.com.cnvideoplayer.R;
import videoplayer.android.com.cnvideoplayer.adpater.SearchAdapter;
import videoplayer.android.com.cnvideoplayer.adpater.SearchHotAdapter;
import videoplayer.android.com.cnvideoplayer.base.BaseCompatActivity;
import videoplayer.android.com.cnvideoplayer.entity.BaseEntity;
import videoplayer.android.com.cnvideoplayer.entity.HotData;
import videoplayer.android.com.cnvideoplayer.entity.Video;
import videoplayer.android.com.cnvideoplayer.entity.VideoEntity;
import videoplayer.android.com.cnvideoplayer.entity.VideoListEntity;
import videoplayer.android.com.cnvideoplayer.fragment.HotFragment;
import videoplayer.android.com.cnvideoplayer.utils.DisplayUtil;
import videoplayer.android.com.cnvideoplayer.utils.LogUtil;
import videoplayer.android.com.cnvideoplayer.utils.ShareUtils;
import videoplayer.android.com.cnvideoplayer.utils.StaticClass;
import videoplayer.android.com.cnvideoplayer.utils.ToastUtil;
import videoplayer.android.com.cnvideoplayer.view.GridSpacingItemDecoration;
import videoplayer.android.com.cnvideoplayer.view.StatusBarCompat;

public class SearchResultActivity extends BaseCompatActivity implements OnTabSelectListener {


    RecyclerView mRecyclerView1;
    RecyclerView mRecyclerView2;
    RecyclerView mRecyclerView3;

    EditText edSearch;
    TextView tvSX;
    //  private final String[] mTitles = {"最新", "排行", "自拍", "中文", "排行412", "排WAR行", "排行啊啊撒HAF"};
    private List<String> keys = new ArrayList<>();
    private final String[] mTitlesT = {"电影", "视频"};
    private ArrayList<Fragment> mFragments = new ArrayList<>();
    private MyPagerAdapter mAdapter;


    private SearchAdapter mSearchAdapter;


    private List<Video> mDataList1 = new ArrayList<>();
    private List<Video> mDataList2 = new ArrayList<>();


    private SearchHotAdapter mSearchHotAdapter1;
    private SearchHotAdapter mSearchHotAdapter2;

    private SlidingTabLayout tl_3;
    private ViewPager vp;

    private String tokenCode;
    private String tokenInfo;

    private LinearLayout layout1;
    private LinearLayout layout2;
    private NestedScrollView scrollView;

    private int pageNo = 1;
    private int pageSize = 12;
    private HotFragment fragmentOne1 ;
    private HotFragment fragmentOne2 ;

    private LinearLayout to_del;

    @Override
    protected String initTitle() {
        return "搜索结果";
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_search_result;
    }

    @Override
    public void initView() {
       StatusBarCompat.setStatusBarColor(this, ContextCompat.getColor(this, R.color.main_color));

        tokenCode = ShareUtils.getString(this, "tokenCode", "");
        tokenInfo = ShareUtils.getString(this, "tokenInfo", "");

        to_del = (LinearLayout) this.findViewById(R.id.to_del);
        to_del.setOnClickListener(this);
        scrollView = (NestedScrollView) this.findViewById(R.id.scrollView);
        layout1 = (LinearLayout) this.findViewById(R.id.layout1);
        layout2 = (LinearLayout) this.findViewById(R.id.layout2);

        tl_3 = (SlidingTabLayout) this.findViewById(R.id.tl_3);
        vp = (ViewPager) this.findViewById(R.id.vp);
        tl_3.setOnTabSelectListener(this);

        fragmentOne1 = new HotFragment();
        Bundle bundle = new Bundle();
        bundle.putString("type", "0");
        //fragment保存参数，传入一个Bundle对象
        fragmentOne1.setArguments(bundle);
        mFragments.add(fragmentOne1);


        fragmentOne2 = new HotFragment();
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


        mRecyclerView1 = (RecyclerView) this.findViewById(R.id.recycler_view_1);
        mRecyclerView2 = (RecyclerView) this.findViewById(R.id.recycler_view_2);
        mRecyclerView3 = (RecyclerView) this.findViewById(R.id.recycler_view_3);
        edSearch = (EditText) this.findViewById(R.id.edSearch);
        tvSX = (TextView) this.findViewById(R.id.tvSX);
        tvSX.requestFocus();
        tvSX.setOnClickListener(this);
        edSearch.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    scrollView.setVisibility(View.GONE);
                    layout2.setVisibility(View.VISIBLE);
                    if (!TextUtils.isEmpty(edSearch.getText().toString().trim())) {
                        //请求接口


                        boolean isR = true;
                        String search_keys = ShareUtils.getString(SearchResultActivity.this, "search_keys", "");
                        if (!TextUtils.isEmpty(search_keys)) {
                            String[] mT = search_keys.split(",");
                            for (String title : mT) {
                                if (!TextUtils.isEmpty(title)) {
                                    if (title.equals(edSearch.getText().toString().trim())) {
                                        isR = false;
                                        break;
                                    }
                                }

                            }
                        }

                        if (isR) {
                            ShareUtils.putString(SearchResultActivity.this, "search_keys", search_keys + "," + edSearch.getText().toString().trim());
                        }
                        fragmentOne1.setKey("0",edSearch.getText().toString().trim(),tokenCode,tokenInfo);
                        fragmentOne2.setKey("1",edSearch.getText().toString().trim(),tokenCode,tokenInfo);
                    }


                    return true;
                }
                return false;
            }
        });


        int spanCount = 3;//跟布局里面的spanCount属性是一致的
        int spacing = 20;//每一个矩形的间距
        boolean includeEdge = false;//如果设置成false那边缘地带就没有间距
        //设置每个item间距
        mRecyclerView1.setLayoutManager(new GridLayoutManager(this, 3));
        mRecyclerView1.addItemDecoration(new GridSpacingItemDecoration(spanCount, spacing, includeEdge));
        mRecyclerView1.setHasFixedSize(true);
        //设置适配器
        mSearchAdapter = new SearchAdapter(this, keys);
        mSearchAdapter.setOnItemClickListener(new SearchAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position, String data) {

                edSearch.setText(data);
                edSearch.setSelection(edSearch.getText().length());

                scrollView.setVisibility(View.GONE);
                layout2.setVisibility(View.VISIBLE);
                if (!TextUtils.isEmpty(edSearch.getText().toString().trim())) {
                    //请求接口


                    boolean isR = true;
                    String search_keys = ShareUtils.getString(SearchResultActivity.this, "search_keys", "");
                    if (!TextUtils.isEmpty(search_keys)) {
                        String[] mT = search_keys.split(",");
                        for (String title : mT) {
                            if (!TextUtils.isEmpty(title)) {
                                if (title.equals(edSearch.getText().toString().trim())) {
                                    isR = false;
                                    break;
                                }
                            }

                        }
                    }

                    if (isR) {
                        ShareUtils.putString(SearchResultActivity.this, "search_keys", search_keys + "," + edSearch.getText().toString().trim());
                    }
                    fragmentOne1.setKey("0",edSearch.getText().toString().trim(),tokenCode,tokenInfo);
                    fragmentOne2.setKey("1",edSearch.getText().toString().trim(),tokenCode,tokenInfo);
                }


            }
        });
        mRecyclerView1.setAdapter(mSearchAdapter);

        mRecyclerView2.setLayoutManager(new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL));
        mRecyclerView2.setHasFixedSize(true);
        mSearchHotAdapter1 = new SearchHotAdapter(this, mDataList1);
        mSearchHotAdapter1.setOnItemClickListener(new SearchHotAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position, Video data) {
                edSearch.setText(data.getName());
                edSearch.setSelection(edSearch.getText().length());


                if (!TextUtils.isEmpty(edSearch.getText().toString().trim())) {
                    //请求接口

                    boolean isR = true;
                    String search_keys = ShareUtils.getString(SearchResultActivity.this, "search_keys", "");
                    if (!TextUtils.isEmpty(search_keys)) {
                        String[] mT = search_keys.split(",");
                        for (String title : mT) {
                            if (!TextUtils.isEmpty(title)) {
                                if (title.equals(edSearch.getText().toString().trim())) {
                                    isR = false;
                                    break;
                                }
                            }

                        }
                    }

                    if (isR) {
                        ShareUtils.putString(SearchResultActivity.this, "search_keys", search_keys + "," + edSearch.getText().toString().trim());
                    }
                    VideoDetailActivity.startActivity(SearchResultActivity.this,data.getVideoid());
                }

            }
        });
        mRecyclerView2.setAdapter(mSearchHotAdapter1);

        mRecyclerView3.setLayoutManager(new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL));
        mRecyclerView3.setHasFixedSize(true);
        mSearchHotAdapter2 = new SearchHotAdapter(this, mDataList2);
        mSearchHotAdapter2.setOnItemClickListener(new SearchHotAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position, Video data) {
                edSearch.setText(data.getName());
                edSearch.setSelection(edSearch.getText().length());

                if (!TextUtils.isEmpty(edSearch.getText().toString().trim())) {
                    //请求接口


                    boolean isR = true;
                    String search_keys = ShareUtils.getString(SearchResultActivity.this, "search_keys", "");
                    if (!TextUtils.isEmpty(search_keys)) {
                        String[] mT = search_keys.split(",");
                        for (String title : mT) {
                            if (!TextUtils.isEmpty(title)) {
                                if (title.equals(edSearch.getText().toString().trim())) {
                                    isR = false;
                                    break;
                                }
                            }

                        }
                    }

                    if (isR) {
                        ShareUtils.putString(SearchResultActivity.this, "search_keys", search_keys + "," + edSearch.getText().toString().trim());
                    }
                    VideoDetailActivity.startActivity(SearchResultActivity.this, data.getVideoid());
                }
            }
        });
        mRecyclerView3.setAdapter(mSearchHotAdapter2);


    }

    @Override
    public void initData() {
        getMBLists();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tvSX:

                if (!TextUtils.isEmpty(edSearch.getText().toString().trim())) {
                    //请求接口
                    scrollView.setVisibility(View.GONE);
                    layout2.setVisibility(View.VISIBLE);

                    boolean isR = true;
                    String search_keys = ShareUtils.getString(SearchResultActivity.this, "search_keys", "");
                    if (!TextUtils.isEmpty(search_keys)) {
                        String[] mT = search_keys.split(",");
                        for (String title : mT) {
                            if (!TextUtils.isEmpty(title)) {
                                if (title.equals(edSearch.getText().toString().trim())) {
                                    isR = false;
                                    break;
                                }
                            }

                        }
                    }

                    if (isR) {
                        ShareUtils.putString(SearchResultActivity.this, "search_keys", search_keys + "," + edSearch.getText().toString().trim());
                    }
                    fragmentOne1.setKey("0",edSearch.getText().toString().trim(),tokenCode,tokenInfo);
                    fragmentOne2.setKey("1",edSearch.getText().toString().trim(),tokenCode,tokenInfo);
                }else{
                    ToastUtil.showToast(SearchResultActivity.this,"请输入搜索内容");
                }

                break;
            case R.id.to_del:
                ShareUtils.putString(this, "search_keys", "");
                keys.clear();
                mSearchAdapter.notifyDataSetChanged();

                break;
        }
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

    public void getMBLists() {
        //http://serpro/mobile/video/hots.html
        HttpParams post_params = new HttpParams();
        post_params.putHeaders("tokenCode", tokenCode);
        post_params.putHeaders("tokenInfo", tokenInfo);
        post_params.putHeaders("Content-Type", "application/json");
       // showProgress("正在加载...");
        RxVolley.jsonPost(StaticClass.domain1 + "/video/hots.html", post_params, new HttpCallback() {
            @Override
            public void onSuccess(String t) {
               // dismissProgress();
                LogUtil.i("*************hots**list*sq****:" + t);
                if (!TextUtils.isEmpty(t)) {
                    try {
                        BaseEntity mBaseEntity = new Gson().fromJson(t, BaseEntity.class);
                        if (mBaseEntity != null) {
                            if (mBaseEntity.getStatus() == 100) {
                                HotData mVideoList = new Gson().fromJson(t, HotData.class);
                                if (mVideoList != null && mVideoList.getResult() != null) {
                                    if (mVideoList.getResult().getMovie() != null) {
                                        //刷新
                                        mDataList1.clear();
                                        mDataList1.addAll(mVideoList.getResult().getMovie());
                                        mSearchHotAdapter1.notifyDataSetChanged();

                                    }
                                    if (mVideoList.getResult().getVideo() != null) {
                                        //刷新
                                        mDataList2.clear();
                                        mDataList2.addAll(mVideoList.getResult().getVideo());
                                        mSearchHotAdapter2.notifyDataSetChanged();

                                    }
                                }

                            } else {
                                ToastUtil.showToast(SearchResultActivity.this, "获取热门列表失败");
                            }

                        } else {
                            ToastUtil.showToast(SearchResultActivity.this, "返回数据异常");
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        ToastUtil.showToast(SearchResultActivity.this, "返回数据异常");
                    }

                } else {
                    ToastUtil.showToast(SearchResultActivity.this, "返回数据异常");
                }

            }

            @Override
            public void onFailure(VolleyError error) {
               // dismissProgress();
                LogUtil.i("onFailure...." + error.getMessage());
                ToastUtil.showToast(SearchResultActivity.this, "请求失败");
            }

        });
    }

    @Override
    public void onResume() {
        super.onResume();

        keys.clear();
        String search_keys = ShareUtils.getString(this, "search_keys", "");
        if (!TextUtils.isEmpty(search_keys)) {
            String[] mT = search_keys.split(",");
            for (String title : mT) {
                if (!TextUtils.isEmpty(title)) {
                    keys.add(title);
                }

            }
        }
        mSearchAdapter.notifyDataSetChanged();
    }
}
