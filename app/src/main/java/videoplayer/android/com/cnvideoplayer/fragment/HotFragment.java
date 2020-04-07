package videoplayer.android.com.cnvideoplayer.fragment;

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
import videoplayer.android.com.cnvideoplayer.base.BaseFragment;
import videoplayer.android.com.cnvideoplayer.entity.BaseEntity;
import videoplayer.android.com.cnvideoplayer.entity.VideoEntity;
import videoplayer.android.com.cnvideoplayer.entity.VideoListEntity;
import videoplayer.android.com.cnvideoplayer.utils.LogUtil;
import videoplayer.android.com.cnvideoplayer.utils.ShareUtils;
import videoplayer.android.com.cnvideoplayer.utils.StaticClass;
import videoplayer.android.com.cnvideoplayer.utils.ToastUtil;
import videoplayer.android.com.cnvideoplayer.view.GridSpacingItemDecoration;
import videoplayer.android.com.cnvideoplayer.view.NormalTitleBar;

/**
 * Date: 2018/10/22
 * Author:
 * Email：
 * Des：
 */

public class HotFragment extends BaseFragment {

  /*  @Bind(R.id.ntb)
    NormalTitleBar ntb;*/

    @Bind(R.id.normal_view)
    SmartRefreshLayout mRefreshLayout;
    @Bind(R.id.m_recycler_view)
    RecyclerView mRecyclerView;
    private List<VideoEntity> data=new ArrayList<>();
    private ClassifyAdapter mClassifyAdapter;

    private int pageNo=1;
    private int pageSize=12;

    private String tokenCode;
    private String tokenInfo;
    private String type;

    private boolean isFirst=true;
    @Override
    protected int getLayoutResource() {
        return R.layout.fragment_collect;
    }

    @Override
    public void initPresenter() {
        type = getArguments().getString("type");



    }

    @Override
    protected void initView() {
      //  ntb.setVisibility(View.GONE);
        tokenCode = ShareUtils.getString(getActivity(), "tokenCode", "");
        tokenInfo = ShareUtils.getString(getActivity(), "tokenInfo", "");
        int spanCount = 2;//跟布局里面的spanCount属性是一致的
        int spacing = 20;//每一个矩形的间距
        boolean includeEdge = false;//如果设置成false那边缘地带就没有间距
        //设置每个item间距
        mRecyclerView.setLayoutManager(new GridLayoutManager(getActivity(),2));
        mRecyclerView.addItemDecoration(new GridSpacingItemDecoration(spanCount,spacing,includeEdge));
        //设置适配器
        mClassifyAdapter=new ClassifyAdapter(getActivity(),data);
        mRecyclerView.setAdapter(mClassifyAdapter);
        mRecyclerView.setHasFixedSize(true);
        setRefresh();
       // mRefreshLayout.autoRefresh();
    }

    @Override
    public void onResume() {
        super.onResume();
        if(!isFirst){
            isFirst=false;
            refresh();
        }else{
            isFirst=false;
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

                loadMore();
                refreshLayout.finishLoadMore(1000);
            }
        });
    }


    private void refresh(){
        pageNo=1;
        getViedosData(key,false);

    }

    private void loadMore(){
        pageNo++;
        getViedosData(key,true);

    }

   private String key="";



    private void getViedosData(String key, final boolean isMore) {
        //http://serpro/mobile/video/list.html

        HttpParams post_params = new HttpParams();
      //  LogUtil.e("----tokenCode---"+tokenCode);
       // LogUtil.e("----tokenInfo---"+tokenInfo);
        post_params.putHeaders("tokenCode", tokenCode);
        post_params.putHeaders("tokenInfo", tokenInfo);
        post_params.putHeaders("Content-Type", "application/json");
        Map<String, String> params = new HashMap<String, String>();
        params.put("key", key);
        params.put("actorid", "");
        params.put("sortsid", "");
        params.put("order", "");
        params.put("pageNo", "" + pageNo);
        params.put("pageSize", "" + pageSize);
        LogUtil.e("-------------type----------:"+type);
        params.put("type",type);
        params.put("channelid", "");
        post_params.putJsonParams(new Gson().toJson(params));


        RxVolley.jsonPost(StaticClass.domain1 + "/video/list.html", post_params, new HttpCallback() {
            @Override
            public void onSuccess(String t) {

                LogUtil.i("*************getKeyViedosData**list*sq****:" + t);
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
                                ToastUtil.showToast(getActivity(), "搜索失败");
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

     public  void setKey(String type1,String name, String tokenCode,String tokenInfo){
        this.type=type1;
         key=name;
         this.tokenCode=tokenCode;
         this.tokenInfo=tokenInfo;
         refresh();
     }




}

