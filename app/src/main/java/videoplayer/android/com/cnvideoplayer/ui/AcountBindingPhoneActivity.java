package videoplayer.android.com.cnvideoplayer.ui;

import android.content.Intent;
import android.os.CountDownTimer;
import android.support.v4.content.ContextCompat;
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

import java.util.HashMap;
import java.util.Map;

import videoplayer.android.com.cnvideoplayer.R;
import videoplayer.android.com.cnvideoplayer.base.BaseCompatActivity;
import videoplayer.android.com.cnvideoplayer.entity.BaseEntity;
import videoplayer.android.com.cnvideoplayer.utils.LogUtil;
import videoplayer.android.com.cnvideoplayer.utils.RegexUtils;
import videoplayer.android.com.cnvideoplayer.utils.ShareUtils;
import videoplayer.android.com.cnvideoplayer.utils.StaticClass;
import videoplayer.android.com.cnvideoplayer.utils.ToastUtil;
import videoplayer.android.com.cnvideoplayer.view.StatusBarCompat;

public class AcountBindingPhoneActivity extends BaseCompatActivity {

    private EditText account_edit;
    private EditText login_password_edit;
    private Button login_code;
    private EditText account_pwd;
    private EditText account_pwd_again;

    private CheckBox box1;
    private TextView tv_zctk,to_email;
    private Button commit;
    private boolean isChecked;
    private String tokenCode;
    private String tokenInfo;
    private int mode=0;


    @Override
    protected String initTitle() {
        return "手机绑定";
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_acount_binding_phone;
    }

    @Override
    public void initView() {
        mode=getIntent().getIntExtra("mode",0);
        StatusBarCompat.setStatusBarColor(this, ContextCompat.getColor(this, R.color.main_color));
        account_edit = (EditText) this.findViewById(R.id.account_edit);
        login_password_edit = (EditText) this.findViewById(R.id.login_password_edit);
        login_code = (Button) this.findViewById(R.id.login_code);
        account_pwd = (EditText) this.findViewById(R.id.account_pwd);
        account_pwd_again = (EditText) this.findViewById(R.id.account_pwd_again);

        tv_zctk = (TextView) this.findViewById(R.id.tv_zctk);
        to_email = (TextView) this.findViewById(R.id.to_email);
        box1 = findViewById(R.id.box1);
        commit = (Button) this.findViewById(R.id.commit);

        login_code.setOnClickListener(this);
        commit.setOnClickListener(this);
        tv_zctk.setOnClickListener(this);
        to_email.setOnClickListener(this);
        //给CheckBox设置事件监听
        box1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener(){
            @Override
            public void onCheckedChanged(CompoundButton buttonView,
                                         boolean isChecked) {
                // TODO Auto-generated method stub
                AcountBindingPhoneActivity.this.isChecked=isChecked;

            }
        });
        AcountBindingPhoneActivity.this.isChecked=box1.isChecked();


    }

    @Override
    public void initData() {
        tokenCode = ShareUtils.getString(AcountBindingPhoneActivity.this, "tokenCode", "");
        tokenInfo = ShareUtils.getString(AcountBindingPhoneActivity.this, "tokenInfo", "");
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){

            case R.id.login_code:
                getSMSCode();
                break;
            case R.id.tv_zctk:
                startActivityForResult(new Intent(AcountBindingPhoneActivity.this, PolicyTermsActivity.class), 0);
                break;
            case R.id.to_email:
                startActivityForResult(new Intent(AcountBindingPhoneActivity.this, AcountBindingActivity.class), 0);
                finish();
                break;
            case R.id.commit:
                commit();
                break;
        }
    }

    private void getSMSCode() {
        if (TextUtils.isEmpty(account_edit.getText().toString().trim())) {
            ToastUtil.showToast(this, "请输入手机号码");
            return;
        }
        if (!RegexUtils.isMobileNO(account_edit.getText().toString().trim())) {
            ToastUtil.showToast(this, "请输入正确的手机号码");
            return;
        }
        HttpParams post_params = new HttpParams();
        Map<String, String> params = new HashMap<String, String>();
        params.put("mobile", account_edit.getText().toString().trim());
        post_params.putHeaders("Content-Type", "application/json");

        post_params.putJsonParams(new Gson().toJson(params));
        showProgress("正在请求...");
        //http://serpro/mobile/user/getSMSCode.html

        RxVolley.jsonPost(StaticClass.domain + "/getSMSCode.html", post_params, new HttpCallback() {
            @Override
            public void onSuccess(String t) {
                dismissProgress();
                LogUtil.i("********Account*****getSMSCode***sq****:" + t);
                if (!TextUtils.isEmpty(t)) {
                    try {
                        BaseEntity mBaseEntity = new Gson().fromJson(t, BaseEntity.class);
                        if (mBaseEntity != null) {
                            if (mBaseEntity.getStatus() == 100) {

                                ToastUtil.showToast(AcountBindingPhoneActivity.this, "获取成功");
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
                                    ToastUtil.showToast(AcountBindingPhoneActivity.this, mBaseEntity.getMsg());
                                }else{
                                    ToastUtil.showToast(AcountBindingPhoneActivity.this, "获取失败");
                                }


                            }

                        } else {
                            ToastUtil.showToast(AcountBindingPhoneActivity.this, "返回数据异常");
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        ToastUtil.showToast(AcountBindingPhoneActivity.this, "数据解析异常");
                    }

                } else {
                    ToastUtil.showToast(AcountBindingPhoneActivity.this, "返回数据异常");
                }


            }

            @Override
            public void onFailure(VolleyError error) {
                LogUtil.i("onFailure...." + error.getMessage());
                dismissProgress();
                ToastUtil.showToast(AcountBindingPhoneActivity.this, "请求失败");
            }

        });
    }

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
    private class CountDownTask extends CountDownTimer {

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




    private void commit() {
        //http://serpro/mobile/user/bindmobile.html
       //http://serpro/mobile/user/updatemobile.html

        if (TextUtils.isEmpty(account_edit.getText().toString().trim())) {
            ToastUtil.showToast(this, "请输入您的手机号码");
            return;
        }
        if (!RegexUtils.isMobileNO(account_edit.getText().toString().trim())) {
            ToastUtil.showToast(this, "请输入正确的手机号码");
            return;
        }

        if (TextUtils.isEmpty(login_password_edit.getText().toString().trim())) {
            ToastUtil.showToast(this, "请输入验证码");
            return;
        }


        if (TextUtils.isEmpty(account_pwd.getText().toString().trim())) {
            ToastUtil.showToast(this, "请输入您的密码");
            return;
        }

        if (TextUtils.isEmpty(account_pwd_again.getText().toString().trim())) {
            ToastUtil.showToast(this, "请再次输入您的密码");
            return;
        }


        if(account_pwd.getText().toString().trim().equals(account_pwd_again.getText().toString().trim())==false){
            ToastUtil.showToast(this, "密码不一致");
            return;
        }


        if(AcountBindingPhoneActivity.this.isChecked==false){
            ToastUtil.showToast(this, "请同意服务条款");
            return;
        }


        HttpParams post_params = new HttpParams();
        post_params.putHeaders("tokenCode", tokenCode);
        post_params.putHeaders("tokenInfo", tokenInfo);
        post_params.putHeaders("Content-Type", "application/json");

        Map<String, String> params = new HashMap<String, String>();
        params.put("mobile", account_edit.getText().toString().trim());
        params.put("password", account_pwd.getText().toString().trim());
       // params.put("pwd_again", account_pwd_again.getText().toString().trim());
        post_params.putJsonParams(new Gson().toJson(params));
        showProgress("正在加载...");
        String url;
        if(mode==1){
            url=StaticClass.domain + "/updatemobile.html";
        }else{
            url=StaticClass.domain + "/bindmobile.html";
        }


        RxVolley.jsonPost(url, post_params, new HttpCallback() {
            @Override
            public void onSuccess(String t) {
                dismissProgress();
                LogUtil.i("******bindmobile*******commit*sq****:" + t);
                if (!TextUtils.isEmpty(t)) {
                    try {
                        BaseEntity mBaseEntity = new Gson().fromJson(t, BaseEntity.class);
                        if (mBaseEntity != null) {
                            if (mBaseEntity.getStatus() == 100) {
                                ToastUtil.showToast(AcountBindingPhoneActivity.this, "绑定成功");
                                finish();
                            } else {
                                if(!TextUtils.isEmpty(mBaseEntity.getMsg())){
                                    ToastUtil.showToast(AcountBindingPhoneActivity.this, mBaseEntity.getMsg());
                                }else{
                                    ToastUtil.showToast(AcountBindingPhoneActivity.this, "绑定失败");
                                }

                            }

                        } else {
                            ToastUtil.showToast(AcountBindingPhoneActivity.this, "返回数据异常");
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        ToastUtil.showToast(AcountBindingPhoneActivity.this, "返回数据异常");
                    }

                } else {
                    ToastUtil.showToast(AcountBindingPhoneActivity.this, "返回数据异常");
                }

            }

            @Override
            public void onFailure(VolleyError error) {
                dismissProgress();
                LogUtil.i("onFailure...." + error.getMessage());
                ToastUtil.showToast(AcountBindingPhoneActivity.this, "请求失败");
            }

        });
    }



}
