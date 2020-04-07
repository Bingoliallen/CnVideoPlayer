package videoplayer.android.com.cnvideoplayer.ui;

import android.content.Intent;
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

import java.util.HashMap;
import java.util.Map;

import videoplayer.android.com.cnvideoplayer.MainActivity;
import videoplayer.android.com.cnvideoplayer.R;
import videoplayer.android.com.cnvideoplayer.base.BaseCompatActivity;
import videoplayer.android.com.cnvideoplayer.entity.BaseEntity;
import videoplayer.android.com.cnvideoplayer.entity.LoginEntity;
import videoplayer.android.com.cnvideoplayer.utils.LogUtil;
import videoplayer.android.com.cnvideoplayer.utils.RegexUtils;
import videoplayer.android.com.cnvideoplayer.utils.ShareUtils;
import videoplayer.android.com.cnvideoplayer.utils.StaticClass;
import videoplayer.android.com.cnvideoplayer.utils.ToastUtil;
import videoplayer.android.com.cnvideoplayer.view.StatusBarCompat;

public class EmailLoginActivity extends BaseCompatActivity {

    private EditText account_edit;
    private EditText account_pwd;

    private CheckBox box1;
    private TextView tv_zctk,to_phone,to_forget;
    private Button commit;
    private boolean isChecked;

    private String tokenCode;
    private String tokenInfo;

    @Override
    protected String initTitle() {
        return "电子邮件登录";
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_email_login;
    }

    @Override
    public void initView() {
        StatusBarCompat.setStatusBarColor(this, ContextCompat.getColor(this, R.color.main_color));
        account_edit = (EditText) this.findViewById(R.id.account_edit);
        account_pwd = (EditText) this.findViewById(R.id.account_pwd);

        to_forget = (TextView) this.findViewById(R.id.to_forget);
        tv_zctk = (TextView) this.findViewById(R.id.tv_zctk);
        to_phone = (TextView) this.findViewById(R.id.to_phone);
        box1 = findViewById(R.id.box1);
        commit = (Button) this.findViewById(R.id.commit);

        commit.setOnClickListener(this);
        tv_zctk.setOnClickListener(this);
        to_phone.setOnClickListener(this);
        to_forget.setOnClickListener(this);

        //给CheckBox设置事件监听
        box1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener(){
            @Override
            public void onCheckedChanged(CompoundButton buttonView,
                                         boolean isChecked) {
                // TODO Auto-generated method stub
                EmailLoginActivity.this.isChecked=isChecked;

            }
        });
        EmailLoginActivity.this.isChecked=box1.isChecked();

    }

    @Override
    public void initData() {
        tokenCode = ShareUtils.getString(EmailLoginActivity.this, "tokenCode", "");
        tokenInfo = ShareUtils.getString(EmailLoginActivity.this, "tokenInfo", "");
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.tv_zctk:
                startActivityForResult(new Intent(EmailLoginActivity.this, PolicyTermsActivity.class), 0);
                break;
            case R.id.to_phone:
                startActivityForResult(new Intent(EmailLoginActivity.this, PhoneLoginActivity.class), 0);
                finish();
                break;
            case R.id.to_forget:

                Intent mIntent=new Intent(EmailLoginActivity.this, AcountBindingActivity.class);
                mIntent.putExtra("mode",1);
               startActivityForResult(mIntent, 0);

               break;
            case R.id.commit:
                commit();
                break;
        }

    }


    private void commit() {
        //http://serpro/mobile/user/loginbyemail.html

        if (TextUtils.isEmpty(account_edit.getText().toString().trim())) {
            ToastUtil.showToast(this, "请输入您的邮箱");
            return;
        }
        if (!RegexUtils.isEmail(account_edit.getText().toString().trim())) {
            ToastUtil.showToast(this, "请输入正确的邮箱账号");
            return;
        }

        if (TextUtils.isEmpty(account_pwd.getText().toString().trim())) {
            ToastUtil.showToast(this, "请输入您的密码");
            return;
        }

        if(EmailLoginActivity.this.isChecked==false){
            ToastUtil.showToast(this, "请同意服务条款");
            return;
        }


        HttpParams post_params = new HttpParams();
       // post_params.putHeaders("tokenCode", tokenCode);
      //  post_params.putHeaders("tokenInfo", tokenInfo);
        post_params.putHeaders("Content-Type", "application/json");

        Map<String, String> params = new HashMap<String, String>();
        params.put("email", account_edit.getText().toString().trim());
        params.put("password", account_pwd.getText().toString().trim());
        post_params.putJsonParams(new Gson().toJson(params));
        showProgress("正在加载...");
        RxVolley.jsonPost(StaticClass.domain + "/loginbyemail.html", post_params, new HttpCallback() {
            @Override
            public void onSuccess(String t) {
                dismissProgress();
                LogUtil.i("******loginbyemail*******commit*sq****:" + t);
                if (!TextUtils.isEmpty(t)) {
                    try {
                        BaseEntity mBaseEntity = new Gson().fromJson(t, BaseEntity.class);
                        if (mBaseEntity != null) {
                            if (mBaseEntity.getStatus() == 100) {

                                setStatus(0);
                                LoginEntity mEntity = new Gson().fromJson(t, LoginEntity.class);
                                if (mEntity != null && mEntity.getResult() != null) {

                                    ShareUtils.putString(EmailLoginActivity.this, "nickname", mEntity.getResult().getNickname());
                                    ShareUtils.putString(EmailLoginActivity.this, "mobile", mEntity.getResult().getMobile());
                                    ShareUtils.putString(EmailLoginActivity.this, "portrait", mEntity.getResult().getPortrait());
                                    ShareUtils.putString(EmailLoginActivity.this, "tokenCode", mEntity.getResult().getTokenCode());
                                    ShareUtils.putString(EmailLoginActivity.this, "tokenInfo", mEntity.getResult().getTokenInfo());
                                    ShareUtils.putString(EmailLoginActivity.this, "userno", mEntity.getResult().getUserno());
                                    ShareUtils.putInt(EmailLoginActivity.this, "vip", mEntity.getResult().getVip());
                                    ShareUtils.putString(EmailLoginActivity.this, "extime", mEntity.getResult().getExtime());

                                    ShareUtils.putString(EmailLoginActivity.this, "url", mEntity.getResult().getUrl());

                                    ShareUtils.putString(EmailLoginActivity.this, "balance", mEntity.getResult().getBalance());
                                    ShareUtils.putString(EmailLoginActivity.this, "email", mEntity.getResult().getEmail());

                                    ShareUtils.putInt(EmailLoginActivity.this, "usertype", mEntity.getResult().getUsertype());
                                    ShareUtils.putInt(EmailLoginActivity.this, "exdays", mEntity.getResult().getExdays());


                                }
                                ToastUtil.showToast(EmailLoginActivity.this, "登录成功");
                                Intent intent = new Intent(EmailLoginActivity.this, MainActivity.class);
                                startActivity(intent);
                                setStatus(1);
                                finish();

                            } else {
                                if(!TextUtils.isEmpty(mBaseEntity.getMsg())){
                                    ToastUtil.showToast(EmailLoginActivity.this, mBaseEntity.getMsg());
                                }else{
                                    ToastUtil.showToast(EmailLoginActivity.this, "登录失败");
                                }

                            }

                        } else {
                            ToastUtil.showToast(EmailLoginActivity.this, "返回数据异常");
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        ToastUtil.showToast(EmailLoginActivity.this, "返回数据异常");
                    }

                } else {
                    ToastUtil.showToast(EmailLoginActivity.this, "返回数据异常");
                }

            }

            @Override
            public void onFailure(VolleyError error) {
                dismissProgress();
                LogUtil.i("onFailure...." + error.getMessage());
                ToastUtil.showToast(EmailLoginActivity.this, "提交失败");
            }

        });
    }


    private void setStatus(int type){
        //http://serpro/mobile/user/status.html
        String tokenCode = ShareUtils.getString(EmailLoginActivity.this, "tokenCode", "");
        String tokenInfo = ShareUtils.getString(EmailLoginActivity.this, "tokenInfo", "");
        HttpParams post_params = new HttpParams();
        post_params.putHeaders("tokenCode", tokenCode);
        post_params.putHeaders("tokenInfo", tokenInfo);
        post_params.putHeaders("Content-Type", "application/json");
        Map<String, String> params = new HashMap<String, String>();
        params.put("status", ""+type);
        post_params.putJsonParams(new Gson().toJson(params));

        RxVolley.jsonPost(StaticClass.domain + "/status.html", post_params, new HttpCallback() {
            @Override
            public void onSuccess(String t) {

                LogUtil.i("*******0******status***sq****:" + t);
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
