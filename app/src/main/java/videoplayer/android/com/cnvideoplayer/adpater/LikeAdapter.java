package videoplayer.android.com.cnvideoplayer.adpater;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.load.engine.DiskCacheStrategy;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import videoplayer.android.com.cnvideoplayer.GlideApp;
import videoplayer.android.com.cnvideoplayer.R;
import videoplayer.android.com.cnvideoplayer.entity.ActorEntity;
import videoplayer.android.com.cnvideoplayer.ui.ViedoListActivity;
import videoplayer.android.com.cnvideoplayer.utils.DisplayUtil;

/**
 * Date: 2018/11/6
 * Author:
 * Email：
 * Des：
 */

public class LikeAdapter extends RecyclerView.Adapter<LikeAdapter.ViewHolder> {
    private LayoutInflater mInflater;
    private List<ActorEntity> data;
    private Activity mContext;

    public LikeAdapter(Activity context, List<ActorEntity> data) {
        this.mContext = context;
        this.mInflater = LayoutInflater.from(context);
        this.data = data;
    }

    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View rootView = this.mInflater.inflate(R.layout.layout_item_like, parent, false);
        return new ViewHolder(rootView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {

        GlideApp.with(mContext)
                .load(data.get(position).getPhoto())
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .placeholder(R.color.text_gray)
                .error(R.mipmap.moren_1)
                .fitCenter()
                .into(holder.icon);

        holder.name.setText(data.get(position).getName());


        if(data.get(position).getFavor()==1){
            holder.btn_change.setText("已收藏");
            holder.btn_change.setSelected(true);
        }else{
            holder.btn_change.setText("收藏");
            holder.btn_change.setSelected(false);
        }

        holder.btn_change.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //
                if(mOnItemClickListener!=null){
                    mOnItemClickListener.onClick(position,data.get(position),v);
                }
            }
        });
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               ViedoListActivity.startActivity(mContext,data.get(position).getName(),data.get(position).getActorid(),"","","","");

            }
        });
    }

    @Override
    public int getItemCount() {
        return data.size();//数据数量，不想搞太复杂
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        private CircleImageView icon;
        private TextView name;
        private Button btn_change;

        public ViewHolder(View itemView) {
            super(itemView);
            icon = (CircleImageView) itemView.findViewById(R.id.iv_avatar);
            name = (TextView) itemView.findViewById(R.id.tv_name);
            btn_change = (Button) itemView.findViewById(R.id.btn_change);


        }
    }
   public interface OnItemClickListener1 {
        void onClick(int pos,ActorEntity data,View v);
    }
    private OnItemClickListener1 mOnItemClickListener;

    public void setmOnItemClickListener(OnItemClickListener1 mOnItemClickListener) {
        this.mOnItemClickListener = mOnItemClickListener;
    }
}
