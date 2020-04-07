package videoplayer.android.com.cnvideoplayer.base;

import android.app.Application;
import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.support.multidex.MultiDexApplication;
import android.support.v7.app.AppCompatDelegate;
import android.util.Log;

import com.ansen.http.entity.HttpConfig;
import com.ansen.http.net.HTTPCaller;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.DefaultRefreshFooterCreator;
import com.scwang.smartrefresh.layout.api.DefaultRefreshHeaderCreator;
import com.scwang.smartrefresh.layout.api.RefreshFooter;
import com.scwang.smartrefresh.layout.api.RefreshHeader;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.footer.ClassicsFooter;
import com.scwang.smartrefresh.layout.header.ClassicsHeader;
import com.shuyu.gsyvideoplayer.GSYVideoManager;
import com.shuyu.gsyvideoplayer.player.IjkPlayerManager;
import com.shuyu.gsyvideoplayer.player.PlayerFactory;
import com.shuyu.gsyvideoplayer.utils.GSYVideoType;
import com.umeng.analytics.MobclickAgent;
import com.umeng.commonsdk.UMConfigure;
import com.wanjian.cockroach.Cockroach;

import videoplayer.android.com.cnvideoplayer.R;
import videoplayer.android.com.cnvideoplayer.utils.DynamicTimeFormat;
import videoplayer.android.com.cnvideoplayer.utils.LogUtil;
import videoplayer.android.com.cnvideoplayer.utils.Utils;

/**
 * Date: 2018/8/15
 * Author:
 * Email：
 * Des：
 */

public class BaseApplication extends MultiDexApplication {
    static {
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);//启用矢量图兼容
        SmartRefreshLayout.setDefaultRefreshHeaderCreator(new DefaultRefreshHeaderCreator() {
            @NonNull
            @Override
            public RefreshHeader createRefreshHeader(@NonNull Context context, @NonNull RefreshLayout layout) {
                layout.setPrimaryColorsId(R.color.colorPrimary, android.R.color.white);//全局设置主题颜色
                return new ClassicsHeader(context).setTimeFormat(new DynamicTimeFormat("更新于 %s"));
            }
        });
        //设置全局的Footer构建器
        SmartRefreshLayout.setDefaultRefreshFooterCreator(new DefaultRefreshFooterCreator() {
            @Override
            public RefreshFooter createRefreshFooter(Context context, RefreshLayout layout) {
                //指定为经典Footer，默认是 BallPulseFooter
                return new ClassicsFooter(context).setDrawableSize(20);
            }
        });
    }

    private static BaseApplication mBaseApplication;

    public static Context getAppContext() {
        return mBaseApplication;
    }

    public static BaseApplication getBaseApplication() {
        return mBaseApplication;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mBaseApplication = this;

        HttpConfig httpConfig = new HttpConfig();
        httpConfig.setAgent(true);//有代理的情况能不能访问
        httpConfig.setDebug(true);//是否debug模式 如果是debug模式打印log
        httpConfig.setTagName("Ansen");//打印log的tagname

        //可以添加一些公共字段 每个接口都会带上
        httpConfig.addCommonField("pf", "android");
        httpConfig.addCommonField("version_code", "" + Utils.getVersionCode(getApplicationContext()));
        //初始化HTTPCaller类
        HTTPCaller.getInstance().setHttpConfig(httpConfig);


        /*
          注意：如果您已经在AndroidManifest.xml中配置过appkey和channel值，可以调用此版本初始化函数。
        */
        UMConfigure.init(this, UMConfigure.DEVICE_TYPE_PHONE,"");
        MobclickAgent.setScenarioType(this, MobclickAgent.EScenarioType.E_UM_NORMAL);
        UMConfigure.setLogEnabled(true);

        GSYVideoType.enableMediaCodec();
        GSYVideoType.enableMediaCodecTexture();
        PlayerFactory.setPlayManager(new IjkPlayerManager());//ijk模式
        Cockroach.install(new Cockroach.ExceptionHandler() {

            // handlerException内部建议手动try{  你的异常处理逻辑  }catch(Throwable e){ } ，以防handlerException内部再次抛出异常，导致循环调用handlerException

            @Override
            public void handlerException(final Thread thread, final Throwable throwable) {
                //开发时使用Cockroach可能不容易发现bug，所以建议开发阶段在handlerException中用Toast谈个提示框，
                //由于handlerException可能运行在非ui线程中，Toast又需要在主线程，所以new了一个new Handler(Looper.getMainLooper())，
                //所以千万不要在下面的run方法中执行耗时操作，因为run已经运行在了ui线程中。
                //new Handler(Looper.getMainLooper())只是为了能弹出个toast，并无其他用途
                new Handler(Looper.getMainLooper()).post(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            //建议使用下面方式在控制台打印异常，这样就可以在Error级别看到红色log
                            Log.e("AndroidRuntime", "--->CockroachException:" + thread + "<---", throwable);
                            //       Toast.makeText(BaseApplication.this, "Exception Happend\n" + thread + "\n" + throwable.toString(), Toast.LENGTH_SHORT).show();
//                        throw new RuntimeException("..."+(i++));
                        } catch (Throwable e) {

                        }
                    }
                });
            }
        });
    }

    @Override
    public void onTerminate() {
        // 程序终止的时候执行
        LogUtil.d("--------------------onTerminate-----程序终止的时候执行-----------");
        super.onTerminate();
    }


}
