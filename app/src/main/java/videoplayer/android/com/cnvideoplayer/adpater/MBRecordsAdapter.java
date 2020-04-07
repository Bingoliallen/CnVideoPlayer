package videoplayer.android.com.cnvideoplayer.adpater;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import videoplayer.android.com.cnvideoplayer.R;
import videoplayer.android.com.cnvideoplayer.entity.MB;

/**
 * Date: 2018/10/22
 * Author:
 * Email：
 * Des：
 */

public class MBRecordsAdapter extends RecyclerView.Adapter<MBRecordsAdapter.AgeItemViewHolder> {


    public static final int ITEM_NUM = 4; // 每行拥有的Item数, 必须是奇数
    private List<MB> data=new ArrayList<>();
    private Context mContext;

    private OnItemClickListener mListener;

    public interface OnItemClickListener {
        void onItemClick(int position, MB data);
    }

    public void setOnItemClickListener(OnItemClickListener li) {
        mListener = li;
    }


    public MBRecordsAdapter(Context mContext, List<MB> data) {
        this.mContext = mContext;
        this.data = data;
    }

    @Override
    public AgeItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View item = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_recyclerview_mb_records, parent, false);
        // 设置Item的宽度
        //  ViewGroup.LayoutParams lp = item.getLayoutParams();
        //  lp.width = getItemStdWidth(mContext);
        return new AgeItemViewHolder(item);
    }

    @Override
    public void onBindViewHolder(AgeItemViewHolder holder, final int position) {

        holder.item_age_value1.setText(data.get(position).getContent());
        holder.item_age_value2.setText(data.get(position).getTime());
        if(data.get(position).getType()==1){
            holder.item_icon.setImageResource(R.mipmap.tx);
            holder.num.setText("-"+data.get(position).getNums());
            holder.num.setTextColor(mContext.getResources().getColor(R.color.text_color));
        }else{
            holder.item_icon.setImageResource(R.mipmap.mb);
            holder.num.setText("+"+data.get(position).getNums());
            holder.num.setTextColor(mContext.getResources().getColor(R.color.main_color));
        }

        holder.item_age_value1.setOnClickListener(new View.OnClickListener() {
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

        private ImageView item_icon;
        private TextView item_age_value1;
        private TextView item_age_value2;
        private TextView num;


        public AgeItemViewHolder(View itemView) {
            super(itemView);
            item_icon = (ImageView) itemView.findViewById(R.id.item_icon);
            item_age_value1 = (TextView) itemView.findViewById(R.id.item_age_value1);
            item_age_value2 = (TextView) itemView.findViewById(R.id.item_age_value2);
            num = (TextView) itemView.findViewById(R.id.num);
        }

    }
}



