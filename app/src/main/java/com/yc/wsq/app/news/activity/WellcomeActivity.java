package com.yc.wsq.app.news.activity;

import android.content.Intent;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.wsq.library.utils.DensityUtil;
import com.yc.wsq.app.news.R;
import com.yc.wsq.app.news.adapter.WellcomeAdapter;
import com.yc.wsq.app.news.base.BaseActivity;
import com.yc.wsq.app.news.constant.Constant;
import com.yc.wsq.app.news.mvp.presenter.BasePresenter;
import com.yc.wsq.app.news.tools.SharedTools;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

public class WellcomeActivity extends BaseActivity implements ViewPager.OnPageChangeListener {

    @BindView(R.id.viewPager)
    ViewPager viewPager;
    @BindView(R.id.layout_Indicator)
    LinearLayout layout_Indicator;
    @BindView(R.id.tv_start)
    TextView tv_start;

    private List<View> mDate;
    private WellcomeAdapter mAdapter;

    @Override
    protected BasePresenter createPresenter() {
        return null;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.layout_activity_wellcome;
    }

    @Override
    protected void initView() {
        getImages();
        drawIndicator();
        mAdapter = new WellcomeAdapter(this, mDate);
        viewPager.setAdapter(mAdapter);
        viewPager.addOnPageChangeListener(this);
    }

    public void getImages(){

        mDate=  new ArrayList<>();
        LayoutInflater inflater = LayoutInflater.from(this);

        for (int i = 0 ; i < 3; i++){

            mDate.add(inflater.inflate(R.layout.layout_activity_start, null));
        }

    }
    public  void drawIndicator(){

        for (int i = 0 ; i<mDate.size(); i++){
            ImageView imageView = new ImageView(this);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                    DensityUtil.dp2px(this, 5), DensityUtil.dp2px(this, 5));
            params.leftMargin = DensityUtil.dp2px(this, 10);
            imageView.setLayoutParams(params);

            imageView.setImageResource(i==0 ? R.mipmap.image_indicator_selector : R.mipmap.image_indicator_default);

            layout_Indicator.addView(imageView);
        }

    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {

        tv_start.setVisibility(position == mDate.size()-1 ? View.VISIBLE : View.GONE);
        for (int i = 0 ;  i< mDate.size() ; i ++) {
            ImageView view = (ImageView) layout_Indicator.getChildAt(i);
            view.setImageResource(i==position ? R.mipmap.image_indicator_selector :R.mipmap.image_indicator_default);
        }
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    @OnClick({R.id.tv_start})
    public void onClick(View view){
        switch (view.getId()){
            case R.id.tv_start:
                startActivity(new Intent(this, MainActivity.class));
                SharedTools.getInstance(WellcomeActivity.this).onPutData(Constant.getInstance().ISFIRST, true);
                finish();
                break;
        }
    }
}
