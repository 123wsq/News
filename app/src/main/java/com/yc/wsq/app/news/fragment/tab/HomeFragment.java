package com.yc.wsq.app.news.fragment.tab;

import android.content.Intent;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadmoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.wsq.library.listener.OnRecyclerViewItemClickListener;
import com.wsq.library.tools.RecyclerViewDivider;
import com.wsq.library.tools.ToastUtils;
import com.wsq.library.utils.DensityUtil;
import com.yc.wsq.app.news.R;
import com.yc.wsq.app.news.activity.QRcodeScanActivity;
import com.yc.wsq.app.news.adapter.NewsAdapter;
import com.yc.wsq.app.news.adapter.TitleAdapter;
import com.yc.wsq.app.news.base.BaseFragment;
import com.yc.wsq.app.news.constant.ResponseKey;
import com.yc.wsq.app.news.mvp.presenter.NewsPresenter;
import com.yc.wsq.app.news.mvp.view.NewsView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 主页面
 */
public class HomeFragment extends BaseFragment<NewsView, NewsPresenter<NewsView>> implements NewsView{

    public static final String TAG =  HomeFragment.class.getName();
    public static final String INTERFACE_WITHP = TAG+ _INTERFACE_WITHP;
    public static final String INTERFACE_WITHPS = TAG+ _INTERFACE_WITHP+"_STRING";

    @BindView(R.id.rv_RecyclerView_title)
    RecyclerView rv_RecyclerView_title;
    @BindView(R.id.rv_RecyclerView_content) RecyclerView rv_RecyclerView_content;
    @BindView(R.id.refreshLayout) SmartRefreshLayout refreshLayout;
    @BindView(R.id.tv_not_data) TextView tv_not_data;

    private TitleAdapter mTitleAdapter;
    private List<Map<String, Object>> mTitleData;
    private NewsAdapter mNewsAdapter;
    private List<Map<String, Object>> mNewsData;
    private int refreshState = 0;  //0 正常  1 刷新  2 加载
    private String cat_id="";

    @Override
    protected NewsPresenter<NewsView> createPresenter() {
        return new NewsPresenter<>();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.layout_fragment_home;
    }

    @Override
    protected void initView() {
        mTitleData = new ArrayList<>();
        mNewsData = new ArrayList<>();

        onInitRecyclerView();
        onInitRefreshLayout();

        onStartRequest();

    }

    private void onStartRequest(){
        Map<String, String> param = new HashMap<>();
        if (refreshState == 1){  //刷新

            param.put(ResponseKey.getInstace().max_id, mNewsData.get(0).get(ResponseKey.getInstace().article_id)+"");
        }else if(refreshState == 2){ //更多
            param.put(ResponseKey.getInstace().min_id, mNewsData.get(mNewsData.size()-1).get(ResponseKey.getInstace().article_id)+"");
        }
            param.put(ResponseKey.getInstace().cat_id, cat_id);
        try {
            ipresenter.onGetNewsList(param);
            ipresenter.onGetNewsType(param);
        } catch (Exception e) {
            ToastUtils.onToast(e.getMessage());
            onResetRefreshState();
            e.printStackTrace();
        }
    }

    @Override
    public void onNewsResponse(Map<String, Object> result) {

        List<Map<String, Object>> newsData = (List<Map<String, Object>>) result.get(ResponseKey.getInstace().data);
        if (refreshState ==1) {
            mNewsData.addAll(0, newsData);
        }else {
            mNewsData.addAll(newsData);
        }
        mNewsAdapter.notifyDataSetChanged();
        tv_not_data.setVisibility(mNewsData.size()==0 ? View.VISIBLE : View.GONE);

        onResetRefreshState();
    }

    @Override
    public void onNewsTypeResponse(Map<String, Object> result) {

        if (mTitleData.size() != 0){
            mTitleData.clear();
        }
        List<Map<String, Object>> list = (List<Map<String, Object>>) result.get(ResponseKey.getInstace().result);
        mTitleData.addAll(list);
        mTitleAdapter.notifyDataSetChanged();
    }

    /**
     * 重置刷新状态
     */
    private void onResetRefreshState(){
        if (refreshState ==1){
            refreshLayout.finishRefresh();
        }else if(refreshState ==2){
            refreshLayout.finishLoadmore();
        }
        refreshState =0;
    }


    /**
     * RecyclenView 的初始化
     */
    private void  onInitRecyclerView(){


        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        rv_RecyclerView_title.setLayoutManager(layoutManager);
        rv_RecyclerView_title.setHasFixedSize(true);

        mTitleAdapter = new TitleAdapter(getActivity(), mTitleData, onTitleItemListener);
        rv_RecyclerView_title.setAdapter(mTitleAdapter);


        rv_RecyclerView_content.addItemDecoration(new RecyclerViewDivider(
        getActivity(), LinearLayoutManager.HORIZONTAL, DensityUtil.dp2px(getActivity(), 1),
        ContextCompat.getColor(getActivity(), R.color.default_backgroud_color)));
        rv_RecyclerView_content.setLayoutManager(new LinearLayoutManager(getActivity()));
        rv_RecyclerView_content.setHasFixedSize(true);

        mNewsAdapter = new NewsAdapter(getActivity(), mNewsData, onNewsItemListener);
        rv_RecyclerView_content.setAdapter(mNewsAdapter);

    }
    private void onInitRefreshLayout(){

        refreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(RefreshLayout refreshlayout) {

                refreshState = 1;
                onStartRequest();
//                ToastUtils.onToast("开始刷新数据");
            }
        });

        refreshLayout.setOnLoadmoreListener(new OnLoadmoreListener() {
            @Override
            public void onLoadmore(RefreshLayout refreshlayout) {

                refreshState =2;
                onStartRequest();
//                ToastUtils.onToast("开始加载更多");
            }
        });
    }

    OnRecyclerViewItemClickListener onTitleItemListener = new OnRecyclerViewItemClickListener() {
        @Override
        public void onRecyclerItemClickListener(View view, int i) {

            cat_id = mTitleData.get(i).get(ResponseKey.getInstace().cat_id)+"";
            refreshState = 3;
            mNewsData.clear();
            onStartRequest();
//            ToastUtils.onToast(mTitleData.get(i).);

        }

        @Override
        public void onRecyclerItemLongClickListener(View view, int i) {

        }
    };

    OnRecyclerViewItemClickListener onNewsItemListener = new OnRecyclerViewItemClickListener() {
        @Override
        public void onRecyclerItemClickListener(View view, int position) {

            mFunctionsManage.invokeFunction(INTERFACE_WITHPS, mNewsData.get(position).get(ResponseKey.getInstace().article_id)+"",
                    mNewsData.get(position).get(ResponseKey.getInstace().title)+"");
        }

        @Override
        public void onRecyclerItemLongClickListener(View view, int i) {

        }
    };

    @OnClick({R.id.ll_search, R.id.iv_qcode_scan})
    public void onClick(View view){
        switch (view.getId()){
            case R.id.ll_search: //搜索 param= -1
                mFunctionsManage.invokeFunction(INTERFACE_WITHP, -1);
                break;
            case R.id.iv_qcode_scan:

                startActivity(new Intent(getActivity(), QRcodeScanActivity.class));
                break;
        }
    }
}
