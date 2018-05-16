package com.yc.wsq.app.news.fragment.tab;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.orhanobut.logger.Logger;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadmoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.wsq.library.listener.OnRecyclerViewItemClickListener;
import com.wsq.library.tools.RecyclerViewDivider;
import com.wsq.library.tools.ToastUtils;
import com.wsq.library.utils.DateUtil;
import com.wsq.library.utils.DensityUtil;
import com.yc.wsq.app.news.R;
import com.yc.wsq.app.news.activity.benefit.BenefitDetailsActivity;
import com.yc.wsq.app.news.adapter.BenefitAdapter;
import com.yc.wsq.app.news.adapter.BenefitAmountAdapter;
import com.yc.wsq.app.news.adapter.NewsAdapter;
import com.yc.wsq.app.news.adapter.TitleAdapter;
import com.yc.wsq.app.news.base.BaseFragment;
import com.yc.wsq.app.news.bean.AmountBean;
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


    @BindView(R.id.tv_title)TextView tv_title;
    @BindView(R.id.ll_back)LinearLayout ll_back;
    @BindView(R.id.refreshLayout)SmartRefreshLayout refreshLayout;
    @BindView(R.id.rv_RecyclerView) RecyclerView rv_RecyclerView;
    @BindView(R.id.tv_cur_time) TextView tv_cur_time;
    @BindView(R.id.rv_RecyclerView_title)RecyclerView rv_RecyclerView_title;

    private List<Map<String, Object>> mData;
    private int refreshState = 0;
    private BenefitAdapter mAdapter;
    private List<AmountBean> mAmountData;
    private BenefitAmountAdapter mAmountAdapter;

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
        mAmountData = new ArrayList<>();

        onInitRecyclerView();
        onInitRefreshLayout();


    }


    /**
     * RecyclenView 的初始化
     */
    private void  onInitRecyclerView(){


        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);

        rv_RecyclerView_title.setLayoutManager(layoutManager);
        rv_RecyclerView_title.setHasFixedSize(true);


        mAmountAdapter = new BenefitAmountAdapter(getActivity(), mAmountData);
        rv_RecyclerView_title.setAdapter(mAmountAdapter);


        rv_RecyclerView.addItemDecoration(new RecyclerViewDivider(
                getActivity(), LinearLayoutManager.HORIZONTAL, DensityUtil.dp2px(getActivity(), 2),
                ContextCompat.getColor(getActivity(), R.color.default_backgroud_color)));
        rv_RecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        rv_RecyclerView.setHasFixedSize(true);

        mAdapter = new BenefitAdapter(getActivity(), mData, listener);
        rv_RecyclerView.setAdapter(mAdapter);

        onStartRequest();
        handler.postDelayed(runnable, 0);

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

            Intent intent = new Intent(getActivity(), BenefitDetailsActivity.class);
            intent.putExtra(ResponseKey.getInstace().id,  mData.get(i).get(ResponseKey.getInstace().id)+"");
            startActivity(intent);
        }

        @Override
        public void onRecyclerItemLongClickListener(View view, int i) {

        }
    };

    @Override
    public void onBenefitResponse(Map<String, Object> result) {
        String amount =(String) result.get(ResponseKey.getInstace().sum);

            String zero = "";
            if(amount.length() < 10){

                for (int i = 0; i < 10-amount.length(); i++) {
                    zero += "0";
                }
            }
            onBenefitAmount(zero+amount);



            List<Map<String, Object>> list = (List<Map<String, Object>>) result.get(ResponseKey.getInstace().data);
            if (refreshState ==1){
                mData.addAll(0, list);
            }else{
            mData.addAll(list);
        }
            onResetRefreshState();
            mAdapter.notifyDataSetChanged();
    }

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

    Handler handler = new Handler(){};

    Runnable runnable = new Runnable() {
        @Override
        public void run() {
            tv_cur_time.setText(DateUtil.onDateFormat("yyyy年MM月dd日HH时mm分ss秒"));
            handler.postDelayed(this, 1000);

        }
    };

    @Override
    public void onDestroy() {
        super.onDestroy();

        handler.removeCallbacks(runnable);
    }


    private void onBenefitAmount(String amount){

        List<AmountBean> list = new ArrayList<>();
        int num = amount.length() % 3;

        String amount1 = "";
        for (int i =  amount.length()-1; i >=0; i --) {
            amount1 += amount.substring(i, i+1);
        }
        for (int i =0; i< amount1.length(); i++){

            if (i % 3 == 0 && i != 0){
                AmountBean bean1 = new AmountBean();
                bean1.setIsnum(false);
                list.add(bean1);
            }
            AmountBean bean1 = new AmountBean();
            bean1.setIsnum(true);
            bean1.setNum(Integer.parseInt(amount1.substring(i, i+1)));
            list.add(bean1);
        }

        List<AmountBean> list2 = new ArrayList<>();
        list2.add(new AmountBean());
        for (int i = list.size()-1; i >=0 ; i--) {

            list2.add(list.get(i));
        }

        mAmountData.clear();
        mAmountData.addAll(list2);
        mAmountAdapter.notifyDataSetChanged();
    }


}
