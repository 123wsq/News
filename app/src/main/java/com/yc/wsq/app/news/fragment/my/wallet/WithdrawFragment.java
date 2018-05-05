package com.yc.wsq.app.news.fragment.my.wallet;

import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.wsq.library.tools.ToastUtils;
import com.wsq.library.views.view.CustomPopup;
import com.yc.wsq.app.news.R;
import com.yc.wsq.app.news.base.BaseFragment;
import com.yc.wsq.app.news.constant.ResponseKey;
import com.yc.wsq.app.news.mvp.presenter.UserPresenter;
import com.yc.wsq.app.news.mvp.view.WithdrawView;
import com.yc.wsq.app.news.tools.SharedTools;
import com.yc.wsq.app.news.views.CustomPsdKeyboardPopup;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 提现页面
 */
public class WithdrawFragment extends BaseFragment<WithdrawView, UserPresenter<WithdrawView>> implements WithdrawView{

    public static final String TAG  = WithdrawFragment.class.getName() ;

    @BindView(R.id.tv_title) TextView tv_title;
    @BindView(R.id.ll_layout) LinearLayout ll_layout;
    @BindView(R.id.tv_withdraw_to) TextView tv_withdraw_to;
    @BindView(R.id.et_recharge_money) EditText et_recharge_money;
    @BindView(R.id.tv_withdraw_all) TextView tv_withdraw_all;
    @BindView(R.id.tv_withdraw_available) TextView tv_withdraw_available;

    private CustomPopup popup;
    private CustomPsdKeyboardPopup keyboardPopup;
    private int withdrawType = -1;
    private double amount = 0;

    @Override
    protected UserPresenter<WithdrawView> createPresenter() {
        return new UserPresenter<>();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.layout_fragment_withdraw;
    }

    @Override
    protected void initView() {
        tv_title.setText(getResources().getString(R.string.str_withdraw_text));
        onEditChange();
    }

    @Override
    public void onResume() {
        super.onResume();
        try {
            amount = Double.parseDouble(SharedTools.getInstance(getActivity()).onGetString(ResponseKey.getInstace().total_amount));
        }catch (Exception e){
            e.printStackTrace();
        }
        tv_withdraw_available.setText(String.format(getResources().getString(R.string.str_balance_available_text), amount+""));
        tv_withdraw_available.setTextColor(getResources().getColor(R.color.color_blue));
    }

    @OnClick({R.id.ll_back, R.id.tv_withdraw, R.id.ll_withdraw_to, R.id.tv_withdraw_all})
    public void onClick(View view){
        switch (view.getId()){
            case R.id.ll_back:
                mFunctionsManage.invokeFunction(INTERFACE_BACK);
                break;
            case R.id.ll_withdraw_to:
                onInitPopup();
                break;
            case R.id.tv_withdraw:

                onWithdraw();

                break;
            case R.id.tv_withdraw_all:
                et_recharge_money.setText(amount+"");
                break;
        }
    }

    /**
     * 提现参数验证
     */
    private void onWithdraw(){
        if (withdrawType == -1){
            ToastUtils.onToast("请选择收款地址");
            return;
        }
        try {
            String str_amount = et_recharge_money.getText().toString().trim();
            double money = Double.parseDouble(str_amount);
            if (money == 0) {
                ToastUtils.onToast("请输入提现金额");
                return;
            }
            onOpenWithdrawPassword();

        }catch (Exception e){
            ToastUtils.onToast("请输入提现金额");
            e.printStackTrace();
        }
    }

    /**
     * 输入提现密码
     */
    private void onOpenWithdrawPassword(){

        keyboardPopup = new CustomPsdKeyboardPopup(getActivity(), "请输入提现密码", new CustomPsdKeyboardPopup.OnInputKeyBoardListener() {
            @Override
            public void onResultListener(String password) {

                onValidateWithdrawPassword();
                keyboardPopup.dismiss();
            }
        });
        keyboardPopup.showAtLocation(ll_layout, Gravity.BOTTOM|Gravity.CENTER, 0, 0);
    }



    /**
     * 选择提现目的
     */
    private void onInitPopup(){
        List<String> list = new ArrayList<>();
        list.add("支付宝");
        list.add("微信");
        popup = new CustomPopup(getActivity(), list, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popup.dismiss();
            }
        }, new CustomPopup.PopupItemListener() {
            @Override
            public void onClickItemListener(int i, String s) {
                tv_withdraw_to.setText(s);
                popup.dismiss();
                ToastUtils.onToast(s);
            }
        });
        popup.showAtLocation(ll_layout, Gravity.BOTTOM|Gravity.CENTER, 0, 0);
    }



    @Override
    public void onApplyWithdrawResponse(Map<String, Object> result) {

    }

    @Override
    public void onValidateWithdrawPasswordResponse(Map<String, Object> result) {

        onApplyWithdraw();
    }

    /**
     * 提现密码验证
     */
    private void onValidateWithdrawPassword(){
        Map<String, String> param = new HashMap<>();

        try {
            ipresenter.onValidateWithdrawPassword(param);
        } catch (Exception e) {
            ToastUtils.onToast(e.getMessage());
            e.printStackTrace();
        }
    }
    /**
     * 开始提现
     */
    private void onApplyWithdraw(){

        Map<String, String> param = new HashMap<>();

        try {
            ipresenter.onApplyWithdraw(param);
        } catch (Exception e) {
            ToastUtils.onToast(e.getMessage());
            e.printStackTrace();
        }

    }

    private void onEditChange(){
        et_recharge_money.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

                String data = s.toString().trim();
                if (data.startsWith(".")){
                    et_recharge_money.setText("0.");
                    et_recharge_money.setSelection(et_recharge_money.getText().length());
                }

                if (data.indexOf(".")>1){
                    data.substring(0, data.length()-1);
                    et_recharge_money.setSelection(et_recharge_money.getText().length());
                }
                double a1 =0;
                try{
                    a1 = Double.parseDouble(data);
                }catch (Exception e){
                    e.printStackTrace();
                }
                if (a1 > amount){
                    tv_withdraw_available.setText(getString(R.string.str_balance_no_text));
                    tv_withdraw_available.setTextColor(getResources().getColor(R.color.red));
                }else{
                    tv_withdraw_available.setText(String.format(getResources().getString(R.string.str_balance_available_text), amount+""));
                    tv_withdraw_available.setTextColor(getResources().getColor(R.color.color_blue));
                }
            }
        });
    }
}
