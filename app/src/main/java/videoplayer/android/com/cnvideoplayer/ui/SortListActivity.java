package videoplayer.android.com.cnvideoplayer.ui;

import android.app.Activity;
import android.content.Intent;
import android.support.v4.content.ContextCompat;
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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.Bind;
import videoplayer.android.com.cnvideoplayer.R;
import videoplayer.android.com.cnvideoplayer.adpater.ClassifyAdapter;
import videoplayer.android.com.cnvideoplayer.adpater.ClassifyTitleAdapter;
import videoplayer.android.com.cnvideoplayer.base.BaseCompatActivity;
import videoplayer.android.com.cnvideoplayer.entity.BaseEntity;
import videoplayer.android.com.cnvideoplayer.entity.SortsData;
import videoplayer.android.com.cnvideoplayer.entity.SortsList;
import videoplayer.android.com.cnvideoplayer.entity.VideoEntity;
import videoplayer.android.com.cnvideoplayer.entity.VideoListEntity;
import videoplayer.android.com.cnvideoplayer.utils.LogUtil;
import videoplayer.android.com.cnvideoplayer.utils.ShareUtils;
import videoplayer.android.com.cnvideoplayer.utils.StaticClass;
import videoplayer.android.com.cnvideoplayer.utils.ToastUtil;
import videoplayer.android.com.cnvideoplayer.view.GridSpacingItemDecoration;
import videoplayer.android.com.cnvideoplayer.view.StatusBarCompat;

public class SortListActivity extends BaseCompatActivity {

    public static void startActivity(Activity activity, int num) {
        Intent intent = new Intent(activity, SortListActivity.class);
        intent.putExtra("num", num);

        activity.startActivityForResult(intent, 1005);
    }

    /* @Bind(R.id.ntb)
  private NormalTitleBar ntb;*/

    private  SmartRefreshLayout mRefreshLayout;

    private RecyclerView mRecyclerView;

    private RecyclerView c_recycler_view1;
    private RecyclerView c_recycler_view2;
    private RecyclerView c_recycler_view3;
    private  RecyclerView c_recycler_view4;
    private RecyclerView c_recycler_view5;
    private RecyclerView c_recycler_view6;
    private RecyclerView c_recycler_view7;
    private RecyclerView c_recycler_view8;
    private RecyclerView c_recycler_view9;
    private  RecyclerView c_recycler_view10;

    private List<SortsData> data1 = new ArrayList<>();
    private List<SortsData> data2 = new ArrayList<>();
    private List<SortsData> data3 = new ArrayList<>();
    private List<SortsData> data4 = new ArrayList<>();

    private List<SortsData> data5 = new ArrayList<>();
    private List<SortsData> data6 = new ArrayList<>();
    private List<SortsData> data7 = new ArrayList<>();
    private List<SortsData> data8 = new ArrayList<>();
    private List<SortsData> data9 = new ArrayList<>();
    private List<SortsData> data10 = new ArrayList<>();


    private ClassifyTitleAdapter mClassifyTitleAdapter1;
    private ClassifyTitleAdapter mClassifyTitleAdapter2;
    private ClassifyTitleAdapter mClassifyTitleAdapter3;
    private ClassifyTitleAdapter mClassifyTitleAdapter4;

    private ClassifyTitleAdapter mClassifyTitleAdapter5;
    private ClassifyTitleAdapter mClassifyTitleAdapter6;
    private ClassifyTitleAdapter mClassifyTitleAdapter7;
    private ClassifyTitleAdapter mClassifyTitleAdapter8;
    private ClassifyTitleAdapter mClassifyTitleAdapter9;
    private ClassifyTitleAdapter mClassifyTitleAdapter10;


    private List<VideoEntity> data = new ArrayList<>();
    private ClassifyAdapter mClassifyAdapter;

    private String tokenCode;
    private String tokenInfo;
    private int pageNo = 1;
    private int pageSize = 12;

    private String order;
    private String sortsid1;
    private String sortsid2;
    private String sortsid3;

    private String sortsid4;
    private String sortsid5;
    private String sortsid6;
    private String sortsid7;
    private String sortsid8;
    private String sortsid9;

    private boolean isFirst = true;

    private int num;

    @Override
    protected String initTitle() {
        return "分类";
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_sort_list;
    }

    @Override
    public void initView() {
        StatusBarCompat.setStatusBarColor(this, ContextCompat.getColor(this, R.color.main_color));

        num = getIntent().getIntExtra("num",-1);
         tokenCode = ShareUtils.getString(SortListActivity.this, "tokenCode", "");
         tokenInfo = ShareUtils.getString(SortListActivity.this, "tokenInfo", "");
         pageNo = 1;

         mRefreshLayout=(SmartRefreshLayout)this.findViewById(R.id.normal_view);
         mRecyclerView=(RecyclerView)this.findViewById(R.id.m_recycler_view);

         c_recycler_view1=(RecyclerView)this.findViewById(R.id.c_recycler_view1);
         c_recycler_view2=(RecyclerView)this.findViewById(R.id.c_recycler_view2);
         c_recycler_view3=(RecyclerView)this.findViewById(R.id.c_recycler_view3);
         c_recycler_view4=(RecyclerView)this.findViewById(R.id.c_recycler_view4);
         c_recycler_view5=(RecyclerView)this.findViewById(R.id.c_recycler_view5);
         c_recycler_view6=(RecyclerView)this.findViewById(R.id.c_recycler_view6);
         c_recycler_view7=(RecyclerView)this.findViewById(R.id.c_recycler_view7);
         c_recycler_view8=(RecyclerView)this.findViewById(R.id.c_recycler_view8);
         c_recycler_view9=(RecyclerView)this.findViewById(R.id.c_recycler_view9);
         c_recycler_view10=(RecyclerView)this.findViewById(R.id.c_recycler_view10);




        LinearLayoutManager linearLayoutManager1 = new LinearLayoutManager(SortListActivity.this);
        linearLayoutManager1.setOrientation(LinearLayoutManager.HORIZONTAL);
        c_recycler_view1.setLayoutManager(linearLayoutManager1);
        mClassifyTitleAdapter1 = new ClassifyTitleAdapter(SortListActivity.this, data1);
        mClassifyTitleAdapter1.setOnItemClickListener(new ClassifyTitleAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position, SortsData data) {
                order = data1.get(position).getOrder();
                for (SortsData mSortsData : data1) {
                    mSortsData.setSelected(false);
                }
                data1.get(position).setSelected(true);
                mClassifyTitleAdapter1.notifyDataSetChanged();
                pageNo = 1;
                getVideoLists("", "", order, false);
            }
        });

        c_recycler_view1.setAdapter(mClassifyTitleAdapter1);

        LinearLayoutManager linearLayoutManager2 = new LinearLayoutManager(SortListActivity.this);
        linearLayoutManager2.setOrientation(LinearLayoutManager.HORIZONTAL);
        c_recycler_view2.setLayoutManager(linearLayoutManager2);
        mClassifyTitleAdapter2 = new ClassifyTitleAdapter(SortListActivity.this, data2);
        mClassifyTitleAdapter2.setOnItemClickListener(new ClassifyTitleAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position, SortsData data) {
                sortsid1 = data2.get(position).getSortsid();
                for (SortsData mSortsData : data2) {
                    mSortsData.setSelected(false);
                }
                data2.get(position).setSelected(true);
                mClassifyTitleAdapter2.notifyDataSetChanged();
                pageNo = 1;
                getVideoLists("", "", order, false);
            }
        });

        c_recycler_view2.setAdapter(mClassifyTitleAdapter2);


        LinearLayoutManager linearLayoutManager3 = new LinearLayoutManager(SortListActivity.this);
        linearLayoutManager3.setOrientation(LinearLayoutManager.HORIZONTAL);
        c_recycler_view3.setLayoutManager(linearLayoutManager3);
        mClassifyTitleAdapter3 = new ClassifyTitleAdapter(SortListActivity.this, data3);
        mClassifyTitleAdapter3.setOnItemClickListener(new ClassifyTitleAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position, SortsData data) {
                sortsid2 = data3.get(position).getSortsid();
                for (SortsData mSortsData : data3) {
                    mSortsData.setSelected(false);
                }
                data3.get(position).setSelected(true);
                mClassifyTitleAdapter3.notifyDataSetChanged();
                pageNo = 1;
                getVideoLists("", "", order, false);
            }
        });
        c_recycler_view3.setAdapter(mClassifyTitleAdapter3);


        LinearLayoutManager linearLayoutManager4 = new LinearLayoutManager(SortListActivity.this);
        linearLayoutManager4.setOrientation(LinearLayoutManager.HORIZONTAL);
        c_recycler_view4.setLayoutManager(linearLayoutManager4);
        mClassifyTitleAdapter4 = new ClassifyTitleAdapter(SortListActivity.this, data4);
        mClassifyTitleAdapter4.setOnItemClickListener(new ClassifyTitleAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position, SortsData data) {
                sortsid3 = data4.get(position).getSortsid();
                for (SortsData mSortsData : data4) {
                    mSortsData.setSelected(false);
                }
                data4.get(position).setSelected(true);
                mClassifyTitleAdapter4.notifyDataSetChanged();

                pageNo = 1;
                getVideoLists("", "", order, false);
            }
        });
        c_recycler_view4.setAdapter(mClassifyTitleAdapter4);


        LinearLayoutManager linearLayoutManager5 = new LinearLayoutManager(SortListActivity.this);
        linearLayoutManager5.setOrientation(LinearLayoutManager.HORIZONTAL);
        c_recycler_view5.setLayoutManager(linearLayoutManager5);
        mClassifyTitleAdapter5 = new ClassifyTitleAdapter(SortListActivity.this, data5);
        mClassifyTitleAdapter5.setOnItemClickListener(new ClassifyTitleAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position, SortsData data) {
                sortsid4 = data5.get(position).getSortsid();
                for (SortsData mSortsData : data5) {
                    mSortsData.setSelected(false);
                }
                data5.get(position).setSelected(true);
                mClassifyTitleAdapter5.notifyDataSetChanged();

                pageNo = 1;
                getVideoLists("", "", order, false);
            }
        });
        c_recycler_view5.setAdapter(mClassifyTitleAdapter5);

        LinearLayoutManager linearLayoutManager6 = new LinearLayoutManager(SortListActivity.this);
        linearLayoutManager6.setOrientation(LinearLayoutManager.HORIZONTAL);
        c_recycler_view6.setLayoutManager(linearLayoutManager6);
        mClassifyTitleAdapter6 = new ClassifyTitleAdapter(SortListActivity.this, data6);
        mClassifyTitleAdapter6.setOnItemClickListener(new ClassifyTitleAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position, SortsData data) {
                sortsid5 = data6.get(position).getSortsid();
                for (SortsData mSortsData : data6) {
                    mSortsData.setSelected(false);
                }
                data6.get(position).setSelected(true);
                mClassifyTitleAdapter6.notifyDataSetChanged();

                pageNo = 1;
                getVideoLists("", "", order, false);
            }
        });
        c_recycler_view6.setAdapter(mClassifyTitleAdapter6);


        LinearLayoutManager linearLayoutManager7 = new LinearLayoutManager(SortListActivity.this);
        linearLayoutManager7.setOrientation(LinearLayoutManager.HORIZONTAL);
        c_recycler_view7.setLayoutManager(linearLayoutManager7);
        mClassifyTitleAdapter7 = new ClassifyTitleAdapter(SortListActivity.this, data7);
        mClassifyTitleAdapter7.setOnItemClickListener(new ClassifyTitleAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position, SortsData data) {
                sortsid6 = data7.get(position).getSortsid();
                for (SortsData mSortsData : data7) {
                    mSortsData.setSelected(false);
                }
                data7.get(position).setSelected(true);
                mClassifyTitleAdapter7.notifyDataSetChanged();

                pageNo = 1;
                getVideoLists("", "", order, false);
            }
        });
        c_recycler_view7.setAdapter(mClassifyTitleAdapter7);


        LinearLayoutManager linearLayoutManager8 = new LinearLayoutManager(SortListActivity.this);
        linearLayoutManager8.setOrientation(LinearLayoutManager.HORIZONTAL);
        c_recycler_view8.setLayoutManager(linearLayoutManager8);
        mClassifyTitleAdapter8 = new ClassifyTitleAdapter(SortListActivity.this, data8);
        mClassifyTitleAdapter8.setOnItemClickListener(new ClassifyTitleAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position, SortsData data) {
                sortsid7 = data8.get(position).getSortsid();
                for (SortsData mSortsData : data8) {
                    mSortsData.setSelected(false);
                }
                data8.get(position).setSelected(true);
                mClassifyTitleAdapter8.notifyDataSetChanged();

                pageNo = 1;
                getVideoLists("", "", order, false);
            }
        });
        c_recycler_view8.setAdapter(mClassifyTitleAdapter8);

        LinearLayoutManager linearLayoutManager9 = new LinearLayoutManager(SortListActivity.this);
        linearLayoutManager9.setOrientation(LinearLayoutManager.HORIZONTAL);
        c_recycler_view9.setLayoutManager(linearLayoutManager9);
        mClassifyTitleAdapter9 = new ClassifyTitleAdapter(SortListActivity.this, data9);
        mClassifyTitleAdapter9.setOnItemClickListener(new ClassifyTitleAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position, SortsData data) {
                sortsid8 = data9.get(position).getSortsid();
                for (SortsData mSortsData : data9) {
                    mSortsData.setSelected(false);
                }
                data9.get(position).setSelected(true);
                mClassifyTitleAdapter9.notifyDataSetChanged();

                pageNo = 1;
                getVideoLists("", "", order, false);
            }
        });
        c_recycler_view9.setAdapter(mClassifyTitleAdapter9);

        LinearLayoutManager linearLayoutManager10 = new LinearLayoutManager(SortListActivity.this);
        linearLayoutManager10.setOrientation(LinearLayoutManager.HORIZONTAL);
        c_recycler_view10.setLayoutManager(linearLayoutManager10);
        mClassifyTitleAdapter10 = new ClassifyTitleAdapter(SortListActivity.this, data10);
        mClassifyTitleAdapter10.setOnItemClickListener(new ClassifyTitleAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position, SortsData data) {
                sortsid9 = data10.get(position).getSortsid();
                for (SortsData mSortsData : data10) {
                    mSortsData.setSelected(false);
                }
                data10.get(position).setSelected(true);
                mClassifyTitleAdapter10.notifyDataSetChanged();

                pageNo = 1;
                getVideoLists("", "", order, false);
            }
        });
        c_recycler_view10.setAdapter(mClassifyTitleAdapter10);

        int spanCount = 2;//跟布局里面的spanCount属性是一致的
        int spacing = 20;//每一个矩形的间距
        boolean includeEdge = false;//如果设置成false那边缘地带就没有间距
        //设置每个item间距
        mRecyclerView.setLayoutManager(new GridLayoutManager(SortListActivity.this, 2));
        mRecyclerView.addItemDecoration(new GridSpacingItemDecoration(spanCount, spacing, includeEdge));
        //设置适配器
        mClassifyAdapter = new ClassifyAdapter(SortListActivity.this, data);
        mRecyclerView.setAdapter(mClassifyAdapter);
        mRecyclerView.setHasFixedSize(true);
        setRefresh();
        //  mRefreshLayout.autoRefresh();
        refresh();
    }

    @Override
    public void initData() {

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
        pageNo = 1;

        if (isFirst) {
            isFirst = false;
            data1.clear();
            SortsData mSortsData = new SortsData();
            mSortsData.setOrder("0");
            mSortsData.setName("最新上映");
            mSortsData.setSelected(true);
            order = mSortsData.getOrder();
            data1.add(mSortsData);
            mSortsData = new SortsData();
            mSortsData.setOrder("1");
            mSortsData.setName("最多播放");
            mSortsData.setSelected(false);
            data1.add(mSortsData);
            c_recycler_view2.setVisibility(View.GONE);
            c_recycler_view3.setVisibility(View.GONE);
            c_recycler_view4.setVisibility(View.GONE);
            c_recycler_view5.setVisibility(View.GONE);
            c_recycler_view6.setVisibility(View.GONE);
            c_recycler_view7.setVisibility(View.GONE);
            c_recycler_view8.setVisibility(View.GONE);
            c_recycler_view9.setVisibility(View.GONE);
            c_recycler_view10.setVisibility(View.GONE);
            mClassifyTitleAdapter1.notifyDataSetChanged();
            getSorts();
        } else {

            getVideoLists("", "", order, false);
        }


    }

    private void loadMore() {
        pageNo++;

        getVideoLists("", "", order, true);
    }


    private void getSorts() {
        //http://serpro/mobile/sorts/list.html
        //0-电影分类；1-视频分类

        HttpParams post_params = new HttpParams();

        post_params.putHeaders("tokenCode", tokenCode);
        post_params.putHeaders("tokenInfo", tokenInfo);
        post_params.putHeaders("Content-Type", "application/json");

        Map<String, String> params = new HashMap<String, String>();
        params.put("vtype", ""+num);
        post_params.putJsonParams(new Gson().toJson(params));


        RxVolley.jsonPost(StaticClass.domain1 + "/sorts/list.html", post_params, new HttpCallback() {
            @Override
            public void onSuccess(String t) {

                LogUtil.i("*************sorts*list**sq****:" + t);
                if (!TextUtils.isEmpty(t)) {
                    try {
                        BaseEntity mBaseEntity = new Gson().fromJson(t, BaseEntity.class);
                        if (mBaseEntity != null) {
                            if (mBaseEntity.getStatus() == 100) {
                                SortsList mSortsList = new Gson().fromJson(t, SortsList.class);
                                if (mSortsList != null && mSortsList.getResult() != null) {
                                    if (mSortsList.getResult().getItems() != null) {
                                        if (mSortsList.getResult().getItems().size() > 0) {
                                            data2.clear();
                                            SortsData mSortsData = mSortsList.getResult().getItems().get(0);
                                            sortsid1 = mSortsData.getSortsid();
                                            mSortsData.setSelected(true);
                                            data2.add(mSortsData);
                                            if (mSortsList.getResult().getItems().get(0).getChildren() != null) {
                                                data2.addAll(mSortsList.getResult().getItems().get(0).getChildren());
                                            }
                                            mClassifyTitleAdapter2.notifyDataSetChanged();
                                            c_recycler_view2.setVisibility(View.VISIBLE);
                                        } else {
                                            c_recycler_view2.setVisibility(View.GONE);

                                        }
                                        if (mSortsList.getResult().getItems().size() > 1) {
                                            data3.clear();
                                            SortsData mSortsData = mSortsList.getResult().getItems().get(1);
                                            sortsid2 = mSortsData.getSortsid();
                                            mSortsData.setSelected(true);
                                            data3.add(mSortsData);
                                            if (mSortsList.getResult().getItems().get(1).getChildren() != null) {
                                                data3.addAll(mSortsList.getResult().getItems().get(1).getChildren());
                                            }
                                            mClassifyTitleAdapter3.notifyDataSetChanged();
                                            c_recycler_view3.setVisibility(View.VISIBLE);
                                        } else {
                                            c_recycler_view3.setVisibility(View.GONE);

                                        }
                                        if (mSortsList.getResult().getItems().size() > 2) {
                                            data4.clear();
                                            SortsData mSortsData = mSortsList.getResult().getItems().get(2);
                                            sortsid3 = mSortsData.getSortsid();
                                            mSortsData.setSelected(true);
                                            data4.add(mSortsData);
                                            if (mSortsList.getResult().getItems().get(2).getChildren() != null) {
                                                data4.addAll(mSortsList.getResult().getItems().get(2).getChildren());
                                            }
                                            mClassifyTitleAdapter4.notifyDataSetChanged();
                                            c_recycler_view4.setVisibility(View.VISIBLE);
                                        } else {
                                            c_recycler_view4.setVisibility(View.GONE);

                                        }

                                        if (mSortsList.getResult().getItems().size() > 3) {
                                            data5.clear();
                                            SortsData mSortsData = mSortsList.getResult().getItems().get(3);
                                            sortsid4 = mSortsData.getSortsid();
                                            mSortsData.setSelected(true);
                                            data5.add(mSortsData);
                                            if (mSortsList.getResult().getItems().get(3).getChildren() != null) {
                                                data5.addAll(mSortsList.getResult().getItems().get(3).getChildren());
                                            }
                                            mClassifyTitleAdapter5.notifyDataSetChanged();
                                            c_recycler_view5.setVisibility(View.VISIBLE);
                                        } else {
                                            c_recycler_view5.setVisibility(View.GONE);

                                        }

                                        if (mSortsList.getResult().getItems().size() > 4) {
                                            data6.clear();
                                            SortsData mSortsData = mSortsList.getResult().getItems().get(4);
                                            sortsid5 = mSortsData.getSortsid();
                                            mSortsData.setSelected(true);
                                            data6.add(mSortsData);
                                            if (mSortsList.getResult().getItems().get(4).getChildren() != null) {
                                                data6.addAll(mSortsList.getResult().getItems().get(4).getChildren());
                                            }
                                            mClassifyTitleAdapter6.notifyDataSetChanged();
                                            c_recycler_view6.setVisibility(View.VISIBLE);
                                        } else {
                                            c_recycler_view6.setVisibility(View.GONE);

                                        }

                                        if (mSortsList.getResult().getItems().size() > 5) {
                                            data7.clear();
                                            SortsData mSortsData = mSortsList.getResult().getItems().get(5);
                                            sortsid6 = mSortsData.getSortsid();
                                            mSortsData.setSelected(true);
                                            data7.add(mSortsData);
                                            if (mSortsList.getResult().getItems().get(5).getChildren() != null) {
                                                data7.addAll(mSortsList.getResult().getItems().get(5).getChildren());
                                            }
                                            mClassifyTitleAdapter7.notifyDataSetChanged();
                                            c_recycler_view7.setVisibility(View.VISIBLE);
                                        } else {
                                            c_recycler_view7.setVisibility(View.GONE);

                                        }

                                        if (mSortsList.getResult().getItems().size() > 6) {
                                            data8.clear();
                                            SortsData mSortsData = mSortsList.getResult().getItems().get(6);
                                            sortsid7 = mSortsData.getSortsid();
                                            mSortsData.setSelected(true);
                                            data8.add(mSortsData);
                                            if (mSortsList.getResult().getItems().get(6).getChildren() != null) {
                                                data8.addAll(mSortsList.getResult().getItems().get(6).getChildren());
                                            }
                                            mClassifyTitleAdapter8.notifyDataSetChanged();
                                            c_recycler_view8.setVisibility(View.VISIBLE);
                                        } else {
                                            c_recycler_view8.setVisibility(View.GONE);

                                        }

                                        if (mSortsList.getResult().getItems().size() > 7) {
                                            data9.clear();
                                            SortsData mSortsData = mSortsList.getResult().getItems().get(7);
                                            sortsid8 = mSortsData.getSortsid();
                                            mSortsData.setSelected(true);
                                            data9.add(mSortsData);
                                            if (mSortsList.getResult().getItems().get(7).getChildren() != null) {
                                                data9.addAll(mSortsList.getResult().getItems().get(7).getChildren());
                                            }
                                            mClassifyTitleAdapter9.notifyDataSetChanged();
                                            c_recycler_view9.setVisibility(View.VISIBLE);
                                        } else {
                                            c_recycler_view9.setVisibility(View.GONE);

                                        }

                                        if (mSortsList.getResult().getItems().size() > 8) {
                                            data10.clear();
                                            SortsData mSortsData = mSortsList.getResult().getItems().get(8);
                                            sortsid9 = mSortsData.getSortsid();
                                            mSortsData.setSelected(true);
                                            data10.add(mSortsData);
                                            if (mSortsList.getResult().getItems().get(8).getChildren() != null) {
                                                data10.addAll(mSortsList.getResult().getItems().get(8).getChildren());
                                            }
                                            mClassifyTitleAdapter10.notifyDataSetChanged();
                                            c_recycler_view10.setVisibility(View.VISIBLE);
                                        } else {
                                            c_recycler_view10.setVisibility(View.GONE);

                                        }

                                        getVideoLists("", "", order, false);
                                    }
                                }

                            } else {
                                ToastUtil.showToast(SortListActivity.this, "分类列表获取失败");
                            }

                        } else {
                            ToastUtil.showToast(SortListActivity.this, "返回数据异常");
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        ToastUtil.showToast(SortListActivity.this, "返回数据异常");
                    }

                } else {
                    ToastUtil.showToast(SortListActivity.this, "返回数据异常");
                }


            }

            @Override
            public void onFailure(VolleyError error) {
                LogUtil.i("onFailure...." + error.getMessage());
                ToastUtil.showToast(SortListActivity.this, "请求失败");
            }

        });
    }

    private void getVideoLists(String key, String actorid, String order, final boolean isMore) {
        String sortsid = "";
        if (!TextUtils.isEmpty(sortsid1)) {
            sortsid = sortsid1;
        }
        if (!TextUtils.isEmpty(sortsid2)) {
            if (!TextUtils.isEmpty(sortsid)) {
                sortsid = sortsid + "," + sortsid2;
            } else {
                sortsid = sortsid2;
            }

        }
        if (!TextUtils.isEmpty(sortsid3)) {
            if (!TextUtils.isEmpty(sortsid)) {
                sortsid = sortsid + "," + sortsid3;
            } else {
                sortsid = sortsid3;
            }
        }

        if (!TextUtils.isEmpty(sortsid4)) {
            if (!TextUtils.isEmpty(sortsid)) {
                sortsid = sortsid + "," + sortsid4;
            } else {
                sortsid = sortsid4;
            }
        }

        if (!TextUtils.isEmpty(sortsid5)) {
            if (!TextUtils.isEmpty(sortsid)) {
                sortsid = sortsid + "," + sortsid5;
            } else {
                sortsid = sortsid5;
            }
        }

        if (!TextUtils.isEmpty(sortsid6)) {
            if (!TextUtils.isEmpty(sortsid)) {
                sortsid = sortsid + "," + sortsid6;
            } else {
                sortsid = sortsid6;
            }
        }

        if (!TextUtils.isEmpty(sortsid7)) {
            if (!TextUtils.isEmpty(sortsid)) {
                sortsid = sortsid + "," + sortsid7;
            } else {
                sortsid = sortsid7;
            }
        }

        if (!TextUtils.isEmpty(sortsid8)) {
            if (!TextUtils.isEmpty(sortsid)) {
                sortsid = sortsid + "," + sortsid8;
            } else {
                sortsid = sortsid8;
            }
        }

        if (!TextUtils.isEmpty(sortsid9)) {
            if (!TextUtils.isEmpty(sortsid)) {
                sortsid = sortsid + "," + sortsid9;
            } else {
                sortsid = sortsid9;
            }
        }


        //http://serpro/mobile/video/list.html
        HttpParams post_params = new HttpParams();

        post_params.putHeaders("tokenCode", tokenCode);
        post_params.putHeaders("tokenInfo", tokenInfo);
        post_params.putHeaders("Content-Type", "application/json");

        Map<String, String> params = new HashMap<String, String>();
        params.put("key", key);
        params.put("actorid", actorid);
        params.put("sortsid", sortsid);
        LogUtil.e("--------sortsid---:" + sortsid);
        params.put("order", order);
        params.put("pageNo", "" + pageNo);
        params.put("pageSize", "" + pageSize);
        post_params.putJsonParams(new Gson().toJson(params));


        RxVolley.jsonPost(StaticClass.domain1 + "/video/list.html", post_params, new HttpCallback() {
            @Override
            public void onSuccess(String t) {

                LogUtil.i("*************video**list*sq****:" + t);
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
                                            data.clear();
                                            data.addAll(mVideoList.getResult().getItems());
                                            mClassifyAdapter.notifyDataSetChanged();
                                        } else {
                                            //更多
                                            data.addAll(mVideoList.getResult().getItems());
                                            mClassifyAdapter.notifyDataSetChanged();
                                        }

                                    }
                                }

                            } else {
                                ToastUtil.showToast(SortListActivity.this, "获取视频列表失败");
                            }

                        } else {
                            ToastUtil.showToast(SortListActivity.this, "返回数据异常");
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        ToastUtil.showToast(SortListActivity.this, "返回数据异常");
                    }

                } else {
                    ToastUtil.showToast(SortListActivity.this, "返回数据异常");
                }

            }

            @Override
            public void onFailure(VolleyError error) {
                LogUtil.i("onFailure...." + error.getMessage());
                ToastUtil.showToast(SortListActivity.this, "请求失败");
            }

        });

    }


}
