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

import videoplayer.android.com.cnvideoplayer.R;
import videoplayer.android.com.cnvideoplayer.base.BaseCompatActivity;
import videoplayer.android.com.cnvideoplayer.entity.BaseEntity;
import videoplayer.android.com.cnvideoplayer.utils.LogUtil;
import videoplayer.android.com.cnvideoplayer.utils.RegexUtils;
import videoplayer.android.com.cnvideoplayer.utils.ShareUtils;
import videoplayer.android.com.cnvideoplayer.utils.StaticClass;
import videoplayer.android.com.cnvideoplayer.utils.ToastUtil;
import videoplayer.android.com.cnvideoplayer.view.StatusBarCompat;

public class AcountBindingActivity extends BaseCompatActivity {

    private EditText account_edit;
    private EditText account_pwd;
    private EditText account_pwd_again;
    private CheckBox box1;
    private TextView tv_zctk,to_phone;
    private Button commit;
    private boolean isChecked;
    private String tokenCode;
    private String tokenInfo;
    private int mode=0;

    @Override
    protected String initTitle() {
        return "电子邮件绑定";
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_acount_binding;
    }

    @Override
    public void initView() {
        mode=getIntent().getIntExtra("mode",0);
        StatusBarCompat.setStatusBarColor(this, ContextCompat.getColor(this, R.color.main_color));
        account_edit = (EditText) this.findViewById(R.id.account_edit);
        account_pwd = (EditText) this.findViewById(R.id.account_pwd);
        account_pwd_again = (EditText) this.findViewById(R.id.account_pwd_again);
        tv_zctk = (TextView) this.findViewById(R.id.tv_zctk);
        to_phone = (TextView) this.findViewById(R.id.to_phone);
        box1 = findViewById(R.id.box1);
        commit = (Button) this.findViewById(R.id.commit);

        commit.setOnClickListener(this);
        tv_zctk.setOnClickListener(this);
        to_phone.setOnClickListener(this);
        //给CheckBox设置事件监听
        box1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener(){
            @Override
            public void onCheckedChanged(CompoundButton buttonView,
                                         boolean isChecked) {
                // TODO Auto-generated method stub
                AcountBindingActivity.this.isChecked=isChecked;

            }
        });
        AcountBindingActivity.this.isChecked=box1.isChecked();

    }

    @Override
    public void initData() {
        tokenCode = ShareUtils.getString(AcountBindingActivity.this, "tokenCode", "");
        tokenInfo = ShareUtils.getString(AcountBindingActivity.this, "tokenInfo", "");
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.tv_zctk:
                startActivityForResult(new Intent(AcountBindingActivity.this, PolicyTermsActivity.class), 0);
                break;
            case R.id.to_phone:
                startActivityForResult(new Intent(AcountBindingActivity.this, AcountBindingPhoneActivity.class), 0);
                finish();
                break;
            case R.id.commit:
                commit();
                break;
        }

    }

    private void commit() {
        //http://serpro/mobile/user/bindemail.html
        //http://serpro/mobile/user/updateemail.html
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

        if (TextUtils.isEmpty(account_pwd_again.getText().toString().trim())) {
            ToastUtil.showToast(this, "请再次输入您的密码");
            return;
        }


        if(account_pwd.getText().toString().trim().equals(account_pwd_again.getText().toString().trim())==false){
            ToastUtil.showToast(this, "密码不一致");
            return;
        }


        if(AcountBindingActivity.this.isChecked==false){
            ToastUtil.showToast(this, "请同意服务条款");
            return;
        }

        HttpParams post_params = new HttpParams();
        post_params.putHeaders("tokenCode", tokenCode);
        post_params.putHeaders("tokenInfo", tokenInfo);
        post_params.putHeaders("Content-Type", "application/json");

        Map<String, String> params = new HashMap<String, String>();
        params.put("email", account_edit.getText().toString().trim());
        params.put("password", account_pwd.getText().toString().trim());
        //params.put("pwd_again", account_pwd_again.getText().toString().trim());
        post_params.putJsonParams(new Gson().toJson(params));
        showProgress("正在加载...");
        String url;
        if(mode==1){
            url=StaticClass.domain + "/updateemail.html";
        }else{
            url=StaticClass.domain + "/bindemail.html";
        }
        RxVolley.jsonPost( url , post_params, new HttpCallback() {
            @Override
            public void onSuccess(String t) {
                dismissProgress();
                LogUtil.i("******bindemail*******commit*sq****:" + t);
                if (!TextUtils.isEmpty(t)) {
                    try {
                        BaseEntity mBaseEntity = new Gson().fromJson(t, BaseEntity.class);
                        if (mBaseEntity != null) {
                            if (mBaseEntity.getStatus() == 100) {
                                ToastUtil.showToast(AcountBindingActivity.this, "绑定成功");
                                finish();
                            } else {
                                if(!TextUtils.isEmpty(mBaseEntity.getMsg())){
                                    ToastUtil.showToast(AcountBindingActivity.this, mBaseEntity.getMsg());
                                }else{
                                    ToastUtil.showToast(AcountBindingActivity.this, "绑定失败");
                                }

                            }

                        } else {
                            ToastUtil.showToast(AcountBindingActivity.this, "返回数据异常");
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        ToastUtil.showToast(AcountBindingActivity.this, "返回数据异常");
                    }

                } else {
                    ToastUtil.showToast(AcountBindingActivity.this, "返回数据异常");
                }

            }

            @Override
            public void onFailure(VolleyError error) {
                dismissProgress();
                LogUtil.i("onFailure...." + error.getMessage());
                ToastUtil.showToast(AcountBindingActivity.this, "请求失败");
            }

        });
    }





}
