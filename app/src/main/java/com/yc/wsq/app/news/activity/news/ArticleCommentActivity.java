package com.yc.wsq.app.news.activity.news;

import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.orhanobut.logger.Logger;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadmoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.wsq.library.listener.OnRecyclerViewItemClickListener;
import com.wsq.library.tools.RecyclerViewDivider;
import com.wsq.library.tools.ToastUtils;
import com.wsq.library.utils.DensityUtil;
import com.yc.wsq.app.news.R;
import com.yc.wsq.app.news.adapter.ArtivleCommentAdapter;
import com.yc.wsq.app.news.base.BaseActivity;
import com.yc.wsq.app.news.base.BaseFragment;
import com.yc.wsq.app.news.constant.ResponseKey;
import com.yc.wsq.app.news.mvp.presenter.NewsPresenter;
import com.yc.wsq.app.news.mvp.view.NewsView;
import com.yc.wsq.app.news.tools.SharedTools;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;

public class ArticleCommentActivity extends BaseActivity<NewsView, NewsPresenter<NewsView>> implements NewsView{


    @BindView(R.id.tv_title)TextView tv_title;
    @BindView(R.id.refreshLayout)SmartRefreshLayout refreshLayout;
    @BindView(R.id.rv_RecyclerView) RecyclerView rv_RecyclerView;
    @BindView(R.id.tv_not_data) TextView tv_not_data;

    private int refreshState = 0;
    private List<Map<String, Object>> mData;
    private ArtivleCommentAdapter mAdapter;
    private String article_id;
    private int Selectposition = 0;

    @Override
    protected NewsPresenter<NewsView> createPresenter() {
        return new NewsPresenter<>();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.layout_fragment_article_comment;
    }

    @Override
    protected void initView() {

        tv_title.setText(getString(R.string.str_discuss_text));
        mData = new ArrayList<>();
        article_id = getIntent().getStringExtra(ResponseKey.getInstace().article_id);
        onInitRecyclerView();
        onInitRefreshLayout();
        onStartRequest();
    }

    /**
     * RecyclenView 的初始化
     */
    private void  onInitRecyclerView(){


        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);



        rv_RecyclerView.addItemDecoration(new RecyclerViewDivider(
                this, LinearLayoutManager.HORIZONTAL, DensityUtil.dp2px(this, 1),
                ContextCompat.getColor(this, R.color.default_backgroud_color)));
        rv_RecyclerView.setLayoutManager(new LinearLayoutManager(this));
        rv_RecyclerView.setHasFixedSize(true);

        mAdapter = new ArtivleCommentAdapter(this, mData, listener);
        rv_RecyclerView.setAdapter(mAdapter);



    }

    private void onInitRefreshLayout(){

        refreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(RefreshLayout refreshlayout) {

                refreshState = 1;
                onStartRequest();
            }
        });

        refreshLayout.setOnLoadmoreListener(new OnLoadmoreListener() {
            @Override
            public void onLoadmore(RefreshLayout refreshlayout) {

                refreshState = 2;
                onStartRequest();
            }
        });
    }

    public void onStartRequest(){

        Map<String, String> param = new HashMap<>();
        if (mData.size() !=0) {
            if (refreshState == 1) {
                param.put(ResponseKey.getInstace().max_id, mData.get(0).get(ResponseKey.getInstace().comment_id) + "");
            } else if (refreshState == 2) {
                param.put(ResponseKey.getInstace().min_id, mData.get(mData.size() - 1).get(ResponseKey.getInstace().comment_id) + "");
            }
        }
        try {
            param.put(ResponseKey.getInstace().article_id, article_id);
            param.put(ResponseKey.getInstace().user_id, SharedTools.getInstance(this).onGetString(ResponseKey.getInstace().user_id));
            ipresenter.onArticleCommentList(param);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @OnClick({R.id.ll_back})
    public void onClick(View view){
        switch (view.getId()){
            case R.id.ll_back:
                finish();
                break;
        }
    }

    OnRecyclerViewItemClickListener listener = new OnRecyclerViewItemClickListener() {
        @Override
        public void onRecyclerItemClickListener(View view, int i) {

            Selectposition = i;
            int is_zan = (int) mData.get(i).get(ResponseKey.getInstace().is_int);
            if (is_zan ==1){
                ToastUtils.onToast("已经点过赞了");
                return;
            }
            onAriclePraise(mData.get(i).get(ResponseKey.getInstace().comment_id)+"");

        }

        @Override
        public void onRecyclerItemLongClickListener(View view, int i) {

        }
    };

    private void onAriclePraise(String comment_id){
        Map<String, String> param = new HashMap<>();

        try {
            param.put(ResponseKey.getInstace().comment_id, comment_id);
            param.put(ResponseKey.getInstace().user_id, SharedTools.getInstance(this).onGetString(ResponseKey.getInstace().user_id));
            ipresenter.onArticlePraise(param);
        } catch (Exception e) {
            e.printStackTrace();
        }
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


    @Override
    public void onNewsResponse(Map<String, Object> result) {

        List<Map<String, Object>> list = (List<Map<String, Object>>) result.get(ResponseKey.getInstace().data);
        if (refreshState ==1){
            mData.addAll(0, list);
        }else{
            mData.addAll(list);
        }

        tv_not_data.setVisibility(mData.size() ==0 ? View.VISIBLE  : View.GONE);
        refreshLayout.setVisibility(mData.size() ==0 ? View.GONE : View.VISIBLE);
        onResetRefreshState();
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void onNewsTypeResponse(Map<String, Object> result) {

        String msg = (String) result.get(ResponseKey.getInstace().rsp_msg);
        ToastUtils.onToast(msg);
        try {
            Map<String, Object> map = mData.get(Selectposition);
            int zan_num = (int) map.get(ResponseKey.getInstace().zan_num);
            map.put(ResponseKey.getInstace().is_int, 1);
            map.put(ResponseKey.getInstace().zan_num, zan_num + 1 );
//
            mAdapter.notifyDataSetChanged();
        }catch (Exception e){
            Logger.wtf(e.getMessage());
            e.printStackTrace();
        }
    }
}
