package videoplayer.android.com.cnvideoplayer.ui;

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

public class InvitationCodeActivity extends BaseCompatActivity {

    private Button commit;
    private EditText account_pwd;
    private String tokenCode;
    private String tokenInfo;

    @Override
    protected String initTitle() {
        return "好友推荐礼";
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_invitation_code;
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
        tokenCode = ShareUtils.getString(InvitationCodeActivity.this, "tokenCode", "");
        tokenInfo = ShareUtils.getString(InvitationCodeActivity.this, "tokenInfo", "");
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
        //http://serpro/mobile/user/invite.html



        if (TextUtils.isEmpty(account_pwd.getText().toString().trim())) {
            ToastUtil.showToast(this, "请输入推荐人邀请码");
            return;
        }


        HttpParams post_params = new HttpParams();
        post_params.putHeaders("tokenCode", tokenCode);
        post_params.putHeaders("tokenInfo", tokenInfo);
        post_params.putHeaders("Content-Type", "application/json");

        Map<String, String> params = new HashMap<String, String>();
        params.put("userno", account_pwd.getText().toString());
        post_params.putJsonParams(new Gson().toJson(params));
        showProgress("正在加载...");
        RxVolley.jsonPost(StaticClass.domain + "/invite.html", post_params, new HttpCallback() {
            @Override
            public void onSuccess(String t) {
                dismissProgress();
                LogUtil.i("******invite*******commit*sq****:" + t);
                if (!TextUtils.isEmpty(t)) {
                    try {
                        BaseEntity mBaseEntity = new Gson().fromJson(t, BaseEntity.class);
                        if (mBaseEntity != null) {
                            if (mBaseEntity.getStatus() == 100) {
                                ToastUtil.showToast(InvitationCodeActivity.this, "兑换成功");

                            } else {
                                if(!TextUtils.isEmpty(mBaseEntity.getMsg())){
                                    ToastUtil.showToast(InvitationCodeActivity.this, mBaseEntity.getMsg());
                                }else{
                                    ToastUtil.showToast(InvitationCodeActivity.this, "兑换失败");
                                }

                            }

                        } else {
                            ToastUtil.showToast(InvitationCodeActivity.this, "返回数据异常");
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        ToastUtil.showToast(InvitationCodeActivity.this, "返回数据异常");
                    }

                } else {
                    ToastUtil.showToast(InvitationCodeActivity.this, "返回数据异常");
                }

            }

            @Override
            public void onFailure(VolleyError error) {
                dismissProgress();
                LogUtil.i("onFailure...." + error.getMessage());
                ToastUtil.showToast(InvitationCodeActivity.this, "提交失败");
            }

        });
    }






}
