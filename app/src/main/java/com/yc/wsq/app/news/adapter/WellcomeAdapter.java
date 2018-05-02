package com.yc.wsq.app.news.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.yc.wsq.app.news.R;

import java.util.List;

public class WellcomeAdapter extends PagerAdapter {

    private int drawables[] = {R.mipmap.wellcome_1, R.mipmap.wellcome_2, R.mipmap.wellcome_3, R.mipmap.wellcome_4};
    private Context mContext;
    private List<View> mData;


    public WellcomeAdapter(Context context, List<View> list){

        this.mContext = context;
        this.mData = list;
    }

    @Override
    public int getCount() {
        return mData.size();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == object;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView(mData.get(position));
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        View view = mData.get(position);
        ImageView image= view.findViewById(R.id.iv_image);
        image.setImageResource(drawables[position]);
        container.addView(mData.get(position));
        return mData.get(position);
    }
}
