package videoplayer.android.com.cnvideoplayer.base;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.umeng.analytics.MobclickAgent;

import butterknife.ButterKnife;
import videoplayer.android.com.cnvideoplayer.utils.ToastUtil;

/**
 * Date: 2018/8/17
 * Des：
 */

public abstract class BaseFragment extends Fragment {
    protected View rootView;
    ProgressDialog mProgressDialog;
    public BaseApplication abApplication = null;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (rootView == null)
            rootView = inflater.inflate(getLayoutResource(), container, false);
        ButterKnife.bind(this, rootView);
        abApplication = (BaseApplication) getActivity().getApplication();

        initPresenter();
        initView();
        return rootView;
    }

    //获取布局文件
    protected abstract int getLayoutResource();

    //简单页面无需mvp就不用管此方法即可,完美兼容各种实际场景的变通
    public abstract void initPresenter();

    //初始化view
    protected abstract void initView();


    /**
     * 通过Class跳转界面
     **/
    public void startActivity(Class<?> cls) {
        startActivity(cls, null);
    }

    /**
     * 通过Class跳转界面
     **/
    public void startActivityForResult(Class<?> cls, int requestCode) {
        startActivityForResult(cls, null, requestCode);
    }

    /**
     * 含有Bundle通过Class跳转界面
     **/
    public void startActivityForResult(Class<?> cls, Bundle bundle,
                                       int requestCode) {
        Intent intent = new Intent();
        intent.setClass(getActivity(), cls);
        if (bundle != null) {
            intent.putExtras(bundle);
        }
        startActivityForResult(intent, requestCode);
    }

    /**
     * 含有Bundle通过Class跳转界面
     **/
    public void startActivity(Class<?> cls, Bundle bundle) {
        Intent intent = new Intent();
        intent.setClass(getActivity(), cls);
        if (bundle != null) {
            intent.putExtras(bundle);
        }
        startActivity(intent);
    }


    /**
     * 开启加载进度条
     */
    public void startProgressDialog() {
        if (mProgressDialog == null) mProgressDialog = new ProgressDialog(getActivity());
        mProgressDialog.setMessage("正在加载...");
        if (!mProgressDialog.isShowing())
            mProgressDialog.show();
    }

    /**
     * 开启加载进度条
     *
     * @param msg
     */
    public void startProgressDialog(String msg) {
        if (mProgressDialog == null) mProgressDialog = new ProgressDialog(getActivity());
        mProgressDialog.setMessage(msg);
        if (!mProgressDialog.isShowing())
            mProgressDialog.show();
    }

    /**
     * 停止加载进度条
     */
    public void stopProgressDialog() {
        if (mProgressDialog != null && mProgressDialog.isShowing())
            mProgressDialog.dismiss();
    }


    /**
     * 短暂显示Toast提示(来自String)
     **/
    public void showShortToast(String text) {
        ToastUtil.showToast(getActivity(), text);
    }

    /**
     * 短暂显示Toast提示(id)
     **/
    public void showShortToast(int resId) {
        ToastUtil.showToast(getActivity(), resId);
    }

    /**
     * 长时间显示Toast提示(来自res)
     **/
    public void showLongToast(int resId) {

    }

    /**
     * 长时间显示Toast提示(来自String)
     **/
    public void showLongToast(String text) {

    }


    public void showToastWithImg(String text, int res) {

    }

    /**
     * 网络访问错误提醒
     */
    public void showNetErrorTip() {

    }

    public void showNetErrorTip(String error) {

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);

    }

    public void onResume() {
        super.onResume();
        MobclickAgent.onResume(getActivity());
    }
    @Override
    public void onPause() {
        super.onPause();
        MobclickAgent.onPause(getActivity());
    }

}