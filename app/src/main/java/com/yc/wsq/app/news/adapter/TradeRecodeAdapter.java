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

public class TradeRecodeAdapter extends RecyclerView.Adapter<TradeRecodeAdapter.ViewHolder>{

    private Context mContext;
    private List<Map<String, Object>> mData;
    private OnRecyclerViewItemClickListener mClickLister;

    public TradeRecodeAdapter(Context context, List<Map<String, Object>>list, OnRecyclerViewItemClickListener listener){
        this.mContext = context;
        this.mData = list;
        this.mClickLister = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(mContext).inflate(R.layout.layout_item_trade_recode, parent, false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        holder.tv_trade_num.setText(mData.get(position).get(ResponseKey.getInstace().id)+"");
        holder.tv_trade_money.setText(mData.get(position).get(ResponseKey.getInstace().money)+"");
        holder.tv_trade_time.setText(mData.get(position).get(ResponseKey.getInstace().create_time)+"");
        holder.tv_trade_state.setText(mData.get(position).get(ResponseKey.getInstace().status_name)+"");
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        private TextView tv_trade_num, tv_trade_time, tv_trade_money, tv_trade_state;

        public ViewHolder(View itemView) {
            super(itemView);

            tv_trade_num = itemView.findViewById(R.id.tv_trade_num);
            tv_trade_money = itemView.findViewById(R.id.tv_trade_money);
            tv_trade_time = itemView.findViewById(R.id.tv_trade_time);
            tv_trade_state = itemView.findViewById(R.id.tv_trade_state);

        }


    }
}
