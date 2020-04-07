package videoplayer.android.com.cnvideoplayer.ui;

import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;

import com.google.gson.Gson;
import com.kymjs.rxvolley.RxVolley;
import com.kymjs.rxvolley.client.HttpCallback;
import com.kymjs.rxvolley.client.HttpParams;
import com.kymjs.rxvolley.http.VolleyError;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.Bind;
import videoplayer.android.com.cnvideoplayer.R;
import videoplayer.android.com.cnvideoplayer.adpater.ClassifyAdapter;
import videoplayer.android.com.cnvideoplayer.base.BaseCompatActivity;
import videoplayer.android.com.cnvideoplayer.entity.BaseEntity;
import videoplayer.android.com.cnvideoplayer.entity.VideoEntity;
import videoplayer.android.com.cnvideoplayer.entity.VideoListEntity;
import videoplayer.android.com.cnvideoplayer.utils.LogUtil;
import videoplayer.android.com.cnvideoplayer.utils.ShareUtils;
import videoplayer.android.com.cnvideoplayer.utils.StaticClass;
import videoplayer.android.com.cnvideoplayer.utils.ToastUtil;
import videoplayer.android.com.cnvideoplayer.view.GridSpacingItemDecoration;
import videoplayer.android.com.cnvideoplayer.view.StatusBarCompat;

public class BrowseRecordsActivity extends BaseCompatActivity {

    SmartRefreshLayout mRefreshLayout;
    RecyclerView mRecyclerView;
    private List<VideoEntity> data=new ArrayList<>();
    private ClassifyAdapter mClassifyAdapter;

    private int pageNo=1;
    private int pageSize=12;

    private String tokenCode;
    private String tokenInfo;


    @Override
    protected String initTitle() {
        return "浏览记录";
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_browse_records;
    }

    @Override
    public void initView() {
        StatusBarCompat.setStatusBarColor(this, ContextCompat.getColor(this, R.color.main_color));
        mRefreshLayout = (SmartRefreshLayout) findViewById(R.id.normal_view1);
        mRecyclerView = (RecyclerView) findViewById(R.id.m_recycler_view1);

        int spanCount = 2;//跟布局里面的spanCount属性是一致的
        int spacing = 20;//每一个矩形的间距
        boolean includeEdge = false;//如果设置成false那边缘地带就没有间距
        //设置每个item间距
        mRecyclerView.setLayoutManager(new GridLayoutManager(this, 2));
        mRecyclerView.addItemDecoration(new GridSpacingItemDecoration(spanCount, spacing, includeEdge));
        //设置适配器
        mClassifyAdapter = new ClassifyAdapter(this, data);
        mRecyclerView.setAdapter(mClassifyAdapter);
        mRecyclerView.setHasFixedSize(true);
        setRefresh();

    }

    @Override
    public void initData() {
        tokenCode = ShareUtils.getString(BrowseRecordsActivity.this, "tokenCode", "");
        tokenInfo = ShareUtils.getString(BrowseRecordsActivity.this, "tokenInfo", "");
        mRefreshLayout.autoRefresh();
    }

    @Override
    public void onClick(View v) {

    }

    private void setRefresh() {
        mRefreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(RefreshLayout refreshLayout) {
                refresh();
                refreshLayout.finishRefresh(1000);
            }
        });
        mRefreshLayout.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(RefreshLayout refreshLayout) {

                loadMore();
                refreshLayout.finishLoadMore(1000);
            }
        });
    }

    private void refresh() {
        pageNo=1;
        getVideoLists(false);
    }

    private void loadMore() {
        pageNo++;
        getVideoLists(true);
    }
    private void getVideoLists(final boolean isMore) {
        //http://serpro/mobile/user/history.html

        HttpParams post_params = new HttpParams();
        post_params.putHeaders("tokenCode", tokenCode);
        post_params.putHeaders("tokenInfo", tokenInfo);
        post_params.putHeaders("Content-Type", "application/json");

        Map<String, String> params = new HashMap<String, String>();
        params.put("pageNo", ""+pageNo);
        params.put("pageSize", ""+pageSize);
        post_params.putJsonParams(new Gson().toJson(params));
        RxVolley.jsonPost(StaticClass.domain + "/history.html", post_params, new HttpCallback() {
            @Override
            public void onSuccess(String t) {

                LogUtil.i("*************history**list*sq****:" + t);
                if (!TextUtils.isEmpty(t)) {
                    try {
                        BaseEntity mBaseEntity = new Gson().fromJson(t, BaseEntity.class);
                        if (mBaseEntity != null) {
                            if (mBaseEntity.getStatus() == 100) {
                                VideoListEntity mVideoList = new Gson().fromJson(t, VideoListEntity.class);
                                if (mVideoList != null && mVideoList.getResult() != null) {
                                    if (mVideoList.getResult().getItems() != null) {
                                        if(!isMore){
                                            //刷新
                                            data.clear();
                                            data.addAll(mVideoList.getResult().getItems());
                                            mClassifyAdapter.notifyDataSetChanged();
                                        }else{
                                            //更多
                                            data.addAll(mVideoList.getResult().getItems());
                                            mClassifyAdapter.notifyDataSetChanged();
                                        }

                                    }
                                }

                            } else {
                                ToastUtil.showToast(BrowseRecordsActivity.this, "获取浏览列表失败");
                            }

                        } else {
                            ToastUtil.showToast(BrowseRecordsActivity.this, "返回数据异常");
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        ToastUtil.showToast(BrowseRecordsActivity.this, "返回数据异常");
                    }

                } else {
                    ToastUtil.showToast(BrowseRecordsActivity.this, "返回数据异常");
                }

            }

            @Override
            public void onFailure(VolleyError error) {
                LogUtil.i("onFailure...." + error.getMessage());
                ToastUtil.showToast(BrowseRecordsActivity.this, "请求失败");
            }

        });
    }
}
