package com.yc.wsq.app.news.fragment.tab;

import android.support.v4.content.ContextCompat;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.orhanobut.logger.Logger;
import com.wsq.library.listener.OnRecyclerViewItemClickListener;
import com.wsq.library.tools.RecyclerViewDivider;
import com.wsq.library.utils.DensityUtil;
import com.yc.wsq.app.news.R;
import com.yc.wsq.app.news.adapter.GoodsAdapter;
import com.yc.wsq.app.news.adapter.TitleAdapter;
import com.yc.wsq.app.news.base.BaseFragment;
import com.yc.wsq.app.news.constant.ResponseKey;
import com.yc.wsq.app.news.loader.GlideImageLoader;
import com.yc.wsq.app.news.mvp.presenter.BasePresenter;
import com.yc.wsq.app.news.mvp.presenter.ShopPresenter;
import com.yc.wsq.app.news.mvp.view.ShopView;
import com.youth.banner.Banner;
import com.youth.banner.BannerConfig;
import com.youth.banner.Transformer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;

/**
 * 商城页面
 */
public class ShopFragment extends BaseFragment<ShopView, ShopPresenter<ShopView>> implements ShopView{

    @BindView(R.id.tv_title)TextView tv_title;
    @BindView(R.id.ll_back)LinearLayout ll_back;
    @BindView(R.id.banner)Banner banner;
    @BindView(R.id.rv_RecyclerView) RecyclerView rv_RecyclerView;

    private List<String> mTitles;
    private List<String> mImages;
    private List<Map<String, Object>> mData;

    private GoodsAdapter mAdapter;


    @Override
    protected ShopPresenter<ShopView> createPresenter() {
        return new ShopPresenter<>();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.layout_fragment_shop;
    }

    @Override
    protected void initView() {
        tv_title.setText(getResources().getString(R.string.str_shop_text));
        ll_back.setVisibility(View.GONE);

        onInitBanner();
        onInitRecyclerView();
    }

    private void onInitBanner(){
        mTitles = new ArrayList<>();
        mImages = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            mTitles.add("");

        }
        mImages.add("https://m.360buyimg.com/babel/jfs/t18706/351/1712024772/96472/d33c446e/5ad47d32N9cb85605.jpg");
        mImages.add("https://m.360buyimg.com/babel/jfs/t16675/185/1951384807/98912/a2fa59d/5adda9bbN794d907f.jpg");
        mImages.add("https://m.360buyimg.com/babel/jfs/t18706/351/1712024772/96472/d33c446e/5ad47d32N9cb85605.jpg");
        mImages.add("https://img1.360buyimg.com/da/jfs/t17248/356/1931644346/96206/4b354076/5ada10c6N86c3a8f9.jpg");
        mImages.add("https://m.360buyimg.com/babel/jfs/t19453/51/1747333993/73047/15a2c92f/5ad70fcaN1c1daaa3.jpg");

        //设置banner样式
        banner.setBannerStyle(BannerConfig.CIRCLE_INDICATOR);
        //设置图片加载器
        banner.setImageLoader(new GlideImageLoader());
        //设置图片集合
        banner.setImages(mImages);
        //设置banner动画效果
        banner.setBannerAnimation(Transformer.DepthPage);
        //设置标题集合（当banner样式有显示title时）
        banner.setBannerTitles(mTitles);
        //设置自动轮播，默认为true
        banner.isAutoPlay(true);
        //设置轮播时间
        banner.setDelayTime(3000);
        //设置指示器位置（当banner模式中有指示器时）
        banner.setIndicatorGravity(BannerConfig.CENTER);
        //banner设置方法全部调用完毕时最后调用
        banner.start();
    }

    private void onInitRecyclerView(){
        mData = new ArrayList<>();

        rv_RecyclerView.addItemDecoration(new RecyclerViewDivider(
                getActivity(), LinearLayoutManager.HORIZONTAL, DensityUtil.dp2px(getActivity(), 2),
                ContextCompat.getColor(getActivity(), R.color.default_backgroud_color)));
        rv_RecyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 2));
        rv_RecyclerView.setHasFixedSize(true);

        mAdapter = new GoodsAdapter(getActivity(), mData, listener);
        rv_RecyclerView.setAdapter(mAdapter);

        Map<String, String> param = new HashMap<>();
        try {
            ipresenter.onGetShopList(param);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    OnRecyclerViewItemClickListener listener = new OnRecyclerViewItemClickListener() {
        @Override
        public void onRecyclerItemClickListener(View view, int i) {

        }

        @Override
        public void onRecyclerItemLongClickListener(View view, int i) {

        }
    };

    @Override
    public void onShopResponse(Map<String, Object> result) {

        List<Map<String, Object>> list = (List<Map<String, Object>>) result.get(ResponseKey.getInstace().goods);
        mData.addAll(list);
        mAdapter.notifyDataSetChanged();
    }
}
