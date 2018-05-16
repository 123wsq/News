package com.yc.wsq.app.news.activity.my.setting;

import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.wsq.library.tools.ToastUtils;
import com.yc.wsq.app.news.base.BaseActivity;
import com.yc.wsq.app.news.base.BaseFragment;
import com.yc.wsq.app.news.constant.ResponseKey;
import com.yc.wsq.app.news.mvp.presenter.UserPresenter;
import com.yc.wsq.app.news.mvp.view.UpdateUserInfoView;
import com.yc.wsq.app.news.R;
import com.yc.wsq.app.news.tools.SharedTools;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;

public class UpdatePasswordActivity extends BaseActivity<UpdateUserInfoView, UserPresenter<UpdateUserInfoView>> implements UpdateUserInfoView{


    @BindView(R.id.tv_title) TextView tv_title;
    @BindView(R.id.et_password1) EditText et_password1;
    @BindView(R.id.et_password2) EditText et_password2;
    @BindView(R.id.et_password3) EditText et_password3;

    @Override
    protected UserPresenter<UpdateUserInfoView> createPresenter() {
        return new UserPresenter<>();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.layout_fragment_update_password;
    }

    @Override
    protected void initView() {

        tv_title.setText(getString(R.string.str_setting_login_text));
    }


    @OnClick({R.id.ll_back, R.id.tv_submit})
    public void onClick(View view){
        switch (view.getId()){
            case R.id.ll_back:
                finish();
                break;
            case R.id.tv_submit:
                onUpdatePassword();
                break;

        }
    }

    private void onUpdatePassword(){
        Map<String, String> param = new HashMap<>();

        try {
            param.put(ResponseKey.getInstace().user_id, SharedTools.getInstance(this).onGetString(ResponseKey.getInstace().user_id));
            param.put(ResponseKey.getInstace().password, et_password1.getText().toString().trim());
            param.put(ResponseKey.getInstace().newpassword,  et_password2.getText().toString().trim());
            param.put(ResponseKey.getInstace().newpassword2,  et_password3.getText().toString().trim());
            ipresenter.onUpdateAccountInfo(param);
        } catch (Exception e) {
            ToastUtils.onToast(e.getMessage());
            e.printStackTrace();
        }
    }


    @Override
    public void onUserLogOutResponseData(Map<String, Object> result) {

    }

    @Override
    public void onUpdateHeaderResponseData(Map<String, Object> result) {

    }

    @Override
    public void onUpdateUserResponseData(Map<String, Object> result) {
        String msg = (String) result.get(ResponseKey.getInstace().rsp_msg);
        onShowDialog("提示", msg, null);
        finish();
    }
}
