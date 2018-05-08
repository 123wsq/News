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
import com.orhanobut.logger.Logger;
import com.wsq.library.listener.OnRecyclerViewItemClickListener;
import com.yc.wsq.app.news.R;
import com.yc.wsq.app.news.constant.ResponseKey;
import com.yc.wsq.app.news.constant.Urls;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.List;
import java.util.Map;

public class NewsAdapter extends RecyclerView.Adapter<NewsAdapter.ViewHolder>{

    private Context mContext;
    private List<Map<String, Object>> mData;
    private OnRecyclerViewItemClickListener mOnTitleItemListener;
    private int mFlag; //0 新闻   1搜索  2 收藏

    public NewsAdapter(Context context, List<Map<String, Object>> data, OnRecyclerViewItemClickListener onTitleItemListener, int flag){
        this.mContext = context;
        this.mData = data;
        this.mOnTitleItemListener = onTitleItemListener;
        this.mFlag = flag;
    }
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(mContext).inflate(R.layout.layout_news_item,parent, false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        Map<String, Object> map = mData.get(position);
        holder.tv_title.setText(map.get(ResponseKey.getInstace().title)+"");
        String recommend = map.get(ResponseKey.getInstace().is_recommend)+"";

        if (recommend.equals("1")){
            holder.tv_recommend.setText("推荐");
            holder.tv_recommend.setVisibility(View.VISIBLE);
        }else{
            holder.tv_recommend.setVisibility(View.GONE);
        }
        holder.tv_clickNum.setText(map.get(ResponseKey.getInstace().click)+" 阅读");
        holder.tv_source.setText(map.get(ResponseKey.getInstace().source)+"");
        String images = (String) map.get(ResponseKey.getInstace().thumb);
        try {
            JSONArray jsona = new JSONArray(images);
            String imgs[] = new String[jsona.length()];
            for (int i = 0; i < jsona.length(); i++) {
                imgs[i] = jsona.get(i)+"";
            }
            Glide.with(mContext).load(Urls.HOST+imgs[0]).into(holder.iv_image_1);
            Glide.with(mContext).load(Urls.HOST+imgs[1]).into(holder.iv_image_2);
            Glide.with(mContext).load(Urls.HOST+imgs[2]).into(holder.iv_image_3);
        } catch (JSONException e) {
            e.printStackTrace();
        }


        switch (mFlag){
            case 0:
            case 1:
                holder.ll_status.setVisibility(View.VISIBLE);
                break;
            case 2:
                holder.ll_status.setVisibility(View.GONE);
                break;

        }
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private TextView tv_title, tv_recommend, tv_source, tv_clickNum;
        private LinearLayout ll_news_item;
        private ImageView iv_image_1, iv_image_2, iv_image_3;
        private LinearLayout ll_status;
        public ViewHolder(View itemView) {
            super(itemView);
            ll_news_item = itemView.findViewById(R.id.ll_news_item);
            tv_title = itemView.findViewById(R.id.tv_item_title);
            iv_image_1 = itemView.findViewById(R.id.iv_image_1);
            iv_image_2 = itemView.findViewById(R.id.iv_image_2);
            iv_image_3 = itemView.findViewById(R.id.iv_image_3);
            tv_recommend = itemView.findViewById(R.id.tv_recommend);
            tv_source = itemView.findViewById(R.id.tv_source);
            tv_clickNum = itemView.findViewById(R.id.tv_clickNum);
            ll_status = itemView.findViewById(R.id.ll_status);
            ll_news_item.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.ll_news_item:
                    if (mOnTitleItemListener != null) {
                        mOnTitleItemListener.onRecyclerItemClickListener(null, getPosition());
                    }
                    break;
            }
        }
    }
}