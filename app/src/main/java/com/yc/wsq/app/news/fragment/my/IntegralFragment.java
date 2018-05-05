package com.yc.wsq.app.news.fragment.my;

import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
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
import com.yc.wsq.app.news.activity.LoginActivity;
import com.yc.wsq.app.news.adapter.IntegralRecodeAdapter;
import com.yc.wsq.app.news.adapter.NewsAdapter;
import com.yc.wsq.app.news.base.BaseFragment;
import com.yc.wsq.app.news.constant.ResponseKey;
import com.yc.wsq.app.news.fragment.tab.MyFragment;
import com.yc.wsq.app.news.mvp.presenter.UserPresenter;
import com.yc.wsq.app.news.mvp.view.UserView;
import com.yc.wsq.app.news.tools.SharedTools;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 积分页面
 */
public class IntegralFragment extends BaseFragment<UserView, UserPresenter<UserView>> implements UserView, RadioGroup.OnCheckedChangeListener {

    public static final String TAG = IntegralFragment.class.getName();

    @BindView(R.id.tv_title) TextView tv_title;
    @BindView(R.id.rv_RecyclerView) RecyclerView rv_RecyclerView;
    @BindView(R.id.refreshLayout) SmartRefreshLayout refreshLayout;
    @BindView(R.id.ll_integral_rule) LinearLayout ll_integral_rule;
    @BindView(R.id.rg_group) RadioGroup rg_group;
    @BindView(R.id.rb_record) RadioButton rb_record;
    @BindView(R.id.tv_total_integral) TextView tv_total_integral;

    private IntegralRecodeAdapter mAdapter;
    private List<Map<String, Object>> mData;
    private int refreshState = 0;

    @Override
    protected UserPresenter<UserView> createPresenter() {
        return new UserPresenter<>();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.layout_fragment_integral;
    }

    @Override
    protected void initView() {

        tv_title.setText(getResources().getString(R.string.str_my_integral_text));
        rg_group.setOnCheckedChangeListener(this);
        rb_record.setChecked(true);

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



        mAdapter = new IntegralRecodeAdapter(getActivity(), mData, listener);
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

                refreshState =2;
                onStartRequest();
            }
        });
    }
    OnRecyclerViewItemClickListener listener = new OnRecyclerViewItemClickListener() {
        @Override
        public void onRecyclerItemClickListener(View view, int i) {

        }

        @Override
        public void onRecyclerItemLongClickListener(View view, int i) {

        }
    };

    private void onStartRequest(){

        Map<String, String> param = new HashMap<>();
        param.put(ResponseKey.getInstace().uid, SharedTools.getInstance(getActivity()).onGetString(ResponseKey.getInstace().user_id));
        param.put(ResponseKey.getInstace().token, SharedTools.getInstance(getActivity()).onGetString(ResponseKey.getInstace().token));
        if (refreshState ==1){
            param.put(ResponseKey.getInstace().max_id, mData.get(0).get(ResponseKey.getInstace().log_id)+"");
        }else if(refreshState == 2){
            param.put(ResponseKey.getInstace().min_id, mData.get(mData.size() -1).get(ResponseKey.getInstace().log_id)+"");
        }
        try {
            ipresenter.onGetIntegralRecord(param);
        } catch (Exception e) {


            e.printStackTrace();
        }
    }
    @OnClick({R.id.ll_back})
    public void onClick(View view){
        switch (view.getId()){
            case R.id.ll_back:
                mFunctionsManage.invokeFunction(INTERFACE_BACK);
                break;
        }
    }
    @Override
    public void onResponseData(Map<String, Object> result) {
        String points = (String) result.get(ResponseKey.getInstace().points);
        tv_total_integral.setText(points);

        List<Map<String, Object>> list = (List<Map<String, Object>>) result.get(ResponseKey.getInstace().data);
        if (refreshState ==1){
            mData.addAll(0, list);
        }else{
            mData.addAll(list);
        }

        mAdapter.notifyDataSetChanged();
        onResetRefreshState();
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
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        switch (checkedId){
            case R.id.rb_record:
                refreshLayout.setVisibility(View.VISIBLE);
                ll_integral_rule.setVisibility(View.GONE);
                break;
            case R.id.rb_rule:
                refreshLayout.setVisibility(View.GONE);
                ll_integral_rule.setVisibility(View.VISIBLE);
                break;
        }
    }
}
