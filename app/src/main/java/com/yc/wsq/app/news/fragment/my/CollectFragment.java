package com.yc.wsq.app.news.fragment.my;

import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadmoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.wsq.library.listener.OnRecyclerViewItemClickListener;
import com.wsq.library.tools.RecyclerViewDivider;
import com.wsq.library.utils.DensityUtil;
import com.yanzhenjie.recyclerview.swipe.SwipeItemClickListener;
import com.yanzhenjie.recyclerview.swipe.SwipeMenu;
import com.yanzhenjie.recyclerview.swipe.SwipeMenuBridge;
import com.yanzhenjie.recyclerview.swipe.SwipeMenuCreator;
import com.yanzhenjie.recyclerview.swipe.SwipeMenuItem;
import com.yanzhenjie.recyclerview.swipe.SwipeMenuItemClickListener;
import com.yanzhenjie.recyclerview.swipe.SwipeMenuRecyclerView;
import com.yc.wsq.app.news.R;
import com.yc.wsq.app.news.adapter.BenefitAdapter;
import com.yc.wsq.app.news.adapter.NewsAdapter;
import com.yc.wsq.app.news.base.BaseFragment;
import com.yc.wsq.app.news.constant.ResponseKey;
import com.yc.wsq.app.news.fragment.tab.HomeFragment;
import com.yc.wsq.app.news.mvp.presenter.NewsPresenter;
import com.yc.wsq.app.news.mvp.presenter.UserPresenter;
import com.yc.wsq.app.news.mvp.view.CollectView;
import com.yc.wsq.app.news.mvp.view.NewsView;
import com.yc.wsq.app.news.mvp.view.UserView;
import com.yc.wsq.app.news.tools.SharedTools;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 收藏页面
 */
public class CollectFragment extends BaseFragment<CollectView, NewsPresenter<CollectView>> implements CollectView{

    public static final String TAG = CollectFragment.class.getName();

    @BindView(R.id.tv_title) TextView tv_title;
    @BindView(R.id.refreshLayout) SmartRefreshLayout refreshLayout;
    @BindView(R.id.rv_RecyclerView) RecyclerView rv_RecyclerView;

    private int refreshState = 0;
    private List<Map<String, Object>> mData;
    private NewsAdapter mAdapter;


    @Override
    protected NewsPresenter<CollectView> createPresenter() {
        return new NewsPresenter<>();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.layout_fragment_collect;
    }

    @Override
    protected void initView() {

        tv_title.setText(getResources().getString(R.string.str_collect_text));



        onInitRecyclerView();
        onInitRefreshLayout();
    }

    /**
     * RecyclenView 的初始化
     */
    private void  onInitRecyclerView(){

        mData = new ArrayList<>();
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);

        rv_RecyclerView.addItemDecoration(new RecyclerViewDivider(
                getActivity(), LinearLayoutManager.HORIZONTAL, DensityUtil.dp2px(getActivity(), 2),
                ContextCompat.getColor(getActivity(), R.color.default_backgroud_color)));
        rv_RecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        rv_RecyclerView.setHasFixedSize(true);

        mAdapter = new NewsAdapter(getActivity(), mData, listener, 2);
        rv_RecyclerView.setAdapter(mAdapter);

        onStartRequest();

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

    @OnClick({R.id.ll_back})
    public void onClick(View view){
        switch (view.getId()){
            case R.id.ll_back:
                mFunctionsManage.invokeFunction(INTERFACE_BACK);
                break;
        }
    }

    OnRecyclerViewItemClickListener listener = new OnRecyclerViewItemClickListener() {
        @Override
        public void onRecyclerItemClickListener(View view, int position) {

            mFunctionsManage.invokeFunction(HomeFragment.INTERFACE_WITHPS,
                    mData.get(position).get(ResponseKey.getInstace().article_id)+"",
                    mData.get(position).get(ResponseKey.getInstace().title)+"");
        }

        @Override
        public void onRecyclerItemLongClickListener(View view, int i) {

        }
    };


    public void onStartRequest(){

        Map<String, String> param = new HashMap<>();
        if (mData.size() !=0) {
            if (refreshState == 1) {
                param.put(ResponseKey.getInstace().max_id, mData.get(0).get(ResponseKey.getInstace().id) + "");
            } else if (refreshState == 2) {
                param.put(ResponseKey.getInstace().min_id, mData.get(mData.size() - 1).get(ResponseKey.getInstace().id) + "");
            }
        }
        try {
            param.put(ResponseKey.getInstace().uid, SharedTools.getInstance(getActivity()).onGetString(ResponseKey.getInstace().user_id));
            param.put(ResponseKey.getInstace().token, SharedTools.getInstance(getActivity()).onGetString(ResponseKey.getInstace().token));
            ipresenter.onGetCollectList(param);
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
    public void onCollectListResponse(Map<String, Object> result) {
        List<Map<String, Object>> list = (List<Map<String, Object>>) result.get(ResponseKey.getInstace().data);
        if (refreshState ==1){
            mData.addAll(0, list);
        }else{
            mData.addAll(list);
        }
        onResetRefreshState();
        mAdapter.notifyDataSetChanged();
    }


}
