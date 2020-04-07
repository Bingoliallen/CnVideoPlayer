package videoplayer.android.com.cnvideoplayer.adpater;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.CardView;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.text.Html;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.ArrayList;
import java.util.List;

import videoplayer.android.com.cnvideoplayer.GlideApp;
import videoplayer.android.com.cnvideoplayer.R;
import videoplayer.android.com.cnvideoplayer.entity.FeedArticleData;
import videoplayer.android.com.cnvideoplayer.entity.VideoEntity;
import videoplayer.android.com.cnvideoplayer.utils.DisplayUtil;

/**
 * Date: 2018/8/17
 * Author:
 * Email：
 * Des：
 */

public class ArticleListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    public static final int TYPE_HEADER = 0;
    public static final int TYPE_NORMAL = 1;

    private LayoutInflater mInflater;
    private Context mContext;

    private List<VideoEntity> mDatas = new ArrayList<>();

    private View mHeaderView;

    private OnItemClickListener mListener;

    public ArticleListAdapter(Context context, List<VideoEntity> data) {
        this.mContext = context;
        this.mInflater = LayoutInflater.from(context);
        this.mDatas = data;
    }

    public void setOnItemClickListener(OnItemClickListener li) {
        mListener = li;
    }

    public void setHeaderView(View headerView) {
        mHeaderView = headerView;
        notifyItemInserted(0);
    }

    public View getHeaderView() {
        return mHeaderView;
    }

    public void addDatas(List<VideoEntity> datas) {
        mDatas.addAll(datas);
        notifyDataSetChanged();
    }

    @Override
    public int getItemViewType(int position) {
        if (mHeaderView == null) return TYPE_NORMAL;
        if (position == 0) return TYPE_HEADER;
        return TYPE_NORMAL;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (mHeaderView != null && viewType == TYPE_HEADER) return new ViewHolder(mHeaderView);
        View layout = mInflater.inflate(R.layout.item_search_pager, parent, false);
        return new ViewHolder(layout);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, final int position) {
        if (getItemViewType(position) == TYPE_HEADER)
            return;

        final int pos = getRealPosition(viewHolder);
        final VideoEntity data = mDatas.get(pos);
        if (viewHolder instanceof ViewHolder) {
            GlideApp.with(mContext)
                    .load(data.getPic())
                    .error(R.mipmap.moren_1)
                    .fitCenter()
                    .into((ImageView) ((ViewHolder) viewHolder).icon);

             ((ViewHolder) viewHolder).name.setText(data.getName());
            /* if(!TextUtils.isEmpty(data.getViews())){
                 ((ViewHolder) viewHolder).num.setText(data.getViews() + "次播放");
             }else{
                 ((ViewHolder) viewHolder).num.setText( "0次播放");
             }*/
            ((ViewHolder) viewHolder).num.setText( data.getMemo());

            if(data.getFree()==1){
                ((ViewHolder) viewHolder). isFree.setVisibility(View.VISIBLE);
            }else{
                ((ViewHolder) viewHolder). isFree.setVisibility(View.GONE);
            }
            if (mListener == null) return;
            viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(mListener!=null){
                        mListener.onItemClick(pos, data);
                    }

                }
            });
        }
    }

    public int getRealPosition(RecyclerView.ViewHolder holder) {
        int position = holder.getLayoutPosition();
        return mHeaderView == null ? position : position - 1;
    }

    @Override
    public int getItemCount() {
        return mHeaderView == null ? mDatas.size() : mDatas.size() + 1;
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
        RecyclerView.LayoutManager manager = recyclerView.getLayoutManager();
        if (manager instanceof GridLayoutManager) {
            final GridLayoutManager gridManager = ((GridLayoutManager) manager);
            gridManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
                @Override
                public int getSpanSize(int position) {
                    return getItemViewType(position) == TYPE_HEADER
                            ? gridManager.getSpanCount() : 1;
                }
            });
        }
    }

    @Override
    public void onViewAttachedToWindow(RecyclerView.ViewHolder holder) {
        super.onViewAttachedToWindow(holder);
        ViewGroup.LayoutParams lp = holder.itemView.getLayoutParams();
        if (lp != null && lp instanceof StaggeredGridLayoutManager.LayoutParams) {
            StaggeredGridLayoutManager.LayoutParams p = (StaggeredGridLayoutManager.LayoutParams) lp;
            p.setFullSpan(holder.getLayoutPosition() == 0);
        }
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        private ImageView isFree;
        private ImageView icon;
        private TextView name;
        private TextView num;
        private RelativeLayout mRelat;
        public ViewHolder(View itemView) {
            super(itemView);
            if (itemView == mHeaderView) return;
            isFree = (ImageView) itemView.findViewById(R.id.isFree);
            icon = (ImageView) itemView.findViewById(R.id.icon);
            name = (TextView) itemView.findViewById(R.id.name);
            num = (TextView) itemView.findViewById(R.id.num);
            mRelat = (RelativeLayout) itemView.findViewById(R.id.mRelat);
            LinearLayout.LayoutParams linearParams =  (LinearLayout.LayoutParams)mRelat.getLayoutParams();
            linearParams.height =(DisplayUtil.getScreenWidth(mContext)-DisplayUtil.dip2px(20))/3*2;
            mRelat.setLayoutParams(linearParams);

        }
    }

    public interface OnItemClickListener {
        void onItemClick(int position, VideoEntity data);
    }
}






