package com.yc.wsq.app.news.activity.my.wallet;

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
import com.yc.wsq.app.news.adapter.TradeRecodeAdapter;
import com.yc.wsq.app.news.base.BaseActivity;
import com.yc.wsq.app.news.base.BaseFragment;
import com.yc.wsq.app.news.constant.ResponseKey;
import com.yc.wsq.app.news.mvp.presenter.UserPresenter;
import com.yc.wsq.app.news.mvp.view.UserView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 交易记录
 */
public class TradeRecordActivity extends BaseActivity<UserView, UserPresenter<UserView>> implements UserView{


    @BindView(R.id.tv_title) TextView tv_title;
    @BindView(R.id.rv_RecyclerView)
    RecyclerView rv_RecyclerView;

    private List<Map<String, Object>> mData;
    private TradeRecodeAdapter mAdapter;

    @Override
    protected UserPresenter<UserView> createPresenter() {
        return new UserPresenter<>();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.layout_fragment_trade_record;
    }

    @Override
    protected void initView() {

        tv_title.setText(getResources().getString(R.string.str_trade_record_text));

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

        mAdapter = new TradeRecodeAdapter(this, mData, listener);
        rv_RecyclerView.setAdapter(mAdapter);
        Map<String, String> param = new HashMap<>();

        try {
            ipresenter.onGetTradeRecord(param);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @OnClick({R.id.ll_back})
    public void  onClick(View view){

        switch (view.getId()){
            case R.id.ll_back:
                finish();
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

    @Override
    public void onResponseData(Map<String, Object> result) {

        Logger.d(result);
        List<Map<String, Object>> list = (List<Map<String, Object>>) result.get(ResponseKey.getInstace().trade);
        mData.addAll(list);
        mAdapter.notifyDataSetChanged();
    }
}
