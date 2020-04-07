package videoplayer.android.com.cnvideoplayer.ui;

import android.content.Intent;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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
import videoplayer.android.com.cnvideoplayer.utils.ShareUtils;
import videoplayer.android.com.cnvideoplayer.utils.StaticClass;
import videoplayer.android.com.cnvideoplayer.utils.ToastUtil;
import videoplayer.android.com.cnvideoplayer.view.StatusBarCompat;

public class ConfirmPwdActivity extends BaseCompatActivity {

    private EditText account_pwd;
    private Button commit;
    private String tokenCode;
    private String tokenInfo;

    @Override
    protected String initTitle() {
        return "验证密码";
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_forget_pwd;
    }

    @Override
    public void initView() {
        StatusBarCompat.setStatusBarColor(this, ContextCompat.getColor(this, R.color.main_color));
        account_pwd = (EditText) this.findViewById(R.id.account_pwd);
        commit = (Button) this.findViewById(R.id.commit);
        commit.setOnClickListener(this);
    }

    @Override
    public void initData() {
        tokenCode = ShareUtils.getString(ConfirmPwdActivity.this, "tokenCode", "");
        tokenInfo = ShareUtils.getString(ConfirmPwdActivity.this, "tokenInfo", "");
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){

            case R.id.commit:
                commit();
                break;
        }
    }

    private void commit() {
        //http://serpro/mobile/user/checkpwd.html


        if (TextUtils.isEmpty(account_pwd.getText().toString().trim())) {
            ToastUtil.showToast(this, "请输入您的密码");
            return;
        }



        HttpParams post_params = new HttpParams();
        post_params.putHeaders("tokenCode", tokenCode);
        post_params.putHeaders("tokenInfo", tokenInfo);
        post_params.putHeaders("Content-Type", "application/json");

        Map<String, String> params = new HashMap<String, String>();
        params.put("password", account_pwd.getText().toString());
        post_params.putJsonParams(new Gson().toJson(params));
        showProgress("正在加载...");
        RxVolley.jsonPost(StaticClass.domain + "/checkpwd.html", post_params, new HttpCallback() {
            @Override
            public void onSuccess(String t) {
                dismissProgress();
                LogUtil.i("******checkpwd*******commit*sq****:" + t);
                if (!TextUtils.isEmpty(t)) {
                    try {
                        BaseEntity mBaseEntity = new Gson().fromJson(t, BaseEntity.class);
                        if (mBaseEntity != null) {
                            if (mBaseEntity.getStatus() == 100) {
                                ToastUtil.showToast(ConfirmPwdActivity.this, "验证成功");
                                int type = ShareUtils.getInt(ConfirmPwdActivity.this, "type", 0);
                                if(type==1){
                                    Intent mIntent=new Intent(ConfirmPwdActivity.this, AcountBindingActivity.class);
                                    mIntent.putExtra("mode",1);
                                    startActivityForResult( mIntent , 0);
                                }else {
                                    Intent mIntent=new Intent(ConfirmPwdActivity.this, AcountBindingPhoneActivity.class);
                                    mIntent.putExtra("mode",1);
                                    startActivityForResult( mIntent , 0);
                                }
                                finish();


                            } else {
                                if(!TextUtils.isEmpty(mBaseEntity.getMsg())){
                                    ToastUtil.showToast(ConfirmPwdActivity.this, mBaseEntity.getMsg());
                                }else{
                                    ToastUtil.showToast(ConfirmPwdActivity.this, "验证失败");
                                }

                            }

                        } else {
                            ToastUtil.showToast(ConfirmPwdActivity.this, "返回数据异常");
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        ToastUtil.showToast(ConfirmPwdActivity.this, "返回数据异常");
                    }

                } else {
                    ToastUtil.showToast(ConfirmPwdActivity.this, "返回数据异常");
                }

            }

            @Override
            public void onFailure(VolleyError error) {
                dismissProgress();
                LogUtil.i("onFailure...." + error.getMessage());
                ToastUtil.showToast(ConfirmPwdActivity.this, "提交失败");
            }

        });
    }




}
