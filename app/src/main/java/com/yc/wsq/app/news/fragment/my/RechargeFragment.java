package com.yc.wsq.app.news.fragment.my;

import android.view.Gravity;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.wsq.library.tools.ToastUtils;
import com.wsq.library.views.view.CustomPopup;
import com.yc.wsq.app.news.R;
import com.yc.wsq.app.news.base.BaseFragment;
import com.yc.wsq.app.news.mvp.presenter.UserPresenter;
import com.yc.wsq.app.news.mvp.view.UserView;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 充值页面
 */
public class RechargeFragment extends BaseFragment<UserView, UserPresenter<UserView>> implements UserView{


    public static final String TAG = RechargeFragment.class.getName();

    @BindView(R.id.tv_title) TextView tv_title;
    @BindView(R.id.ll_layout) LinearLayout ll_layout;
    @BindView(R.id.et_recharge_money) EditText et_recharge_money;

    private CustomPopup popup;

    @Override
    protected UserPresenter<UserView> createPresenter() {
        return new UserPresenter<>();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.layout_fragment_recharge;
    }

    @Override
    protected void initView() {

        tv_title.setText(getResources().getString(R.string.str_recharge_wallet_text));
    }

    @OnClick({R.id.ll_back, R.id.tv_next, R.id.ll_money_1, R.id.ll_money_10, R.id.ll_money_20, R.id.ll_money_30,
            R.id.ll_money_50, R.id.ll_money_100, R.id.ll_money_200, R.id.ll_money_500})
    public void onClick(View view){
        switch (view.getId()){
            case R.id.ll_back:
                mFunctionsManage.invokeFunction(INTERFACE_BACK);
                break;
            case R.id.ll_money_1:
                et_recharge_money.setText("1");
                break;
            case R.id.ll_money_10:
                et_recharge_money.setText("10");
                break;
            case R.id.ll_money_20:
                et_recharge_money.setText("20");
                break;
            case R.id.ll_money_30:
                et_recharge_money.setText("30");
                break;
            case R.id.ll_money_50:
                et_recharge_money.setText("50");
                break;
            case R.id.ll_money_100:
                et_recharge_money.setText("100");
                break;
            case R.id.ll_money_200:
                et_recharge_money.setText("200");
                break;
            case R.id.ll_money_500:
                et_recharge_money.setText("500");
                break;
            case R.id.tv_next:
                onInitPopup();
                break;
        }
    }
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
                popup.dismiss();
                ToastUtils.onToast(s);
            }
        });
        popup.showAtLocation(ll_layout, Gravity.BOTTOM|Gravity.CENTER, 0, 0);
    }

    @Override
    public void onResponseData(Map<String, Object> result) {

    }
}
