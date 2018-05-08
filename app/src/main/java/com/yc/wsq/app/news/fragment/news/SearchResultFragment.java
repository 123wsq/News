package com.yc.wsq.app.news.fragment.news;

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
import com.yc.wsq.app.news.adapter.HotSearchAdapter;
import com.yc.wsq.app.news.adapter.NewsAdapter;
import com.yc.wsq.app.news.adapter.SearchRecordAdapter;
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

public class SearchResultFragment extends BaseFragment<NewsView, NewsPresenter<NewsView>> implements NewsView{

    public static final String TAG = SearchResultFragment.class.getName();

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
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);



        rv_RecyclerView.addItemDecoration(new RecyclerViewDivider(
                getActivity(), LinearLayoutManager.HORIZONTAL, DensityUtil.dp2px(getActivity(), 2),
                ContextCompat.getColor(getActivity(), R.color.default_backgroud_color)));
        rv_RecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        rv_RecyclerView.setHasFixedSize(true);



        mAdapter = new NewsAdapter(getActivity(), mData, listener, 1);
        rv_RecyclerView.setAdapter(mAdapter);


        Map<String, String> param = new HashMap<>();
        try {
            Logger.d("参数 ： "+getArguments().getString(ResponseKey.getInstace().keywords));
            param.put(ResponseKey.getInstace().keywords, getArguments().getString(ResponseKey.getInstace().keywords));
            ipresenter.onSearchNews(param);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
    OnRecyclerViewItemClickListener listener = new OnRecyclerViewItemClickListener() {
        @Override
        public void onRecyclerItemClickListener(View view, int i) {
            mFunctionsManage.invokeFunction(HomeFragment.INTERFACE_WITHPS,
                    mData.get(i).get(ResponseKey.getInstace().article_id)+"",
                    mData.get(i).get(ResponseKey.getInstace().title)+"");
        }

        @Override
        public void onRecyclerItemLongClickListener(View view, int i) {

        }
    };

    @OnClick({R.id.ll_back})
    public void onClick(View view){
        switch (view.getId()){
            case R.id.ll_back:
                mFunctionsManage.invokeFunction(INTERFACE_BACK);
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
