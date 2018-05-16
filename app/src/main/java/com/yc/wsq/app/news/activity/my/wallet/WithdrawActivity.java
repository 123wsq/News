package com.yc.wsq.app.news.activity.my.wallet;

import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.wsq.library.tools.ToastUtils;
import com.wsq.library.views.view.CustomPopup;
import com.yc.wsq.app.news.R;
import com.yc.wsq.app.news.base.BaseActivity;
import com.yc.wsq.app.news.base.BaseFragment;
import com.yc.wsq.app.news.constant.ResponseKey;
import com.yc.wsq.app.news.mvp.presenter.UserPresenter;
import com.yc.wsq.app.news.mvp.view.UserView;
import com.yc.wsq.app.news.tools.SharedTools;
import com.yc.wsq.app.news.views.popup.CustomPsdKeyboardPopup;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 提现页面
 */
public class WithdrawActivity extends BaseActivity<UserView, UserPresenter<UserView>> implements UserView, RadioGroup.OnCheckedChangeListener {


    @BindView(R.id.tv_title) TextView tv_title;
    @BindView(R.id.ll_layout) LinearLayout ll_layout;
    @BindView(R.id.tv_withdraw_available) TextView tv_withdraw_available;

    @BindView(R.id.rg_pay_type) RadioGroup rg_pay_type;
    @BindView(R.id.rb_alipay) RadioButton rb_alipay;
    @BindView(R.id.rb_wechat) RadioButton rb_wechat;
    @BindView(R.id.tv_message) TextView tv_message;
    @BindView(R.id.rg_withdraw_money) RadioGroup rg_withdraw_money;
    @BindView(R.id.ll_withdraw_account) LinearLayout ll_withdraw_account;


    private CustomPopup popup;
    private CustomPsdKeyboardPopup keyboardPopup;
    private int withdrawType = 1;
    private double amount = 0;
    private int withdrawMoney = 0;

    @Override
    protected UserPresenter<UserView> createPresenter() {
        return new UserPresenter<>();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.layout_fragment_withdraw;
    }

    @Override
    protected void initView() {
        tv_title.setText(getResources().getString(R.string.str_withdraw_text));

        String alipayAccount = SharedTools.getInstance(this).onGetString(ResponseKey.getInstace().alipay_bank_card);
        tv_message.setText(TextUtils.isEmpty(alipayAccount) ? "您尚未设置支付宝账户" : alipayAccount);
        rb_alipay.setChecked(true);
        rg_withdraw_money.setOnCheckedChangeListener(this);
        if (TextUtils.isEmpty(alipayAccount)){

        }
    }

    @Override
    public void onResume() {
        super.onResume();
        try {
            amount = Double.parseDouble(SharedTools.getInstance(this).onGetString(ResponseKey.getInstace().user_money));
        }catch (Exception e){
            e.printStackTrace();
        }
        tv_withdraw_available.setText( amount+"");
        tv_withdraw_available.setTextColor(getResources().getColor(R.color.color_blue));
    }

    @OnClick({R.id.ll_back, R.id.tv_withdraw})
    public void onClick(View view){
        switch (view.getId()){
            case R.id.ll_back:
                finish();
                break;

            case R.id.tv_withdraw:

                onWithdrawValidate();
                break;

        }
    }

    private void onWithdrawValidate(){

        if (withdrawMoney == 0 ){
            ToastUtils.onToast("请选择提现金额");
            return;
        }

        double fee_money = withdrawMoney *0.05;
        if ((fee_money + withdrawMoney) > amount){

            onShowDialog("提示", "您的余额不足以支付提现金额和手续费！", null);
            return;
        }
        onShowDialog("提示", "您的提现金额为：" +withdrawMoney +"元，需支付手续费:" +fee_money +"元, 共计："+(fee_money+withdrawMoney)+"元", new OnDialogClickListener() {
            @Override
            public void onClickListener() {
                onOpenWithdrawPassword();
            }
        });

    }


    /**
     * 输入提现密码
     */
    private void onOpenWithdrawPassword(){

        keyboardPopup = new CustomPsdKeyboardPopup(this, "请输入提现密码", new CustomPsdKeyboardPopup.OnInputKeyBoardListener() {
            @Override
            public void onResultListener(String password) {

                onApplyWithdraw(password);
                keyboardPopup.dismiss();
            }
        });
        keyboardPopup.showAtLocation(ll_layout, Gravity.BOTTOM|Gravity.CENTER, 0, 0);
    }
    /**
     * 申请提现
     */
    private void onApplyWithdraw(String password){

        Map<String, String> param = new HashMap<>();
        try {
            param.put(ResponseKey.getInstace().user_id, SharedTools.getInstance(this).onGetString(ResponseKey.getInstace().user_id));
            param.put(ResponseKey.getInstace().bank_type, 1+"");
            param.put(ResponseKey.getInstace().money, withdrawMoney+"");
            param.put(ResponseKey.getInstace().paypwd, password);
            param.put(ResponseKey.getInstace().city, SharedTools.getInstance(this).onGetString(ResponseKey.getInstace().city_name));
            param.put(ResponseKey.getInstace().commonweal, (withdrawMoney * 0.05) +"");
            ipresenter.onApplyWithdraw(param);
        } catch (Exception e) {
            ToastUtils.onToast(e.getMessage());
            e.printStackTrace();
        }

    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {

        switch (checkedId){
            case R.id.rb_money_50:

                withdrawMoney = 50;
                break;
            case R.id.rb_money_100:

                withdrawMoney = 100;
                break;
            case R.id.rb_money_500:

                withdrawMoney = 500;
                break;
        }
    }

    @Override
    public void onResponseData(Map<String, Object> result) {

        SharedTools.getInstance(this).onPutData(ResponseKey.getInstace().user_money, (amount-withdrawMoney-withdrawMoney*0.05)+"");
        String msg = (String) result.get(ResponseKey.getInstace().rsp_msg);
        ToastUtils.onToast(msg);
        finish();
    }
}
