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
import com.yc.wsq.app.news.bean.AmountBean;
import com.yc.wsq.app.news.constant.ResponseKey;

import java.util.List;
import java.util.Map;

public class BenefitAmountAdapter extends RecyclerView.Adapter<BenefitAmountAdapter.ViewHolder>{

    private int[] drawableId= {R.mipmap.image_0, R.mipmap.image_1, R.mipmap.image_2, R.mipmap.image_3, R.mipmap.image_4, R.mipmap.image_5, R.mipmap.image_6, R.mipmap.image_7, R.mipmap.image_8, R.mipmap.image_9};
    private Context mContext;
    private List<AmountBean> mData;
    private int selectPosition = 0;

    public BenefitAmountAdapter(Context context, List<AmountBean> list){
        this.mContext = context;
        this.mData = list;
    }
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(mContext).inflate(R.layout.layout_item_benefit_amount,parent, false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        AmountBean bean = mData.get(position);
        holder.iv_benefit_amount.setVisibility(bean.isIsnum() ? View.VISIBLE :View.GONE);
        holder.iv_comma.setVisibility(bean.isIsnum() ? View.GONE : View.VISIBLE);
        if (bean.isIsnum()){
            holder.iv_benefit_amount.setImageResource(drawableId[bean.getNum()]);
        }
    }

    @Override
    public int getItemCount() {

        return mData.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private ImageView iv_benefit_amount;
        private ImageView iv_comma;

        public ViewHolder(View itemView) {
            super(itemView);
            iv_benefit_amount = itemView.findViewById(R.id.iv_benefit_amount);
            iv_comma = itemView.findViewById(R.id.iv_comma);
        }


    }
}