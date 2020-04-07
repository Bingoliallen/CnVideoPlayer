package videoplayer.android.com.cnvideoplayer.adpater;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.load.engine.DiskCacheStrategy;

import java.util.List;

import videoplayer.android.com.cnvideoplayer.GlideApp;
import videoplayer.android.com.cnvideoplayer.R;
import videoplayer.android.com.cnvideoplayer.entity.Movie;
import videoplayer.android.com.cnvideoplayer.ui.VideoDetailActivity;
import videoplayer.android.com.cnvideoplayer.ui.ViedoListActivity;
import videoplayer.android.com.cnvideoplayer.utils.DisplayUtil;

/**
 * Date: 2018/11/8
 * Author:
 * Email：
 * Des：
 */

public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.ViewHolder> {
    private LayoutInflater mInflater;
    private List<Movie> data;
    private Activity mContext;

    public MovieAdapter(Activity context, List<Movie> data) {
        this.mContext = context;
        this.mInflater = LayoutInflater.from(context);
        this.data = data;
    }

    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View rootView = this.mInflater.inflate(R.layout.item_grid_classify_list, parent, false);
        return new ViewHolder(rootView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {

        GlideApp.with(mContext)
                .load(data.get(position).getIcon())
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .placeholder(R.color.text_gray)
                .error(R.mipmap.moren_1)
                .fitCenter()
                .into(holder.icon);

        holder.name.setText(data.get(position).getName());
        /*if(!TextUtils.isEmpty(""+data.get(position).getViews())){
            holder.num.setText(data.get(position).getViews() + "次播放");
        }else{
            holder.num.setText(0 + "次播放");
        }*/

       /* if(data.get(position).get()==1){
            holder.isFree.setVisibility(View.VISIBLE);
        }else{
            holder.isFree.setVisibility(View.GONE);
        }*/
        holder.isFree.setVisibility(View.GONE);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ViedoListActivity.startActivity(mContext,data.get(position).getName(),"","",data.get(position).getSortsid(),"","");
            }
        });
    }

    @Override
    public int getItemCount() {
        return data.size();//数据数量，不想搞太复杂
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        private ImageView icon;
        private TextView name;
        private TextView num;
        private ImageView isFree;
        private RelativeLayout mRelat;


        public ViewHolder(View itemView) {
            super(itemView);
            icon = (ImageView) itemView.findViewById(R.id.icon);
            name = (TextView) itemView.findViewById(R.id.name);
            num = (TextView) itemView.findViewById(R.id.num);
            isFree = (ImageView) itemView.findViewById(R.id.isFree);

            mRelat = (RelativeLayout) itemView.findViewById(R.id.mRelat);
            LinearLayout.LayoutParams linearParams =  (LinearLayout.LayoutParams)mRelat.getLayoutParams();
            linearParams.height =(DisplayUtil.getScreenWidth(mContext)-DisplayUtil.dip2px(20)-20)/2/3*2;
            mRelat.setLayoutParams(linearParams);


        }
    }
}
