package videoplayer.android.com.cnvideoplayer.ui;


import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
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

import videoplayer.android.com.cnvideoplayer.R;
import videoplayer.android.com.cnvideoplayer.adpater.MBRecordsAdapter;
import videoplayer.android.com.cnvideoplayer.base.BaseCompatActivity;
import videoplayer.android.com.cnvideoplayer.entity.BaseEntity;
import videoplayer.android.com.cnvideoplayer.entity.MB;
import videoplayer.android.com.cnvideoplayer.entity.MBData;
import videoplayer.android.com.cnvideoplayer.utils.LogUtil;
import videoplayer.android.com.cnvideoplayer.utils.ShareUtils;
import videoplayer.android.com.cnvideoplayer.utils.StaticClass;
import videoplayer.android.com.cnvideoplayer.utils.ToastUtil;
import videoplayer.android.com.cnvideoplayer.view.StatusBarCompat;

public class MBRecordsActivity extends BaseCompatActivity {

    SmartRefreshLayout mRefreshLayout;
    RecyclerView mRecyclerView;
    MBRecordsAdapter mMBRecordsAdapter;
    private List<MB> data = new ArrayList<>();

  //  private int pageNo = 1;
  //  private int pageSize = 12;

    private String tokenCode;
    private String tokenInfo;

    @Override
    protected String initTitle() {
        return "秘币记录";
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_mbrecords;
    }

    @Override
    public void initView() {
        StatusBarCompat.setStatusBarColor(this, ContextCompat.getColor(this, R.color.main_color));
        mRefreshLayout = (SmartRefreshLayout) findViewById(R.id.normal_view1);
        mRecyclerView = (RecyclerView) findViewById(R.id.m_recycler_view1);

        mRecyclerView.setLayoutManager(new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL));
        mRecyclerView.setHasFixedSize(true);
        mMBRecordsAdapter = new MBRecordsAdapter(this, data);
        mRecyclerView.setAdapter(mMBRecordsAdapter);
        mRecyclerView.setHasFixedSize(true);
        setRefresh();
    }

    @Override
    public void initData() {
        tokenCode = ShareUtils.getString(MBRecordsActivity.this, "tokenCode", "");
        tokenInfo = ShareUtils.getString(MBRecordsActivity.this, "tokenInfo", "");

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
       // pageNo = 1;
        getMBLists();
    }

    private void loadMore() {
        //  pageNo++;
        // getVideoLists(true);
    }

    public void getMBLists() {
        //http://serpro/mobile/user/logs.html
        HttpParams post_params = new HttpParams();
        post_params.putHeaders("tokenCode", tokenCode);
        post_params.putHeaders("tokenInfo", tokenInfo);
        post_params.putHeaders("Content-Type", "application/json");

        // Map<String, String> params = new HashMap<String, String>();
        // params.put("pageNo", ""+pageNo);
        // params.put("pageSize", ""+pageSize);
        // post_params.putJsonParams(new Gson().toJson(params));
        showProgress("正在加载...");
        RxVolley.jsonPost(StaticClass.domain + "/logs.html", post_params, new HttpCallback() {
            @Override
            public void onSuccess(String t) {
                dismissProgress();
                LogUtil.i("*************logs**list*sq****:" + t);
                if (!TextUtils.isEmpty(t)) {
                    try {
                        BaseEntity mBaseEntity = new Gson().fromJson(t, BaseEntity.class);
                        if (mBaseEntity != null) {
                            if (mBaseEntity.getStatus() == 100) {
                                MBData mVideoList = new Gson().fromJson(t, MBData.class);
                                if (mVideoList != null && mVideoList.getResult() != null) {
                                    if (mVideoList.getResult().getItems() != null) {
                                        //刷新
                                        data.clear();
                                        data.addAll(mVideoList.getResult().getItems());
                                        mMBRecordsAdapter.notifyDataSetChanged();

                                    }
                                }

                            } else {
                                ToastUtil.showToast(MBRecordsActivity.this, "获取列表失败");
                            }

                        } else {
                            ToastUtil.showToast(MBRecordsActivity.this, "返回数据异常");
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        ToastUtil.showToast(MBRecordsActivity.this, "返回数据异常");
                    }

                } else {
                    ToastUtil.showToast(MBRecordsActivity.this, "返回数据异常");
                }

            }

            @Override
            public void onFailure(VolleyError error) {
                dismissProgress();
                LogUtil.i("onFailure...." + error.getMessage());
                ToastUtil.showToast(MBRecordsActivity.this, "请求失败");
            }

        });
    }

}
