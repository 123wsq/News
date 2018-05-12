package com.yc.wsq.app.news.fragment.my.wallet;


import android.os.Handler;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.wsq.library.tools.ToastUtils;
import com.yc.wsq.app.news.base.BaseFragment;
import com.yc.wsq.app.news.constant.ResponseKey;
import com.yc.wsq.app.news.mvp.presenter.UserPresenter;
import com.yc.wsq.app.news.R;
import com.yc.wsq.app.news.mvp.view.UserRegisterView;
import com.yc.wsq.app.news.tools.SharedTools;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;
import cn.sharesdk.framework.authorize.RegisterView;

public class ValidateAccountFragment extends BaseFragment<UserRegisterView, UserPresenter<UserRegisterView>> implements UserRegisterView{

    public static final String TAG = ValidateAccountFragment.class.getName();

    public static final String INTERFACE_WITHP = TAG +_INTERFACE_WITHP;

    @BindView(R.id.tv_title)
    TextView tv_title;
    @BindView(R.id.tv_mobile)
    TextView tv_mobile;
    @BindView(R.id.et_validate_code) EditText et_validate_code;
    @BindView(R.id.tv_get_validate)TextView tv_get_validate;

    private String  mobile;
    private int curLen = 60;

    @Override
    protected UserPresenter<UserRegisterView> createPresenter() {
        return new UserPresenter<>();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.layout_fragment_validate_account;
    }

    @Override
    protected void initView() {

        tv_title.setText(getText(R.string.str_validate_account_text));
        mobile = SharedTools.getInstance(getActivity()).onGetString(ResponseKey.getInstace().mobile);

        tv_mobile.setText(mobile.substring(0, 3) +"****"+mobile.substring(7));

    }

    @OnClick({R.id.ll_back, R.id.tv_next, R.id.tv_get_validate})
    public void onClick(View view){
        switch (view.getId()){
            case R.id.ll_back:

                mFunctionsManage.invokeFunction(INTERFACE_BACK);
                break;
            case R.id.tv_get_validate:
                onGetValidateCode();
                break;
            case R.id.tv_next: //下一步， param =1

                onCheckValidateCode();
                break;
        }
    }

    private void onGetValidateCode(){
        Map<String, String> param = new HashMap<>();

        try {
            param.put(ResponseKey.getInstace().mobile,mobile);
            param.put(ResponseKey.getInstace().scene, "6");
            ipresenter.onGetValidateCode(param);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void onCheckValidateCode(){
        Map<String, String> param = new HashMap<>();

        try {
            param.put(ResponseKey.getInstace().code, et_validate_code.getText().toString().trim());
            param.put(ResponseKey.getInstace().mobile, mobile);
            param.put(ResponseKey.getInstace().scene, "6");
            ipresenter.onCheckValidateCode(param);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @Override
    public void onGetValidateData(Map<String, Object> result) {

        String msg = result.get(ResponseKey.getInstace().rsp_msg)+"";
        ToastUtils.onToast(msg);
        handler.postDelayed(runnable, 1000);
    }

    @Override
    public void onRegisterData(Map<String, Object> result) {

        mFunctionsManage.invokeFunction(INTERFACE_WITHP, 1);
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
}
