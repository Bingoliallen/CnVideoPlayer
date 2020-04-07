package videoplayer.android.com.cnvideoplayer.adpater;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.bumptech.glide.load.engine.DiskCacheStrategy;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import videoplayer.android.com.cnvideoplayer.GlideApp;
import videoplayer.android.com.cnvideoplayer.R;
import videoplayer.android.com.cnvideoplayer.entity.Collect;
import videoplayer.android.com.cnvideoplayer.ui.ViedoListActivity;

/**
 * Date: 2018/11/8
 * Author:
 * Email：
 * Des：
 */

public class CollectActorAdaper extends RecyclerView.Adapter<CollectActorAdaper.ViewHolder> {
    private LayoutInflater mInflater;
    private List<Collect> data;
    private Activity mContext;

    public CollectActorAdaper(Activity context, List<Collect> data) {
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
                .load(data.get(position).getPic())
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .placeholder(R.color.text_gray)
                .error(R.mipmap.moren_1)
                .fitCenter()
                .into(holder.icon);

        holder.name.setText(data.get(position).getTitle());


        if(data.get(position).getObjtype()==2){
            holder.btn_change.setText("已收藏");
            holder.btn_change.setSelected(true);
           // holder.btn_change.setEnabled(false);
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

                ViedoListActivity.startActivity(mContext,data.get(position).getTitle(),data.get(position).getObjid(),"","","","");

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
        void onClick(int pos,Collect data,View v);
    }
    private OnItemClickListener1 mOnItemClickListener;

    public void setmOnItemClickListener(OnItemClickListener1 mOnItemClickListener) {
        this.mOnItemClickListener = mOnItemClickListener;
    }
}

