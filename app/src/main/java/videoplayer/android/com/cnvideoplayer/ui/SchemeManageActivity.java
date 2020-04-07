package videoplayer.android.com.cnvideoplayer.ui;

import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;
import com.google.gson.Gson;
import com.kymjs.rxvolley.RxVolley;
import com.kymjs.rxvolley.client.HttpCallback;
import com.kymjs.rxvolley.client.HttpParams;
import com.kymjs.rxvolley.http.VolleyError;
import videoplayer.android.com.cnvideoplayer.R;
import videoplayer.android.com.cnvideoplayer.base.BaseCompatActivity;
import videoplayer.android.com.cnvideoplayer.entity.BaseEntity;
import videoplayer.android.com.cnvideoplayer.entity.Programme;
import videoplayer.android.com.cnvideoplayer.utils.LogUtil;
import videoplayer.android.com.cnvideoplayer.utils.ShareUtils;
import videoplayer.android.com.cnvideoplayer.utils.StaticClass;
import videoplayer.android.com.cnvideoplayer.utils.ToastUtil;
import videoplayer.android.com.cnvideoplayer.view.StatusBarCompat;

public class SchemeManageActivity extends BaseCompatActivity {

    private TextView  tv_name, tv_time,tv_status;
    private String tokenCode;
    private String tokenInfo;

    @Override
    protected String initTitle() {
        return "方案管理";
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_scheme_manage;
    }

    @Override
    public void initView() {
        StatusBarCompat.setStatusBarColor(this, ContextCompat.getColor(this, R.color.main_color));

        tv_name = (TextView) this.findViewById(R.id.tv_name);
        tv_time = (TextView) this.findViewById(R.id.tv_time);
        tv_status = (TextView) this.findViewById(R.id.tv_status);

    }

    @Override
    public void initData() {
        tokenCode = ShareUtils.getString(SchemeManageActivity.this, "tokenCode", "");
        tokenInfo = ShareUtils.getString(SchemeManageActivity.this, "tokenInfo", "");

        getProgramme();

    }

    @Override
    public void onClick(View v) {

    }



    private void getProgramme() {
        //http://serpro/mobile/user/programme.html

        HttpParams post_params = new HttpParams();
        post_params.putHeaders("tokenCode", tokenCode);
        post_params.putHeaders("tokenInfo", tokenInfo);
        post_params.putHeaders("Content-Type", "application/json");

        showProgress("正在加载...");
        RxVolley.jsonPost(StaticClass.domain + "/programme.html", post_params, new HttpCallback() {
            @Override
            public void onSuccess(String t) {
                dismissProgress();
                LogUtil.i("******programme********sq****:" + t);
                if (!TextUtils.isEmpty(t)) {
                    try {
                        BaseEntity mBaseEntity = new Gson().fromJson(t, BaseEntity.class);
                        if (mBaseEntity != null) {
                            if (mBaseEntity.getStatus() == 100) {
                                Programme mVideoList = new Gson().fromJson(t, Programme.class);
                                if (mVideoList != null && mVideoList.getResult() != null) {
                                    if (mVideoList.getResult().getItems() != null) {
                                        if (mVideoList.getResult().getItems().size()>0 ) {
                                            tv_name.setText(mVideoList.getResult().getItems().get(0).getName());
                                            tv_time.setText(mVideoList.getResult().getItems().get(0).getTime());
                                            tv_status.setText(mVideoList.getResult().getItems().get(0).getStatus());
                                        }

                                    }
                                }

                            } else {
                                if(!TextUtils.isEmpty(mBaseEntity.getMsg())){
                                    ToastUtil.showToast(SchemeManageActivity.this, mBaseEntity.getMsg());
                                }else{
                                    ToastUtil.showToast(SchemeManageActivity.this, "获取失败");
                                }

                            }

                        } else {
                            ToastUtil.showToast(SchemeManageActivity.this, "返回数据异常");
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        ToastUtil.showToast(SchemeManageActivity.this, "返回数据异常");
                    }

                } else {
                    ToastUtil.showToast(SchemeManageActivity.this, "返回数据异常");
                }

            }

            @Override
            public void onFailure(VolleyError error) {
                dismissProgress();
                LogUtil.i("onFailure...." + error.getMessage());
                ToastUtil.showToast(SchemeManageActivity.this, "获取失败");
            }

        });




    }


}
