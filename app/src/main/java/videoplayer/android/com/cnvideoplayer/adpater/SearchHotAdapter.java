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
import videoplayer.android.com.cnvideoplayer.entity.Video;


/**
 * Date: 2018/10/22
 * Author:
 * Email：
 * Des：
 */

public class SearchHotAdapter extends RecyclerView.Adapter<SearchHotAdapter.AgeItemViewHolder> {


    public static final int ITEM_NUM = 4; // 每行拥有的Item数, 必须是奇数
    private List<Video> data;
    private Context mContext;

    private OnItemClickListener mListener;
    public interface OnItemClickListener {
        void onItemClick(int position, Video data);
    }

    public void setOnItemClickListener(OnItemClickListener li) {
        mListener = li;
    }


    public SearchHotAdapter(Context mContext, List<Video> data) {
        this.mContext = mContext;
        this.data = data;
    }

    @Override
    public AgeItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View item = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_recyclerview_search_hot, parent, false);
        // 设置Item的宽度
        //  ViewGroup.LayoutParams lp = item.getLayoutParams();
        //  lp.width = getItemStdWidth(mContext);
        return new AgeItemViewHolder(item);
    }

    @Override
    public void onBindViewHolder(AgeItemViewHolder holder, final int position) {

        holder.mTextView.setText(data.get(position).getName());
        if(position==0){
            holder.item_num.setTextColor(mContext.getResources().getColor(R.color.main_color));
        }else if(position==1){
            holder.item_num.setTextColor(mContext.getResources().getColor(R.color.light_red));
        }else if(position==2){
            holder.item_num.setTextColor(mContext.getResources().getColor(R.color.light_green));
        }else{
            holder.item_num.setTextColor(mContext.getResources().getColor(R.color.text_color));
        }
        holder.item_num.setText(""+(position+1));
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
        private TextView item_num;

        public AgeItemViewHolder(View itemView) {
            super(itemView);
            mTextView = (TextView) itemView.findViewById(R.id.item_age_value);
            item_num = (TextView) itemView.findViewById(R.id.item_num);
        }

    }
}


