package com.yc.wsq.app.news.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.wsq.library.listener.OnRecyclerViewItemClickListener;
import com.yc.wsq.app.news.R;
import com.yc.wsq.app.news.bean.SearchBean;

import java.util.List;

public class SearchRecordAdapter extends RecyclerView.Adapter<SearchRecordAdapter.ViewHolder>{

    private Context mContext;
    private List<SearchBean> mData;
    private OnRecyclerViewItemClickListener mOnSearchRecordListener;

    public SearchRecordAdapter(Context context, List<SearchBean> data, OnRecyclerViewItemClickListener onSearchRecordListener){
        this.mContext = context;
        this.mData = data;
        this.mOnSearchRecordListener = onSearchRecordListener;
    }
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(mContext).inflate(R.layout.layout_item_search_record,parent, false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        holder.tv_title.setText((position+1)+". "+mData.get(position).getKeywords());
        holder.ll_delete.setVisibility(View.VISIBLE);
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private TextView tv_title;
        private LinearLayout ll_delete;
        public ViewHolder(View itemView) {
            super(itemView);
            tv_title = itemView.findViewById(R.id.tv_title_name);
            ll_delete = itemView.findViewById(R.id.ll_delete);
            tv_title.setOnClickListener(this);
            ll_delete.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.tv_title_name:
                    if (mOnSearchRecordListener != null) {
                        mOnSearchRecordListener.onRecyclerItemClickListener(null, getPosition());
                    }
                    break;
                case R.id.ll_delete:
                    if (mOnSearchRecordListener != null) {
                        mOnSearchRecordListener.onRecyclerItemClickListener(ll_delete, getPosition());
                    }
                    break;
            }
        }
    }
}