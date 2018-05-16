package com.yc.wsq.app.news.activity.news;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.orhanobut.logger.Logger;
import com.wsq.library.listener.OnRecyclerViewItemClickListener;
import com.wsq.library.tools.RecyclerViewDivider;
import com.wsq.library.utils.DensityUtil;
import com.yc.wsq.app.news.R;
import com.yc.wsq.app.news.adapter.NewsAdapter;
import com.yc.wsq.app.news.base.BaseActivity;
import com.yc.wsq.app.news.base.BaseFragment;
import com.yc.wsq.app.news.constant.ResponseKey;
import com.yc.wsq.app.news.fragment.tab.HomeFragment;
import com.yc.wsq.app.news.mvp.presenter.NewsPresenter;
import com.yc.wsq.app.news.mvp.view.NewsView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;

public class SearchResultActivity extends BaseActivity<NewsView, NewsPresenter<NewsView>> implements NewsView{

    public static final String TAG = SearchResultActivity.class.getName();

    @BindView(R.id.tv_title)
    TextView tv_title;
    @BindView(R.id.rv_RecyclerView)
    RecyclerView rv_RecyclerView;
    @BindView(R.id.tv_search_no_data) TextView tv_search_no_data;

    private NewsAdapter mAdapter;
    private List<Map<String, Object>> mData;


    @Override
    protected NewsPresenter<NewsView> createPresenter() {
        return new NewsPresenter<>();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.layout_fragment_search_result;
    }

    @Override
    protected void initView() {

        tv_title.setText(getResources().getString(R.string.str_search_result_text));
        onInitRecyclerView();
    }

    /**
     * RecyclenView 的初始化
     */
    private void  onInitRecyclerView(){

        mData = new ArrayList<>();
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);



        rv_RecyclerView.addItemDecoration(new RecyclerViewDivider(
                this, LinearLayoutManager.HORIZONTAL, DensityUtil.dp2px(this, 2),
                ContextCompat.getColor(this, R.color.default_backgroud_color)));
        rv_RecyclerView.setLayoutManager(new LinearLayoutManager(this));
        rv_RecyclerView.setHasFixedSize(true);



        mAdapter = new NewsAdapter(this, mData, listener, 1);
        rv_RecyclerView.setAdapter(mAdapter);


        Map<String, String> param = new HashMap<>();
        try {
            Logger.d("参数 ： "+getIntent().getStringExtra(ResponseKey.getInstace().keywords));
            param.put(ResponseKey.getInstace().keywords, getIntent().getStringExtra(ResponseKey.getInstace().keywords));
            ipresenter.onSearchNews(param);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
    OnRecyclerViewItemClickListener listener = new OnRecyclerViewItemClickListener() {
        @Override
        public void onRecyclerItemClickListener(View view, int i) {
            Intent intent = new Intent(SearchResultActivity.this, NewsDetailsActivity.class);
            intent.putExtra(ResponseKey.getInstace().article_id, mData.get(i).get(ResponseKey.getInstace().article_id)+"");
            intent.putExtra(ResponseKey.getInstace().title, mData.get(i).get(ResponseKey.getInstace().title)+"");
            startActivity(intent);
        }

        @Override
        public void onRecyclerItemLongClickListener(View view, int i) {

        }
    };

    @OnClick({R.id.ll_back})
    public void onClick(View view){
        switch (view.getId()){
            case R.id.ll_back:
                finish();
                break;
        }
    }

    @Override
    public void onNewsResponse(Map<String, Object> result) {

        List<Map<String, Object>> newsData = (List<Map<String, Object>>) result.get(ResponseKey.getInstace().data);
        mData.addAll(newsData);
        mAdapter.notifyDataSetChanged();
        if (mData.size() == 0 ){
            rv_RecyclerView.setVisibility(View.GONE);
            tv_search_no_data.setVisibility(View.VISIBLE);
        }else{
            rv_RecyclerView.setVisibility(View.VISIBLE);
            tv_search_no_data.setVisibility(View.GONE);
        }
    }

    @Override
    public void onNewsTypeResponse(Map<String, Object> result) {

    }
}
