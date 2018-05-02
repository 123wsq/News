package com.yc.wsq.app.news.activity;

import android.content.Intent;
import android.graphics.Paint;
import android.text.util.Linkify;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.wsq.library.tools.ToastUtils;
import com.yc.wsq.app.news.R;
import com.yc.wsq.app.news.base.BaseActivity;
import com.yc.wsq.app.news.constant.ResponseKey;
import com.yc.wsq.app.news.mvp.presenter.BasePresenter;
import com.yc.wsq.app.news.mvp.presenter.UserPresenter;
import com.yc.wsq.app.news.mvp.view.UserView;
import com.yc.wsq.app.news.tools.SharedTools;

import java.util.HashMap;
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

    @OnClick({R.id.ll_back, R.id.tv_login, R.id.tv_no_account_register})
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
        }
    }

    @Override
    public void onResponseData(Map<String, Object> result) {

        int status = (Integer) result.get(ResponseKey.getInstace().rsp_status);
        if (status == 1){
            SharedTools shared = SharedTools.getInstance(getContext());
            Map<String, Object> data = (Map<String, Object>) result.get(ResponseKey.getInstace().result);
            shared.onPutData(ResponseKey.getInstace().user_id,data.get(ResponseKey.getInstace().user_id)+"");
            shared.onPutData(ResponseKey.getInstace().paypwd,data.get(ResponseKey.getInstace().paypwd)+"");
            shared.onPutData(ResponseKey.getInstace().user_money,data.get(ResponseKey.getInstace().user_money)+"");
            shared.onPutData(ResponseKey.getInstace().mobile,data.get(ResponseKey.getInstace().mobile)+"");
            shared.onPutData(ResponseKey.getInstace().head_pic,data.get(ResponseKey.getInstace().head_pic)+"");
            shared.onPutData(ResponseKey.getInstace().nickname,data.get(ResponseKey.getInstace().nickname)+"");
            shared.onPutData(ResponseKey.getInstace().total_amount,data.get(ResponseKey.getInstace().total_amount)+"");
            shared.onPutData(ResponseKey.getInstace().token,data.get(ResponseKey.getInstace().token)+"");
            shared.onPutData(ResponseKey.getInstace().is_vip,data.get(ResponseKey.getInstace().is_vip)+"");
            shared.onPutData(ResponseKey.getInstace().level_name,data.get(ResponseKey.getInstace().level_name)+"");

            setResult(MainActivity.LOGIN_REQUEST_CODE);
            finish();
        }else {
            ToastUtils.onToast(result.get(ResponseKey.getInstace().rsp_msg)+"");
        }
    }
}
