package com.yc.wsq.app.news.fragment.news;

import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;

import com.orhanobut.logger.Logger;
import com.wsq.library.listener.OnRecyclerViewItemClickListener;
import com.wsq.library.tools.RecyclerViewDivider;
import com.wsq.library.tools.ToastUtils;
import com.wsq.library.utils.DensityUtil;
import com.yc.wsq.app.news.R;
import com.yc.wsq.app.news.adapter.BenefitAdapter;
import com.yc.wsq.app.news.adapter.HotSearchAdapter;
import com.yc.wsq.app.news.adapter.SearchRecordAdapter;
import com.yc.wsq.app.news.base.BaseFragment;
import com.yc.wsq.app.news.bean.SearchBean;
import com.yc.wsq.app.news.constant.ResponseKey;
import com.yc.wsq.app.news.fragment.my.IntegralFragment;
import com.yc.wsq.app.news.mvp.presenter.NewsPresenter;
import com.yc.wsq.app.news.mvp.view.NewsView;
import com.yc.wsq.app.news.mvp.view.SearchView;

import org.litepal.crud.DataSupport;
import org.litepal.crud.callback.SaveCallback;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;

public class SearchFragment extends BaseFragment<SearchView, NewsPresenter<SearchView>> implements SearchView{

    public static final String TAG = SearchFragment.class.getName();
    public static final String INTERFACE_WITHP = TAG +  _INTERFACE_WITHP;

    @BindView(R.id.et_search_content) EditText et_search_content;
    @BindView(R.id.rv_hot_search_RecyclerView) RecyclerView rv_hot_search_RecyclerView;
    @BindView(R.id.rv_search_record_RecyclerView) RecyclerView rv_search_record_RecyclerView;
    @BindView(R.id.tv_clear_search_record) TextView tv_clear_search_record;

    private HotSearchAdapter mHotAdapter;
    private SearchRecordAdapter mRecordAdapter;
    private List<String> mHotData;
    private List<SearchBean> mRecordData;

    @Override
    protected NewsPresenter<SearchView> createPresenter() {
        return new NewsPresenter<>();
    }



    @Override
    protected int getLayoutId() {
        return R.layout.layout_fragment_search;
    }

    @Override
    protected void initView() {

        onInitRecyclerView();
    }

    /**
     * RecyclenView 的初始化
     */
    private void  onInitRecyclerView(){

        mHotData = new ArrayList<>();
        mRecordData = new ArrayList<>();
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);



        rv_hot_search_RecyclerView.addItemDecoration(new RecyclerViewDivider(
                getActivity(), LinearLayoutManager.HORIZONTAL, DensityUtil.dp2px(getActivity(), 2),
                ContextCompat.getColor(getActivity(), R.color.default_backgroud_color)));
        rv_hot_search_RecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        rv_hot_search_RecyclerView.setHasFixedSize(true);


        rv_search_record_RecyclerView.addItemDecoration(new RecyclerViewDivider(
                getActivity(), LinearLayoutManager.HORIZONTAL, DensityUtil.dp2px(getActivity(), 2),
                ContextCompat.getColor(getActivity(), R.color.default_backgroud_color)));
        rv_search_record_RecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        rv_search_record_RecyclerView.setHasFixedSize(true);

        mHotAdapter = new HotSearchAdapter(getActivity(), mHotData, listener);
        rv_hot_search_RecyclerView.setAdapter(mHotAdapter);

        mRecordAdapter = new SearchRecordAdapter(getContext(), mRecordData, onSearchRecordListener);
        rv_search_record_RecyclerView.setAdapter(mRecordAdapter);

        Map<String, String> param = new HashMap<>();

        try {
            ipresenter.onHotSearchNews(param);
            ipresenter.onGetSearchRecord();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    OnRecyclerViewItemClickListener listener = new OnRecyclerViewItemClickListener() {
        @Override
        public void onRecyclerItemClickListener(View view, int i) {
            et_search_content.setText(mHotData.get(i));
            mFunctionsManage.invokeFunction(INTERFACE_WITHP, mHotData.get(i));
        }

        @Override
        public void onRecyclerItemLongClickListener(View view, int i) {

        }
    };
    OnRecyclerViewItemClickListener onSearchRecordListener = new OnRecyclerViewItemClickListener() {
        @Override
        public void onRecyclerItemClickListener(View view, int i) {

            if (view== null){
                et_search_content.setText(mRecordData.get(i).getKeywords());
                mFunctionsManage.invokeFunction(INTERFACE_WITHP, mRecordData.get(i).getKeywords());

            }else {
                try {
                    ipresenter.onRemoveNativiteSearchRecord(mRecordData.get(i).getId());
                    mRecordData.remove(i);
                    mRecordAdapter.notifyDataSetChanged();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }

        @Override
        public void onRecyclerItemLongClickListener(View view, int i) {

        }
    };


    @OnClick({R.id.ll_back, R.id.tv_clear_search_record, R.id.tv_search})
    public void onClick(View view){
        switch (view.getId()){
            case R.id.ll_back:
                mFunctionsManage.invokeFunction(INTERFACE_BACK);
                break;
            case R.id.tv_clear_search_record:
                try {
                    ipresenter.onRemoveAllNativiteSearchRecord();
                    mRecordData.clear();
                    mRecordAdapter.notifyDataSetChanged();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            case R.id.tv_search:
                String searchContent = et_search_content.getText().toString().trim();
                if (!TextUtils.isEmpty(searchContent)) {
                    onSaveSearchRecord();
                    mFunctionsManage.invokeFunction(INTERFACE_WITHP, et_search_content.getText().toString().trim());
                }else {
                    ToastUtils.onToast("请输入您要搜索的内容");
                }
                break;
        }
    }

    /**
     * 保存搜索记录
     */
    private void onSaveSearchRecord(){
        final SearchBean bean = new SearchBean(et_search_content.getText().toString().trim());
        List<SearchBean> list = DataSupport.where("keywords=?", bean.getKeywords()).find(SearchBean.class);
        if (list.size()>0)return;
        bean.saveAsync().listen(new SaveCallback() {
            @Override
            public void onFinish(boolean success) {
                if (success){
                    mRecordData.add(bean);
                    mRecordAdapter.notifyDataSetChanged();
                }
            }
        });
    }

    /**
     * 搜索
     */
//    private void onRequestSearch(){
//        Map<String, String> param = new HashMap<>();
//        param.put(ResponseKey.getInstace().keywords, et_search_content.getText().toString().trim());
//        try {
//            ipresenter.onSearchNews(param);
//        } catch (Exception e) {
//            ToastUtils.onToast(e.getMessage());
//            e.printStackTrace();
//        }
//    }

    @Override
    public void onLoadHotSearch(Map<String, Object> result) {
        mHotData.clear();
        List<String> list = (List<String>) result.get(ResponseKey.getInstace().data);
        mHotData.addAll(list);
        mHotAdapter.notifyDataSetChanged();
    }

    @Override
    public void onResponseSearch(Map<String, Object> result) {


    }

    @Override
    public void onLoadNativeSearchRecord(List<SearchBean> list) {

        mRecordData.clear();
        mRecordData.addAll(list);
        mRecordAdapter.notifyDataSetChanged();
    }


}
