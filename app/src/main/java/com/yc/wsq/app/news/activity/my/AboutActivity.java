package com.yc.wsq.app.news.activity.my;

import android.content.Intent;
import android.net.Uri;
import android.view.View;
import android.widget.TextView;

import com.wsq.library.utils.AppManager;
import com.yc.wsq.app.news.R;
import com.yc.wsq.app.news.base.BaseActivity;
import com.yc.wsq.app.news.base.BaseFragment;
import com.yc.wsq.app.news.mvp.presenter.BasePresenter;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 关于
 */
public class AboutActivity extends BaseActivity{


    @BindView(R.id.tv_title) TextView tv_title;
    @BindView(R.id.tv_app_name) TextView tv_app_name;
    @BindView(R.id.tv_app_version) TextView tv_app_version;
    @BindView(R.id.tv_service) TextView tv_service;
    @BindView(R.id.tv_qq) TextView tv_qq;

    private String tel = "0532-84852600";
    private String qq= "837113866";

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
        tv_service.setText(tel);
        tv_qq.setText(qq);
    }

    @OnClick({R.id.ll_back, R.id.tv_qq})
    public void onClick(View view){
        switch (view.getId()){
            case R.id.ll_back:
                finish();
                break;
            case R.id.tv_qq:
                String url="mqqwpa://im/chat?chat_type=wpa&uin="+qq;
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(url)));
                break;
        }
    }
}
