package com.yc.wsq.app.news.fragment.my;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.yc.wsq.app.news.R;
import com.yc.wsq.app.news.base.BaseFragment;
import com.yc.wsq.app.news.mvp.presenter.UserPresenter;
import com.yc.wsq.app.news.mvp.view.UserView;

import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 我的钱包
 */
public class WalletFragment extends BaseFragment<UserView, UserPresenter<UserView>> implements UserView{

    public static final String TAG = WalletFragment.class.getName();
    public static final String INTERFACE_WITHP = TAG+_INTERFACE_WITHP;
    @BindView(R.id.tv_title)TextView tv_title;
    @BindView(R.id.iv_detail) ImageView iv_detail;

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
        iv_detail.setImageResource(R.mipmap.image_trade_recode);
    }

    @OnClick({R.id.ll_back, R.id.tv_recharge, R.id.tv_withdraw, R.id.iv_detail})
    public void onClick(View view){

        switch (view.getId()){
            case R.id.ll_back:
                mFunctionsManage.invokeFunction(INTERFACE_BACK);
                break;
            case R.id.tv_recharge://param =1 充值
                mFunctionsManage.invokeFunction(INTERFACE_WITHP, 1);
                break;
            case R.id.tv_withdraw:  //param =2 提现
                mFunctionsManage.invokeFunction(INTERFACE_WITHP, 2);
                break;
            case R.id.iv_detail: //交易记录  param =3
                mFunctionsManage.invokeFunction(INTERFACE_WITHP, 3);
                break;
        }
    }

    @Override
    public void onResponseData(Map<String, Object> result) {

    }
}
