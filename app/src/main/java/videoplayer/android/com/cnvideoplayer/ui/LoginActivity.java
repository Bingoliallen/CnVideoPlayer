package videoplayer.android.com.cnvideoplayer.ui;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.CountDownTimer;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;

import com.google.gson.Gson;
import com.kymjs.rxvolley.RxVolley;
import com.kymjs.rxvolley.client.HttpCallback;
import com.kymjs.rxvolley.client.HttpParams;
import com.kymjs.rxvolley.http.VolleyError;
import com.yanzhenjie.permission.AndPermission;
import com.yanzhenjie.permission.PermissionListener;
import com.yanzhenjie.permission.Rationale;
import com.yanzhenjie.permission.RationaleListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import videoplayer.android.com.cnvideoplayer.MainActivity;
import videoplayer.android.com.cnvideoplayer.R;
import videoplayer.android.com.cnvideoplayer.base.BaseCompatActivity;
import videoplayer.android.com.cnvideoplayer.entity.BaseEntity;
import videoplayer.android.com.cnvideoplayer.entity.GetSMSCodeEntity;
import videoplayer.android.com.cnvideoplayer.entity.LoginEntity;
import videoplayer.android.com.cnvideoplayer.utils.LogUtil;
import videoplayer.android.com.cnvideoplayer.utils.RegexUtils;
import videoplayer.android.com.cnvideoplayer.utils.ShareUtils;
import videoplayer.android.com.cnvideoplayer.utils.StaticClass;
import videoplayer.android.com.cnvideoplayer.utils.ToastUtil;
import videoplayer.android.com.cnvideoplayer.view.StatusBarCompat;

public class LoginActivity extends BaseCompatActivity {
    private TextView login_tv;
    private EditText login_account_edit;
    private EditText login_password_edit;
    private EditText login_account_tj;
    private CheckBox box1;
    private Button login_btn, login_code;

    private String smscode;
    private boolean isChecked;

    @Override
    protected String initTitle() {
        return null;
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_login;
    }

    @Override
    public void initView() {
        StatusBarCompat.setStatusBarColor(this, ContextCompat.getColor(this, R.color.main_color));
        login_tv = findViewById(R.id.login_tv);
        login_account_edit = findViewById(R.id.login_account_edit);
        login_password_edit = findViewById(R.id.login_password_edit);
        login_account_tj = findViewById(R.id.login_account_tj);
        login_tv.requestFocus();
        box1 = findViewById(R.id.box1);
        login_btn = findViewById(R.id.login_btn);
        login_code = findViewById(R.id.login_code);
        login_btn.setOnClickListener(this);
        login_code.setOnClickListener(this);

        findViewById(R.id.tv_zctk).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivityForResult(new Intent(LoginActivity.this, PolicyTermsActivity.class), 0);
            }
        });

        //给CheckBox设置事件监听
        box1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener(){
            @Override
            public void onCheckedChanged(CompoundButton buttonView,
                                         boolean isChecked) {
                // TODO Auto-generated method stub
                LoginActivity.this.isChecked=isChecked;
                if(isChecked){

                }else{

                }
            }
        });
        LoginActivity.this.isChecked=box1.isChecked();

    }

    @Override
    public void initData() {
        String mobile = ShareUtils.getString(this, "mobile", "");
        if (!TextUtils.isEmpty(mobile)) {
            login_account_edit.setText(mobile);
            login_account_edit.setSelection(login_account_edit.getText().length());
        }
        getAuthority();

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.login_code:
                getSMSCode();

                break;
            case R.id.login_btn:
                tologin();
                break;
        }
    }

    private void getSMSCode() {
        if (TextUtils.isEmpty(login_account_edit.getText().toString().trim())) {
            ToastUtil.showToast(this, "请输入手机号码");
            return;
        }
        if (!RegexUtils.isMobileNO(login_account_edit.getText().toString().trim())) {
            ToastUtil.showToast(this, "请输入正确的手机号码");
            return;
        }
        HttpParams post_params = new HttpParams();
        Map<String, String> params = new HashMap<String, String>();
        params.put("mobile", login_account_edit.getText().toString().trim());
        post_params.putHeaders("Content-Type", "application/json");

        post_params.putJsonParams(new Gson().toJson(params));
        showProgress("正在请求...");
        //http://serpro/mobile/user/getSMSCode.html

        RxVolley.jsonPost(StaticClass.domain + "/getSMSCode.html", post_params, new HttpCallback() {
            @Override
            public void onSuccess(String t) {
                dismissProgress();
                LogUtil.i("*************getSMSCode***sq****:" + t);
                if (!TextUtils.isEmpty(t)) {
                    try {
                        BaseEntity mBaseEntity = new Gson().fromJson(t, BaseEntity.class);
                        if (mBaseEntity != null) {
                            if (mBaseEntity.getStatus() == 100) {

                                ToastUtil.showToast(LoginActivity.this, "获取成功");
                                startCountTask();

                               /* GetSMSCodeEntity mGetSMSCodeEntity = new Gson().fromJson(t, GetSMSCodeEntity.class);
                                if (mGetSMSCodeEntity != null && mGetSMSCodeEntity.getResult() != null) {
                                    smscode = mGetSMSCodeEntity.getResult().getSmscode();
                                    login_password_edit.setText(smscode);


                                } else {

                                    ToastUtil.showToast(LoginActivity.this, "获取失败");

                                }*/

                            } else {
                                if(!TextUtils.isEmpty(mBaseEntity.getMsg())){
                                    ToastUtil.showToast(LoginActivity.this, mBaseEntity.getMsg());
                                }else{
                                    ToastUtil.showToast(LoginActivity.this, "获取失败");
                                }


                            }

                        } else {
                            ToastUtil.showToast(LoginActivity.this, "返回数据异常");
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        ToastUtil.showToast(LoginActivity.this, "数据解析异常");
                    }

                } else {
                    ToastUtil.showToast(LoginActivity.this, "返回数据异常");
                }


            }

            @Override
            public void onFailure(VolleyError error) {
                LogUtil.i("onFailure...." + error.getMessage());
                dismissProgress();
                ToastUtil.showToast(LoginActivity.this, "请求失败");
            }

        });
    }


    private void tologin() {
        if (TextUtils.isEmpty(login_account_edit.getText().toString().trim())) {
            ToastUtil.showToast(this, "请输入手机号码");
            return;
        }
        if (!RegexUtils.isMobileNO(login_account_edit.getText().toString().trim())) {
            ToastUtil.showToast(this, "请输入正确的手机号码");
            return;
        }

        if (TextUtils.isEmpty(login_password_edit.getText().toString().trim())) {
            ToastUtil.showToast(this, "请输入验证码");
            return;
        }

        if(LoginActivity.this.isChecked==false){
            ToastUtil.showToast(this, "请同意服务条款");
            return;
        }

        HttpParams post_params = new HttpParams();
        Map<String, String> params = new HashMap<String, String>();
        params.put("mobile", login_account_edit.getText().toString().trim());
        params.put("smscode", login_password_edit.getText().toString().trim());
        params.put("userno", login_account_tj.getText().toString().trim());
        post_params.putHeaders("Content-Type", "application/json");
        post_params.putJsonParams(new Gson().toJson(params));

        showProgress("正在登录...");
        //http://serpro/mobile/user/login.html

        RxVolley.jsonPost(StaticClass.domain + "/login.html", post_params, new HttpCallback() {
            @Override
            public void onSuccess(String t) {
                dismissProgress();
                LogUtil.i("*************login***sq****:" + t);
                if (!TextUtils.isEmpty(t)) {
                    try {
                        BaseEntity mBaseEntity = new Gson().fromJson(t, BaseEntity.class);
                        if (mBaseEntity != null) {
                            //     ToastUtil.showToast(LoginActivity.this, mBaseEntity.getMsg());

                            if (mBaseEntity.getStatus() == 100) {
                                LoginEntity mEntity = new Gson().fromJson(t, LoginEntity.class);
                                if (mEntity != null && mEntity.getResult() != null) {
                                    ShareUtils.putString(LoginActivity.this, "nickname", mEntity.getResult().getNickname());
                                    ShareUtils.putString(LoginActivity.this, "mobile", mEntity.getResult().getMobile());
                                    ShareUtils.putString(LoginActivity.this, "portrait", mEntity.getResult().getPortrait());
                                    ShareUtils.putString(LoginActivity.this, "tokenCode", mEntity.getResult().getTokenCode());
                                    ShareUtils.putString(LoginActivity.this, "tokenInfo", mEntity.getResult().getTokenInfo());
                                    ShareUtils.putString(LoginActivity.this, "userno", mEntity.getResult().getUserno());
                                    ShareUtils.putInt(LoginActivity.this, "vip", mEntity.getResult().getVip());
                                    ShareUtils.putString(LoginActivity.this, "extime", mEntity.getResult().getExtime());

                                    ShareUtils.putString(LoginActivity.this, "url", mEntity.getResult().getUrl());

                                    ShareUtils.putString(LoginActivity.this, "balance", mEntity.getResult().getBalance());
                                    ShareUtils.putString(LoginActivity.this, "email", mEntity.getResult().getEmail());

                                    //ShareUtils.putInt(LoginActivity.this, "type", mEntity.getResult().getType());

                                }
                                ToastUtil.showToast(LoginActivity.this, "登录成功");
                                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                                startActivity(intent);
                                stopCountTask();
                                setStatus();
                                finish();
                            } else {
                                if(!TextUtils.isEmpty(mBaseEntity.getMsg())){
                                    ToastUtil.showToast(LoginActivity.this, mBaseEntity.getMsg());
                                }else{
                                    ToastUtil.showToast(LoginActivity.this, "登录失败");
                                }

                            }

                        } else {
                            ToastUtil.showToast(LoginActivity.this, "返回数据异常");
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        ToastUtil.showToast(LoginActivity.this, "数据解析异常");
                    }

                } else {
                    ToastUtil.showToast(LoginActivity.this, "返回数据异常");
                }


            }

            @Override
            public void onFailure(VolleyError error) {
                LogUtil.i("onFailure...." + error.getMessage());
                dismissProgress();
                ToastUtil.showToast(LoginActivity.this, "请求失败");
            }

        });
    }

    //申请权限
    private void getAuthority() {
        // 申请多个权限。,Manifest.permission.WRITE_EXTERNAL_STORAGE
        AndPermission.with(this)
                .requestCode(100)
                .permission(Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE
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
                                rationaleDialog(LoginActivity.this, rationale)
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
                if (AndPermission.hasAlwaysDeniedPermission(LoginActivity.this, deniedPermissions)) {
                    // 第一种：用AndPermission默认的提示语。
                    AndPermission.defaultSettingDialog(LoginActivity.this, 400)
                            .setTitle("温馨提醒")
                            .setMessage("权限申请已被您拒绝，可能将影响部分功能的正常使用！")
                            .show();
                } else {
                    getAuthority();
                }
            }
        }
    };

    @Override
    protected void onDestroy() {
        super.onDestroy();
        stopCountTask();
    }

    /** 倒计时线程 */
    private CountDownTask countDownTask;

    //开始倒计时
    private void startCountTask(){
        if(countDownTask !=null){
            stopCountTask();
        }
        countDownTask = new CountDownTask(60000, 1000);
        countDownTask.start();// 开始倒计时的方法

    }



    // 停止倒计时
    private void stopCountTask() {
        try {
            if (countDownTask != null) {
                countDownTask.cancel();
                countDownTask = null;
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {

        }

    }


    /**
     * 倒计时
     */
    private class CountDownTask extends CountDownTimer{

        // 这个是原生的倒计时类，第一个参数是总毫秒数，第二个是倒计时的毫秒数。
        public CountDownTask(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);
        }

        @Override
        public void onTick(long millisUntilFinished) {
            // 倒计时中的方法
            login_code.setEnabled(false);
            login_code.setText(millisUntilFinished / 1000 + "秒");
        }

        @Override
        public void onFinish() {
            // 倒计时结束后的方法
            login_code.setEnabled(true);
            login_code.setText("获取验证码");
        }
    }

    private void setStatus(){
        //http://serpro/mobile/user/status.html
        String tokenCode = ShareUtils.getString(LoginActivity.this, "tokenCode", "");
        String tokenInfo = ShareUtils.getString(LoginActivity.this, "tokenInfo", "");
        HttpParams post_params = new HttpParams();
        post_params.putHeaders("tokenCode", tokenCode);
        post_params.putHeaders("tokenInfo", tokenInfo);
        post_params.putHeaders("Content-Type", "application/json");
        Map<String, String> params = new HashMap<String, String>();
        params.put("status", ""+1);
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




}
