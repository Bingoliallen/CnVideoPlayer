package videoplayer.android.com.cnvideoplayer.fragment;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.google.gson.Gson;
import com.kymjs.rxvolley.RxVolley;
import com.kymjs.rxvolley.client.HttpCallback;
import com.kymjs.rxvolley.client.HttpParams;
import com.kymjs.rxvolley.http.VolleyError;
import com.luck.picture.lib.PictureSelector;
import com.luck.picture.lib.config.PictureConfig;
import com.luck.picture.lib.config.PictureMimeType;
import com.luck.picture.lib.entity.LocalMedia;
import com.luck.picture.lib.tools.DateUtils;
import com.luck.picture.lib.tools.StringUtils;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.Bind;
import de.hdodenhof.circleimageview.CircleImageView;
import videoplayer.android.com.cnvideoplayer.GlideApp;
import videoplayer.android.com.cnvideoplayer.R;
import videoplayer.android.com.cnvideoplayer.base.BaseApplication;
import videoplayer.android.com.cnvideoplayer.base.BaseFragment;
import videoplayer.android.com.cnvideoplayer.entity.BaseEntity;
import videoplayer.android.com.cnvideoplayer.entity.UploadAttachement;
import videoplayer.android.com.cnvideoplayer.entity.UploadEntity;
import videoplayer.android.com.cnvideoplayer.entity.UserInfo;
import videoplayer.android.com.cnvideoplayer.ui.AcountBindingActivity;
import videoplayer.android.com.cnvideoplayer.ui.BrowseRecordsActivity;
import videoplayer.android.com.cnvideoplayer.ui.ConfirmPwdActivity;
import videoplayer.android.com.cnvideoplayer.ui.EmailLoginActivity;
import videoplayer.android.com.cnvideoplayer.ui.InvitationCodeActivity;
import videoplayer.android.com.cnvideoplayer.ui.LoginActivity;
import videoplayer.android.com.cnvideoplayer.ui.MBRecordsActivity;
import videoplayer.android.com.cnvideoplayer.ui.PhoneLoginActivity;
import videoplayer.android.com.cnvideoplayer.ui.PolicyTermsActivity;
import videoplayer.android.com.cnvideoplayer.ui.ProblemsActivity;
import videoplayer.android.com.cnvideoplayer.ui.SchemeManageActivity;
import videoplayer.android.com.cnvideoplayer.ui.ShareActivity;
import videoplayer.android.com.cnvideoplayer.ui.VipActivity;
import videoplayer.android.com.cnvideoplayer.utils.LogUtil;
import videoplayer.android.com.cnvideoplayer.utils.ShareUtils;
import videoplayer.android.com.cnvideoplayer.utils.StaticClass;
import videoplayer.android.com.cnvideoplayer.utils.TimeUtils;
import videoplayer.android.com.cnvideoplayer.utils.ToastUtil;
import videoplayer.android.com.cnvideoplayer.view.NormalTitleBar;


/**
 * Date: 2018/8/15
 * Author:
 * Email：
 * Des：
 */

public class MineFragment extends BaseFragment implements View.OnClickListener {

    TextView xb;
    TextView jh;
    TextView jx;
    TextView dw, dwCode;
    TextView dz;
    TextView userName;
    ImageView userLogo;

   @Bind(R.id.ntb)
    NormalTitleBar ntb;

    @Bind(R.id.iv_avatar)
    CircleImageView iv_avatar;


    @Bind(R.id.itemMB)
    LinearLayout itemMB;
    @Bind(R.id.itemLLJL)
    LinearLayout itemLLJL;
    @Bind(R.id.iteFXTJ)
    LinearLayout iteFXTJ;
    @Bind(R.id.itemwtyj)
    LinearLayout itemwtyj;
    @Bind(R.id.itemzctk)
    LinearLayout itemzctk;
    @Bind(R.id.itemSRYQM)
    LinearLayout itemSRYQM;
    @Bind(R.id.itemZHBD)
    LinearLayout itemZHBD;
    @Bind(R.id.itemFAGL)
    LinearLayout itemFAGL;


    @Bind(R.id.openVIP)
    Button openVIP;

    @Bind(R.id.openVIP1)
    Button openVIP1;

    @Bind(R.id.btn_change)
    Button btn_change;
    @Bind(R.id.tv_id)
    TextView tv_id;
    @Bind(R.id.tv_name)
    TextView tv_name;
    @Bind(R.id.tv_mb)
    TextView tv_mb;
    @Bind(R.id.tv_vip)
    TextView tv_vip;
    @Bind(R.id.tv_time_vip)
    TextView tv_time_vip;
    @Bind(R.id.tv_code)
    TextView tv_code;


    private PopupWindow pop;
    private int maxSelectNum = 1;
    private List<LocalMedia> selectList = new ArrayList<>();
    private boolean isFist=true;
    private String tokenCode;
    private String tokenInfo;
    private String email;
    private String mobile;
   // private int type;


    @Override
    protected int getLayoutResource() {
        return R.layout.fragment_mine;
    }

    @Override
    public void initPresenter() {
        iv_avatar.setOnClickListener(this);
        itemLLJL.setOnClickListener(this);
        iteFXTJ.setOnClickListener(this);
        itemMB.setOnClickListener(this);
        itemwtyj.setOnClickListener(this);
        itemzctk.setOnClickListener(this);
        openVIP.setOnClickListener(this);
        openVIP1.setOnClickListener(this);
        btn_change.setOnClickListener(this);

        itemFAGL.setOnClickListener(this);
        itemZHBD.setOnClickListener(this);
        itemSRYQM.setOnClickListener(this);
    }

    @Override
    protected void initView() {
        ntb.setBackgroundResource(R.mipmap.tar_bg);
        ntb.setTvLeftVisiable(false);
        ntb.setTitleText(getString(R.string.mine_title));
        initData();

    }

    protected void initData() {
        tokenCode = ShareUtils.getString(getActivity(), "tokenCode", "");
        tokenInfo = ShareUtils.getString(getActivity(), "tokenInfo", "");

        String nickname = ShareUtils.getString(getActivity(), "nickname", "");
        String portrait = ShareUtils.getString(getActivity(), "portrait", "");
        String userno = ShareUtils.getString(getActivity(), "userno", "");
        String extime = ShareUtils.getString(getActivity(), "extime", "");
        String balance = ShareUtils.getString(getActivity(), "balance", "");
        String userid = ShareUtils.getString(getActivity(), "userid", "");
        email = ShareUtils.getString(getActivity(), "email", "");
        mobile = ShareUtils.getString(getActivity(), "mobile", "");
       // type = ShareUtils.getInt(getActivity(), "type", 0);
        tv_id.setText("" + userid);
        if (!TextUtils.isEmpty(balance)) {
            tv_mb.setText(balance+"秘币");
        }else{
            tv_mb.setText(0+"秘币");
        }


        int vip = ShareUtils.getInt(getActivity(), "vip", 0);
        tv_name.setText(nickname);
        if (!TextUtils.isEmpty(userno)) {

            tv_code.setText("我的邀请码：" + userno);
        } else {

            tv_code.setText("");
        }

        if (vip == 1) {

            if(!TextUtils.isEmpty(extime) && TimeUtils.isAfter(
                    TimeUtils.getDateTimeForStr(TimeUtils.DF_YYYY_MM_DD_HH_MM_SS,extime),TimeUtils.getCurrentDate(TimeUtils.DF_YYYY_MM_DD_HH_MM_SS))){
                tv_vip.setVisibility(View.VISIBLE);
             //   openVIP.setVisibility(View.GONE);
                openVIP1.setVisibility(View.GONE);
                tv_time_vip.setVisibility(View.VISIBLE);
                tv_time_vip.setText("会员到期时间：" + extime);
            }else{
                tv_vip.setVisibility(View.INVISIBLE);
           //     openVIP.setVisibility(View.GONE);
                openVIP1.setVisibility(View.VISIBLE);
                tv_time_vip.setText("会员到期时间：");
                tv_time_vip.setVisibility(View.GONE);
            }

        } else {
            tv_vip.setVisibility(View.INVISIBLE);
            openVIP1.setVisibility(View.GONE);
           // openVIP.setVisibility(View.VISIBLE);
            tv_time_vip.setText("会员到期时间：");
            tv_time_vip.setVisibility(View.GONE);
        }

        GlideApp.with(getActivity())
                .load(portrait)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .placeholder(R.mipmap.moren_2)
                .error(R.mipmap.moren_2)
                .fitCenter()
                .into(iv_avatar);
        getUserInfo();
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {

            case R.id.btn_change:
                if(!TextUtils.isEmpty(email)){
                    startActivityForResult(new Intent(getActivity(), EmailLoginActivity.class), 0);
                }else if(!TextUtils.isEmpty(mobile)){
                    startActivityForResult(new Intent(getActivity(), PhoneLoginActivity.class), 0);
                }else{
                    startActivityForResult(new Intent(getActivity(), EmailLoginActivity.class), 0);
                }

             /*   setStatus();
                ShareUtils.putString(getActivity(), "tokenCode", "");
                ShareUtils.putString(getActivity(), "tokenInfo", "");
                ShareUtils.deleteAll(getActivity());
                startActivity(new Intent(getActivity(), LoginActivity.class));
                getActivity().finish();*/
                break;
            case R.id.iv_avatar:
                //     selPhoto();
                showPop();
                break;

            case R.id.itemZHBD:
               //有绑定过的时：先确认密码ConfirmPwdActivity再修改 type: 0 验证码，1邮件，2手机密码
                if(!TextUtils.isEmpty(email) || !TextUtils.isEmpty(mobile)){
                    startActivityForResult(new Intent(getActivity(), ConfirmPwdActivity.class), 0);
                }else{
                    Intent mIntent=new Intent(getActivity(), AcountBindingActivity.class);
                    mIntent.putExtra("mode",0);
                    startActivityForResult(mIntent, 0);
                }

                break;
            case R.id.itemFAGL:
                 startActivityForResult(new Intent(getActivity(), SchemeManageActivity.class), 0);
                break;
            case R.id.itemSRYQM:
                 startActivityForResult(new Intent(getActivity(), InvitationCodeActivity.class), 0);
                break;
            case R.id.itemMB:
                startActivityForResult(new Intent(getActivity(), MBRecordsActivity.class), 0);
                break;
            case R.id.itemLLJL:
                startActivityForResult(new Intent(getActivity(), BrowseRecordsActivity.class), 0);
                break;
            case R.id.iteFXTJ:
                startActivityForResult(new Intent(getActivity(), ShareActivity.class), 0);
                break;
            case R.id.itemwtyj:
                startActivityForResult(new Intent(getActivity(), ProblemsActivity.class), 0);
                break;
            case R.id.itemzctk:
                startActivityForResult(new Intent(getActivity(), PolicyTermsActivity.class), 0);
                break;
            case R.id.openVIP:
                startActivityForResult(new Intent(getActivity(), VipActivity.class), 0);
                break;
            case R.id.openVIP1:
                startActivityForResult(new Intent(getActivity(), VipActivity.class), 0);
                break;
        }
    }


    @Override
    public void onResume() {
        super.onResume();
        if(!isFist){
            isFist=false;
            getUserInfo();
        }else{
            isFist=false;

        }
    }

    private void selPhoto() {

        PictureSelector.create(MineFragment.this)
                .openGallery(PictureMimeType.ofImage())//全部.PictureMimeType.ofAll()、图片.ofImage()、视频.ofVideo()、音频.ofAudio()
                .theme(R.style.picture_default_style)//主题样式(不设置为默认样式) 也可参考demo values/styles下 例如：R.style.picture.white.style
                .maxSelectNum(1)// 最大图片选择数量 int
                .minSelectNum(1)// 最小选择数量 int
                .imageSpanCount(4)// 每行显示个数 int
                .selectionMode(PictureConfig.SINGLE)// 多选 or 单选 PictureConfig.MULTIPLE or PictureConfig.SINGLE
                .previewImage(true)// 是否可预览图片 true or false
                .previewVideo(true)// 是否可预览视频 true or false
                .enablePreviewAudio(true) // 是否可播放音频 true or false
                .isCamera(true)// 是否显示拍照按钮 true or false
                .imageFormat(PictureMimeType.PNG)// 拍照保存图片格式后缀,默认jpeg
                .isZoomAnim(true)// 图片列表点击 缩放效果 默认true
                .sizeMultiplier(0.5f)// glide 加载图片大小 0~1之间 如设置 .glideOverride()无效
                .setOutputCameraPath("/CustomPath")// 自定义拍照保存路径,可不填
                .enableCrop(true)// 是否裁剪 true or false
                .compress(true)// 是否压缩 true or false
                .glideOverride(160, 160)// int glide 加载宽高，越小图片列表越流畅，但会影响列表图片浏览的清晰度
                .withAspectRatio(1, 1)// int 裁剪比例 如16:9 3:2 3:4 1:1 可自定义
                .hideBottomControls(true)// 是否显示uCrop工具栏，默认不显示 true or false
                .isGif(true)// 是否显示gif图片 true or false
                .compressSavePath(getPath())//压缩图片保存地址
                .freeStyleCropEnabled(true)// 裁剪框是否可拖拽 true or false
                .circleDimmedLayer(true)// 是否圆形裁剪 true or false
                .showCropFrame(false)// 是否显示裁剪矩形边框 圆形裁剪时建议设为false   true or false
                .showCropGrid(false)// 是否显示裁剪矩形网格 圆形裁剪时建议设为false    true or false
                .openClickSound(true)// 是否开启点击声音 true or false
                .selectionMedia(null)// 是否传入已选图片 List<LocalMedia> list
                .previewEggs(true)// 预览图片时 是否增强左右滑动图片体验(图片滑动一半即可看到上一张是否选中) true or false
                .cropCompressQuality(90)// 裁剪压缩质量 默认90 int
                .minimumCompressSize(100)// 小于100kb的图片不压缩
                .synOrAsy(true)//同步true或异步false 压缩 默认同步
                //  .cropWH()// 裁剪宽高比，设置如果大于图片本身宽高则无效 int
                .rotateEnabled(false) // 裁剪是否可旋转图片 true or false
                .scaleEnabled(false)// 裁剪是否可放大缩小图片 true or false
                //.videoQuality()// 视频录制质量 0 or 1 int
                .videoMaxSecond(15)// 显示多少秒以内的视频or音频也可适用 int
                .videoMinSecond(10)// 显示多少秒以内的视频or音频也可适用 int
                .recordVideoSecond(60)//视频秒数录制 默认60s int
                .isDragFrame(false)// 是否可拖动裁剪框(固定)
                .forResult(PictureConfig.CHOOSE_REQUEST);//结果回调onActivityResult code

    }

    private void showPop() {
        View bottomView = View.inflate(getActivity(), R.layout.layout_bottom_dialog, null);
        TextView mAlbum = (TextView) bottomView.findViewById(R.id.tv_album);
        TextView mCamera = (TextView) bottomView.findViewById(R.id.tv_camera);
        TextView mCancel = (TextView) bottomView.findViewById(R.id.tv_cancel);

        pop = new PopupWindow(bottomView, -1, -2);
        pop.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        pop.setOutsideTouchable(true);
        pop.setFocusable(true);
        WindowManager.LayoutParams lp = getActivity().getWindow().getAttributes();
        lp.alpha = 0.5f;
        getActivity().getWindow().setAttributes(lp);
        pop.setOnDismissListener(new PopupWindow.OnDismissListener() {

            @Override
            public void onDismiss() {
                WindowManager.LayoutParams lp = getActivity().getWindow().getAttributes();
                lp.alpha = 1f;
                getActivity().getWindow().setAttributes(lp);
            }
        });
        pop.setAnimationStyle(R.style.main_menu_photo_anim);
        pop.showAtLocation(getActivity().getWindow().getDecorView(), Gravity.BOTTOM, 0, 0);

        View.OnClickListener clickListener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switch (view.getId()) {
                    case R.id.tv_album:
                        //相册
                        PictureSelector.create(MineFragment.this)
                                .openGallery(PictureMimeType.ofImage())
                                .maxSelectNum(maxSelectNum)
                                .minSelectNum(1)
                                .imageSpanCount(4)
                                .selectionMode(PictureConfig.SINGLE)// 多选 or 单选 PictureConfig.MULTIPLE or PictureConfig.SINGLE
                                .openClickSound(true)// 是否开启点击声音 true or false
                                .forResult(PictureConfig.CHOOSE_REQUEST);
                        break;
                    case R.id.tv_camera:
                        //拍照
                        PictureSelector.create(MineFragment.this)
                                .openCamera(PictureMimeType.ofImage())
                                .forResult(PictureConfig.CHOOSE_REQUEST);
                        break;
                    case R.id.tv_cancel:
                        //取消
                        //closePopupWindow();
                        break;
                }
                closePopupWindow();
            }
        };

        mAlbum.setOnClickListener(clickListener);
        mCamera.setOnClickListener(clickListener);
        mCancel.setOnClickListener(clickListener);
    }


    public void closePopupWindow() {
        if (pop != null && pop.isShowing()) {
            pop.dismiss();
            pop = null;
        }
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        List<LocalMedia> images;
        if (resultCode == Activity.RESULT_OK) {
            switch (requestCode) {
                case PictureConfig.CHOOSE_REQUEST:
                    // 图片选择结果回调

                    images = PictureSelector.obtainMultipleResult(data);
                    selectList.addAll(images);
                    for (LocalMedia media : selectList) {
                        int mimeType = media.getMimeType();
                        String path = "";
                        if (media.isCut() && !media.isCompressed()) {
                            // 裁剪过
                            path = media.getCutPath();
                        } else if (media.isCompressed() || (media.isCut() && media.isCompressed())) {
                            // 压缩过,或者裁剪同时压缩过,以最终压缩过图片为准
                            path = media.getCompressPath();
                        } else {
                            // 原图
                            path = media.getPath();
                        }
                        // 图片
                        if (media.isCompressed()) {
                            Log.i("compress image result:", new File(media.getCompressPath()).length() / 1024 + "k");
                            Log.i("压缩地址::", media.getCompressPath());
                        }

                        Log.i("原图地址::", media.getPath());
                        int pictureType = PictureMimeType.isPictureType(media.getPictureType());
                        if (media.isCut()) {
                            Log.i("裁剪地址::", media.getCutPath());
                        }
                        long duration = media.getDuration();


                        if (mimeType == PictureMimeType.ofAudio()) {
                            return;
                        } else {
                            RequestOptions options = new RequestOptions()
                                    .centerCrop()
                                    .placeholder(R.mipmap.moren_2)
                                    .diskCacheStrategy(DiskCacheStrategy.ALL);
                            Glide.with(iv_avatar.getContext())
                                    .load(path)
                                    .apply(options)
                                    .into(iv_avatar);
                        }
                       // setPic(path);
                        new MyTask(path).execute();
                    }


//                    selectList = PictureSelector.obtainMultipleResult(data);
                    // 例如 LocalMedia 里面返回三种path
                    // 1.media.getPath(); 为原图path
                    // 2.media.getCutPath();为裁剪后path，需判断media.isCut();是否为true
                    // 3.media.getCompressPath();为压缩后path，需判断media.isCompressed();是否为true
                    // 如果裁剪并压缩了，以取压缩路径为准，因为是先裁剪后压缩的
                    // adapter.setList(selectList);
                    //  adapter.notifyDataSetChanged();
                    break;
            }
        }
    }


    /**
     * 自定义压缩存储地址
     *
     * @return
     */
    private String getPath() {
        String path = Environment.getExternalStorageDirectory() + "/Luban/image/";
        File file = new File(path);
        if (file.mkdirs()) {
            return path;
        }
        return path;
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

                LogUtil.i("*************getUserInfo***sq****:" + t);
                if (!TextUtils.isEmpty(t)) {
                    try {
                        BaseEntity mBaseEntity = new Gson().fromJson(t, BaseEntity.class);
                        if (mBaseEntity != null) {
                            if (mBaseEntity.getStatus() == 100) {
                                UserInfo mUserInfo = new Gson().fromJson(t, UserInfo.class);
                                if (mUserInfo != null && mUserInfo.getResult() != null) {
                                    ShareUtils.putString(getActivity(), "nickname", mUserInfo.getResult().getNickname());
                                    ShareUtils.putString(getActivity(), "userid", mUserInfo.getResult().getUserid());
                                    ShareUtils.putString(getActivity(), "portrait", mUserInfo.getResult().getPortrait());

                                    ShareUtils.putString(getActivity(), "userno", mUserInfo.getResult().getUserno());
                                    ShareUtils.putInt(getActivity(), "vip", mUserInfo.getResult().getVip());
                                    ShareUtils.putString(getActivity(), "extime", mUserInfo.getResult().getExtime());

                                    ShareUtils.putString(getActivity(), "url", mUserInfo.getResult().getUrl());
                                    ShareUtils.putString(getActivity(), "balance", mUserInfo.getResult().getBalance());
                                    ShareUtils.putString(getActivity(), "email", mUserInfo.getResult().getEmail());
                                    ShareUtils.putString(getActivity(), "mobile", mUserInfo.getResult().getMobile());

                                    ShareUtils.putInt(getActivity(), "usertype", mUserInfo.getResult().getUsertype());
                                    ShareUtils.putInt(getActivity(), "exdays", mUserInfo.getResult().getExdays());

                                    mobile=mUserInfo.getResult().getMobile();
                                    email=mUserInfo.getResult().getEmail();

                                    if (!TextUtils.isEmpty(mUserInfo.getResult().getBalance())) {
                                        tv_mb.setText(mUserInfo.getResult().getBalance()+"秘币");
                                    }else{
                                        tv_mb.setText(0+"秘币");
                                    }


                                    tv_id.setText("" + mUserInfo.getResult().getUserid());
                                    tv_name.setText(mUserInfo.getResult().getNickname());
                                    if (!TextUtils.isEmpty(mUserInfo.getResult().getUserno())) {

                                        tv_code.setText("我的邀请码：" + mUserInfo.getResult().getUserno());
                                    } else {

                                        tv_code.setText("");
                                    }

                                    if (mUserInfo.getResult().getVip() == 1) {

                                        if(!TextUtils.isEmpty(mUserInfo.getResult().getExtime()) && TimeUtils.isAfter(
                                                TimeUtils.getDateTimeForStr(TimeUtils.DF_YYYY_MM_DD_HH_MM_SS,mUserInfo.getResult().getExtime()),TimeUtils.getCurrentDate(TimeUtils.DF_YYYY_MM_DD_HH_MM_SS))){
                                            tv_vip.setVisibility(View.VISIBLE);
                                          //  openVIP.setVisibility(View.GONE);
                                            openVIP1.setVisibility(View.GONE);
                                            tv_time_vip.setVisibility(View.VISIBLE);
                                            tv_time_vip.setText("会员到期时间：" + mUserInfo.getResult().getExtime());
                                        }else{
                                            tv_vip.setVisibility(View.INVISIBLE);
                                        //    openVIP.setVisibility(View.GONE);
                                            openVIP1.setVisibility(View.VISIBLE);
                                            tv_time_vip.setText("会员到期时间：");
                                            tv_time_vip.setVisibility(View.GONE);
                                        }


                                    } else {
                                        tv_vip.setVisibility(View.INVISIBLE);
                                        openVIP1.setVisibility(View.GONE);
                                     //   openVIP.setVisibility(View.VISIBLE);
                                        tv_time_vip.setText("会员到期时间：");
                                        tv_time_vip.setVisibility(View.GONE);
                                    }

                                    GlideApp.with(getActivity())
                                            .load(mUserInfo.getResult().getPortrait())
                                            .diskCacheStrategy(DiskCacheStrategy.ALL)
                                            .placeholder(R.mipmap.moren_2)
                                            .error(R.mipmap.moren_2)
                                            .fitCenter()
                                            .into(iv_avatar);


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


    private void setPic(String path) {
      //http://serpro/mobile/upload/uploadAttachement.html
        String boundary = "******";

        HttpParams post_params = new HttpParams();
        // Map<String, String> params = new HashMap<String, String>();
        post_params.putHeaders("tokenCode", tokenCode);
        post_params.putHeaders("tokenInfo", tokenInfo);
        post_params.putHeaders("Content-Type", "multipart/form-data" );
        post_params.putHeaders("Connection", "Keep-Alive");
        post_params.putHeaders("Charset", "UTF-8");
        post_params.put("imgFile",new File(path));

        RxVolley.post(StaticClass.domain1 + "/upload/uploadAttachement.html", post_params, new HttpCallback() {
            @Override
            public void onSuccess(String t) {

                LogUtil.i("*************setPic***sq****:" + t);
                if (!TextUtils.isEmpty(t)) {
                    try {
                        BaseEntity mBaseEntity = new Gson().fromJson(t, BaseEntity.class);
                        if (mBaseEntity != null) {
                            if (mBaseEntity.getStatus() == 100) {
                                UploadEntity mUploadEntity = new Gson().fromJson(t, UploadEntity.class);
                                if (mUploadEntity != null && mUploadEntity.getResult() != null) {
                                    if(mUploadEntity.getResult().getItems()!=null){
                                        for(UploadAttachement mUploadAttachement: mUploadEntity.getResult().getItems()){
                                            GlideApp.with(getActivity())
                                                    .load(mUploadAttachement.getHttppath())
                                                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                                                    .placeholder(R.mipmap.moren_2)
                                                    .error(R.mipmap.moren_2)
                                                    .fitCenter()
                                                    .into(iv_avatar);
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


    private class MyTask extends AsyncTask<Void, Integer, Boolean> {

        private String uploadFilePath;

        public MyTask(String uploadFilePath) {
            this.uploadFilePath = uploadFilePath;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }


        @Override
        protected Boolean doInBackground(Void... params) {
            uploadFile(StaticClass.domain1 + "/upload/uploadAttachement.html", uploadFilePath);
            return null;
        }


        @Override
        protected void onPostExecute(Boolean aBoolean) {
            super.onPostExecute(aBoolean);

        }


    }



    /**
     * @category 上传文件至Server的方法
     * @param uploadUrl 上传路径参数
     * @param uploadFilePath 文件路径
     * @author ylbf_dev
     */
    private void uploadFile(String uploadUrl,String uploadFilePath) {
        String end = "\r\n";
        String twoHyphens = "--";
        String boundary = "******";
        try {
            URL url = new URL(uploadUrl);
            HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
            httpURLConnection.setDoInput(true);
            httpURLConnection.setDoOutput(true);
            httpURLConnection.setUseCaches(false);

            httpURLConnection.setRequestMethod("POST");
            httpURLConnection.setRequestProperty("tokenCode", tokenCode);
            httpURLConnection.setRequestProperty("tokenInfo", tokenInfo);
            httpURLConnection.setRequestProperty("Connection", "Keep-Alive");
            httpURLConnection.setRequestProperty("Charset", "UTF-8");
            httpURLConnection.setRequestProperty("Content-Type", "multipart/form-data;boundary=" + boundary);

            DataOutputStream dos = new DataOutputStream(httpURLConnection.getOutputStream());
            dos.writeBytes(twoHyphens + boundary + end);
            dos.writeBytes("Content-Disposition: form-data; name=\"imgFile\"; filename=\"test.jpg\"" + end);
//          dos.writeBytes("Content-Disposition: form-data; name=\"file\"; filename=\""
//                  + uploadFilePath.substring(uploadFilePath.lastIndexOf("/") + 1) + "\"" + end);
            dos.writeBytes(end);
            // 文件通过输入流读到Java代码中-++++++++++++++++++++++++++++++`````````````````````````
            FileInputStream fis = new FileInputStream(new File(uploadFilePath));
            byte[] buffer = new byte[8192]; // 8k
            int count = 0;
            while ((count = fis.read(buffer)) != -1) {
                dos.write(buffer, 0, count);

            }
            fis.close();
            System.out.println("file send to server............");
            dos.writeBytes(end);
            dos.writeBytes(twoHyphens + boundary + twoHyphens + end);
            dos.flush();

            // 读取服务器返回结果
            InputStream is = httpURLConnection.getInputStream();
            InputStreamReader isr = new InputStreamReader(is, "utf-8");
            BufferedReader br = new BufferedReader(isr);
            String result = br.readLine();
            LogUtil.e(result);
            dos.close();
            is.close();
            if (!TextUtils.isEmpty(result)) {
                try {
                    BaseEntity mBaseEntity = new Gson().fromJson(result, BaseEntity.class);
                    if (mBaseEntity != null) {
                        if (mBaseEntity.getStatus() == 100) {
                            UploadEntity mUploadEntity = new Gson().fromJson(result, UploadEntity.class);
                            if (mUploadEntity != null && mUploadEntity.getResult() != null) {
                                if(mUploadEntity.getResult().getItems()!=null){
                                    for(final UploadAttachement mUploadAttachement: mUploadEntity.getResult().getItems()){
                                        getActivity().runOnUiThread(new Runnable()
                                        {
                                            public void run()
                                            {
                                                GlideApp.with(getActivity())
                                                        .load(mUploadAttachement.getHttppath())
                                                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                                                        .placeholder(R.mipmap.moren_2)
                                                        .error(R.mipmap.moren_2)
                                                        .fitCenter()
                                                        .into(iv_avatar);
                                            }

                                        });

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



        } catch (Exception e) {
            e.printStackTrace();
            LogUtil.e(e.getMessage());
        }

    }

}

