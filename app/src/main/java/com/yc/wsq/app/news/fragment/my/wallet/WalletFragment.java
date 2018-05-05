package com.yc.wsq.app.news.fragment.my.wallet;

import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.wsq.library.tools.ToastUtils;
import com.yc.wsq.app.news.R;
import com.yc.wsq.app.news.base.BaseFragment;
import com.yc.wsq.app.news.constant.ResponseKey;
import com.yc.wsq.app.news.mvp.presenter.UserPresenter;
import com.yc.wsq.app.news.mvp.view.UserView;
import com.yc.wsq.app.news.tools.SharedTools;
import com.yc.wsq.app.news.views.CustomPsdKeyboardPopup;

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
    @BindView(R.id.ll_setting_withdraw_password) LinearLayout ll_setting_withdraw_password;
    @BindView(R.id.rl_layout) RelativeLayout rl_layout;
    @BindView(R.id.tv_account_balance) TextView tv_account_balance;

    private String withdrawPsd1, withdrawPsd2;
    private CustomPsdKeyboardPopup keyboardPopup;
    private final int FLAG_TYPE_SETTING = 1; //设置提现密码
    private final int FLAG_TYPE_AFFIRM =2;   //确认提现密码


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
    @Override
    public void onResume() {
        super.onResume();
        tv_account_balance.setText(SharedTools.getInstance(getActivity()).onGetString(ResponseKey.getInstace().total_amount));
    }


    @OnClick({R.id.ll_back, R.id.tv_recharge, R.id.tv_withdraw, R.id.iv_detail, R.id.ll_setting_withdraw_password})
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
            case R.id.ll_setting_withdraw_password: //设置提现密码

                onSettingWithdrawPassword("设置提现密码",FLAG_TYPE_SETTING);
                break;
        }
    }



    @Override
    public void onResponseData(Map<String, Object> result) {

    }

    private void onSettingWithdrawPassword(String title, final int flag){

        keyboardPopup = new CustomPsdKeyboardPopup(getActivity(), title, new CustomPsdKeyboardPopup.OnInputKeyBoardListener() {
            @Override
            public void onResultListener(String password) {
                switch (flag){
                    case FLAG_TYPE_SETTING:
                        withdrawPsd1 = password;
                        keyboardPopup.dismiss();
                        onSettingWithdrawPassword("确认提现密码", FLAG_TYPE_AFFIRM);
                        break;
                    case FLAG_TYPE_AFFIRM:
                        withdrawPsd2 = password;

                        if (withdrawPsd1.equals(withdrawPsd2)){

                        }else {
                            ToastUtils.onToast("两次密码不一致");
                        }
                        keyboardPopup.dismiss();
                        break;
                }

            }
        });
        keyboardPopup.showAtLocation(rl_layout, Gravity.BOTTOM|Gravity.CENTER, 0, 0);
    }
}
