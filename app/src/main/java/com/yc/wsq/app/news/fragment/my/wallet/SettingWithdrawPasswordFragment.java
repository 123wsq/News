package com.yc.wsq.app.news.fragment.my.wallet;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.yc.wsq.app.news.base.BaseFragment;
import com.yc.wsq.app.news.mvp.presenter.UserPresenter;
import com.yc.wsq.app.news.mvp.view.UserView;
import com.yc.wsq.app.news.R;
import com.yc.wsq.app.news.tools.PasswordLevel;
import com.yc.wsq.app.news.views.PasswordInputView;

import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;

public class SettingWithdrawPasswordFragment extends BaseFragment<UserView, UserPresenter<UserView>> implements UserView{

    public static final String TAG = SettingWithdrawPasswordFragment.class.getName();

    public static final String INTERFACE_WITHP = TAG +_INTERFACE_WITHP;

    @BindView(R.id.tv_title) TextView tv_title;
    @BindView(R.id.et_psdInput) PasswordInputView et_psdInput;
    @BindView(R.id.et_psdInput2) PasswordInputView et_psdInput2;
    @BindView(R.id.rg_password_level) RadioGroup rg_password_level;
    @BindView(R.id.rb_password_low) RadioButton rb_password_low;
    @BindView(R.id.rb_password_middle) RadioButton rb_password_middle;
    @BindView(R.id.rb_password_high) RadioButton rb_password_high;


    @Override
    protected UserPresenter<UserView> createPresenter() {
        return null;
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
                mFunctionsManage.invokeFunction(INTERFACE_BACK);
                break;
            case R.id.tv_submit:

                break;
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

    }
}
