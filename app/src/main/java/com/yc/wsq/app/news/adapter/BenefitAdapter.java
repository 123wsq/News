package com.yc.wsq.app.news.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.wsq.library.listener.OnRecyclerViewItemClickListener;
import com.yc.wsq.app.news.R;
import com.yc.wsq.app.news.constant.ResponseKey;
import com.yc.wsq.app.news.constant.Urls;
import com.yc.wsq.app.news.tools.DateFormat;

import java.util.List;
import java.util.Map;

public class BenefitAdapter extends RecyclerView.Adapter<BenefitAdapter.ViewHolder>{

    private Context mContext;
    private List<Map<String, Object>> mData;
    private OnRecyclerViewItemClickListener mClickLister;

    public BenefitAdapter(Context context, List<Map<String, Object>>list, OnRecyclerViewItemClickListener listener){
        this.mContext = context;
        this.mData = list;
        this.mClickLister = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(mContext).inflate(R.layout.layout_item_benefit, parent, false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        String url = mData.get(position).get(ResponseKey.getInstace().image)+"";

        RequestOptions cropOptions = new RequestOptions().circleCrop();
        Glide.with(mContext)
                .load(Urls.HOST+ url)
                .apply(cropOptions)
                .into(holder.image_header);
        holder.tv_name.setText(mData.get(position).get(ResponseKey.getInstace().title)+"");
        holder.tv_desc.setText(mData.get(position).get(ResponseKey.getInstace().description)+"");
        String date = mData.get(position).get(ResponseKey.getInstace().ctime)+"";

        holder.tv_time.setText(DateFormat.onDateFormat(date, DateFormat.DATE_FORMAT_1));
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private LinearLayout ll_layout;
        private ImageView image_header;
        private TextView tv_name, tv_desc, tv_time;

        public ViewHolder(View itemView) {
            super(itemView);

            ll_layout = itemView.findViewById(R.id.ll_layout);
            image_header = itemView.findViewById(R.id.image_header);
            tv_name = itemView.findViewById(R.id.tv_name);
            tv_desc = itemView.findViewById(R.id.tv_desc);
            tv_time = itemView.findViewById(R.id.tv_time);

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
