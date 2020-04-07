package videoplayer.android.com.cnvideoplayer.fragment;

import android.support.v7.widget.GridLayoutManager;
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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.Bind;
import videoplayer.android.com.cnvideoplayer.R;
import videoplayer.android.com.cnvideoplayer.adpater.BQAdapter;
import videoplayer.android.com.cnvideoplayer.adpater.MovieAdapter;
import videoplayer.android.com.cnvideoplayer.base.BaseFragment;
import videoplayer.android.com.cnvideoplayer.entity.BaseEntity;
import videoplayer.android.com.cnvideoplayer.entity.LabelsListEntity;
import videoplayer.android.com.cnvideoplayer.entity.Movie;
import videoplayer.android.com.cnvideoplayer.entity.MovieListEntity;
import videoplayer.android.com.cnvideoplayer.entity.QB;
import videoplayer.android.com.cnvideoplayer.ui.SearchResultActivity;
import videoplayer.android.com.cnvideoplayer.ui.ViedoListActivity;
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

public class MovieFragment extends BaseFragment {

  /*  @Bind(R.id.ntb)
    NormalTitleBar ntb;*/

    @Bind(R.id.normal_view)
    SmartRefreshLayout mRefreshLayout;
    @Bind(R.id.m_recycler_view)
    RecyclerView mRecyclerView;



    private List<Movie> data = new ArrayList<>();
    private MovieAdapter mMovieAdapter;



 /*   private int pageNo = 1;
    private int pageSize = 12;*/

    private String tokenCode;
    private String tokenInfo;
   // private int num;
    private boolean isFirst = true;

    @Override
    protected int getLayoutResource() {
        return R.layout.fragment_collect;
    }

    @Override
    public void initPresenter() {
        tokenCode = ShareUtils.getString(getActivity(), "tokenCode", "");
        tokenInfo = ShareUtils.getString(getActivity(), "tokenInfo", "");


    }

    @Override
    protected void initView() {
       // num = getArguments().getInt("num",-1);

      /*  ntb.setTvLeftVisiable(false);
        ntb.setTitleText("收藏");*/


        int spanCount = 2;//跟布局里面的spanCount属性是一致的
        int spacing = 20;//每一个矩形的间距
        boolean includeEdge = false;//如果设置成false那边缘地带就没有间距
        //设置每个item间距
        mRecyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 2));
        mRecyclerView.addItemDecoration(new GridSpacingItemDecoration(spanCount, spacing, includeEdge));
        //设置适配器
        mMovieAdapter = new MovieAdapter(getActivity(), data);
        mRecyclerView.setAdapter(mMovieAdapter);
        mRecyclerView.setHasFixedSize(true);




        setRefresh();
        mRefreshLayout.autoRefresh();

    }

    @Override
    public void onResume() {
        super.onResume();
        if (!isFirst) {
            isFirst = false;
            refresh();
        } else {
            isFirst = false;
        }
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

                //loadMore();
                refreshLayout.finishLoadMore(1000);
            }
        });
    }


    private void refresh() {
     //   pageNo = 1;
        getVideoLists(false);

    }

    private void loadMore() {
       // pageNo++;
       // getVideoLists(true);

    }

    private void getVideoLists(final boolean isMore) {
        //http://serpro/mobile/sorts/movie.html

        HttpParams post_params = new HttpParams();
        post_params.putHeaders("tokenCode", tokenCode);
        post_params.putHeaders("tokenInfo", tokenInfo);
        post_params.putHeaders("Content-Type", "application/json");

      //  Map<String, String> params = new HashMap<String, String>();
       // params.put("pageNo", "" + pageNo);
     //   params.put("pageSize", "" + pageSize);
      //  post_params.putJsonParams(new Gson().toJson(params));
        RxVolley.jsonPost(StaticClass.domain1 + "/sorts/movie.html", post_params, new HttpCallback() {
            @Override
            public void onSuccess(String t) {

                LogUtil.i("***********sorts**movie**list*sq****:" + t);
                if (!TextUtils.isEmpty(t)) {
                    try {
                        BaseEntity mBaseEntity = new Gson().fromJson(t, BaseEntity.class);
                        if (mBaseEntity != null) {
                            if (mBaseEntity.getStatus() == 100) {
                                MovieListEntity mVideoList = new Gson().fromJson(t, MovieListEntity.class);
                                if (mVideoList != null && mVideoList.getResult() != null) {
                                    if (mVideoList.getResult().getItems() != null) {
                                        if (!isMore) {
                                            //刷新
                                            data.clear();
                                            data.addAll(mVideoList.getResult().getItems());

                                            mMovieAdapter.notifyDataSetChanged();
                                        } else {
                                            //更多
                                            data.addAll(mVideoList.getResult().getItems());

                                            mMovieAdapter.notifyDataSetChanged();
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


}

