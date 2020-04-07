package videoplayer.android.com.cnvideoplayer.fragment;

import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
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

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.Bind;
import videoplayer.android.com.cnvideoplayer.R;
import videoplayer.android.com.cnvideoplayer.adpater.ClassifyTitleAdapter;
import videoplayer.android.com.cnvideoplayer.adpater.LikeAdapter;
import videoplayer.android.com.cnvideoplayer.base.BaseFragment;
import videoplayer.android.com.cnvideoplayer.entity.ActorEntity;
import videoplayer.android.com.cnvideoplayer.entity.ActorListEntity;
import videoplayer.android.com.cnvideoplayer.entity.BaseEntity;
import videoplayer.android.com.cnvideoplayer.entity.SortsData;
import videoplayer.android.com.cnvideoplayer.entity.SortsList;
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

public class ActorFragment extends BaseFragment {
    /* @Bind(R.id.ntb)
     NormalTitleBar ntb;*/
    @Bind(R.id.normal_view)
    SmartRefreshLayout mRefreshLayout;
    @Bind(R.id.m_recycler_view)
    RecyclerView mRecyclerView;

    @Bind(R.id.c_recycler_view1)
    RecyclerView c_recycler_view1;
    @Bind(R.id.c_recycler_view2)
    RecyclerView c_recycler_view2;


    private List<SortsData> data1 = new ArrayList<>();
    private List<SortsData> data2 = new ArrayList<>();

    private ClassifyTitleAdapter mClassifyTitleAdapter1;
    private ClassifyTitleAdapter mClassifyTitleAdapter2;

    private List<ActorEntity> data = new ArrayList<>();
    private LikeAdapter mLikeAdapter;

    private String tokenCode;
    private String tokenInfo;
    private int pageNo = 1;
    private int pageSize = 12;

    private String order1;
    private String order2;

    private boolean isFirst = true;

    @Override
    protected int getLayoutResource() {
        return R.layout.fragment_actor;
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


        LinearLayoutManager linearLayoutManager1 = new LinearLayoutManager(getActivity());
        linearLayoutManager1.setOrientation(LinearLayoutManager.HORIZONTAL);
        c_recycler_view1.setLayoutManager(linearLayoutManager1);
        mClassifyTitleAdapter1 = new ClassifyTitleAdapter(getActivity(), data1);
        mClassifyTitleAdapter1.setOnItemClickListener(new ClassifyTitleAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position, SortsData data) {
                order1 = data1.get(position).getOrder();
                for (SortsData mSortsData : data1) {
                    mSortsData.setSelected(false);
                }
                data1.get(position).setSelected(true);
                mClassifyTitleAdapter1.notifyDataSetChanged();
                pageNo = 1;
                getVideoLists("", "", order1, false);
            }
        });

        c_recycler_view1.setAdapter(mClassifyTitleAdapter1);

        LinearLayoutManager linearLayoutManager2 = new LinearLayoutManager(getActivity());
        linearLayoutManager2.setOrientation(LinearLayoutManager.HORIZONTAL);
        c_recycler_view2.setLayoutManager(linearLayoutManager2);
        mClassifyTitleAdapter2 = new ClassifyTitleAdapter(getActivity(), data2);
        mClassifyTitleAdapter2.setOnItemClickListener(new ClassifyTitleAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position, SortsData data) {
                order2 = data2.get(position).getSortsid();
                for (SortsData mSortsData : data2) {
                    mSortsData.setSelected(false);
                }
                data2.get(position).setSelected(true);
                mClassifyTitleAdapter2.notifyDataSetChanged();
                pageNo = 1;
                getVideoLists("", "", order1, false);
            }
        });

        c_recycler_view2.setAdapter(mClassifyTitleAdapter2);


        int spanCount = 3;//跟布局里面的spanCount属性是一致的
        int spacing = 20;//每一个矩形的间距
        boolean includeEdge = false;//如果设置成false那边缘地带就没有间距
        //设置每个item间距
        mRecyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 3));
        mRecyclerView.addItemDecoration(new GridSpacingItemDecoration(spanCount, spacing, includeEdge));
        //设置适配器
        mLikeAdapter = new LikeAdapter(getActivity(), data);
        mLikeAdapter.setmOnItemClickListener(new LikeAdapter.OnItemClickListener1() {
            @Override
            public void onClick(int pos,ActorEntity data, View v) {
                setfavor(pos,data);
            }
        });
        mRecyclerView.setAdapter(mLikeAdapter);
        mRecyclerView.setHasFixedSize(true);
        setRefresh();
        mRefreshLayout.autoRefresh();
        // refresh();

    }

    @Override
    public void onResume() {
        super.onResume();
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
        pageNo = 1;

        if (isFirst) {
            //0-人气排序；1-片量最高

            isFirst = false;
            data1.clear();
            SortsData mSortsData = new SortsData();
            mSortsData.setOrder("0");
            mSortsData.setName("人气排序");
            mSortsData.setSelected(true);
            order1 = mSortsData.getOrder();
            data1.add(mSortsData);
            mSortsData = new SortsData();
            mSortsData.setOrder("1");
            mSortsData.setName("片量最高");
            mSortsData.setSelected(false);
            data1.add(mSortsData);
            mClassifyTitleAdapter1.notifyDataSetChanged();

            data2.clear();
            SortsData mSortsData1 = new SortsData();
            mSortsData1.setOrder("");
            mSortsData1.setName("所有罩杯");
            mSortsData1.setSelected(true);
            order2 = mSortsData1.getOrder();
            data2.add(mSortsData1);
            mSortsData1 = new SortsData();
            mSortsData1.setOrder("A");
            mSortsData1.setName("A");
            mSortsData1.setSelected(false);
            data2.add(mSortsData1);
            mSortsData1 = new SortsData();
            mSortsData1.setOrder("B");
            mSortsData1.setName("B");
            mSortsData1.setSelected(false);
            data2.add(mSortsData1);

            mSortsData1 = new SortsData();
            mSortsData1.setOrder("C");
            mSortsData1.setName("C");
            mSortsData1.setSelected(false);
            data2.add(mSortsData1);

            mSortsData1 = new SortsData();
            mSortsData1.setOrder("D");
            mSortsData1.setName("D");
            mSortsData1.setSelected(false);
            data2.add(mSortsData1);

            mSortsData1 = new SortsData();
            mSortsData1.setOrder("E");
            mSortsData1.setName("E");
            mSortsData1.setSelected(false);
            data2.add(mSortsData1);

            mSortsData1 = new SortsData();
            mSortsData1.setOrder("F");
            mSortsData1.setName("F");
            mSortsData1.setSelected(false);
            data2.add(mSortsData1);

            mSortsData1 = new SortsData();
            mSortsData1.setOrder("G+");
            mSortsData1.setName("G+");
            mSortsData1.setSelected(false);
            data2.add(mSortsData1);

            mClassifyTitleAdapter2.notifyDataSetChanged();

            getSorts();
        } else {

            getVideoLists("", "", order1, false);
        }


    }

    private void loadMore() {
        pageNo++;

        getVideoLists("", "", order1, true);
    }


    private void getSorts() {
        //http://serpro/mobile/sorts/list.html
        //0-电影分类；1-视频分类

        HttpParams post_params = new HttpParams();

        post_params.putHeaders("tokenCode", tokenCode);
        post_params.putHeaders("tokenInfo", tokenInfo);
        post_params.putHeaders("Content-Type", "application/json");

        Map<String, String> params = new HashMap<String, String>();
        params.put("vtype", "0");
        post_params.putJsonParams(new Gson().toJson(params));


        RxVolley.jsonPost(StaticClass.domain1 + "/sorts/list.html", post_params, new HttpCallback() {
            @Override
            public void onSuccess(String t) {

                LogUtil.i("********电影分类*****sorts*list**sq****:" + t);
                if (!TextUtils.isEmpty(t)) {
                    try {
                        BaseEntity mBaseEntity = new Gson().fromJson(t, BaseEntity.class);
                        if (mBaseEntity != null) {
                            if (mBaseEntity.getStatus() == 100) {
                                SortsList mSortsList = new Gson().fromJson(t, SortsList.class);
                                if (mSortsList != null && mSortsList.getResult() != null) {
                                    if (mSortsList.getResult().getItems() != null) {

                                        getVideoLists("", "", order1, false);
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

    private void getVideoLists(String key, String actorid, String order, final boolean isMore) {

        //http://serpro/mobile/video/actors.html

        HttpParams post_params = new HttpParams();
        post_params.putHeaders("tokenCode", tokenCode);
        post_params.putHeaders("tokenInfo", tokenInfo);
        post_params.putHeaders("Content-Type", "application/json");

        Map<String, String> params = new HashMap<String, String>();
        params.put("order", order1);
        params.put("nsize", order2);
        params.put("pageNo", "" + pageNo);
        params.put("pageSize", "" + pageSize);
        post_params.putJsonParams(new Gson().toJson(params));


        RxVolley.jsonPost(StaticClass.domain1 + "/video/actors.html", post_params, new HttpCallback() {
            @Override
            public void onSuccess(String t) {

                LogUtil.i("*************actors**list*sq****:" + t);
                if (!TextUtils.isEmpty(t)) {
                    try {
                        BaseEntity mBaseEntity = new Gson().fromJson(t, BaseEntity.class);
                        if (mBaseEntity != null) {
                            if (mBaseEntity.getStatus() == 100) {
                                ActorListEntity mVideoList = new Gson().fromJson(t, ActorListEntity.class);
                                if (mVideoList != null && mVideoList.getResult() != null) {
                                    if (mVideoList.getResult().getItems() != null) {
                                        if (!isMore) {
                                            //刷新
                                            data.clear();
                                            data.addAll(mVideoList.getResult().getItems());
                                            mLikeAdapter.notifyDataSetChanged();
                                        } else {
                                            //更多
                                            data.addAll(mVideoList.getResult().getItems());
                                            mLikeAdapter.notifyDataSetChanged();
                                        }

                                    }
                                }

                            } else {
                                ToastUtil.showToast(getActivity(), "获取演员列表失败");
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

    private void setfavor(final int pos, final ActorEntity mActor) {
        //http://serpro/mobile/user/favor.html

        HttpParams post_params = new HttpParams();
        post_params.putHeaders("tokenCode", tokenCode);
        post_params.putHeaders("tokenInfo", tokenInfo);
        post_params.putHeaders("Content-Type", "application/json");

        Map<String, String> params = new HashMap<String, String>();
        params.put("objtype", "2");
        params.put("objid", mActor.getActorid());
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
                                if(mActor.getFavor()==1){
                                    mActor.setFavor(0);
                                }else{
                                    mActor.setFavor(1);
                                }

                                data.set(pos,mActor);
                                mLikeAdapter.notifyDataSetChanged();
                                EventBus.getDefault().post("EventBus_RF");
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


}

