package com.yc.wsq.app.news.activity.my.wallet;

import android.content.Intent;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadmoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.wsq.library.listener.OnRecyclerViewItemClickListener;
import com.wsq.library.tools.RecyclerViewDivider;
import com.wsq.library.utils.DensityUtil;
import com.yc.wsq.app.news.R;
import com.yc.wsq.app.news.activity.my.setting.AccountCreditedActivity;
import com.yc.wsq.app.news.adapter.TradeRecodeAdapter;
import com.yc.wsq.app.news.base.BaseActivity;
import com.yc.wsq.app.news.base.BaseFragment;
import com.yc.wsq.app.news.constant.ResponseKey;
import com.yc.wsq.app.news.mvp.presenter.UserPresenter;
import com.yc.wsq.app.news.mvp.view.UserView;
import com.yc.wsq.app.news.tools.SharedTools;
import com.yc.wsq.app.news.views.popup.CustomPsdKeyboardPopup;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 我的钱包
 */
public class WalletActivity extends BaseActivity<UserView, UserPresenter<UserView>> implements UserView{



    @BindView(R.id.tv_title)TextView tv_title;
    @BindView(R.id.rl_layout) RelativeLayout rl_layout;
    @BindView(R.id.tv_account_balance) TextView tv_account_balance;
    @BindView(R.id.refreshLayout) SmartRefreshLayout refreshLayout;
    @BindView(R.id.rv_RecyclerView) RecyclerView rv_RecyclerView;
    @BindView(R.id.tv_frozen_money) TextView tv_frozen_money;
    @BindView(R.id.ll_not_data) LinearLayout ll_not_data;


    private String withdrawPsd1, withdrawPsd2;
    private CustomPsdKeyboardPopup keyboardPopup;
    private final int FLAG_TYPE_SETTING = 1; //设置提现密码
    private final int FLAG_TYPE_AFFIRM =2;   //确认提现密码

    private int refreshState = 0;
    private List<Map<String, Object>> mData;
    private TradeRecodeAdapter mAdapter;

    @Override
    protected UserPresenter<UserView> createPresenter() {
        return new UserPresenter<>();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.layout_fragment_wallet;
    }

    @Override
    protected void initView() {

        tv_title.setText(getResources().getString(R.string.str_my_wallet_text));

        onInitRecyclerView();
        onInitRefreshLayout();

    }
    @Override
    public void onResume() {
        super.onResume();
        tv_account_balance.setText(SharedTools.getInstance(this).onGetString(ResponseKey.getInstace().user_money));
        tv_frozen_money.setText(SharedTools.getInstance(this).onGetString(ResponseKey.getInstace().frozen_money));
    }

    /**
     * RecyclenView 的初始化
     */
    private void  onInitRecyclerView(){

        mData = new ArrayList<>();
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);

        rv_RecyclerView.addItemDecoration(new RecyclerViewDivider(
                this, LinearLayoutManager.HORIZONTAL, DensityUtil.dp2px(this, 1),
                ContextCompat.getColor(this, R.color.default_backgroud_color)));
        rv_RecyclerView.setLayoutManager(new LinearLayoutManager(this));
        rv_RecyclerView.setHasFixedSize(true);

        mAdapter = new TradeRecodeAdapter(this, mData, listener);
        rv_RecyclerView.setAdapter(mAdapter);
//
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
            param.put(ResponseKey.getInstace().uid, SharedTools.getInstance(this).onGetString(ResponseKey.getInstace().user_id));
            param.put(ResponseKey.getInstace().token, SharedTools.getInstance(this).onGetString(ResponseKey.getInstace().token));
            ipresenter.onGetTradeRecord(param);
        } catch (Exception e) {
            e.printStackTrace();
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

    @OnClick({R.id.ll_back, R.id.tv_withdraw})
    public void onClick(View view){

        switch (view.getId()){
            case R.id.ll_back:
                finish();
                break;

            case R.id.tv_withdraw://param =2 提现
                onWithdrawValidate();
                break;

        }
    }

    /**
     * 提现前验证
     */
    private void onWithdrawValidate(){

        String is_vip = SharedTools.getInstance(this).onGetString(ResponseKey.getInstace().is_vip);
        if (!is_vip.equals("1")){
            onShowDialog("提示", "请先升级成为会员，才可以进行提现操作", null);
            return;
        }
        String paywd = SharedTools.getInstance(this).onGetString(ResponseKey.getInstace().paypwd);
        if (TextUtils.isEmpty(paywd)){
            onShowDialog("提示", "您还没有设置支付密码，请先设置支付密码", new OnDialogClickListener() {
                @Override
                public void onClickListener() {

                    //跳转账号验证页面
                    startActivity(new Intent(WalletActivity.this, ValidateAccountActivity.class));
                }
            });
            return;
        }
        String alipayAccount = SharedTools.getInstance(this).onGetString(ResponseKey.getInstace().alipay_bank_card);
        if (TextUtils.isEmpty(alipayAccount)){
            onShowDialog("提示", "您还没有设置提现账户，请先设置提现账户", new OnDialogClickListener() {
                @Override
                public void onClickListener() {

                    //设置提现账户
                    startActivity(new Intent(WalletActivity.this, AccountCreditedActivity.class));
                }
            });
            return;
        }

        startActivity(new Intent(WalletActivity.this, WithdrawActivity.class)); //提现
    }



    @Override
    public void onResponseData(Map<String, Object> result) {

        List<Map<String, Object>> list = (List<Map<String, Object>>) result.get(ResponseKey.getInstace().data);
        if (refreshState ==1){
            mData.addAll(0, list);
        }else{
            mData.addAll(list);
        }
        ll_not_data.setVisibility(mData.size() ==0? View.VISIBLE: View.GONE);
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

}
