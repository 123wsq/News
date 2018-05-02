package com.yc.wsq.app.news.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.wsq.library.listener.OnRecyclerViewItemClickListener;
import com.yc.wsq.app.news.R;

import java.util.List;

public class HotSearchAdapter extends RecyclerView.Adapter<HotSearchAdapter.ViewHolder>{

    private Context mContext;
    private List<String> mData;
    private OnRecyclerViewItemClickListener mOnTitleItemListener;

    public HotSearchAdapter(Context context, List<String> data, OnRecyclerViewItemClickListener onTitleItemListener){
        this.mContext = context;
        this.mData = data;
        this.mOnTitleItemListener = onTitleItemListener;
    }
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(mContext).inflate(R.layout.layout_item_search_record,parent, false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        holder.tv_title.setText((position+1)+". "+mData.get(position));
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private TextView tv_title;
        public ViewHolder(View itemView) {
            super(itemView);
            tv_title = itemView.findViewById(R.id.tv_title_name);
            tv_title.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.tv_title_name:
                    if (mOnTitleItemListener != null) {
                        mOnTitleItemListener.onRecyclerItemClickListener(null, getPosition());
                    }
                    break;
            }
        }
    }
}