package com.yc.wsq.app.news.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.yc.wsq.app.news.R;
import com.wsq.library.listener.OnRecyclerViewItemClickListener;
import com.yc.wsq.app.news.constant.ResponseKey;

import java.util.List;
import java.util.Map;

public class TagAdapter extends RecyclerView.Adapter<TagAdapter.ViewHolder>{

    private Context mContext;
    private List<Map<String, Object>> mData;
    private OnRecyclerViewItemClickListener mListener;
    private int state = 0;

    public TagAdapter(Context context, List<Map<String, Object>> list, OnRecyclerViewItemClickListener listener){
        this.mContext = context;
        this.mData = list;
        this.mListener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(mContext).inflate(R.layout.layout_item_tag, parent, false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        holder.tv_cat_name.setText(mData.get(position).get(ResponseKey.getInstace().cat_name)+"");
        switch (state){
            case 0:
                holder.iv_add.setVisibility(View.GONE);
                holder.iv_delete.setVisibility(View.GONE);
                break;
            case 1:
                holder.iv_add.setVisibility(View.VISIBLE);
                break;
            case 2:
                holder.iv_delete.setVisibility(View.VISIBLE);
                break;
        }
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private TextView tv_cat_name;
        private ImageView iv_add, iv_delete;

        public ViewHolder(View itemView) {
            super(itemView);
            tv_cat_name = itemView.findViewById(R.id.tv_cat_name);
            iv_add = itemView.findViewById(R.id.iv_add);
            iv_delete = itemView.findViewById(R.id.iv_delete);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if (mListener != null){
                mListener.onRecyclerItemClickListener(v, getPosition());
            }
        }
    }

    public void onChangeState(int state){

        this.state = state;
        notifyDataSetChanged();
    }
}
