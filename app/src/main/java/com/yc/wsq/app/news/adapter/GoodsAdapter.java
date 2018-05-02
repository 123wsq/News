package com.yc.wsq.app.news.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.wsq.library.listener.OnRecyclerViewItemClickListener;
import com.wsq.library.utils.DensityUtil;
import com.yc.wsq.app.news.R;
import com.yc.wsq.app.news.constant.ResponseKey;

import java.util.List;
import java.util.Map;

public class GoodsAdapter extends RecyclerView.Adapter<GoodsAdapter.ViewHolder>{


    private Context mContext;
    private List<Map<String, Object>> mData;
    private OnRecyclerViewItemClickListener mClickLister;

    public GoodsAdapter(Context context, List<Map<String, Object>>list, OnRecyclerViewItemClickListener listener){
        this.mContext = context;
        this.mData = list;
        this.mClickLister = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.layout_item_goods, parent, false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {


        Glide.with(mContext).load(mData.get(position).get(ResponseKey.getInstace().images)).into(holder.image_goods_icon);
        holder.tv_goods_title.setText(mData.get(position).get(ResponseKey.getInstace().title)+"");


        holder.tv_more.setVisibility(position % 2 ==0 ? View.GONE :View.VISIBLE);
        RelativeLayout.LayoutParams params= (RelativeLayout.LayoutParams) holder.image_goods_icon.getLayoutParams();
        if (position % 2 == 0){
            holder.tv_goods_type.setText(mData.get(position).get(ResponseKey.getInstace().goods_type)+"");

            params.rightMargin = DensityUtil.dp2px(mContext, 10);
        }else{
            holder.tv_goods_type.setText("");
            params.leftMargin = DensityUtil.dp2px(mContext, 10);
        }
        holder.image_goods_icon.setLayoutParams(params);
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private TextView tv_goods_type, tv_goods_title, tv_more;
        private ImageView image_goods_icon, image_tag;
        private LinearLayout ll_layout;

        public ViewHolder(View itemView) {
            super(itemView);

            ll_layout = itemView.findViewById(R.id.ll_layout);
            tv_goods_type = itemView.findViewById(R.id.tv_goods_type);
            tv_goods_title = itemView.findViewById(R.id.tv_goods_title);
            image_goods_icon = itemView.findViewById(R.id.image_goods_icon);
            image_tag = itemView.findViewById(R.id.image_tag);
            tv_more = itemView.findViewById(R.id.tv_more);

            ll_layout.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if (mClickLister != null){
                mClickLister.onRecyclerItemClickListener(null, getPosition());
            }
        }
    }
}
