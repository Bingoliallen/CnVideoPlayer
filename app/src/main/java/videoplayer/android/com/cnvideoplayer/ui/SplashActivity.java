package videoplayer.android.com.cnvideoplayer.ui;


import android.Manifest;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AlertDialog;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.ansen.http.net.HTTPCaller;
import com.gongwen.marqueen.SimpleMF;
import com.google.gson.Gson;
import com.kymjs.rxvolley.RxVolley;
import com.kymjs.rxvolley.client.HttpCallback;
import com.kymjs.rxvolley.client.HttpParams;
import com.kymjs.rxvolley.http.VolleyError;
import com.yanzhenjie.permission.AndPermission;
import com.yanzhenjie.permission.PermissionListener;
import com.yanzhenjie.permission.Rationale;
import com.yanzhenjie.permission.RationaleListener;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.github.lizhangqu.coreprogress.ProgressUIListener;
import videoplayer.android.com.cnvideoplayer.MainActivity;
import videoplayer.android.com.cnvideoplayer.R;
import videoplayer.android.com.cnvideoplayer.base.BaseCompatActivity;
import videoplayer.android.com.cnvideoplayer.entity.BannerData;
import videoplayer.android.com.cnvideoplayer.entity.BaseEntity;
import videoplayer.android.com.cnvideoplayer.entity.CheckUpdate;
import videoplayer.android.com.cnvideoplayer.entity.HomeData;
import videoplayer.android.com.cnvideoplayer.entity.LoginEntity;
import videoplayer.android.com.cnvideoplayer.entity.Share;
import videoplayer.android.com.cnvideoplayer.utils.DeviceUtil;
import videoplayer.android.com.cnvideoplayer.utils.LogUtil;
import videoplayer.android.com.cnvideoplayer.utils.NetworkUtils;
import videoplayer.android.com.cnvideoplayer.utils.ShareUtils;
import videoplayer.android.com.cnvideoplayer.utils.StaticClass;
import videoplayer.android.com.cnvideoplayer.utils.StatusBarUtil;
import videoplayer.android.com.cnvideoplayer.utils.ToastUtil;
import videoplayer.android.com.cnvideoplayer.utils.Utils;
import videoplayer.android.com.cnvideoplayer.view.StatusBarCompat;
import videoplayer.android.com.cnvideoplayer.view.SweetAlertDialogWeb;

public class SplashActivity extends BaseCompatActivity {

    protected Animation mFadeInScale;
    protected ImageView launcherView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
      /*  if (!isTaskRoot()) {
            finish();
            return;
        }*/
        if ((getIntent().getFlags() & Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT) != 0) {
            finish();
            return;
        }
        super.onCreate(savedInstanceState);

    }

    @Override
    protected String initTitle() {
        return "";
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_launcher;
    }

    @Override
    public void initView() {
        // StatusBarCompat.setStatusBarColor(this, ContextCompat.getColor(this, R.color.main_color));
        StatusBarUtil.immersive(this);

        getAuthority();
        initAnim();


    }

    @Override
    public void initData() {

    }

    protected void initAnim() {

        //  findViewById(R.id.rootView).setBackgroundResource(getLauncherResource());
       /* launcherView = (ImageView) findViewById(R.id.image);
        launcherView.setImageResource(getLauncherResource());
        mFadeInScale = AnimationUtils.loadAnimation(SplashActivity.this,
                R.anim.launcher_fade_in);

        mFadeInScale.setFillAfter(true);

        mFadeInScale.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }

            @Override
            public void onAnimationEnd(Animation animation) {





            }
        });
        launcherView.startAnimation(mFadeInScale);*/

        //Android 6.0以上版本需要临时获取权限
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP_MR1 &&
                PackageManager.PERMISSION_GRANTED != checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                &&
                PackageManager.PERMISSION_GRANTED != checkSelfPermission(Manifest.permission.REQUEST_INSTALL_PACKAGES)) {
            requestPermissions(perms, PERMS_REQUEST_CODE);
        } else {
            chaeckUpdate();
        }


    }

    protected int getLauncherResource() {
        return R.mipmap.splash_logo;
    }

    /**
     * 未登录-进行登录
     */
    protected void go2LoginUI() {
      /*  Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);*/

        //第一次登录
        if (!NetworkUtils.isAvailable(SplashActivity.this)) {
            ToastUtil.showToast(SplashActivity.this, "网络异常");
            return;
        }

        //http://serpro/mobile/user/serialnoLogin.html
        HttpParams post_params = new HttpParams();
        // post_params.putHeaders("tokenCode", tokenCode);
        //  post_params.putHeaders("tokenInfo", tokenInfo);
        post_params.putHeaders("Content-Type", "application/json");
        Map<String, String> params = new HashMap<String, String>();
        String deviceId = DeviceUtil.getDeviceId(this);
        if (TextUtils.isEmpty(deviceId)) {
            deviceId = DeviceUtil.getUniquePsuedoID();
        }
        params.put("serialno", deviceId);
        post_params.putJsonParams(new Gson().toJson(params));

        RxVolley.jsonPost(StaticClass.domain + "/serialnoLogin.html", post_params, new HttpCallback() {
            @Override
            public void onSuccess(String t) {

                LogUtil.i("*************serialnoLogin***sq****:" + t);
                if (!TextUtils.isEmpty(t)) {
                    try {
                        BaseEntity mBaseEntity = new Gson().fromJson(t, BaseEntity.class);
                        if (mBaseEntity != null) {
                            if (mBaseEntity.getStatus() == 100) {
                                LoginEntity mEntity = new Gson().fromJson(t, LoginEntity.class);
                                if (mEntity != null && mEntity.getResult() != null) {
                                    ShareUtils.putString(SplashActivity.this, "nickname", mEntity.getResult().getNickname());
                                    ShareUtils.putString(SplashActivity.this, "userid", mEntity.getResult().getUserid());
                                    ShareUtils.putString(SplashActivity.this, "portrait", mEntity.getResult().getPortrait());
                                    ShareUtils.putString(SplashActivity.this, "tokenCode", mEntity.getResult().getTokenCode());
                                    ShareUtils.putString(SplashActivity.this, "tokenInfo", mEntity.getResult().getTokenInfo());
                                    ShareUtils.putString(SplashActivity.this, "userno", mEntity.getResult().getUserno());
                                    ShareUtils.putInt(SplashActivity.this, "vip", mEntity.getResult().getVip());
                                    ShareUtils.putString(SplashActivity.this, "extime", mEntity.getResult().getExtime());

                                    ShareUtils.putString(SplashActivity.this, "url", mEntity.getResult().getUrl());

                                    ShareUtils.putString(SplashActivity.this, "balance", mEntity.getResult().getBalance());
                                    ShareUtils.putString(SplashActivity.this, "email", mEntity.getResult().getEmail());

                                    ShareUtils.putString(SplashActivity.this, "mobile", mEntity.getResult().getMobile());

                                    ShareUtils.putInt(SplashActivity.this, "usertype", mEntity.getResult().getUsertype());
                                    ShareUtils.putInt(SplashActivity.this, "exdays", mEntity.getResult().getExdays());

                                }
                                MainActivity.startAction(SplashActivity.this);
                                setStatus();
                                finish();

                            } else {

                            }

                        } else {

                        }
                    } catch (Exception e) {
                        e.printStackTrace();

                    }

                } else {
                    ToastUtil.showToast(SplashActivity.this, "服务器异常");
                }

            }

            @Override
            public void onFailure(VolleyError error) {

                LogUtil.i("onFailure...." + error.getMessage());
                ToastUtil.showToast(SplashActivity.this, "服务器异常");

            }

        });


    }

    /**
     * 已登录-跳到主页
     */
    protected void go2HomeUI() {

        setStatus();
        MainActivity.startAction(SplashActivity.this);
        finish();
    }

    @Override
    public void onClick(View v) {

    }

    private void setStatus() {
        //http://serpro/mobile/user/status.html
        String tokenCode = ShareUtils.getString(SplashActivity.this, "tokenCode", "");
        String tokenInfo = ShareUtils.getString(SplashActivity.this, "tokenInfo", "");
        HttpParams post_params = new HttpParams();
        post_params.putHeaders("tokenCode", tokenCode);
        post_params.putHeaders("tokenInfo", tokenInfo);
        post_params.putHeaders("Content-Type", "application/json");
        Map<String, String> params = new HashMap<String, String>();
        params.put("status", "" + 1);
        post_params.putJsonParams(new Gson().toJson(params));

        RxVolley.jsonPost(StaticClass.domain + "/status.html", post_params, new HttpCallback() {
            @Override
            public void onSuccess(String t) {

                LogUtil.i("*************status***sq****:" + t);
                if (!TextUtils.isEmpty(t)) {
                    try {
                        BaseEntity mBaseEntity = new Gson().fromJson(t, BaseEntity.class);
                        if (mBaseEntity != null) {
                            if (mBaseEntity.getStatus() == 100) {


                            } else {
                                //   ToastUtil.showToast(S.this, "获取失败");
                            }

                        } else {
                            //  ToastUtil.showToast(PolicyTermsActivity.this, "返回数据异常");
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        // ToastUtil.showToast(PolicyTermsActivity.this, "返回数据异常");
                    }

                } else {
                    //  ToastUtil.showToast(PolicyTermsActivity.this, "返回数据异常");
                }

            }

            @Override
            public void onFailure(VolleyError error) {

                LogUtil.i("onFailure...." + error.getMessage());
                // ToastUtil.showToast(PolicyTermsActivity.this, "请求失败");
            }

        });
    }

    private void toGoMain() {


        //增加判断用户是否已经登录
        String tokenCode = ShareUtils.getString(SplashActivity.this, "tokenCode", "");
        String tokenInfo = ShareUtils.getString(SplashActivity.this, "tokenInfo", "");
        if (!TextUtils.isEmpty(tokenCode) && !TextUtils.isEmpty(tokenInfo)) {
            go2HomeUI();
        } else {
            go2LoginUI();
        }
    }

    private String[] perms = {Manifest.permission.READ_PHONE_STATE,Manifest.permission.REQUEST_INSTALL_PACKAGES, Manifest.permission.WRITE_EXTERNAL_STORAGE};
    private final int PERMS_REQUEST_CODE = 200;

    private void chaeckUpdate() {
        checkIMSI();
        if (!NetworkUtils.isAvailable(SplashActivity.this)) {
            ToastUtil.showToast(SplashActivity.this, "网络异常");
            toGoMain();
            return;
        }
        //请求接口
        //http://serpro/mobile/setting/getVersionInfo.html
        HttpParams post_params = new HttpParams();
        post_params.putHeaders("Content-Type", "application/json");
        //  Map<String, String> params = new HashMap<String, String>();
        //  params.put("versionCode", "" + Utils.getVersionCode(this));
        // post_params.putJsonParams(new Gson().toJson(params));

        LogUtil.e("---------url-------" + StaticClass.domain1 + "/setting/getVersionInfo.html");
        RxVolley.jsonPost(StaticClass.domain1 + "/setting/getVersionInfo.html", post_params, new HttpCallback() {
            @Override
            public void onSuccess(String t) {

                LogUtil.i("*************update***sq****:" + t);
                if (!TextUtils.isEmpty(t)) {
                    try {
                        BaseEntity mBaseEntity = new Gson().fromJson(t, BaseEntity.class);
                        if (mBaseEntity != null) {
                            if (mBaseEntity.getStatus() == 100) {

                                CheckUpdate mUserInfo = new Gson().fromJson(t, CheckUpdate.class);
                                if (mUserInfo != null && mUserInfo.getResult() != null) {

                                    //比较版本信息
                                    try {
                                        int result = Utils.compareVersion(Utils.getVersionName(SplashActivity.this), mUserInfo.getResult().getVersionName());
                                        LogUtil.e("------compareVersion----------:" + result);
                                        if (result < 0) {//不是最新版本
                                            showUpdaloadDialog(mUserInfo.getResult().getUrl(), mUserInfo.getResult().isMustUpdate());
                                        } else {
                                            getBannerData();
                                        }
                                    } catch (PackageManager.NameNotFoundException e) {
                                        e.printStackTrace();
                                        getBannerData();
                                    }

                                } else {
                                    getBannerData();
                                }


                            } else {
                                getBannerData();
                            }

                        } else {
                            getBannerData();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        getBannerData();
                    }

                } else {
                    getBannerData();
                }

            }

            @Override
            public void onFailure(VolleyError error) {

                LogUtil.i("onFailure...." + error.getMessage());
                getBannerData();
            }

        });


    }

    /**
     * 显示更新对话框
     *
     * @param downloadUrl
     */
    private void showUpdaloadDialog(final String downloadUrl, int mustUpdate) {
        // 这里的属性可以一直设置，因为每次设置后返回的是一个builder对象
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        // 设置提示框的标题
        builder.setTitle("版本升级").
                setIcon(R.mipmap.logo). // 设置提示框的图标
                setMessage("发现新版本，请及时更新！").// 设置要显示的信息
                setPositiveButton("确定", new DialogInterface.OnClickListener() {// 设置确定按钮
            @Override
            public void onClick(DialogInterface dialog, int which) {
                startUpload(downloadUrl);//下载最新的版本程序
            }
        });
        if (mustUpdate == 1) {
            builder.setCancelable(false);

        } else {
            builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    getBannerData();
                }
            });
            builder.setCancelable(true);
        }


        //设置取消按钮,null是什么都不做，并关闭对话框
        AlertDialog alertDialog = builder.create();
        // 显示对话框
        alertDialog.show();
    }


    ProgressDialog progressDialog;

    private void startUpload(String downloadUrl) {
        progressDialog = new ProgressDialog(this);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);

        progressDialog.setMessage("正在下载新版本...");
        progressDialog.setCancelable(false);//不能手动取消下载进度对话框
        progressDialog.setCanceledOnTouchOutside(false);

        final String fileSavePath = Utils.getSaveFilePath(downloadUrl);
        HTTPCaller.getInstance().downloadFile(downloadUrl, fileSavePath, null, new ProgressUIListener() {

            @Override
            public void onUIProgressStart(long totalBytes) {//下载开始
                progressDialog.setMax((int) totalBytes);
                progressDialog.show();
            }

            //更新进度
            @Override
            public void onUIProgressChanged(long numBytes, long totalBytes, float percent, float speed) {
                progressDialog.setProgress((int) numBytes);
            }

            @Override
            public void onUIProgressFinish() {//下载完成
                Toast.makeText(SplashActivity.this, "下载完成", Toast.LENGTH_LONG).show();
                progressDialog.dismiss();
                openAPK(fileSavePath);
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        if (isAZ) {
            isAZ = false;
            getBannerData();
        }

    }

    boolean isAZ = false;

    /**
     * 下载完成安装apk
     *
     * @param fileSavePath
     */
    private void openAPK(String fileSavePath) {
        File file = new File(fileSavePath);
        Intent intent = new Intent(Intent.ACTION_VIEW);
        Uri data;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {//判断版本大于等于7.0
            // "com.ansen.checkupdate.fileprovider"即是在清单文件中配置的authorities
            // 通过FileProvider创建一个content类型的Uri
            data = FileProvider.getUriForFile(this, "com.ansen.checkupdate.fileprovider", file);
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);// 给目标应用一个临时授权
        } else {
            data = Uri.fromFile(file);
        }
        intent.setDataAndType(data, "application/vnd.android.package-archive");
        isAZ = true;

        startActivity(intent);

    }

    @Override
    public void onRequestPermissionsResult(int permsRequestCode, String[] permissions, int[] grantResults) {
        switch (permsRequestCode) {
            case PERMS_REQUEST_CODE:
                boolean storageAccepted = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                if (storageAccepted) {
                    //
                    chaeckUpdate();
                } else {
                    getBannerData();
                }
                break;

        }
    }


    //申请权限
    private void getAuthority() {
        // 申请多个权限。,Manifest.permission.WRITE_EXTERNAL_STORAGE
        AndPermission.with(this)
                .requestCode(100)
                .permission(Manifest.permission.READ_PHONE_STATE, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE
                        , Manifest.permission.CAMERA
                        , Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION)
                .callback(mPermissionListener)
                // rationale作用是：用户拒绝一次权限，再次申请时先征求用户同意，再打开授权对话框；
                // 这样避免用户勾选不再提示，导致以后无法申请权限。
                // 你也可以不设置。
                .rationale(new RationaleListener() {
                    @Override
                    public void showRequestPermissionRationale(int requestCode, Rationale rationale) {
                        // 这里的对话框可以自定义，只要调用rationale.resume()就可以继续申请。
                        AndPermission.
                                rationaleDialog(SplashActivity.this, rationale)
                                .setTitle("温馨提醒")
                                .setMessage("权限申请已被您拒绝，可能将影响部分功能的正常使用！")
                                .setNegativeButton("取消", new DialogInterface.OnClickListener() {

                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.dismiss();
                                    }
                                })
                                .setPositiveButton("再次申请")
                                .show();
                    }
                })
                .start();
    }

    /**
     * 申请权限回调
     */
    PermissionListener mPermissionListener = new PermissionListener() {
        @Override
        public void onSucceed(int requestCode, @NonNull List<String> grantPermissions) {
            if (requestCode == 100) {
//                Toast.makeText(LoginActivity.this,"设置成功1", Toast.LENGTH_LONG).show();

            }
        }

        @Override
        public void onFailed(int requestCode, @NonNull List<String> deniedPermissions) {
            if (requestCode == 100) {
                // 是否有不再提示并拒绝的权限。
                if (AndPermission.hasAlwaysDeniedPermission(SplashActivity.this, deniedPermissions)) {
                    // 第一种：用AndPermission默认的提示语。
                    AndPermission.defaultSettingDialog(SplashActivity.this, 400)
                            .setTitle("温馨提醒")
                            .setMessage("权限申请已被您拒绝，可能将影响部分功能的正常使用！")
                            .show();
                } else {
                    getAuthority();
                }
            }
        }
    };


    private void getBannerData() {
        //http://serpro/mobile/index.html

        if (!NetworkUtils.isAvailable(SplashActivity.this)) {
            //  ToastUtil.showToast(SplashActivity.this,"网络异常");
            toGoMain();
            return;
        }

        HttpParams post_params = new HttpParams();
        // Map<String, String> params = new HashMap<String, String>();
        //  post_params.putHeaders("tokenCode", tokenCode);
        //   post_params.putHeaders("tokenInfo", tokenInfo);
        post_params.putHeaders("Content-Type", "application/json");
        // post_params.putJsonParams(new Gson().toJson(params));


        RxVolley.jsonPost(StaticClass.domain1 + "/index.html", post_params, new HttpCallback() {
            @Override
            public void onSuccess(String t) {

                LogUtil.i("*******splash******getBannerData***sq****:" + t);
                if (!TextUtils.isEmpty(t)) {
                    try {
                        BaseEntity mBaseEntity = new Gson().fromJson(t, BaseEntity.class);
                        if (mBaseEntity != null) {
                            if (mBaseEntity.getStatus() == 100) {
                                HomeData mHomeData = new Gson().fromJson(t, HomeData.class);
                                if (mHomeData != null && mHomeData.getResult() != null) {


                                    if (mHomeData.getResult().getIndexFlag() == 1) {

                                        new SweetAlertDialogWeb.Builder(SplashActivity.this)
                                                .setTitleColor(getResources().getColor(R.color.main_color))
                                                .setTitle("最新公告")
                                                .setMessage(mHomeData.getResult().getIndexUrl())
                                                .setCancelable(false)
                                                .setPositiveButton("确定", new SweetAlertDialogWeb.OnDialogClickListener() {
                                                    @Override
                                                    public void onClick(Dialog dialog, int which, String msg) {

                                                        toGoMain();
                                                    }
                                                }).show();

                                    } else {
                                        toGoMain();
                                    }


                                } else {
                                    toGoMain();
                                }

                            } else {
                                toGoMain();
                            }

                        } else {
                            toGoMain();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        toGoMain();
                    }

                } else {
                    toGoMain();
                }


            }

            @Override
            public void onFailure(VolleyError error) {
                LogUtil.i("onFailure...." + error.getMessage());
                toGoMain();
            }

        });
    }


   // 网络那边要判断下，电信的使用域名：kc6666.net:8080，移动联通的网络（非电信）使用域名：kh6666.net:8080

    private void checkIMSI() {
        TelephonyManager telephonyManager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
            //    ActivityCompat#requestPermissions
           ToastUtil.showToast(this,"读取手机状态权限获取失败");

        }else{
            String IMSI = telephonyManager.getSubscriberId();//获取SIM卡的IMSI
            if (!TextUtils.isEmpty(IMSI)&&IMSI.length() > 0){
                //通过前五位判断连的wifi或者数据流量是移动、联通还是电信
                if (IMSI.substring(0, 5).equals("46000") || IMSI.substring(0, 5).equals("46002")){
                    //移动
                    StaticClass.init("kh6666.net:8080");
                }else if (IMSI.substring(0, 5).equals("46001")){
                    //联通
                    StaticClass.init("kh6666.net:8080");
                }else if (IMSI.substring(0, 5).equals("46003")){
                    //电信
                    StaticClass.init("kc6666.net:8080");
                }
            }else{

            }
        }



    }







}
