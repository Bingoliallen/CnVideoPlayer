package videoplayer.android.com.cnvideoplayer.fragment;

import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import com.google.gson.Gson;
import com.kymjs.rxvolley.RxVolley;
import com.kymjs.rxvolley.client.HttpCallback;
import com.kymjs.rxvolley.client.HttpParams;
import com.kymjs.rxvolley.http.VolleyError;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import butterknife.Bind;
import videoplayer.android.com.cnvideoplayer.MainActivity;
import videoplayer.android.com.cnvideoplayer.R;
import videoplayer.android.com.cnvideoplayer.adpater.CollectActorAdaper;
import videoplayer.android.com.cnvideoplayer.base.BaseFragment;
import videoplayer.android.com.cnvideoplayer.entity.BaseEntity;
import videoplayer.android.com.cnvideoplayer.entity.Collect;
import videoplayer.android.com.cnvideoplayer.entity.CollectListEntity;
import videoplayer.android.com.cnvideoplayer.utils.LogUtil;
import videoplayer.android.com.cnvideoplayer.utils.ShareUtils;
import videoplayer.android.com.cnvideoplayer.utils.StaticClass;
import videoplayer.android.com.cnvideoplayer.utils.ToastUtil;
import videoplayer.android.com.cnvideoplayer.view.GridSpacingItemDecoration;

/**
 * Date: 2018/11/8
 * Author:
 * Email：
 * Des：
 */

public class CollectActorFragment  extends BaseFragment {
    /* @Bind(R.id.ntb)
     NormalTitleBar ntb;*/
    @Bind(R.id.normal_view)
    SmartRefreshLayout mRefreshLayout;
    @Bind(R.id.m_recycler_view)
    RecyclerView mRecyclerView;

    @Bind(R.id.btn_more)
    Button btn_more;
    @Bind(R.id.mZ)
    LinearLayout mZ;



    private List<Collect> data = new ArrayList<>();
    private CollectActorAdaper mCollectActorAdaper;

    private String tokenCode;
    private String tokenInfo;
    private int pageNo = 1;
    private int pageSize = 12;
    private int num;


    private boolean isFirst = true;

    @Override
    protected int getLayoutResource() {
        return R.layout.fragment_collect;
    }

    @Override
    public void initPresenter() {

        tokenCode = ShareUtils.getString(getActivity(), "tokenCode", "");
        tokenInfo = ShareUtils.getString(getActivity(), "tokenInfo", "");
        pageNo = 1;
    }

    @Override
    protected void initView() {
      /*  ntb.setTvLeftVisiable(false);
        ntb.setTitleText(getString(R.string.classify_title));*/
        LogUtil.e("----------initView-------");
        num = getArguments().getInt("num",-1);
        btn_more.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MainActivity mainActivity = (MainActivity) getActivity();
                mainActivity.setChangeTab(2);
            }
        });

        int spanCount = 3;//跟布局里面的spanCount属性是一致的
        int spacing = 20;//每一个矩形的间距
        boolean includeEdge = false;//如果设置成false那边缘地带就没有间距
        //设置每个item间距
        mRecyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 3));
        mRecyclerView.addItemDecoration(new GridSpacingItemDecoration(spanCount, spacing, includeEdge));
        //设置适配器
        mCollectActorAdaper = new CollectActorAdaper(getActivity(), data);
        mCollectActorAdaper.setmOnItemClickListener(new CollectActorAdaper.OnItemClickListener1() {
            @Override
            public void onClick(int pos,Collect data, View v) {
                setfavor(pos,data);
            }
        });
        mRecyclerView.setAdapter(mCollectActorAdaper);
        mRecyclerView.setHasFixedSize(true);
        setRefresh();
        mRefreshLayout.autoRefresh();

    }

    @Override
    public void onResume() {
        super.onResume();
        if (!isFirst) {
            isFirst = false;
          //  refresh();
        } else {
            isFirst = false;
        }
        LogUtil.e("-----C---onResume--------");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

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

    public void autoRefresh() {
        mRefreshLayout.autoRefresh();
    }

    private void refresh() {
        pageNo = 1;
        getVideoLists(false);

    }

    private void loadMore() {
        pageNo++;
        getVideoLists(true);

    }

    private void getVideoLists(final boolean isMore) {
        //http://serpro/mobile/user/myfavors.html
        HttpParams post_params = new HttpParams();
        post_params.putHeaders("tokenCode", tokenCode);
        post_params.putHeaders("tokenInfo", tokenInfo);
        post_params.putHeaders("Content-Type", "application/json");

        Map<String, String> params = new HashMap<String, String>();
        params.put("objtype", "" + num);
        params.put("pageNo", "" + pageNo);
        params.put("pageSize", "" + pageSize);
        post_params.putJsonParams(new Gson().toJson(params));
        RxVolley.jsonPost(StaticClass.domain + "/myfavors.html", post_params, new HttpCallback() {
            @Override
            public void onSuccess(String t) {

                LogUtil.i("*************myfavors**list*sq****:" + t);
                if (!TextUtils.isEmpty(t)) {
                    try {
                        BaseEntity mBaseEntity = new Gson().fromJson(t, BaseEntity.class);
                        if (mBaseEntity != null) {
                            if (mBaseEntity.getStatus() == 100) {
                                CollectListEntity mVideoList = new Gson().fromJson(t, CollectListEntity.class);
                                if (mVideoList != null && mVideoList.getResult() != null) {
                                    if (mVideoList.getResult().getItems() != null) {
                                        if (!isMore) {
                                            //刷新
                                            data.clear();
                                            data.addAll(mVideoList.getResult().getItems());
                                            if(data.size()<=0){
                                                mZ.setVisibility(View.VISIBLE);
                                            }else{
                                                mZ.setVisibility(View.GONE);
                                            }
                                            mCollectActorAdaper.notifyDataSetChanged();
                                        } else {
                                            //更多
                                            data.addAll(mVideoList.getResult().getItems());
                                            if(data.size()<=0){
                                                mZ.setVisibility(View.VISIBLE);
                                            }else{
                                                mZ.setVisibility(View.GONE);
                                            }
                                            mCollectActorAdaper.notifyDataSetChanged();
                                        }



                                    }
                                }

                            } else {
                                ToastUtil.showToast(getActivity(), "获取列表失败");
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


    private void setfavor(final int pos, final Collect mActor) {
        //http://serpro/mobile/user/favor.html

        HttpParams post_params = new HttpParams();
        post_params.putHeaders("tokenCode", tokenCode);
        post_params.putHeaders("tokenInfo", tokenInfo);
        post_params.putHeaders("Content-Type", "application/json");

        Map<String, String> params = new HashMap<String, String>();
        params.put("objtype", "2");
        params.put("objid", mActor.getObjid());
        post_params.putJsonParams(new Gson().toJson(params));
        startProgressDialog("正在加载...");
        RxVolley.jsonPost(StaticClass.domain + "/favor.html", post_params, new HttpCallback() {
            @Override
            public void onSuccess(String t) {
                stopProgressDialog();
                LogUtil.i("******favor*******commit*sq****:" + t);
                if (!TextUtils.isEmpty(t)) {
                    try {
                        BaseEntity mBaseEntity = new Gson().fromJson(t, BaseEntity.class);
                        if (mBaseEntity != null) {
                            if (mBaseEntity.getStatus() == 100) {

                                data.remove(pos);
                                mCollectActorAdaper.notifyDataSetChanged();
                                mRefreshLayout.autoRefresh();
                            } else {
                                ToastUtil.showToast(getActivity(), "请求失败");
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
                stopProgressDialog();
                LogUtil.i("onFailure...." + error.getMessage());
                ToastUtil.showToast(getActivity(), "请求失败");
            }

        });


    }


    //消息推送通知收到事件
    @Subscribe
    public void onEvent(String avatarStr) {

        if (avatarStr.equals("EventBus_RF")){
            mRefreshLayout.autoRefresh();
        }else{

        }
    }


}
