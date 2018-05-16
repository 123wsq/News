package com.yc.wsq.app.news.activity.my.setting;

import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.wsq.library.tools.ToastUtils;
import com.yc.wsq.app.news.R;
import com.yc.wsq.app.news.base.BaseActivity;
import com.yc.wsq.app.news.base.BaseFragment;
import com.yc.wsq.app.news.constant.ResponseKey;
import com.yc.wsq.app.news.mvp.presenter.UserPresenter;
import com.yc.wsq.app.news.mvp.view.UserView;
import com.yc.wsq.app.news.tools.SharedTools;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 收款账户设置页面
 */
public class AccountCreditedActivity extends BaseActivity<UserView, UserPresenter<UserView>> implements UserView{


    @BindView(R.id.tv_title)TextView tv_title;
    @BindView(R.id.et_account) EditText et_account;
    @BindView(R.id.et_realname) EditText et_realname;

    @Override
    protected UserPresenter<UserView> createPresenter() {
        return new UserPresenter<>();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.layout_fragment_account_credited;
    }

    @Override
    protected void initView() {

        tv_title.setText(getString(R.string.str_account_withdraw_text));

        et_account.setText(SharedTools.getInstance(this).onGetString(ResponseKey.getInstace().alipay_bank_card));
        et_realname.setText(SharedTools.getInstance(this).onGetString(ResponseKey.getInstace().alipay_realname));
    }

    @OnClick({R.id.ll_back, R.id.tv_submit})
    public void onClick(View view){
        switch (view.getId()){
            case R.id.ll_back:
                finish();
                break;
            case R.id.tv_submit:
                onAddAcount();
                break;
        }
    }

    /**
     * 添加提现账户
     */
    private void onAddAcount(){

        Map<String, String> param = new HashMap<>();
        param.put(ResponseKey.getInstace().user_id, SharedTools.getInstance(this).onGetString(ResponseKey.getInstace().user_id));
        param.put(ResponseKey.getInstace().bank_type, "1");
        param.put(ResponseKey.getInstace().bank_card, et_account.getText().toString().trim());
        param.put(ResponseKey.getInstace().realname, et_realname.getText().toString().trim());

        try {
            ipresenter.onSettingWithdrawAccount(param);
        } catch (Exception e) {
            ToastUtils.onToast(e.getMessage());
            e.printStackTrace();
        }
    }


    @Override
    public void onResponseData(Map<String, Object> result) {

        String msg = (String) result.get(ResponseKey.getInstace().rsp_msg);
        SharedTools.getInstance(this).onPutData(ResponseKey.getInstace().alipay_bank_card, et_account.getText().toString().trim());
        SharedTools.getInstance(this).onPutData(ResponseKey.getInstace().alipay_realname, et_realname.getText().toString().trim());
        ToastUtils.onToast(msg);
        finish();
    }
}
