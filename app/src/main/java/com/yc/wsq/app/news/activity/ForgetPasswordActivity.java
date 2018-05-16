package com.yc.wsq.app.news.activity;

import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.wsq.library.tools.ToastUtils;
import com.yc.wsq.app.news.R;
import com.yc.wsq.app.news.base.BaseActivity;
import com.yc.wsq.app.news.constant.ResponseKey;
import com.yc.wsq.app.news.mvp.presenter.UserPresenter;
import com.yc.wsq.app.news.mvp.view.UserRegisterView;
import com.yc.wsq.app.news.mvp.view.UserView;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;

public class ForgetPasswordActivity extends BaseActivity<UserRegisterView, UserPresenter<UserRegisterView>> implements UserRegisterView{

    @BindView(R.id.tv_title) TextView tv_title;
    @BindView(R.id.tv_get_validate) TextView tv_get_validate;
    @BindView(R.id.et_mobile)
    EditText et_mobile;
    @BindView(R.id.et_validate_code) EditText et_validate_code;
    @BindView(R.id.et_password) EditText et_password;
    @BindView(R.id.et_password2) EditText et_password2;

    private int curLen = 60;


    @Override
    protected UserPresenter<UserRegisterView> createPresenter() {
        return new UserPresenter<>();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.layout_activity_register;
    }

    @Override
    protected void initView() {
        tv_title.setText(getResources().getText(R.string.str_forget_password_text));
    }

    @OnClick({R.id.ll_back, R.id.tv_get_validate, R.id.tv_register})
    public void onClick(View view){

        Map<String, String> param = new HashMap<>();
        switch (view.getId()){
            case R.id.ll_back:
                finish();
                break;
            case R.id.tv_get_validate:
                param.put(ResponseKey.getInstace().mobile,et_mobile.getText().toString().trim());
                param.put(ResponseKey.getInstace().scene, "2");
                try {
                    ipresenter.onGetValidateCode(param);
                } catch (Exception e) {
                    ToastUtils.onToast(e.getMessage());
                    e.printStackTrace();
                }
                break;
            case R.id.tv_register:
                param.put(ResponseKey.getInstace().mobile, et_mobile.getText().toString().trim());
                param.put(ResponseKey.getInstace().scene, 2+"");
                param.put(ResponseKey.getInstace().password, et_password.getText().toString().trim());
                param.put(ResponseKey.getInstace().password2, et_password2.getText().toString().trim());
                param.put(ResponseKey.getInstace().code, et_validate_code.getText().toString().trim());
                try {
                    ipresenter.onForgetPassword(param);
                } catch (Exception e) {
                    ToastUtils.onToast(e.getMessage());
                    e.printStackTrace();
                }
                break;
        }
    }

    Handler handler = new Handler(){};
    Runnable runnable = new Runnable() {
        @Override
        public void run() {
            curLen--;
            if (curLen == 0){
                curLen = 60;
                tv_get_validate.setText("获取验证码");
                tv_get_validate.setClickable(true);
            }else{
                tv_get_validate.setText(curLen+"秒后重发");
                tv_get_validate.setClickable(false);
                handler.postDelayed(this, 1000);
            }

        }
    };


    @Override
    public void onGetValidateData(Map<String, Object> result) {
        String status = result.get(ResponseKey.getInstace().rsp_status)+"";
        String msg = result.get(ResponseKey.getInstace().rsp_msg)+"";
        ToastUtils.onToast(msg);
        if (status.equals("1")){
            handler.postDelayed(runnable, 1000);
        }
    }

    @Override
    public void onRegisterData(Map<String, Object> result) {
        String status = result.get(ResponseKey.getInstace().rsp_status)+"";
        String msg = result.get(ResponseKey.getInstace().rsp_msg)+"";
        ToastUtils.onToast(msg);
        if (status.equals("1")){
            finish();

        }
    }
}
