package videoplayer.android.com.cnvideoplayer.adpater;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import java.util.List;
import videoplayer.android.com.cnvideoplayer.R;
import videoplayer.android.com.cnvideoplayer.entity.VideoLines;

/**
 * Date: 2018/8/26
 * Author:
 * Email：
 * Des：
 */

public class LineAdapter extends RecyclerView.Adapter<LineAdapter.AgeItemViewHolder> {


    public static final int ITEM_NUM = 4; // 每行拥有的Item数, 必须是奇数
    private List<VideoLines> data;
    private Context mContext;

    private OnItemClickListener mListener;
    public interface OnItemClickListener {
        void onItemClick(int position, VideoLines data);
    }

    public void setOnItemClickListener(OnItemClickListener li) {
        mListener = li;
    }


    public LineAdapter(Context mContext, List<VideoLines> data) {
        this.mContext = mContext;
        this.data = data;
    }

    @Override
    public AgeItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View item = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_recyclerview_horuzintal, parent, false);
       // 设置Item的宽度
        ViewGroup.LayoutParams lp = item.getLayoutParams();
        lp.width = getItemStdWidth(mContext);
        return new AgeItemViewHolder(item);
    }

    @Override
    public void onBindViewHolder(AgeItemViewHolder holder, final int position) {

        holder.mTextView.setText(data.get(position).getName());
        // 高亮显示
        if (data.get(position).isSelected()) {
            holder.mTextView.setSelected(true);
        } else {
            holder.mTextView.setSelected(false);
        }
        holder.mTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               if(mListener!=null){
                   mListener.onItemClick(position , data.get(position));
               }
            }
        });
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

        private TextView mTextView;

        public AgeItemViewHolder(View itemView) {
            super(itemView);
            mTextView = (TextView) itemView.findViewById(R.id.item_age_value);

        }

    }
}
