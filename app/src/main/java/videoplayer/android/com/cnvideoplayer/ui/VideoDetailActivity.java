package videoplayer.android.com.cnvideoplayer.ui;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.google.gson.Gson;
import com.kymjs.rxvolley.RxVolley;
import com.kymjs.rxvolley.client.FileRequest;
import com.kymjs.rxvolley.client.HttpCallback;
import com.kymjs.rxvolley.client.HttpParams;
import com.kymjs.rxvolley.client.ProgressListener;
import com.kymjs.rxvolley.client.RequestConfig;
import com.kymjs.rxvolley.http.DefaultRetryPolicy;
import com.kymjs.rxvolley.http.VolleyError;
import com.scwang.smartrefresh.layout.api.RefreshHeader;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.SimpleMultiPurposeListener;
import com.scwang.smartrefresh.layout.util.DensityUtil;
import com.shuyu.gsyvideoplayer.GSYVideoManager;
import com.shuyu.gsyvideoplayer.builder.GSYVideoOptionBuilder;
import com.shuyu.gsyvideoplayer.listener.GSYSampleCallBack;
import com.shuyu.gsyvideoplayer.listener.LockClickListener;
import com.shuyu.gsyvideoplayer.utils.OrientationUtils;
import com.shuyu.gsyvideoplayer.video.StandardGSYVideoPlayer;
import com.shuyu.gsyvideoplayer.video.base.GSYVideoPlayer;
import com.umeng.analytics.MobclickAgent;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import q.rorbin.badgeview.DisplayUtil;
import videoplayer.android.com.cnvideoplayer.GlideApp;
import videoplayer.android.com.cnvideoplayer.R;
import videoplayer.android.com.cnvideoplayer.adpater.BQAdapter;
import videoplayer.android.com.cnvideoplayer.adpater.LineAdapter;
import videoplayer.android.com.cnvideoplayer.adpater.PhotosAdapter;
import videoplayer.android.com.cnvideoplayer.adpater.VideoAdapter;
import videoplayer.android.com.cnvideoplayer.entity.Actors;
import videoplayer.android.com.cnvideoplayer.entity.BaseEntity;
import videoplayer.android.com.cnvideoplayer.entity.QB;
import videoplayer.android.com.cnvideoplayer.entity.SwitchVideoModel;
import videoplayer.android.com.cnvideoplayer.entity.UserInfo;
import videoplayer.android.com.cnvideoplayer.entity.VideoDetailEntity;
import videoplayer.android.com.cnvideoplayer.entity.VideoEntity;
import videoplayer.android.com.cnvideoplayer.entity.VideoLines;
import videoplayer.android.com.cnvideoplayer.entity.VideoLinesEntity;
import videoplayer.android.com.cnvideoplayer.entity.VideoListEntity;
import videoplayer.android.com.cnvideoplayer.utils.DownloadFileTool;
import videoplayer.android.com.cnvideoplayer.utils.LogUtil;
import videoplayer.android.com.cnvideoplayer.utils.ShareUtils;
import videoplayer.android.com.cnvideoplayer.utils.StaticClass;
import videoplayer.android.com.cnvideoplayer.utils.StatusBarUtil;
import videoplayer.android.com.cnvideoplayer.utils.TimeUtils;
import videoplayer.android.com.cnvideoplayer.utils.ToastUtil;
import videoplayer.android.com.cnvideoplayer.utils.VideoDownloadUtil;
import videoplayer.android.com.cnvideoplayer.view.GridSpacingItemDecoration;
import videoplayer.android.com.cnvideoplayer.view.SampleCoverVideo;
import videoplayer.android.com.cnvideoplayer.view.SampleVideo;
import videoplayer.android.com.cnvideoplayer.view.SweetAlertDialog;

public class VideoDetailActivity extends AppCompatActivity {

    public static void startActivity(Activity activity, String videoId) {
        Intent intent = new Intent(activity, VideoDetailActivity.class);
        intent.putExtra("videoId", videoId);
        activity.startActivityForResult(intent, 1005);
    }
    View  tv_yanyuan_line;
    TextView tv_yanyuan;

    private StandardGSYVideoPlayer videoPlayer;
    private OrientationUtils orientationUtils;

    private List<VideoLines> data = new ArrayList<>();
    private List<Actors> photos = new ArrayList<>();
    private List<VideoEntity> videos = new ArrayList<>();
    private int mOffset = 0;
    private int mScrollY = 0;
    private RecyclerView recycler_view, photos_recycler_view, m_recycler_view;
    private LineAdapter adapter;
    private VideoAdapter mVideoAdapter;
    private PhotosAdapter mPhotosAdapter;
    private String videoId;
    private String tokenCode;
    private String tokenInfo;

    private TextView tv_num, videoMemo, videoName, title,tvFK;
    private ImageView tv_sc;
    private int favor = 0;
    private String mVideoName = "";
    private String ipPath;
    private String urlPath;
    private int msCurrentPosition = 0;

    private Button vipTo;

    /**
     * popView相关
     **/
    private View parent;
    private RecyclerView mRecyclerView;
    private TextView tv_text;
    private ImageView t_delete;
    private PopupWindow mPopWindow;
    private BQAdapter mBQAdapter;
    private List<QB> dataQB=new ArrayList<>();

    private void initPopView() {
        parent = this.getWindow().getDecorView();
        View popView = View.inflate(this, R.layout.layout_popwindow_video_detail, null);
        tv_text = (TextView) popView.findViewById(R.id.tv_text);
        t_delete = (ImageView) popView.findViewById(R.id.t_delete);
        t_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPopWindow.dismiss();
            }
        });
        mRecyclerView = (RecyclerView) popView.findViewById(R.id.recycler_view_1);
        int spanCount = 4;//跟布局里面的spanCount属性是一致的
        int spacing = 20;//每一个矩形的间距
        boolean includeEdge = false;//如果设置成false那边缘地带就没有间距
        //设置每个item间距
        mRecyclerView.setLayoutManager(new GridLayoutManager(this, 4));
        mRecyclerView.addItemDecoration(new GridSpacingItemDecoration(spanCount, spacing, includeEdge));
        mRecyclerView.setHasFixedSize(true);
        mBQAdapter=new BQAdapter(this,dataQB);
        mRecyclerView.setAdapter(mBQAdapter);
        int width = getResources().getDisplayMetrics().widthPixels ;
        int height = getResources().getDisplayMetrics().heightPixels- DisplayUtil.dp2px(this,200);
        mPopWindow = new PopupWindow(popView);
        mPopWindow.setHeight(height);
        mPopWindow.setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
        ColorDrawable dw = new ColorDrawable(0x30000000);
        mPopWindow.setBackgroundDrawable(dw);
        mPopWindow.setFocusable(true);
        mPopWindow.setTouchable(true);
        mPopWindow.setOutsideTouchable(false);//允许在外侧点击取消

    }

    private void showPopWindow() {
        mPopWindow.showAtLocation(videoName, Gravity.BOTTOM, 0, 0);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_detail);
        final Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        //状态栏透明和间距处理
        StatusBarUtil.immersive(this);
        StatusBarUtil.setPaddingSmart(this, toolbar);
        EventBus.getDefault().register(this);
        videoId = getIntent().getStringExtra("videoId");

        tokenCode = ShareUtils.getString(VideoDetailActivity.this, "tokenCode", "");
        tokenInfo = ShareUtils.getString(VideoDetailActivity.this, "tokenInfo", "");

        if (!TextUtils.isEmpty(videoId) && !TextUtils.isEmpty(tokenCode)) {
            msCurrentPosition = ShareUtils.getInt(VideoDetailActivity.this, tokenCode + "_id_" + videoId, 0);
        }
          tv_yanyuan_line  = (View) findViewById(R.id.tv_yanyuan_line);
         tv_yanyuan  = (TextView) findViewById(R.id.tv_yanyuan);

        videoPlayer = (StandardGSYVideoPlayer) findViewById(R.id.video_player);
        title = (TextView) findViewById(R.id.title);
        vipTo = (Button) findViewById(R.id.vipTo);
        vipTo.setVisibility(View.GONE);
        tv_num = (TextView) findViewById(R.id.tv_num);
        tv_sc = (ImageView) findViewById(R.id.tv_sc);
        videoMemo = (TextView) findViewById(R.id.videoMemo);
        videoMemo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPopWindow();
            }
        });
        tvFK = (TextView) findViewById(R.id.tvFK);
        tvFK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new SweetAlertDialog.Builder(VideoDetailActivity.this)

                        .setCancelable(true)
                        .setPositiveButton("确定", new SweetAlertDialog.OnDialogClickListener() {
                            @Override
                            public void onClick(Dialog dialog, int which , String msg) {

                                commit("名称:"+videoId+" "+mVideoName+",内容:"+msg);
                            }
                        }).show();
            }
        });
        videoName = (TextView) findViewById(R.id.videoName);
        tv_sc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setfavor();

            }
        });
        final View parallax = findViewById(R.id.parallax);
        final View buttonBar = findViewById(R.id.buttonBarLayout);
        final NestedScrollView scrollView = (NestedScrollView) findViewById(R.id.scrollView);
        final RefreshLayout refreshLayout = (RefreshLayout) findViewById(R.id.refreshLayout);

        refreshLayout.setOnMultiPurposeListener(new SimpleMultiPurposeListener() {
            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                msCurrentPosition = getCurPlay().getCurrentPositionWhenPlaying();
                ipPath = "";
                urlPath = "";
                favor = 0;
                getVideoData(false);
                getVideLines();
                refreshLayout.finishRefresh(3000);
            }

            @Override
            public void onLoadMore(@NonNull RefreshLayout refreshLayout) {

                refreshLayout.finishLoadMore(2000);
            }

            /*  @Override
              public void onHeaderMoving(RefreshHeader header, boolean isDragging, float percent, int offset, int headerHeight, int maxDragHeight) {
                  mOffset = offset / 2;
                  parallax.setTranslationY(mOffset - mScrollY);
                  toolbar.setAlpha(1 - Math.min(percent, 1));
              }*/
            @Override
            public void onHeaderPulling(@NonNull RefreshHeader header, float percent, int offset, int bottomHeight, int maxDragHeight) {
                mOffset = offset / 2;
                parallax.setTranslationY(mOffset - mScrollY);
                toolbar.setAlpha(1 - Math.min(percent, 1));
            }

            @Override
            public void onHeaderReleasing(@NonNull RefreshHeader header, float percent, int offset, int bottomHeight, int maxDragHeight) {
                mOffset = offset / 2;
                parallax.setTranslationY(mOffset - mScrollY);
                toolbar.setAlpha(1 - Math.min(percent, 1));
            }
        });
        scrollView.setOnScrollChangeListener(new NestedScrollView.OnScrollChangeListener() {
            private int lastScrollY = 0;
            private int h = DensityUtil.dp2px(170);
            private int color = ContextCompat.getColor(getApplicationContext(), R.color.colorPrimary) & 0x00ffffff;

            @Override
            public void onScrollChange(NestedScrollView v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
                if (lastScrollY < h) {
                    scrollY = Math.min(h, scrollY);
                    mScrollY = scrollY > h ? h : scrollY;
                    buttonBar.setAlpha(1f * mScrollY / h);
                    toolbar.setBackgroundColor(((255 * mScrollY / h) << 24) | color);
                    parallax.setTranslationY(mOffset - mScrollY);
                }
                lastScrollY = scrollY;
            }
        });
        buttonBar.setAlpha(0);
        toolbar.setBackgroundColor(0);

        recycler_view = (RecyclerView) findViewById(R.id.main_pager_recycler_view);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        recycler_view.setLayoutManager(linearLayoutManager);
        adapter = new LineAdapter(this, data);
        adapter.setOnItemClickListener(new LineAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position, VideoLines item) {
                ipPath = data.get(position).getPath();
                for (VideoLines mVideoLines : data) {
                    mVideoLines.setSelected(false);
                }
                data.get(position).setSelected(true);
                adapter.notifyDataSetChanged();
                if (!TextUtils.isEmpty(urlPath)) {
                    String paths="http://" + ipPath + urlPath;
                    LogUtil.e("------paths---:"+paths);
                    playVideo(paths);
                }
            }
        });
        recycler_view.setAdapter(adapter);

        photos_recycler_view = (RecyclerView) findViewById(R.id.photos_recycler_view);


        LinearLayoutManager linearLayoutManager1 = new LinearLayoutManager(this);
        linearLayoutManager1.setOrientation(LinearLayoutManager.HORIZONTAL);
        photos_recycler_view.setLayoutManager(linearLayoutManager1);
        mPhotosAdapter = new PhotosAdapter(this, photos);
        photos_recycler_view.setAdapter(mPhotosAdapter);


        m_recycler_view = (RecyclerView) findViewById(R.id.m_recycler_view);

        int spanCount = 2;//跟布局里面的spanCount属性是一致的
        int spacing = 20;//每一个矩形的间距
        boolean includeEdge = false;//如果设置成false那边缘地带就没有间距
        //设置每个item间距
        m_recycler_view.setLayoutManager(new GridLayoutManager(this, 2));
        m_recycler_view.addItemDecoration(new GridSpacingItemDecoration(spanCount, spacing, includeEdge));
        //设置适配器
        mVideoAdapter = new VideoAdapter(this, videos);
        m_recycler_view.setAdapter(mVideoAdapter);

        vipTo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivityForResult(new Intent(VideoDetailActivity.this, VipActivity.class), 1106);

            }
        });
        initPopView();
        initData();
        getVideLines();
        getVideoData(true);

    }

    private void commit(String content) {
        //http://serpro/mobile/user/feedback.html

        HttpParams post_params = new HttpParams();
        post_params.putHeaders("tokenCode", tokenCode);
        post_params.putHeaders("tokenInfo", tokenInfo);
        post_params.putHeaders("Content-Type", "application/json");

        Map<String, String> params = new HashMap<String, String>();
        params.put("content", content);
        // params.put("mobile", edPhone.getText().toString());
        post_params.putJsonParams(new Gson().toJson(params));
        showProgress("正在加载...");
        RxVolley.jsonPost(StaticClass.domain + "/feedback.html", post_params, new HttpCallback() {
            @Override
            public void onSuccess(String t) {
                dismissProgress();
                LogUtil.i("******feedback*******commit*sq****:" + t);
                if (!TextUtils.isEmpty(t)) {
                    try {
                        BaseEntity mBaseEntity = new Gson().fromJson(t, BaseEntity.class);
                        if (mBaseEntity != null) {
                            if (mBaseEntity.getStatus() == 100) {
                                ToastUtil.showToast(VideoDetailActivity.this, "提交成功");

                            } else {
                                if(!TextUtils.isEmpty(mBaseEntity.getMsg())){
                                    ToastUtil.showToast(VideoDetailActivity.this, mBaseEntity.getMsg());
                                }else{
                                    ToastUtil.showToast(VideoDetailActivity.this, "提交失败");
                                }

                            }

                        } else {
                            ToastUtil.showToast(VideoDetailActivity.this, "返回数据异常");
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        ToastUtil.showToast(VideoDetailActivity.this, "返回数据异常");
                    }

                } else {
                    ToastUtil.showToast(VideoDetailActivity.this, "返回数据异常");
                }

            }

            @Override
            public void onFailure(VolleyError error) {
                dismissProgress();
                LogUtil.i("onFailure...." + error.getMessage());
                ToastUtil.showToast(VideoDetailActivity.this, "请求失败");
            }

        });

    }


    private GSYVideoOptionBuilder gsyVideoOptionBuilder;
    private ImageView imageView;
    private boolean isPlay;
    private boolean isPause;

    private void initData() {
        //增加封面
        imageView = new ImageView(this);
        imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
        imageView.setImageResource(R.mipmap.play2);
        resolveNormalVideoUI();

        //外部辅助的旋转，帮助全屏
        orientationUtils = new OrientationUtils(this, videoPlayer);
        //初始化不打开外部的旋转
        orientationUtils.setEnable(false);


        gsyVideoOptionBuilder = new GSYVideoOptionBuilder()
                // .setThumbImageView(imageView)
                .setIsTouchWiget(true)
              //  .setSeekOnStart(msCurrentPosition)
                .setRotateViewAuto(false)
                .setLockLand(false)
                .setShowFullAnimation(false)
                .setNeedLockFull(true)
                .setSeekRatio(1)
                .setCacheWithPlay(false)
                .setVideoTitle(mVideoName)
                .setVideoAllCallBack(new GSYSampleCallBack() {
                    @Override
                    public void onPrepared(String url, Object... objects) {
                        super.onPrepared(url, objects);
                        //开始播放了才能旋转和全屏
                        orientationUtils.setEnable(true);
                        isPlay = true;
                    }

                    @Override
                    public void onQuitFullscreen(String url, Object... objects) {
                        super.onQuitFullscreen(url, objects);
                        if (orientationUtils != null) {
                            orientationUtils.backToProtVideo();
                        }
                    }
                });
        gsyVideoOptionBuilder.build(videoPlayer);


        //设置全屏按键功能,这是使用的是选择屏幕，而不是全屏
        videoPlayer.getFullscreenButton().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                orientationUtils.resolveByClick();
                videoPlayer.startWindowFullscreen(VideoDetailActivity.this, false, true);
            }
        });
        videoPlayer.setLockClickListener(new LockClickListener() {
            @Override
            public void onClick(View view, boolean lock) {
                if (orientationUtils != null) {
                    //配合下方的onConfigurationChanged
                    orientationUtils.setEnable(!lock);
                }
            }
        });
        //设置返回按键功能
        videoPlayer.getBackButton().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

    }

    private void resolveNormalVideoUI() {
        //增加title
        videoPlayer.getTitleTextView().setVisibility(View.GONE);
        //设置返回键
        videoPlayer.getBackButton().setVisibility(View.GONE);
    }


    private void toRePlayVideo(boolean isFisrt, String picImg, String url) {

        //  videoPlayer.setUp(source1,true,null,mapHeadData, "测试视频");
        //     videoPlayer.setUpLazy(source1,true,null,mapHeadData, "测试视频");


        RequestOptions options = new RequestOptions()
                .frame(1000000)
                .centerCrop()
                .placeholder(R.mipmap.play2)
                .error(R.mipmap.play2)
                .diskCacheStrategy(DiskCacheStrategy.ALL);
        Glide.with(VideoDetailActivity.this)
                .load(picImg)
              //  .apply(options)
                .into(imageView);
       // videoPlayer.setThumbImageView(imageView);
        videoPlayer.release();
        videoPlayer.setUp(url, false, mVideoName);
        videoPlayer.setSeekOnStart(msCurrentPosition);
        videoPlayer.startPlayLogic();

        if (!TextUtils.isEmpty(url) && url.length() > 8) {
            String url1= url.substring(7, url.length());
            LogUtil.e("---------:" + url1);
            String[] all=url1.split("/");
            String url2="";
            String url3="";
            if(all.length>0){
                url2=all[0];
            }
            if(url2.length()>0){
                url3= url1.substring(url2.length(), url1.length());
            }

            urlPath = url3;
            LogUtil.e("---------:" + urlPath);

        }


       /* String source1 = url;
        String name = "标准";
        SwitchVideoModel switchVideoModel = new SwitchVideoModel(name, source1);

        String source2 = url;
        String name2 = "清晰";
        SwitchVideoModel switchVideoModel2 = new SwitchVideoModel(name2, source2);

        List<SwitchVideoModel> list = new ArrayList<>();
        list.add(switchVideoModel);
        list.add(switchVideoModel2);*/

/*

        try {
            String result = null;
            result = URLEncoder.encode(url, "utf-8");

        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
*/


    }

    private void playVideo(final String url) {
        videoPlayer.release();


       /* String source1 = url;
        String name = "标准";
        SwitchVideoModel switchVideoModel = new SwitchVideoModel(name, source1);

        String source2 = url;
        String name2 = "清晰";
        SwitchVideoModel switchVideoModel2 = new SwitchVideoModel(name2, source2);

        final List<SwitchVideoModel> list = new ArrayList<>();
        list.add(switchVideoModel);
        list.add(switchVideoModel2);*/

        videoPlayer.postDelayed(new Runnable() {
            @Override
            public void run() {

                videoPlayer.setUp(url, false, mVideoName);
                videoPlayer.setSeekOnStart(msCurrentPosition);
                videoPlayer.startPlayLogic();

                String result = null;
                try {
                    result = URLEncoder.encode(url, "utf-8");

                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }

            }
        }, 10);
    }


    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        //如果旋转了就全屏
        if (isPlay && !isPause) {
            videoPlayer.onConfigurationChanged(this, newConfig, orientationUtils, true, true);
        }
    }

    @Override
    protected void onPause() {
        getCurPlay().onVideoPause();
        super.onPause();
        isPause = true;
        MobclickAgent.onPause(this);
    }

    @Override
    protected void onResume() {
        getCurPlay().onVideoResume();
        super.onResume();
        isPause = false;
        MobclickAgent.onResume(this);
    }

    private GSYVideoPlayer getCurPlay() {
        if (videoPlayer.getFullWindowPlayer() != null) {
            return videoPlayer.getFullWindowPlayer();
        }
        return videoPlayer;
    }

    @Override
    protected void onDestroy() {


        super.onDestroy();

        if (!TextUtils.isEmpty(videoId) && !TextUtils.isEmpty(tokenCode)) {
            int mCurrentPosition = getCurPlay().getCurrentPositionWhenPlaying();

            ShareUtils.putInt(VideoDetailActivity.this, tokenCode + "_id_" + videoId, mCurrentPosition);
        }

        if (isPlay) {
            getCurPlay().release();
        }
        //GSYPreViewManager.instance().releaseMediaPlayer();
        if (orientationUtils != null)
            orientationUtils.releaseListener();

        EventBus.getDefault().unregister(this);
    }

    @Override
    public void onBackPressed() {
        if (orientationUtils != null) {
            orientationUtils.backToProtVideo();
        }

        if (GSYVideoManager.backFromWindowFull(this)) {
            return;
        }
        super.onBackPressed();
    }


    private void getVideoData(final boolean isFisrt) {
        //http://serpro/mobile/video/dtl.html


        HttpParams post_params = new HttpParams();
        post_params.putHeaders("tokenCode", tokenCode);
        post_params.putHeaders("tokenInfo", tokenInfo);
        post_params.putHeaders("Content-Type", "application/json");

        Map<String, String> params = new HashMap<String, String>();
        params.put("videoid", videoId);
        post_params.putJsonParams(new Gson().toJson(params));
        showProgress("正在加载...");
        RxVolley.jsonPost(StaticClass.domain1 + "/video/dtl.html", post_params, new HttpCallback() {
            @Override
            public void onSuccess(String t) {
                dismissProgress();
                LogUtil.i("*************video**dtl*sq****:" + t);
                if (!TextUtils.isEmpty(t)) {
                    try {
                        BaseEntity mBaseEntity = new Gson().fromJson(t, BaseEntity.class);
                        if (mBaseEntity != null) {
                            if (mBaseEntity.getStatus() == 100) {
                                VideoDetailEntity mVideo = new Gson().fromJson(t, VideoDetailEntity.class);
                                if (mVideo != null && mVideo.getResult() != null) {
                                    if (mVideo.getResult().getItem() != null) {
                                        mVideoName = mVideo.getResult().getItem().getName();
                                        objtype = mVideo.getResult().getItem().getType();
                                        if(objtype==1){
                                            tv_yanyuan_line.setVisibility(View.GONE);
                                            tv_yanyuan.setVisibility(View.GONE);
                                            photos_recycler_view.setVisibility(View.GONE);
                                        }else{
                                            tv_yanyuan_line.setVisibility(View.VISIBLE);
                                            tv_yanyuan.setVisibility(View.VISIBLE);
                                            photos_recycler_view.setVisibility(View.VISIBLE);
                                        }
                                        toRePlayVideo(isFisrt, mVideo.getResult().getItem().getPic(), mVideo.getResult().getItem().getPath());
                                       // int vip = ShareUtils.getInt(VideoDetailActivity.this, "vip", 0);
                                        int usertype= ShareUtils.getInt(VideoDetailActivity.this, "usertype",0);  // 0-普通用户；1-试用会员；2-正式会员
                                        if(usertype==2){
                                            String extime = ShareUtils.getString(VideoDetailActivity.this, "extime", "");
                                            if(!TextUtils.isEmpty(extime) && TimeUtils.isBefore(
                                                    TimeUtils.getDateTimeForStr(TimeUtils.DF_YYYY_MM_DD_HH_MM_SS,extime),TimeUtils.getCurrentDate(TimeUtils.DF_YYYY_MM_DD_HH_MM_SS))){
                                                //VIP已过期
                                                showOpenShow();
                                            }
                                        }else if(usertype==1){
                                             vipTo.setVisibility(View.VISIBLE);
                                             vipTo.setText("限免24小时！开通无限看>");
                                        }else{
                                            vipTo.setVisibility(View.VISIBLE);
                                            vipTo.setText("您还不是会员，成为VIP会员，内容无限看>");

                                        }
                                       if(mVideo.getResult().getItem().getLabels()!=null){
                                            dataQB.clear();
                                            dataQB.addAll(mVideo.getResult().getItem().getLabels());
                                            mBQAdapter.notifyDataSetChanged();
                                        }

                                        tv_text.setText(mVideo.getResult().getItem().getMemo());
                                        title.setText(mVideo.getResult().getItem().getName());
                                      //  videoMemo.setText(mVideo.getResult().getItem().getMemo());
                                        videoName.setText(mVideo.getResult().getItem().getName());
                                        tv_num.setText(mVideo.getResult().getItem().getViews() + "次数播放");
                                        favor = mVideo.getResult().getItem().getFavor();
                                        if (favor == 1) {
                                            tv_sc.setSelected(true);
                                        } else {
                                            tv_sc.setSelected(false);
                                        }
                                        Map<String, String> params = new HashMap<String, String>();
                                        params.put("videoid", videoId);

                                        //  DownloadFileTool.doDownloadThread(tokenCode,tokenInfo,StaticClass.domain1 + "/video/getdata.html",new Gson().toJson(params));
                                    }

                                    if (mVideo.getResult().getActors() != null) {
                                        photos.clear();
                                        photos.addAll(mVideo.getResult().getActors());

                                        mPhotosAdapter.notifyDataSetChanged();
                                    }

                                    if (mVideo.getResult().getRecommends() != null) {
                                        videos.clear();
                                        videos.addAll(mVideo.getResult().getRecommends());
                                        mVideoAdapter.notifyDataSetChanged();
                                    }
                                }

                            } else {
                                ToastUtil.showToast(VideoDetailActivity.this, "获取视频详情失败");
                            }

                        } else {
                            ToastUtil.showToast(VideoDetailActivity.this, "返回数据异常");
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        LogUtil.e(e.getMessage());
                        ToastUtil.showToast(VideoDetailActivity.this, "返回数据异常");
                    }

                } else {
                    ToastUtil.showToast(VideoDetailActivity.this, "返回数据异常");
                }

            }

            @Override
            public void onFailure(VolleyError error) {
                dismissProgress();
                LogUtil.i("onFailure...." + error.getMessage());
                ToastUtil.showToast(VideoDetailActivity.this, "请求失败");
            }

        });
    }


    private void getVideLines() {
        //http://serpro/mobile/video/lines.html
        HttpParams post_params = new HttpParams();
        post_params.putHeaders("tokenCode", tokenCode);
        post_params.putHeaders("tokenInfo", tokenInfo);
        post_params.putHeaders("Content-Type", "application/json");

        RxVolley.jsonPost(StaticClass.domain1 + "/video/lines.html", post_params, new HttpCallback() {
            @Override
            public void onSuccess(String t) {

                LogUtil.i("*************video**dtl*sq****:" + t);
                if (!TextUtils.isEmpty(t)) {
                    try {
                        BaseEntity mBaseEntity = new Gson().fromJson(t, BaseEntity.class);
                        if (mBaseEntity != null) {
                            if (mBaseEntity.getStatus() == 100) {
                                VideoLinesEntity mVideoLines = new Gson().fromJson(t, VideoLinesEntity.class);
                                if (mVideoLines != null && mVideoLines.getResult() != null) {
                                    if (mVideoLines.getResult().getItems() != null) {
                                        data.clear();
                                        data.addAll(mVideoLines.getResult().getItems());
                                        adapter.notifyDataSetChanged();
                                    }

                                }

                            } else {
                                ToastUtil.showToast(VideoDetailActivity.this, "获取视频线路失败");
                            }

                        } else {
                            ToastUtil.showToast(VideoDetailActivity.this, "返回数据异常");
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        ToastUtil.showToast(VideoDetailActivity.this, "返回数据异常");
                    }

                } else {
                    ToastUtil.showToast(VideoDetailActivity.this, "返回数据异常");
                }

            }

            @Override
            public void onFailure(VolleyError error) {
                LogUtil.i("onFailure...." + error.getMessage());
                ToastUtil.showToast(VideoDetailActivity.this, "请求失败");
            }

        });

    }

    ProgressDialog mProgressDialog;

    public void showProgress(String msg) {
        if (mProgressDialog == null) mProgressDialog = new ProgressDialog(this);
        mProgressDialog.setMessage(msg);
        if (!mProgressDialog.isShowing())
            mProgressDialog.show();
    }

    public void dismissProgress() {
        if (mProgressDialog != null && mProgressDialog.isShowing())
            mProgressDialog.dismiss();
    }

    public void setProgressCancelable(boolean isCancel) {
        if (mProgressDialog == null)
            mProgressDialog = new ProgressDialog(this);
        mProgressDialog.setCancelable(isCancel);
    }

    private int objtype=0;
    private void setfavor() {
        //http://serpro/mobile/user/favor.html


        HttpParams post_params = new HttpParams();
        post_params.putHeaders("tokenCode", tokenCode);
        post_params.putHeaders("tokenInfo", tokenInfo);
        post_params.putHeaders("Content-Type", "application/json");

        Map<String, String> params = new HashMap<String, String>();
        params.put("objtype", ""+objtype);
        params.put("objid", videoId);
        post_params.putJsonParams(new Gson().toJson(params));
        showProgress("正在加载...");
        RxVolley.jsonPost(StaticClass.domain + "/favor.html", post_params, new HttpCallback() {
            @Override
            public void onSuccess(String t) {
                dismissProgress();
                LogUtil.i("******favor*******commit*sq****:" + t);
                if (!TextUtils.isEmpty(t)) {
                    try {
                        BaseEntity mBaseEntity = new Gson().fromJson(t, BaseEntity.class);
                        if (mBaseEntity != null) {
                            if (mBaseEntity.getStatus() == 100) {
                                //  ToastUtil.showToast(VideoDetailActivity.this, "收藏成功");
                                if (favor == 0) {
                                    tv_sc.setSelected(true);
                                    favor = 1;
                                } else if (favor == 1) {
                                    tv_sc.setSelected(false);
                                    favor = 0;
                                }

                            } else {
                                ToastUtil.showToast(VideoDetailActivity.this, "收藏失败");
                            }

                        } else {
                            ToastUtil.showToast(VideoDetailActivity.this, "返回数据异常");
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        ToastUtil.showToast(VideoDetailActivity.this, "返回数据异常");
                    }

                } else {
                    ToastUtil.showToast(VideoDetailActivity.this, "返回数据异常");
                }

            }

            @Override
            public void onFailure(VolleyError error) {
                dismissProgress();
                LogUtil.i("onFailure...." + error.getMessage());
                ToastUtil.showToast(VideoDetailActivity.this, "请求失败");
            }

        });


    }

    private void downloadVide(String name) {
        HttpParams post_params = new HttpParams();
        post_params.putHeaders("tokenCode", tokenCode);
        post_params.putHeaders("tokenInfo", tokenInfo);
        post_params.putHeaders("Content-Type", "application/json");


        Map<String, String> params = new HashMap<String, String>();
        params.put("videoid", videoId);

        post_params.putJsonParams(new Gson().toJson(params));

        final String storeFilePath = Environment.getExternalStorageDirectory().getPath() + "/CnVideo/Videos/" + name + ".mp4";//
        File file = new File(Environment.getExternalStorageDirectory().getPath() + "/CnVideo/Videos");
        if (!file.exists()) {
            if (!file.mkdirs()) {
                LogUtil.d("创建失败，请检查路径和是否配置文件权限！");
            }

        }
        String path = Environment.getExternalStorageDirectory().getPath() + "/AAAAImg/";
        //    VideoDownloadUtil.download( tokenCode,tokenInfo,StaticClass.domain1 + "/video/getdata.html",new Gson().toJson(params),storeFilePath,3*100);
        try {
            VideoDownloadUtil.downLoadFromUrl(tokenCode, tokenInfo, StaticClass.domain1 + "/video/getdata.html", new Gson().toJson(params), name + "_.mp4", path);
        } catch (IOException e) {
            e.printStackTrace();
            LogUtil.e(e.getMessage());
        }
       /* download(storeFilePath, StaticClass.domain1 + "/video/getdata.html", post_params
                , new ProgressListener() {
                    @Override
                    public void onProgress(long transferredBytes, long totalSize) {
                        LogUtil.e("--------onProgress--------"+transferredBytes);
                          if(totalSize/transferredBytes>=10){
                              toStart(storeFilePath);
                          }
                    }
                }, new HttpCallback() {
                    @Override
                    public void onSuccess(String t) {
                        LogUtil.i("******download*******onSuccess*sq****:" + t);
                    }

                    @Override
                    public void onFailure(VolleyError error) {

                        LogUtil.i("onFailure...." + error.getMessage());
                    }
                });*/

    }


    /**
     * 下载
     *
     * @param storeFilePath    本地存储绝对路径
     * @param url              要下载的文件的url
     * @param progressListener 下载进度回调
     * @param callback         回调
     */
    public static void download(String storeFilePath, String url, HttpParams params, ProgressListener
            progressListener, HttpCallback callback) {
        RequestConfig config = new RequestConfig();
        config.mUrl = url;
        config.mRetryPolicy = new DefaultRetryPolicy(DefaultRetryPolicy.DEFAULT_TIMEOUT_MS,
                20, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        FileRequest request = new FileRequest(storeFilePath, config, callback);
        request.setTag(url);
        request.setOnProgressListener(progressListener);
        new RxVolley.Builder().params(params).contentType(RxVolley.ContentType.JSON)
                .httpMethod(RxVolley.Method.POST).setRequest(request).doTask();

    }


    /**
     * 下载
     *
     * @param storeFilePath    本地存储绝对路径
     * @param url              要下载的文件的url
     * @param progressListener 下载进度回调
     * @param callback         回调
     */
    public static void download(String storeFilePath, String url, ProgressListener
            progressListener, HttpCallback callback) {
        RequestConfig config = new RequestConfig();
        config.mUrl = url;
        FileRequest request = new FileRequest(storeFilePath, config, callback);
        request.setOnProgressListener(progressListener);
        new RxVolley.Builder().setRequest(request).doTask();
    }


    private class MyTask extends AsyncTask<Void, Integer, Boolean> {

        private String mVideoName;

        public MyTask(String mVideoName) {
            this.mVideoName = mVideoName;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }


        @Override
        protected Boolean doInBackground(Void... params) {
            downloadVide(mVideoName);
            return null;
        }


        @Override
        protected void onPostExecute(Boolean aBoolean) {
            super.onPostExecute(aBoolean);

        }


    }

    private void showOpenShow() {
        new AlertDialog.Builder(VideoDetailActivity.this)
                .setMessage("您的权限已到期，请开通VIP会员继续享受！")
                .setPositiveButton("开通", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        startActivityForResult(new Intent(VideoDetailActivity.this, VipActivity.class), 1105);
                    }
                }).setNegativeButton("取消", null).show();


    }

    //消息推送通知收到事件
    @Subscribe
    public void onEvent(String name) {

        if (name.equals("EventBus_A") || name.equals("EventBus_B")) {
            getUserInfo();

        } else {

        }
    }


    private void getUserInfo() {
        //http://serpro/mobile/user/info.html
        HttpParams post_params = new HttpParams();
        // Map<String, String> params = new HashMap<String, String>();
        post_params.putHeaders("tokenCode", tokenCode);
        post_params.putHeaders("tokenInfo", tokenInfo);
        post_params.putHeaders("Content-Type", "application/json");
        // post_params.putJsonParams(new Gson().toJson(params));


        RxVolley.jsonPost(StaticClass.domain + "/info.html", post_params, new HttpCallback() {
            @Override
            public void onSuccess(String t) {

                LogUtil.i("**********123***getUserInfo***sq****:" + t);
                if (!TextUtils.isEmpty(t)) {
                    try {
                        BaseEntity mBaseEntity = new Gson().fromJson(t, BaseEntity.class);
                        if (mBaseEntity != null) {
                            if (mBaseEntity.getStatus() == 100) {
                                UserInfo mUserInfo = new Gson().fromJson(t, UserInfo.class);
                                if (mUserInfo != null && mUserInfo.getResult() != null) {
                                    ShareUtils.putString(VideoDetailActivity.this, "nickname", mUserInfo.getResult().getNickname());

                                    ShareUtils.putString(VideoDetailActivity.this, "portrait", mUserInfo.getResult().getPortrait());

                                    ShareUtils.putString(VideoDetailActivity.this, "userno", mUserInfo.getResult().getUserno());
                                    ShareUtils.putInt(VideoDetailActivity.this, "vip", mUserInfo.getResult().getVip());
                                    ShareUtils.putString(VideoDetailActivity.this, "extime", mUserInfo.getResult().getExtime());

                                    ShareUtils.putString(VideoDetailActivity.this, "url", mUserInfo.getResult().getUrl());


                                    if (mUserInfo.getResult().getVip() == 1) {

                                        if(!TextUtils.isEmpty(mUserInfo.getResult().getExtime()) && TimeUtils.isAfter(
                                                TimeUtils.getDateTimeForStr(TimeUtils.DF_YYYY_MM_DD_HH_MM_SS,mUserInfo.getResult().getExtime()),TimeUtils.getCurrentDate(TimeUtils.DF_YYYY_MM_DD_HH_MM_SS))){
                                            msCurrentPosition = getCurPlay().getCurrentPositionWhenPlaying();
                                            ipPath = "";
                                            urlPath = "";
                                            favor = 0;
                                            getVideoData(false);
                                            getVideLines();
                                        }else{
                                          //VIP已过期
                                        }


                                    }



                                } else {

                                }

                            } else {

                            }

                        } else {

                        }
                    } catch (Exception e) {
                        e.printStackTrace();

                    }

                } else {

                }


            }

            @Override
            public void onFailure(VolleyError error) {
                LogUtil.i("onFailure...." + error.getMessage());

            }

        });
    }



}
