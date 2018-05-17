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
import com.yc.wsq.app.news.tools.DateFormat;
import com.yc.wsq.app.news.tools.SharedTools;

import java.util.List;
import java.util.Map;

public class ArtivleCommentAdapter extends RecyclerView.Adapter<ArtivleCommentAdapter.ViewHolder>{

    private Context mContext;
    private List<Map<String, Object>> mData;
    private OnRecyclerViewItemClickListener mListener;
    public ArtivleCommentAdapter(Context context, List<Map<String, Object>> list, OnRecyclerViewItemClickListener listener){

        this.mContext = context;
        this.mData = list;
        this.mListener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.layout_item_article_comment, parent, false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        holder.tv_comment_name.setText(mData.get(position).get(ResponseKey.getInstace().username)+"");
        holder.tv_comment_content.setText(mData.get(position).get(ResponseKey.getInstace().content)+"");
        holder.tv_comment_time.setText(DateFormat.onGetCurIntervalTime(mData.get(position).get(ResponseKey.getInstace().add_time)+""));
        holder.tv_zan.setText(mData.get(position).get(ResponseKey.getInstace().zan_num)+"");
        int is_zan = (int) mData.get(position).get(ResponseKey.getInstace().is_int);
        if (is_zan == 0 ){
            holder.iv_zan.setImageResource(R.mipmap.image_praise_icon);
        }else {
            holder.iv_zan.setImageResource(R.mipmap.image_praise_press_icon);
        }

        String hederImage = SharedTools.getInstance(mContext).onGetString(ResponseKey.getInstace().head_pic).startsWith("http")
                ? SharedTools.getInstance(mContext).onGetString(ResponseKey.getInstace().head_pic) :
                Urls.HOST + SharedTools.getInstance(mContext).onGetString(ResponseKey.getInstace().head_pic);

        RequestOptions options = new RequestOptions();
        options.error(R.mipmap.image_header_default);
        options.circleCrop();
        Glide.with(mContext)
                .load(hederImage)
                .apply(options)
                .into(holder.iv_header);
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private TextView tv_comment_name;
        private TextView tv_comment_content;
        private TextView tv_comment_time, tv_zan;
        private ImageView iv_zan;
        private ImageView iv_header;
        public ViewHolder(View itemView) {
            super(itemView);
            tv_comment_name = itemView.findViewById(R.id.tv_comment_name);
            tv_comment_content = itemView.findViewById(R.id.tv_comment_content);
            tv_comment_time = itemView.findViewById(R.id.tv_comment_time);
            tv_zan = itemView.findViewById(R.id.tv_zan);
            iv_zan = itemView.findViewById(R.id.iv_zan);
            iv_header = itemView.findViewById(R.id.iv_header);
            iv_zan.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if (mListener != null)
            mListener.onRecyclerItemClickListener(v, getPosition());
        }
    }


}
