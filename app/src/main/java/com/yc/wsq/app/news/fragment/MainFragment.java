package com.yc.wsq.app.news.fragment;

import android.content.Context;
import android.graphics.Color;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.yc.wsq.app.news.R;
import com.yc.wsq.app.news.base.BaseFragment;
import com.yc.wsq.app.news.fragment.tab.BenefitFragment;
import com.yc.wsq.app.news.fragment.tab.HomeFragment;
import com.yc.wsq.app.news.fragment.tab.MyFragment;
import com.yc.wsq.app.news.fragment.tab.ShopFragment;
import com.yc.wsq.app.news.mvp.presenter.BasePresenter;
import com.yc.wsq.app.news.views.CustomViewPager;

import net.lucode.hackware.magicindicator.MagicIndicator;
import net.lucode.hackware.magicindicator.ViewPagerHelper;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.CommonNavigator;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.CommonNavigatorAdapter;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.IPagerIndicator;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.IPagerTitleView;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.titles.CommonPagerTitleView;

import butterknife.BindView;

public class MainFragment extends BaseFragment implements ViewPager.OnPageChangeListener, View.OnTouchListener {

    public static final String TAG = MainFragment.class.getName();
    public static final String INTERFACE = TAG ;

    @BindView(R.id.vp_ViewPager) CustomViewPager vp_ViewPager;
    @BindView(R.id.magic_indicator) MagicIndicator magic_indicator;


    private String[] titles = new String[]{"首页", "商城", "公益", "我的"};
    private Integer[] iconDefault = {R.mipmap.image_my_home_default, R.mipmap.image_shop_default, R.mipmap.image_benefit_default, R.mipmap.image_my_default};
    private Integer[] iconSelector = {R.mipmap.image_my_home_selected, R.mipmap.image_shop_selector, R.mipmap.image_benefit_selector, R.mipmap.image_my_selector};
    private Fragment[] fragments = new Fragment[titles.length];

    private MyAdapter adapter;
    private  CommonNavigator commonNavigator;

    private int curPage = 0;

    @Override
    protected BasePresenter createPresenter() {
        return null;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.layout_fragment_main;
    }

    @Override
    protected void initView() {


        fragments[0] = new HomeFragment();
        fragments[1] = new ShopFragment();
        fragments[2] = new BenefitFragment();
        fragments[3] = new MyFragment();
        initMagicIndicator();
        adapter = new MyAdapter(getActivity().getSupportFragmentManager());
        vp_ViewPager.setAdapter(adapter);
        vp_ViewPager.addOnPageChangeListener(this);
        vp_ViewPager.setOnTouchListener(this);
        vp_ViewPager.setCurrentItem(0);

    }

    private void initMagicIndicator() {
        magic_indicator.setBackgroundColor(Color.WHITE);
        commonNavigator = new CommonNavigator(getActivity());
        commonNavigator.setAdjustMode(true);  //ture 即标题平分屏幕宽度的模式
        commonNavigator.setAdapter(new CommonNavigatorAdapter() {
            @Override
            public int getCount() {
                return titles == null ? 0 : titles.length;
            }

            @Override
            public IPagerTitleView getTitleView(Context context, final int index) {
//                SimplePagerTitleView simplePagerTitleView = new SimplePagerTitleView(context);
//                simplePagerTitleView.setText(titles[index]);
//                simplePagerTitleView.setTextSize(18);
//                simplePagerTitleView.setNormalColor(Color.GRAY);
//                simplePagerTitleView.setSelectedColor(Color.BLACK);
//                simplePagerTitleView.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        vp_ViewPager.setCurrentItem(index);
//                    }
//                });
//
//
//                return simplePagerTitleView;

                CommonPagerTitleView commonPagerTitleView = new CommonPagerTitleView(context);
                commonPagerTitleView.setContentView(R.layout.layout_item_tab);
                // 初始化
                final ImageView titleImg = (ImageView) commonPagerTitleView.findViewById(R.id.tv_tab_image);
                titleImg.setImageResource(iconDefault[index]);
                final TextView titleText = (TextView) commonPagerTitleView.findViewById(R.id.tv_tab_name);
                titleText.setText(titles[index]);
                commonPagerTitleView.setOnPagerTitleChangeListener(new CommonPagerTitleView.OnPagerTitleChangeListener() {
                    @Override
                    public void onSelected(int i, int i1) {
                        titleText.setTextColor(Color.RED);

                        titleImg.setImageResource(iconSelector[i]);
//                        Logger.d("onSelected=  "+i+"          "+i1);
                    }

                    @Override
                    public void onDeselected(int i, int i1) {
                        titleText.setTextColor(Color.BLACK);
                        titleImg.setImageResource(iconDefault[i]);
//                        Logger.d("onDeselected=  "+i+"          "+i1);
                    }

                    @Override
                    public void onLeave(int i, int i1, float leavePercent, boolean b) {
//                        titleImg.setScaleX(1.3f + (0.8f - 1.3f) * leavePercent);
//                        titleImg.setScaleY(1.3f + (0.8f - 1.3f) * leavePercent);
                    }

                    @Override
                    public void onEnter(int i, int i1, float enterPercent, boolean b) {
//                        titleImg.setScaleX(0.8f + (1.3f - 0.8f) * enterPercent);
//                        titleImg.setScaleY(0.8f + (1.3f - 0.8f) * enterPercent);
                    }
                });

                commonPagerTitleView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        vp_ViewPager.setCurrentItem(index);
                    }
                });

                return commonPagerTitleView;
            }

            @Override
            public IPagerIndicator getIndicator(Context context) {
                //圆点
//                BezierPagerIndicator indicator = new BezierPagerIndicator(context);
//                indicator.setColors(Color.parseColor("#ff4a42"), Color.parseColor("#fcde64"), Color.parseColor("#73e8f4"), Color.parseColor("#76b0ff"), Color.parseColor("#c683fe"));

                //线条
//                LinePagerIndicator indicator = new LinePagerIndicator(context);
//                indicator.setColors(Color.parseColor("#ff4a42"));
//                indicator.setMode(LinePagerIndicator.MODE_MATCH_EDGE);
//                return indicator;
                return null;
            }
        });
        magic_indicator.setNavigator(commonNavigator);
        magic_indicator.setBottom(0);
        ViewPagerHelper.bind(magic_indicator, vp_ViewPager);
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {

        curPage = position;
        if (position ==1){
            magic_indicator.setVisibility(View.GONE);
            vp_ViewPager.setScanScroll(false);
        }else {
            magic_indicator.setVisibility(View.VISIBLE);
            vp_ViewPager.setScanScroll(true);
        }
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        if (curPage==1){
            return true;
        }
        return false;
    }

    private class MyAdapter extends FragmentPagerAdapter {

        public MyAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return fragments[position];
        }

        @Override
        public int getCount() {
            return titles.length;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return titles[position];
        }
    }

    /**
     * 用户状态发生改变
     * 作用于下级页面中
     */
    public void onUserStatusChange(){

        MyFragment fragment = (MyFragment) fragments[3];
        fragment.onUserStatusChangeListener();
    }

    /**
     * 设置显示位置
     */
    public void onSetShowPosition(){
        vp_ViewPager.setCurrentItem(0);
    }

}
