package videoplayer.android.com.cnvideoplayer.utils;


import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.widget.Toast;

/**
 * Date: 2018/8/15
 * Author:
 * Des：
 */
public class ToastUtil {

    private static Context mContext = null;
    public static final int SHOW_TOAST = 0;
    private static Toast mToast;
    private static Handler mHandler = new Handler();
    private static Runnable r = new Runnable() {
        public void run() {
            ToastUtil.mToast.cancel();
        }
    };
    private static Handler baseHandler = new Handler() {
        public void handleMessage(Message msg) {
            switch(msg.what) {
                case 0:
                    ToastUtil.showToast(ToastUtil.mContext, msg.getData().getString("TEXT"));
                default:
            }
        }
    };

    public ToastUtil() {
    }

    public static void showToast(Context context, String text) {
        showToast(context, text, Toast.LENGTH_SHORT);
    }

    public static void showToast(Context context, String text, int time) {
        mContext = context.getApplicationContext();
        if(!StringUtil.isEmpty(text)) {
            Toast.makeText(mContext, text, time).show();
        }

    }

    public static void showToast(Context context, int gravity, String text) {
        mContext = context.getApplicationContext();
        if(!StringUtil.isEmpty(text)) {
            Toast toast = Toast.makeText(mContext, text, Toast.LENGTH_SHORT);
            toast.setGravity(gravity, 0, 0);
            toast.show();
        }

    }

    public static void showToast(Context context, int resId) {
        mContext = context.getApplicationContext();
        Toast.makeText(mContext, "" + mContext.getResources().getText(resId), Toast.LENGTH_SHORT).show();
    }

    public static void showToastInThread(Context context, int resId) {
        mContext = context.getApplicationContext();
        Message msg = baseHandler.obtainMessage(0);
        Bundle bundle = new Bundle();
        bundle.putString("TEXT", mContext.getResources().getString(resId));
        msg.setData(bundle);
        baseHandler.sendMessage(msg);
    }

    public static void showToastInThread(Context context, String text) {
        mContext = context.getApplicationContext();
        Message msg = baseHandler.obtainMessage(0);
        Bundle bundle = new Bundle();
        bundle.putString("TEXT", text);
        msg.setData(bundle);
        baseHandler.sendMessage(msg);
    }

    public static void showToastNoRepeat(Context mContext, String text) {
        showToastNoRepeat(mContext, text, 2000);
    }

    public static void showToastNoRepeat(Context mContext, String text, int duration) {
        mHandler.removeCallbacks(r);
        if(mToast != null) {
            mToast.setText(text);
        } else {
            mToast = Toast.makeText(mContext, text, Toast.LENGTH_SHORT);
        }

        mHandler.postDelayed(r, (long)duration);
        mToast.show();
    }

    public static void showToastNoRepeat(Context mContext, int resId, int duration) {
        showToastNoRepeat(mContext, mContext.getResources().getString(resId), duration);
    }
}

