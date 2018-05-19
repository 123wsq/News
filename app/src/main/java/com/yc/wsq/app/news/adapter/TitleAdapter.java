package com.yc.wsq.app.news.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.wsq.library.listener.OnRecyclerViewItemClickListener;
import com.yc.wsq.app.news.R;
import com.yc.wsq.app.news.constant.ResponseKey;

import java.util.List;
import java.util.Map;

public class TitleAdapter extends RecyclerView.Adapter<TitleAdapter.ViewHolder>{

    private Context mContext;
    private List<Map<String, Object>> mData;
    private OnRecyclerViewItemClickListener mOnTitleItemListener;
    private int selectPosition = 0;

    public TitleAdapter(Context context, List<Map<String, Object>> data, OnRecyclerViewItemClickListener onTitleItemListener){
        this.mContext = context;
        this.mData = data;
        this.mOnTitleItemListener = onTitleItemListener;
    }
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(mContext).inflate(R.layout.layout_title_item,parent, false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        holder.tv_title.setText(mData.get(position).get(ResponseKey.getInstace().cat_name)+"");
        holder.tv_title.setTextColor(selectPosition == position ? mContext.getResources().getColor(R.color.red) : mContext.getResources().getColor(R.color.black));
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private TextView tv_title;
        private LinearLayout ll_layout;

        public ViewHolder(View itemView) {
            super(itemView);
            tv_title = itemView.findViewById(R.id.tv_title_name);
            ll_layout = itemView.findViewById(R.id.ll_layout);
            tv_title.setOnClickListener(this);

        }

        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.tv_title_name:
                    if (mOnTitleItemListener != null) {
                        mOnTitleItemListener.onRecyclerItemClickListener(null, getPosition());
                        selectPosition = getPosition();
                        notifyDataSetChanged();
                    }
                    break;

            }
        }
    }

    public void onSelectoPosition(int position){
        this.selectPosition = position;
        notifyDataSetChanged();
    }
}