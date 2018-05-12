package com.yc.wsq.app.news.fragment.my;

import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.wsq.library.tools.ToastUtils;
import com.yc.wsq.app.news.R;
import com.yc.wsq.app.news.base.BaseFragment;
import com.yc.wsq.app.news.constant.ResponseKey;
import com.yc.wsq.app.news.mvp.presenter.UserPresenter;
import com.yc.wsq.app.news.mvp.view.UserView;
import com.yc.wsq.app.news.tools.SharedTools;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;

public class InviteCodeFragment extends BaseFragment<UserView, UserPresenter<UserView>> implements UserView{

    public static final String TAG = InviteCodeFragment.class.getName();

    @BindView(R.id.tv_title)
    TextView tv_title;
    @BindView(R.id.et_invite_code)
    EditText et_invite_code;


    @Override
    protected UserPresenter<UserView> createPresenter() {
        return new UserPresenter<>();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.layout_fragment_invite;
    }

    @Override
    protected void initView() {

        tv_title.setText(getString(R.string.str_input_invite_code_text));
    }

    @OnClick({R.id.ll_back, R.id.tv_submit_invite})
    public void onClick(View view){
        switch (view.getId()){
            case R.id.ll_back:
                mFunctionsManage.invokeFunction(INTERFACE_BACK);
                break;
            case R.id.tv_submit_invite:
                onSubmitInviteCode();
                break;
        }
    }


    private void onSubmitInviteCode(){
        Map<String, String> param = new HashMap<>();

        try {
            param.put(ResponseKey.getInstace().user_id, SharedTools.getInstance(getActivity()).onGetString(ResponseKey.getInstace().user_id));
            param.put(ResponseKey.getInstace().first_leader, et_invite_code.getText().toString().trim());

            ipresenter.onSubmitInviteCode(param);

        } catch (Exception e) {
            ToastUtils.onToast(e.getMessage());
            e.printStackTrace();
        }
    }

    @Override
    public void onResponseData(Map<String, Object> result) {

        String msg = (String) result.get(ResponseKey.getInstace().rsp_msg);
        SharedTools.getInstance(getActivity()).onPutData(ResponseKey.getInstace().first_leader, et_invite_code.getText().toString());
        ToastUtils.onToast(msg);
        mFunctionsManage.invokeFunction(INTERFACE_BACK);
    }
}
