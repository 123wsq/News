package com.yc.wsq.app.news.fragment.my;

import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadmoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.wsq.library.listener.OnRecyclerViewItemClickListener;
import com.wsq.library.tools.RecyclerViewDivider;
import com.wsq.library.utils.DensityUtil;
import com.yc.wsq.app.news.R;
import com.yc.wsq.app.news.adapter.ApprenticeAdapter;
import com.yc.wsq.app.news.adapter.BenefitAdapter;
import com.yc.wsq.app.news.base.BaseFragment;
import com.yc.wsq.app.news.constant.ResponseKey;
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
 * 徒弟列表
 */
public class ApprenticeFragment extends BaseFragment<UserView, UserPresenter<UserView>> implements UserView, RadioGroup.OnCheckedChangeListener {

    public static final String TAG = ApprenticeFragment.class.getName();

    @BindView(R.id.tv_title)TextView tv_title;

    @BindView(R.id.rg_vip_level) RadioGroup rg_vip_level;
    @BindView(R.id.rb_general_level) RadioButton rb_general_level;
    @BindView(R.id.refreshLayout) SmartRefreshLayout refreshLayout;
    @BindView(R.id.rv_RecyclerView) RecyclerView rv_RecyclerView;

    private int oldType = 1;
    private  int type = 1;
    private int refreshState = 0;
    private List<Map<String, Object>> mData;
    private ApprenticeAdapter mAdapter;


    @Override
    protected UserPresenter<UserView> createPresenter() {
        return new UserPresenter<>();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.layout_fragment_apprentice;
    }

    @Override
    protected void initView() {

        tv_title.setText(getString(R.string.str_apprentice_text));

        rg_vip_level.setOnCheckedChangeListener(this);

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
                getActivity(), LinearLayoutManager.HORIZONTAL, DensityUtil.dp2px(getActivity(), 1),
                ContextCompat.getColor(getActivity(), R.color.default_backgroud_color)));
        rv_RecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        rv_RecyclerView.setHasFixedSize(true);

        mAdapter = new ApprenticeAdapter(getActivity(), mData, listener);
        rv_RecyclerView.setAdapter(mAdapter);
        rb_general_level.setChecked(true);
//        onStartRequest();

    }

    private void onInitRefreshLayout(){

        refreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(RefreshLayout refreshlayout) {

                refreshState = 1;
                onGetApprenticeList();
            }
        });

        refreshLayout.setOnLoadmoreListener(new OnLoadmoreListener() {
            @Override
            public void onLoadmore(RefreshLayout refreshlayout) {

                refreshState = 2;
                onGetApprenticeList();
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
        public void onRecyclerItemClickListener(View view, int i) {

        }

        @Override
        public void onRecyclerItemLongClickListener(View view, int i) {

        }
    };

    /**
     * 获取徒弟列表
     */
    private void onGetApprenticeList(){
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
            param.put(ResponseKey.getInstace().type, type+"");
            ipresenter.onGetApprenticeList(param);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public void onResponseData(Map<String, Object> result) {
        List<Map<String, Object>> list = (List<Map<String, Object>>) result.get(ResponseKey.getInstace().data);


        if (oldType == type){
            if (refreshState ==1){
                mData.addAll(0, list);
            }else{
                mData.addAll(list);
            }
        }else{
            mData.clear();
            mData.addAll(list);
        }

        oldType = type;
        onResetRefreshState();
        mAdapter.notifyDataSetChanged();
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
        refreshState = 0;
        switch (checkedId){
            case R.id.rb_general_level:
                type = 1;
                onGetApprenticeList();
                break;
            case R.id.rb_vip_level:
                type = 2;
                onGetApprenticeList();
                break;
        }
    }
}
