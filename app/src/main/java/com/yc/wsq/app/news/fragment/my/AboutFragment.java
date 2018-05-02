package com.yc.wsq.app.news.fragment.my;

import android.view.View;
import android.widget.TextView;

import com.wsq.library.utils.AppManager;
import com.yc.wsq.app.news.R;
import com.yc.wsq.app.news.base.BaseFragment;
import com.yc.wsq.app.news.mvp.presenter.BasePresenter;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 关于
 */
public class AboutFragment extends BaseFragment{

    public static final String TAG = AboutFragment.class.getName();

    @BindView(R.id.tv_title) TextView tv_title;
    @BindView(R.id.tv_app_name) TextView tv_app_name;
    @BindView(R.id.tv_app_version) TextView tv_app_version;
    @BindView(R.id.tv_service) TextView tv_service;

    @Override
    protected BasePresenter createPresenter() {
        return null;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.layout_fragment_about;
    }

    @Override
    protected void initView() {

        tv_title.setText(getResources().getString(R.string.str_about_text));
        tv_app_name.setText(AppManager.getAppName());
        tv_app_version.setText(AppManager.getAppVersionName());
        tv_service.setText("13200988978");
    }

    @OnClick({R.id.ll_back})
    public void onClick(View view){
        switch (view.getId()){
            case R.id.ll_back:
                mFunctionsManage.invokeFunction(INTERFACE_BACK);
                break;
        }
    }
}
