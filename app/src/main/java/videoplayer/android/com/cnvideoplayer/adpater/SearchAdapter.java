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

/**
 * Date: 2018/10/22
 * Author:
 * Email：
 * Des：
 */

public class SearchAdapter extends RecyclerView.Adapter<SearchAdapter.AgeItemViewHolder> {


    public static final int ITEM_NUM = 4; // 每行拥有的Item数, 必须是奇数
    private List<String> data;
    private Context mContext;

    private OnItemClickListener mListener;
    public interface OnItemClickListener {
        void onItemClick(int position, String data);
    }

    public void setOnItemClickListener(OnItemClickListener li) {
        mListener = li;
    }


    public SearchAdapter(Context mContext, List<String> data) {
        this.mContext = mContext;
        this.data = data;
    }

    @Override
    public AgeItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View item = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_recyclerview_search, parent, false);
        // 设置Item的宽度
      //  ViewGroup.LayoutParams lp = item.getLayoutParams();
      //  lp.width = getItemStdWidth(mContext);
        return new AgeItemViewHolder(item);
    }

    @Override
    public void onBindViewHolder(AgeItemViewHolder holder, final int position) {

        holder.mTextView.setText(data.get(position));

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

