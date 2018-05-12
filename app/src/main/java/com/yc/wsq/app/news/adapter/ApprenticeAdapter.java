package com.yc.wsq.app.news.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.wsq.library.listener.OnRecyclerViewItemClickListener;
import com.yc.wsq.app.news.R;
import com.yc.wsq.app.news.constant.ResponseKey;
import com.yc.wsq.app.news.constant.Urls;

import java.util.List;
import java.util.Map;

public class ApprenticeAdapter extends RecyclerView.Adapter<ApprenticeAdapter.ViewHolder>{


    private Context mContext;
    private List<Map<String, Object>> mData;
    private OnRecyclerViewItemClickListener mListener;
    public ApprenticeAdapter(Context context, List<Map<String, Object>> list, OnRecyclerViewItemClickListener listener){
        this.mContext = context;
        this.mData = list;
        this.mListener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(mContext).inflate(R.layout.layout_item_apprentice, parent, false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {


        String url = mData.get(position).get(ResponseKey.getInstace().head_pic)+"";

        RequestOptions cropOptions = new RequestOptions().circleCrop();
        cropOptions.error(R.mipmap.image_header_default);
        Glide.with(mContext)
                .load(Urls.HOST+ url)
                .apply(cropOptions)
                .into(holder.iv_header);

        holder.tv_nickname.setText(mData.get(position).get(ResponseKey.getInstace().nickname)+"");
        holder.tv_vip_name.setText(mData.get(position).get(ResponseKey.getInstace().level_name)+"");
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private ImageView  iv_header;
        private TextView tv_nickname;
        private TextView tv_vip_name;

        public ViewHolder(View itemView) {
            super(itemView);

            iv_header= itemView.findViewById(R.id.iv_header);
            tv_nickname = itemView.findViewById(R.id.tv_nickname);
            tv_vip_name = itemView.findViewById(R.id.tv_vip_name);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {

            if (mListener != null){
                mListener.onRecyclerItemClickListener(v, getPosition());
            }
        }
    }
}
