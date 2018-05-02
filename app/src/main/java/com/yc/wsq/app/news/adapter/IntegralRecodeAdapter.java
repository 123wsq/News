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

public class IntegralRecodeAdapter extends RecyclerView.Adapter<IntegralRecodeAdapter.ViewHolder>{

    private Context mContext;
    private List<Map<String, Object>> mData;
    private OnRecyclerViewItemClickListener mClickLister;

    public IntegralRecodeAdapter(Context context, List<Map<String, Object>>list, OnRecyclerViewItemClickListener listener){
        this.mContext = context;
        this.mData = list;
        this.mClickLister = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(mContext).inflate(R.layout.layout_item_integral_recode, parent, false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        holder.tv_trade_type.setText(mData.get(position).get(ResponseKey.getInstace().type_name)+"");
        String pay_points = (String) mData.get(position).get(ResponseKey.getInstace().pay_points);

        holder.tv_trade_money.setText(pay_points.startsWith("-") ? pay_points : "+"+pay_points);
        holder.tv_trade_time.setText(mData.get(position).get(ResponseKey.getInstace().change_time)+"");
        holder.tv_total_money.setText(mData.get(position).get(ResponseKey.getInstace().user_points)+"");

        holder.tv_trade_type.setTextColor(pay_points.startsWith("-") ? mContext.getResources().getColor(R.color.red) : mContext.getResources().getColor(R.color.color_blue));
//            holder.tv_trade_type.setTextColor(mContext.getResources().getColor(R.color.color_blue));
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private LinearLayout ll_layout;
        private TextView tv_trade_type, tv_trade_money, tv_trade_time, tv_total_money;

        public ViewHolder(View itemView) {
            super(itemView);

            ll_layout = itemView.findViewById(R.id.ll_layout);
            tv_trade_type = itemView.findViewById(R.id.tv_trade_type);
            tv_trade_money = itemView.findViewById(R.id.tv_trade_money);
            tv_trade_time = itemView.findViewById(R.id.tv_trade_time);
            tv_total_money = itemView.findViewById(R.id.tv_total_money);

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
