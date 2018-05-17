package com.yc.wsq.app.news.base;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.wsq.library.tools.DialogTools;
import com.wsq.library.views.view.LoadingDialog;
import com.yc.wsq.app.news.activity.LoginActivity;
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

    @Override
    public void onReLogin() {

        startActivity(new Intent(this, LoginActivity.class));
    }

    /**
     * 一个按钮
     * @param title
     * @param msg
     * @param listener
     */
    public void onShowDialog(String title, String msg, final OnDialogClickListener listener){

        DialogTools.showDialog(this, "确定", title, msg, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                if (listener != null){
                    listener.onClickListener();
                }
                dialog.dismiss();
            }
        });
    }

    /**
     * 两个按钮
     * @param ok
     * @param cancel
     * @param title
     * @param msg
     */
    public void onShowDialog(String ok, String cancel, String title, String msg, DialogInterface.OnClickListener okListener, DialogInterface.OnClickListener cancleListener){
        DialogTools.showDialog(this, ok, cancel, title, msg, okListener, cancleListener);
    }

    public interface  OnDialogClickListener{

        void onClickListener();
    }
}
