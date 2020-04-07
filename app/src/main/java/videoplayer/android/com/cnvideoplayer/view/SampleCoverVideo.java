package videoplayer.android.com.cnvideoplayer.view;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Point;
import android.util.AttributeSet;
import android.view.Surface;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.shuyu.gsyvideoplayer.GSYVideoManager;
import com.shuyu.gsyvideoplayer.listener.GSYMediaPlayerListener;
import com.shuyu.gsyvideoplayer.utils.Debuger;
import com.shuyu.gsyvideoplayer.video.StandardGSYVideoPlayer;
import com.shuyu.gsyvideoplayer.video.base.GSYBaseVideoPlayer;
import com.shuyu.gsyvideoplayer.video.base.GSYVideoPlayer;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import videoplayer.android.com.cnvideoplayer.R;
import videoplayer.android.com.cnvideoplayer.entity.SwitchVideoModel;

/**
 * Date: 2018/9/5
 * Author:
 * Email：
 * Des：
 */

public class SampleCoverVideo extends StandardGSYVideoPlayer {
    private TextView mSwitchSize;
    private String mTypeText = "标准";
    private List<SwitchVideoModel> mUrlList = new ArrayList<>();
    //切换过程中最好弹出loading，不给其他任何操作
    private ProgressDialog mLoadingDialog;
    private boolean isChanging;


    ImageView mCoverImage;

    String mCoverOriginUrl;

    int mDefaultRes;

    public SampleCoverVideo(Context context, Boolean fullFlag) {
        super(context, fullFlag);
    }

    public SampleCoverVideo(Context context) {
        super(context);
    }

    public SampleCoverVideo(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void init(Context context) {
        super.init(context);

        mCoverImage = (ImageView) findViewById(R.id.thumbImage);

        if (mThumbImageViewLayout != null &&
                (mCurrentState == -1 || mCurrentState == CURRENT_STATE_NORMAL || mCurrentState == CURRENT_STATE_ERROR)) {
            mThumbImageViewLayout.setVisibility(VISIBLE);
        }

        mSwitchSize = (TextView) findViewById(R.id.switchSize);
        //切换视频清晰度
        mSwitchSize.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mHadPlay && !isChanging) {
                    showSwitchDialog();
                }
            }
        });

    }

    @Override
    public int getLayoutId() {
        return R.layout.video_layout_cover;
    }

    /**
     * 设置播放URL
     *
     * @param url           播放url
     * @param cacheWithPlay 是否边播边缓存
     * @param title         title
     * @return
     */
    public boolean setUp(List<SwitchVideoModel> url, boolean cacheWithPlay, String title) {
        mUrlList = url;
        return setUp(url.get(mSourcePosition).getUrl(), cacheWithPlay, title);
    }

    /**
     * 设置播放URL
     *
     * @param url           播放url
     * @param cacheWithPlay 是否边播边缓存
     * @param cachePath     缓存路径，如果是M3U8或者HLS，请设置为false
     * @param title         title
     * @return
     */
    public boolean setUp(List<SwitchVideoModel> url, boolean cacheWithPlay, File cachePath, String title) {
        mUrlList = url;
        return setUp(url.get(mSourcePosition).getUrl(), cacheWithPlay, cachePath, title);
    }

    public void loadCoverImage(String url, int res) {
        mCoverOriginUrl = url;
        mDefaultRes = res;
        Glide.with(getContext().getApplicationContext())
                .setDefaultRequestOptions(
                        new RequestOptions()
                                .frame(1000000)
                                .centerCrop()
                                .placeholder(res)
                                .error(res) )
                .load(url)
                .into(mCoverImage);
    }

    @Override
    public GSYBaseVideoPlayer startWindowFullscreen(Context context, boolean actionBar, boolean statusBar) {
        GSYBaseVideoPlayer gsyBaseVideoPlayer = super.startWindowFullscreen(context, actionBar, statusBar);
        SampleCoverVideo sampleCoverVideo = (SampleCoverVideo) gsyBaseVideoPlayer;
        sampleCoverVideo.loadCoverImage(mCoverOriginUrl, mDefaultRes);
        return gsyBaseVideoPlayer;
    }


    @Override
    public GSYBaseVideoPlayer showSmallVideo(Point size, boolean actionBar, boolean statusBar) {
        //下面这里替换成你自己的强制转化
        SampleCoverVideo sampleCoverVideo = (SampleCoverVideo) super.showSmallVideo(size, actionBar, statusBar);
        sampleCoverVideo.mStartButton.setVisibility(GONE);
        sampleCoverVideo.mStartButton = null;
        return sampleCoverVideo;
    }


    /******************* 下方两个重载方法，在播放开始前不屏蔽封面，不需要可屏蔽 ********************/
    @Override
    public void onSurfaceUpdated(Surface surface) {
        super.onSurfaceUpdated(surface);
        if (mThumbImageViewLayout != null && mThumbImageViewLayout.getVisibility() == VISIBLE) {
            mThumbImageViewLayout.setVisibility(INVISIBLE);
        }
    }

    @Override
    protected void setViewShowState(View view, int visibility) {
        if (view == mThumbImageViewLayout && visibility != VISIBLE) {
            return;
        }
        super.setViewShowState(view, visibility);
    }


    /******************* 下方重载方法，在播放开始不显示底部进度和按键，不需要可屏蔽 ********************/

    protected boolean byStartedClick;

    @Override
    protected void onClickUiToggle() {
        if (mIfCurrentIsFullscreen && mLockCurScreen && mNeedLockFull) {
            setViewShowState(mLockScreen, VISIBLE);
            return;
        }
        byStartedClick = true;
        super.onClickUiToggle();

    }

    @Override
    protected void changeUiToNormal() {
        super.changeUiToNormal();
        byStartedClick = false;
    }

    @Override
    protected void changeUiToPreparingShow() {
        super.changeUiToPreparingShow();
        Debuger.printfLog("Sample changeUiToPreparingShow");
        setViewShowState(mBottomContainer, INVISIBLE);
        setViewShowState(mStartButton, INVISIBLE);
    }

    @Override
    protected void changeUiToPlayingBufferingShow() {
        super.changeUiToPlayingBufferingShow();
        Debuger.printfLog("Sample changeUiToPlayingBufferingShow");
        if (!byStartedClick) {
            setViewShowState(mBottomContainer, INVISIBLE);
            setViewShowState(mStartButton, INVISIBLE);
        }
    }

    @Override
    protected void changeUiToPlayingShow() {
        super.changeUiToPlayingShow();
        Debuger.printfLog("Sample changeUiToPlayingShow");
        if (!byStartedClick) {
            setViewShowState(mBottomContainer, INVISIBLE);
            setViewShowState(mStartButton, INVISIBLE);
        }
    }

    @Override
    public void startAfterPrepared() {
        super.startAfterPrepared();
        Debuger.printfLog("Sample startAfterPrepared");
        setViewShowState(mBottomContainer, INVISIBLE);
        setViewShowState(mStartButton, INVISIBLE);
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {
        byStartedClick = true;
        super.onStartTrackingTouch(seekBar);
    }

    @Override
    public void onAutoCompletion() {
        super.onAutoCompletion();
        releaseTmpManager();
    }

    @Override
    public void onCompletion() {
        super.onCompletion();
        releaseTmpManager();
    }


    /**
     * 弹出切换清晰度
     */
    private void showSwitchDialog() {
        if (!mHadPlay) {
            return;
        }
        SwitchVideoTypeDialog switchVideoTypeDialog = new SwitchVideoTypeDialog(getContext());
        switchVideoTypeDialog.initList(mUrlList, new SwitchVideoTypeDialog.OnListItemClickListener() {
            @Override
            public void onItemClick(int position) {
                resolveStartChange(position);
            }
        });
        switchVideoTypeDialog.show();
    }


    //数据源
    private int mSourcePosition = 0;
    private int mPreSourcePosition = 0;
    private GSYVideoManager mTmpManager;

    private void releaseTmpManager() {
        if (mTmpManager != null) {
            mTmpManager.releaseMediaPlayer();
            mTmpManager = null;
        }
    }


    private void resolveStartChange(int position) {
        final String name = mUrlList.get(position).getName();
        if (mSourcePosition != position) {
            if ((mCurrentState == GSYVideoPlayer.CURRENT_STATE_PLAYING
                    || mCurrentState == GSYVideoPlayer.CURRENT_STATE_PAUSE)) {
                showLoading();
                final String url = mUrlList.get(position).getUrl();
                cancelProgressTimer();
                hideAllWidget();
                if (mTitle != null && mTitleTextView != null) {
                    mTitleTextView.setText(mTitle);
                }
                mPreSourcePosition = mSourcePosition;
                isChanging = true;
                mTypeText = name;
                mSwitchSize.setText(name);
                mSourcePosition = position;
                //创建临时管理器执行加载播放
                mTmpManager = GSYVideoManager.tmpInstance(gsyMediaPlayerListener);
                mTmpManager.initContext(getContext().getApplicationContext());
                resolveChangeUrl(mCache, mCachePath, url);
                mTmpManager.prepare(mUrl, mMapHeadData, mLooping, mSpeed, mCache, mCachePath);
                changeUiToPlayingBufferingShow();
            }
        } else {
            Toast.makeText(getContext(), "已经是 " + name, Toast.LENGTH_LONG).show();
        }
    }

    private void resolveChangedResult() {
        isChanging = false;
        mTmpManager = null;
        final String name = mUrlList.get(mSourcePosition).getName();
        final String url = mUrlList.get(mSourcePosition).getUrl();
        mTypeText = name;
        mSwitchSize.setText(name);
        resolveChangeUrl(mCache, mCachePath, url);
        hideLoading();
    }

    private void showLoading() {
        hideLoading();
        if (mLoadingDialog == null) mLoadingDialog = new ProgressDialog(mContext);
        mLoadingDialog.setMessage("正在切换...");
        if (!mLoadingDialog.isShowing())
            mLoadingDialog.show();
    }

    private void hideLoading() {
        if (mLoadingDialog != null && mLoadingDialog.isShowing()) {
            mLoadingDialog.dismiss();
            mLoadingDialog = null;
        }
    }

    private void resolveChangeUrl(boolean cacheWithPlay, File cachePath, String url) {
        if (mTmpManager != null) {
            mCache = cacheWithPlay;
            mCachePath = cachePath;
            mOriginUrl = url;
            this.mUrl = url;
        }
    }


    private GSYMediaPlayerListener gsyMediaPlayerListener = new GSYMediaPlayerListener() {
        @Override
        public void onPrepared() {
            if (mTmpManager != null) {
                mTmpManager.start();
                mTmpManager.seekTo(getCurrentPositionWhenPlaying());
            }
        }

        @Override
        public void onAutoCompletion() {

        }

        @Override
        public void onCompletion() {

        }

        @Override
        public void onBufferingUpdate(int percent) {

        }

        @Override
        public void onSeekComplete() {
            if (mTmpManager != null) {
                GSYVideoManager.instance().releaseMediaPlayer();
                GSYVideoManager.changeManager(mTmpManager);
                mTmpManager.setLastListener(SampleCoverVideo.this);
                mTmpManager.setListener(SampleCoverVideo.this);
                mTmpManager.setDisplay(mSurface);
                changeUiToPlayingClear();
                resolveChangedResult();
            }
        }

        @Override
        public void onError(int what, int extra) {
            mSourcePosition = mPreSourcePosition;
            if (mTmpManager != null) {
                mTmpManager.releaseMediaPlayer();
            }
            post(new Runnable() {
                @Override
                public void run() {
                    resolveChangedResult();
                    Toast.makeText(mContext, "切换失败", Toast.LENGTH_LONG).show();
                }
            });
        }

        @Override
        public void onInfo(int what, int extra) {

        }

        @Override
        public void onVideoSizeChanged() {

        }

        @Override
        public void onBackFullscreen() {

        }

        @Override
        public void onVideoPause() {

        }

        @Override
        public void onVideoResume() {

        }

        @Override
        public void onVideoResume(boolean seek) {

        }
    };

}
