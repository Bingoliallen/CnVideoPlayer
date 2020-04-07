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

public class ProblemsActivity extends BaseCompatActivity {

    private EditText edProblem;
    private EditText edPhone;
    private Button commit;
    private String tokenCode;
    private String tokenInfo;
    @Override
    protected String initTitle() {
        return "问题与建议";
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_problems;
    }

    @Override
    public void initView() {
        StatusBarCompat.setStatusBarColor(this, ContextCompat.getColor(this, R.color.main_color));
        edProblem = (EditText) this.findViewById(R.id.edProblem);
        edPhone = (EditText) this.findViewById(R.id.edPhone);
        commit = (Button) this.findViewById(R.id.commit);
        commit.setOnClickListener(this);
    }

    @Override
    public void initData() {
        tokenCode = ShareUtils.getString(ProblemsActivity.this, "tokenCode", "");
        tokenInfo = ShareUtils.getString(ProblemsActivity.this, "tokenInfo", "");

        String mobile = ShareUtils.getString(this, "mobile", "");
        if(!TextUtils.isEmpty(mobile)){
            edPhone.setText(mobile);
            edPhone.setSelection(edPhone.getText().length());
        }
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
        //http://serpro/mobile/user/feedback.html

        HttpParams post_params = new HttpParams();
        post_params.putHeaders("tokenCode", tokenCode);
        post_params.putHeaders("tokenInfo", tokenInfo);
        post_params.putHeaders("Content-Type", "application/json");

        Map<String, String> params = new HashMap<String, String>();
        params.put("content", edProblem.getText().toString().trim());
       // params.put("mobile", edPhone.getText().toString());
        post_params.putJsonParams(new Gson().toJson(params));
        showProgress("正在加载...");
        RxVolley.jsonPost(StaticClass.domain + "/feedback.html", post_params, new HttpCallback() {
            @Override
            public void onSuccess(String t) {
                dismissProgress();
                LogUtil.i("******feedback*******commit*sq****:" + t);
                if (!TextUtils.isEmpty(t)) {
                    try {
                        BaseEntity mBaseEntity = new Gson().fromJson(t, BaseEntity.class);
                        if (mBaseEntity != null) {
                            if (mBaseEntity.getStatus() == 100) {
                                ToastUtil.showToast(ProblemsActivity.this, "提交成功");

                            } else {
                                if(!TextUtils.isEmpty(mBaseEntity.getMsg())){
                                    ToastUtil.showToast(ProblemsActivity.this, mBaseEntity.getMsg());
                                }else{
                                    ToastUtil.showToast(ProblemsActivity.this, "提交失败");
                                }

                            }

                        } else {
                            ToastUtil.showToast(ProblemsActivity.this, "返回数据异常");
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        ToastUtil.showToast(ProblemsActivity.this, "返回数据异常");
                    }

                } else {
                    ToastUtil.showToast(ProblemsActivity.this, "返回数据异常");
                }

            }

            @Override
            public void onFailure(VolleyError error) {
                dismissProgress();
                LogUtil.i("onFailure...." + error.getMessage());
                ToastUtil.showToast(ProblemsActivity.this, "请求失败");
            }

        });




    }
}
