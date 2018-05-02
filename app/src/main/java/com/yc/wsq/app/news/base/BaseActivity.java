package com.yc.wsq.app.news.base;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.wsq.library.views.view.LoadingDialog;
import com.yc.wsq.app.news.mvp.presenter.BasePresenter;
import com.yc.wsq.app.news.mvp.view.BaseView;

import butterknife.ButterKnife;

public abstract class BaseActivity<V, T extends BasePresenter<V >> extends AppCompatActivity implements BaseView {

    protected T ipresenter;
    private LoadingDialog dialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        setContentView(getLayoutId());
        ipresenter = createPresenter();
        if (ipresenter != null) {
            ipresenter.attachView((V) this);
        }
        dialog = new LoadingDialog(this);
        ButterKnife.bind(this);

        initView();
    }

    protected abstract T createPresenter();

    protected abstract int getLayoutId();

    protected abstract void initView();


    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (ipresenter != null) {
            ipresenter.deachView();
        }
    }

    @Override
    public void showLoadding() {
        if (!dialog.isShowing()) dialog.show();
    }

    @Override
    public void dismissLoadding() {
        if (dialog.isShowing())dialog.dismiss();
    }

    @Override
    public void loadFail(String errorMsg) {

    }

    @Override
    public Context getContext() {
        return this;
    }
}
