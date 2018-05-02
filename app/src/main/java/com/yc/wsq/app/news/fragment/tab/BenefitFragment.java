package com.yc.wsq.app.news.fragment.tab;

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
import com.wsq.library.tools.ToastUtils;
import com.wsq.library.utils.DensityUtil;
import com.yc.wsq.app.news.R;
import com.yc.wsq.app.news.adapter.BenefitAdapter;
import com.yc.wsq.app.news.adapter.NewsAdapter;
import com.yc.wsq.app.news.adapter.TitleAdapter;
import com.yc.wsq.app.news.base.BaseFragment;
import com.yc.wsq.app.news.constant.ResponseKey;
import com.yc.wsq.app.news.mvp.presenter.BasePresenter;
import com.yc.wsq.app.news.mvp.presenter.BenefitPresenter;
import com.yc.wsq.app.news.mvp.presenter.UserPresenter;
import com.yc.wsq.app.news.mvp.view.BenefitView;
import com.yc.wsq.app.news.mvp.view.UserView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;

/**
 * 公益页面
 */
public class BenefitFragment extends BaseFragment<BenefitView, BenefitPresenter<BenefitView>> implements BenefitView{

    public static final String TAG = BenefitFragment.class.getName();
    public static final String INTERFACE_WITHP = TAG + _INTERFACE_WITHP;

    @BindView(R.id.tv_title)TextView tv_title;
    @BindView(R.id.ll_back)LinearLayout ll_back;
    @BindView(R.id.refreshLayout)SmartRefreshLayout refreshLayout;
    @BindView(R.id.rv_RecyclerView) RecyclerView rv_RecyclerView;

    private List<Map<String, Object>> mData;
    private int refreshState = 0;
    private BenefitAdapter mAdapter;

    @Override
    protected BenefitPresenter<BenefitView> createPresenter() {
        return new BenefitPresenter<>();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.layout_fragment_benefit;
    }

    @Override
    protected void initView() {
        tv_title.setText(getResources().getString(R.string.str_benefit_text));
        ll_back.setVisibility(View.GONE);

        mData = new ArrayList<>();

        onInitRecyclerView();
        onInitRefreshLayout();
    }


    /**
     * RecyclenView 的初始化
     */
    private void  onInitRecyclerView(){


        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);



        rv_RecyclerView.addItemDecoration(new RecyclerViewDivider(
                getActivity(), LinearLayoutManager.HORIZONTAL, DensityUtil.dp2px(getActivity(), 2),
                ContextCompat.getColor(getActivity(), R.color.default_backgroud_color)));
        rv_RecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        rv_RecyclerView.setHasFixedSize(true);

        mAdapter = new BenefitAdapter(getActivity(), mData, listener);
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

    OnRecyclerViewItemClickListener listener = new OnRecyclerViewItemClickListener() {
        @Override
        public void onRecyclerItemClickListener(View view, int i) {

            mFunctionsManage.invokeFunction(INTERFACE_WITHP, mData.get(i).get(ResponseKey.getInstace().id)+"");
        }

        @Override
        public void onRecyclerItemLongClickListener(View view, int i) {

        }
    };

    @Override
    public void onBenefitResponse(Map<String, Object> result) {

        int status = (int) result.get(ResponseKey.getInstace().rsp_status);

        if (status == -1){
            ToastUtils.onToast(result.get(ResponseKey.getInstace().rsp_msg)+"");
        }else {
            List<Map<String, Object>> list = (List<Map<String, Object>>) result.get(ResponseKey.getInstace().data);
            if (refreshState ==1){
                mData.addAll(0, list);
            }else{
                mData.addAll(list);
            }
        }
       onResetRefreshState();
        mAdapter.notifyDataSetChanged();
    }

    public void onStartRequest(){

        Map<String, String> param = new HashMap<>();
        if (refreshState ==1){
            param.put(ResponseKey.getInstace().max_id, mData.get(0).get(ResponseKey.getInstace().log_id)+"");
        }else if(refreshState == 2){
            param.put(ResponseKey.getInstace().min_id, mData.get(mData.size() -1).get(ResponseKey.getInstace().log_id)+"");
        }
        try {
            ipresenter.onGetBenefitList(param);
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
}
