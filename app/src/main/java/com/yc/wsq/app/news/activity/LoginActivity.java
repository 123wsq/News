package com.yc.wsq.app.news.activity;

import android.content.Intent;
import android.graphics.Paint;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.wsq.library.tools.ToastUtils;
import com.yc.wsq.app.news.R;
import com.yc.wsq.app.news.base.BaseActivity;
import com.yc.wsq.app.news.constant.ResponseKey;
import com.yc.wsq.app.news.mvp.presenter.UserPresenter;
import com.yc.wsq.app.news.mvp.view.UserView;
import com.yc.wsq.app.news.tools.SharedTools;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;

public class LoginActivity extends BaseActivity<UserView, UserPresenter<UserView>> implements UserView{


    @BindView(R.id.tv_title) TextView tv_title;
    @BindView(R.id.tv_no_account_register) TextView tv_no_account_register;
    @BindView(R.id.et_username) EditText et_username;
    @BindView(R.id.et_password) EditText et_password;

    @Override
    protected UserPresenter<UserView> createPresenter() {
        return new UserPresenter<>();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.layout_activity_login;
    }

    @Override
    protected void initView() {

        tv_title.setText(getResources().getText(R.string.str_user_login_text));
        tv_no_account_register.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);
    }

    @OnClick({R.id.ll_back, R.id.tv_login, R.id.tv_no_account_register, R.id.tv_forget_password})
    public void onClick(View view){
        switch (view.getId()){
            case R.id.ll_back:
                finish();
                break;
            case R.id.tv_login:
                Map<String, String> param = new HashMap<>();
                param.put(ResponseKey.getInstace().username, et_username.getText().toString().trim());
                param.put(ResponseKey.getInstace().password, et_password.getText().toString().trim());
                try {
                    ipresenter.onUserLogin(param);
                } catch (Exception e) {
                    ToastUtils.onToast(e.getMessage());
                    e.printStackTrace();
                }
                break;
            case R.id.tv_no_account_register:
                startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
                break;
            case R.id.tv_forget_password:
                startActivity(new Intent(LoginActivity.this, ForgetPasswordActivity.class));
                break;
        }
    }

    @Override
    public void onResponseData(Map<String, Object> result) {


            SharedTools shared = SharedTools.getInstance(getContext());
            shared.onPutData(ResponseKey.getInstace().username, et_username.getText().toString().trim());
            shared.onPutData(ResponseKey.getInstace().password, et_password.getText().toString().trim());
            Map<String, Object> data = (Map<String, Object>) result.get(ResponseKey.getInstace().result);

            Iterator<Map.Entry<String, Object>> it =  data.entrySet().iterator();
            while (it.hasNext()){
                Map.Entry<String, Object> entry =  it.next();
                shared.onPutData(entry.getKey(), entry.getValue()+"");
            }


            finish();

    }
}
