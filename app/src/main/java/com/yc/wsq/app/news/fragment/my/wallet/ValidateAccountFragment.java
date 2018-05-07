package com.yc.wsq.app.news.fragment.my.wallet;


import android.view.View;
import android.widget.TextView;

import com.yc.wsq.app.news.base.BaseFragment;
import com.yc.wsq.app.news.mvp.presenter.UserPresenter;
import com.yc.wsq.app.news.mvp.view.UserView;
import com.yc.wsq.app.news.R;

import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;

public class ValidateAccountFragment extends BaseFragment<UserView, UserPresenter<UserView>> implements UserView{

    public static final String TAG = ValidateAccountFragment.class.getName();

    public static final String INTERFACE_WITHP = TAG +_INTERFACE_WITHP;

    @BindView(R.id.tv_title)
    TextView tv_title;

    @Override
    protected UserPresenter<UserView> createPresenter() {
        return null;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.layout_fragment_validate_account;
    }

    @Override
    protected void initView() {

        tv_title.setText(getText(R.string.str_validate_account_text));
    }

    @OnClick({R.id.ll_back, R.id.tv_next})
    public void onClick(View view){
        switch (view.getId()){
            case R.id.ll_back:

                mFunctionsManage.invokeFunction(INTERFACE_BACK);
                break;
            case R.id.tv_next: //下一步， param =1
                mFunctionsManage.invokeFunction(INTERFACE_WITHP, 1);
                break;
        }
    }


    @Override
    public void onResponseData(Map<String, Object> result) {

    }
}
