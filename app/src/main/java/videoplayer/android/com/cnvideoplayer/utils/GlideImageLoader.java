package videoplayer.android.com.cnvideoplayer.utils;

import android.content.Context;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.youth.banner.loader.ImageLoader;

import videoplayer.android.com.cnvideoplayer.GlideApp;
import videoplayer.android.com.cnvideoplayer.R;

/**
 * Date: 2018/8/17
 * Author:
 * Des：
 */

public class GlideImageLoader extends ImageLoader {

    @Override
    public void displayImage(Context context, Object o, ImageView imageView) {
        LogUtil.d("----------displayImage-------");
        GlideApp.with(context).load(o).error(R.mipmap.moren_3).into(imageView);
    }
}