package videoplayer.android.com.cnvideoplayer.adpater;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import videoplayer.android.com.cnvideoplayer.GlideApp;
import java.util.List;
import de.hdodenhof.circleimageview.CircleImageView;
import videoplayer.android.com.cnvideoplayer.R;
import videoplayer.android.com.cnvideoplayer.entity.Actors;

/**
 * Date: 2018/8/26
 * Author:
 * Email：
 * Des：
 */

public class PhotosAdapter extends RecyclerView.Adapter<PhotosAdapter.AgeItemViewHolder> {


    public static final int ITEM_NUM = 5; // 每行拥有的Item数, 必须是奇数
    private List<Actors> data;
    private Context mContext;

    public PhotosAdapter(Context mContext, List<Actors> data) {
        this.mContext = mContext;
        this.data = data;
    }

    @Override
    public AgeItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // View item = LayoutInflater.from(mContext).inflate(R.layout.item_grid_classify_list, parent, false);
        View item = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_photo_horuzintal, parent, false);
        // 设置Item的宽度
       ViewGroup.LayoutParams lp = item.getLayoutParams();
       lp.width = getItemStdWidth(mContext);
        return new AgeItemViewHolder(item);
    }

    @Override
    public void onBindViewHolder(AgeItemViewHolder holder, int position) {
        GlideApp.with(mContext)
                .load(data.get(position).getPhoto())
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                //.placeholder(R.mipmap.moren_1)
                .error(R.mipmap.moren_1)
                .centerCrop()
                .into(holder.iv_avatar);
        holder.tv_name.setText(data.get(position).getName());
    }


    @Override
    public int getItemCount() {
        return data.size();
    }

    // 获取标准宽度
    public static int getItemStdWidth(Context mContext) {
        DisplayMetrics displayMetrics = mContext.getResources().getDisplayMetrics();
        return displayMetrics.widthPixels / ITEM_NUM;
    }

    // ViewHolder
    public class AgeItemViewHolder extends RecyclerView.ViewHolder {

        private CircleImageView iv_avatar;
        private TextView tv_name;

        public AgeItemViewHolder(View itemView) {
            super(itemView);
            iv_avatar = (CircleImageView) itemView.findViewById(R.id.iv_avatar);
            tv_name = (TextView) itemView.findViewById(R.id.tv_name);
        }

    }
}

