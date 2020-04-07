package videoplayer.android.com.cnvideoplayer.ui;

import android.content.Intent;
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

public class PhoneLoginActivity extends BaseCompatActivity {

    private EditText account_edit;
    private EditText account_pwd;

    private CheckBox box1;
    private TextView tv_zctk,to_email,to_forget;
    private Button commit;
    private boolean isChecked;
    private String tokenCode;
    private String tokenInfo;

    @Override
    protected String initTitle() {
        return "手机登录";
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_phone_login;
    }

    @Override
    public void initView() {
        StatusBarCompat.setStatusBarColor(this, ContextCompat.getColor(this, R.color.main_color));
        account_edit = (EditText) this.findViewById(R.id.account_edit);
        account_pwd = (EditText) this.findViewById(R.id.account_pwd);

        to_forget = (TextView) this.findViewById(R.id.to_forget);
        tv_zctk = (TextView) this.findViewById(R.id.tv_zctk);
        to_email = (TextView) this.findViewById(R.id.to_email);
        box1 = findViewById(R.id.box1);
        commit = (Button) this.findViewById(R.id.commit);

        to_forget.setOnClickListener(this);
        commit.setOnClickListener(this);
        tv_zctk.setOnClickListener(this);
        to_email.setOnClickListener(this);
        //给CheckBox设置事件监听
        box1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener(){
            @Override
            public void onCheckedChanged(CompoundButton buttonView,
                                         boolean isChecked) {
                // TODO Auto-generated method stub
                PhoneLoginActivity.this.isChecked=isChecked;

            }
        });
        PhoneLoginActivity.this.isChecked=box1.isChecked();

    }

    @Override
    public void initData() {
        tokenCode = ShareUtils.getString(PhoneLoginActivity.this, "tokenCode", "");
        tokenInfo = ShareUtils.getString(PhoneLoginActivity.this, "tokenInfo", "");
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){

            case R.id.tv_zctk:
                startActivityForResult(new Intent(PhoneLoginActivity.this, PolicyTermsActivity.class), 0);
                break;
            case R.id.to_email:
                startActivityForResult(new Intent(PhoneLoginActivity.this, EmailLoginActivity.class), 0);
                finish();
                break;
            case R.id.to_forget://跳转到绑定账号
                Intent mIntent=new Intent(PhoneLoginActivity.this, AcountBindingPhoneActivity.class);
                mIntent.putExtra("mode",1);
                startActivityForResult(mIntent, 0);
                break;
            case R.id.commit:
                commit();
                break;
        }
    }


    private void commit() {
        //http://serpro/mobile/user/loginbymobile.html

        if (TextUtils.isEmpty(account_edit.getText().toString().trim())) {
            ToastUtil.showToast(this, "请输入您的手机号码");
            return;
        }
        if (!RegexUtils.isMobileNO(account_edit.getText().toString().trim())) {
            ToastUtil.showToast(this, "请输入正确的手机号码");
            return;
        }

        if (TextUtils.isEmpty(account_pwd.getText().toString().trim())) {
            ToastUtil.showToast(this, "请输入您的密码");
            return;
        }

        if(PhoneLoginActivity.this.isChecked==false){
            ToastUtil.showToast(this, "请同意服务条款");
            return;
        }


        HttpParams post_params = new HttpParams();
      //  post_params.putHeaders("tokenCode", tokenCode);
      //  post_params.putHeaders("tokenInfo", tokenInfo);
        post_params.putHeaders("Content-Type", "application/json");

        Map<String, String> params = new HashMap<String, String>();
        params.put("mobile", account_edit.getText().toString().trim());
        params.put("password", account_pwd.getText().toString().trim());
        post_params.putJsonParams(new Gson().toJson(params));
        showProgress("正在加载...");
        RxVolley.jsonPost(StaticClass.domain + "/loginbymobile.html", post_params, new HttpCallback() {
            @Override
            public void onSuccess(String t) {
                dismissProgress();
                LogUtil.i("******loginbymobile*******commit*sq****:" + t);
                if (!TextUtils.isEmpty(t)) {
                    try {
                        BaseEntity mBaseEntity = new Gson().fromJson(t, BaseEntity.class);
                        if (mBaseEntity != null) {
                            if (mBaseEntity.getStatus() == 100) {
                                setStatus(0);
                                LoginEntity mEntity = new Gson().fromJson(t, LoginEntity.class);
                                if (mEntity != null && mEntity.getResult() != null) {

                                    ShareUtils.putString(PhoneLoginActivity.this, "nickname", mEntity.getResult().getNickname());
                                    ShareUtils.putString(PhoneLoginActivity.this, "mobile", mEntity.getResult().getMobile());
                                    ShareUtils.putString(PhoneLoginActivity.this, "portrait", mEntity.getResult().getPortrait());
                                    ShareUtils.putString(PhoneLoginActivity.this, "tokenCode", mEntity.getResult().getTokenCode());
                                    ShareUtils.putString(PhoneLoginActivity.this, "tokenInfo", mEntity.getResult().getTokenInfo());
                                    ShareUtils.putString(PhoneLoginActivity.this, "userno", mEntity.getResult().getUserno());
                                    ShareUtils.putInt(PhoneLoginActivity.this, "vip", mEntity.getResult().getVip());
                                    ShareUtils.putString(PhoneLoginActivity.this, "extime", mEntity.getResult().getExtime());

                                    ShareUtils.putString(PhoneLoginActivity.this, "url", mEntity.getResult().getUrl());

                                    ShareUtils.putString(PhoneLoginActivity.this, "balance", mEntity.getResult().getBalance());
                                    ShareUtils.putString(PhoneLoginActivity.this, "email", mEntity.getResult().getEmail());

                                    ShareUtils.putInt(PhoneLoginActivity.this, "usertype", mEntity.getResult().getUsertype());
                                    ShareUtils.putInt(PhoneLoginActivity.this, "exdays", mEntity.getResult().getExdays());



                                }
                                ToastUtil.showToast(PhoneLoginActivity.this, "登录成功");
                                Intent intent = new Intent(PhoneLoginActivity.this, MainActivity.class);
                                startActivity(intent);
                                setStatus(1);
                                finish();



                            } else {
                                if(!TextUtils.isEmpty(mBaseEntity.getMsg())){
                                    ToastUtil.showToast(PhoneLoginActivity.this, mBaseEntity.getMsg());
                                }else{
                                    ToastUtil.showToast(PhoneLoginActivity.this, "登录失败");
                                }

                            }

                        } else {
                            ToastUtil.showToast(PhoneLoginActivity.this, "返回数据异常");
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        ToastUtil.showToast(PhoneLoginActivity.this, "返回数据异常");
                    }

                } else {
                    ToastUtil.showToast(PhoneLoginActivity.this, "返回数据异常");
                }

            }

            @Override
            public void onFailure(VolleyError error) {
                dismissProgress();
                LogUtil.i("onFailure...." + error.getMessage());
                ToastUtil.showToast(PhoneLoginActivity.this, "提交失败");
            }

        });
    }

    private void setStatus(int type){
        //http://serpro/mobile/user/status.html
        String tokenCode = ShareUtils.getString(PhoneLoginActivity.this, "tokenCode", "");
        String tokenInfo = ShareUtils.getString(PhoneLoginActivity.this, "tokenInfo", "");
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
