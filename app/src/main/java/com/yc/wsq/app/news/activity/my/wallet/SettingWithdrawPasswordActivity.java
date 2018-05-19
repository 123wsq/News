package com.yc.wsq.app.news.activity.my.wallet;

import android.content.Intent;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.wsq.library.tools.ToastUtils;
import com.yc.wsq.app.news.activity.my.setting.SettingActivity;
import com.yc.wsq.app.news.base.BaseActivity;
import com.yc.wsq.app.news.constant.ResponseKey;
import com.yc.wsq.app.news.mvp.presenter.UserPresenter;
import com.yc.wsq.app.news.mvp.view.UserView;
import com.yc.wsq.app.news.R;
import com.yc.wsq.app.news.tools.PasswordLevel;
import com.yc.wsq.app.news.tools.SharedTools;
import com.yc.wsq.app.news.views.PasswordInputView;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;

public class SettingWithdrawPasswordActivity extends BaseActivity<UserView, UserPresenter<UserView>> implements UserView{



    @BindView(R.id.tv_title) TextView tv_title;
    @BindView(R.id.et_psdInput) PasswordInputView et_psdInput;
    @BindView(R.id.et_psdInput2) PasswordInputView et_psdInput2;
    @BindView(R.id.rg_password_level) RadioGroup rg_password_level;
    @BindView(R.id.rb_password_low) RadioButton rb_password_low;
    @BindView(R.id.rb_password_middle) RadioButton rb_password_middle;
    @BindView(R.id.rb_password_high) RadioButton rb_password_high;


    @Override
    protected UserPresenter<UserView> createPresenter() {
        return new UserPresenter<>();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.layout_fragment_withdraw_password;
    }

    @Override
    protected void initView() {

        tv_title.setText(getText(R.string.str_withdraw_password_text));

        onPasswordLevelListener();
    }

    @OnClick({R.id.ll_back, R.id.tv_submit})
    public void onClick(View view){
        switch (view.getId()){
            case R.id.ll_back:
                finish();
                break;
            case R.id.tv_submit:
                onSettingPassword();
                break;
        }
    }

    private void onSettingPassword(){

        Map<String, String> param = new HashMap<>();
        try {
            param.put(ResponseKey.getInstace().user_id, SharedTools.getInstance(this).onGetString(ResponseKey.getInstace().user_id));
            String pwd1 = et_psdInput.getText().toString();
            String pwd2 = et_psdInput2.getText().toString();
            if (!pwd1.equals(pwd2)){
                throw new Exception("两次密码不一致");
            }
            param.put(ResponseKey.getInstace().paypwd, pwd1);
            ipresenter.onSettingWithdrawPassword(param);
        } catch (Exception e) {
            ToastUtils.onToast(e.getMessage());
            e.printStackTrace();
        }
    }

    private void onPasswordLevelListener(){

        et_psdInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

                String str = s.toString().trim();

                if (str.length() >= 6){
                    int num = PasswordLevel.onValidateLevel(str);
                    switch (num){
                        case 0:
                        case 1:
                            rb_password_low.setChecked(true);
                            break;
                        case 2:
                            rb_password_middle.setChecked(true);
                            break;
                        case 3:
                        case 4:
                            rb_password_high.setChecked(true);
                            break;

                    }
                }
            }
        });

    }

    @Override
    public void onResponseData(Map<String, Object> result) {

        String msg = (String) result.get(ResponseKey.getInstace().rsp_msg);
        ToastUtils.onToast(msg);
        SharedTools.getInstance(this).onPutData(ResponseKey.getInstace().paypwd, et_psdInput.getText().toString());
//        Intent intent = new Intent(this, SettingActivity.class);
//        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//        startActivity(intent);
        finish();
    }
}
