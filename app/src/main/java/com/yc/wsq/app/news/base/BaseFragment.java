package com.yc.wsq.app.news.base;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.wsq.library.struct.FunctionsManage;
import com.wsq.library.tools.DialogTools;
import com.wsq.library.views.view.LoadingDialog;
import com.yc.wsq.app.news.activity.LoginActivity;
import com.yc.wsq.app.news.activity.MainActivity;
import com.yc.wsq.app.news.mvp.presenter.BasePresenter;
import com.yc.wsq.app.news.mvp.view.BaseView;


import butterknife.ButterKnife;

public abstract class BaseFragment<V, T extends BasePresenter<V>> extends Fragment implements BaseView {

    private final String TAG = BaseFragment.class.getName();
    public static String _INTERFACE_NPNR = "_NPNR";  //没参数没有返回值
    public static String _INTERFACE_WITHP = "_WITHP";  //只有参数
    public static String _INTERFACE_WITHR = "_WITHR";  //只有返回值
    public static String _INTERFACE_WITHPR = "_WITHPR";  //有参数有返回值

    /**
     * Fragment当前状态是否可见
     */
    protected boolean isVisible = false;
    /**
     * Fragment的view是否已创建
     */
    protected boolean mIsViewCreated = false;

    public static String INTERFACE_BACK = "com.example.wsq.androidutils.base.BaseFragment";

    public FunctionsManage mFunctionsManage;
    protected T ipresenter;
    private LoadingDialog dialog;
    private long start_Time;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        start_Time = System.currentTimeMillis();
        View view  = inflater.inflate(getLayoutId(), container, false);
        dialog = new LoadingDialog(getContext());
        ipresenter = createPresenter();
        if (ipresenter != null) {
            ipresenter.attachView((V) this);
        }
        ButterKnife.bind(this,view);
        mIsViewCreated = true;
        return view;
    }


    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
//        initPrepare();
    }

    protected  abstract T createPresenter();

    protected abstract int getLayoutId();

    protected abstract void initView();


    public void setFunctionsManager(FunctionsManage functionsManager){

        this.mFunctionsManage = functionsManager;
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

    }

    @Override
    public void onAttachFragment(Fragment childFragment) {
        super.onAttachFragment(childFragment);
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
    public void onReLogin() {
        startActivity(new Intent(getActivity(), LoginActivity.class));
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mIsViewCreated = false;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (ipresenter != null) {
            ipresenter.deachView();
        }
    }

    public void onShowDialog(String title, String msg, final OnDialogClickListener listener){

        DialogTools.showDialog(getActivity(), "确定", title, msg, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                if (listener != null){
                    listener.onClickListener();
                }
                dialog.dismiss();
            }
        });
    }

    public interface  OnDialogClickListener{

        void onClickListener();
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (!mIsViewCreated)//view没有创建的时候不进行操作
            return;

        if (getUserVisibleHint()) {
            if (!isVisible) {//确保在一个可见周期中只调用一次onVisible()
                isVisible = true;
                onVisible();
            }
        } else {
            if (isVisible) {
                isVisible = false;
                onHidden();
            }
        }
    }
    /**
     * 可见
     */
    protected void onVisible() {

    }

    /**
     * fragment不可见的时候操作,onPause的时候,以及不可见的时候调用
     */
    protected void onHidden() {

    }

    @Override
    public void onResume() {//和activity的onResume绑定，Fragment初始化的时候必调用，但切换fragment的hide和visible的时候可能不会调用！
        super.onResume();
        if (isAdded() && !isHidden()) {//用isVisible此时为false，因为mView.getWindowToken为null
            onVisible();
            isVisible = true;
        }
    }

    @Override
    public void onPause() {
        if (isVisible()||isVisible) {
            onHidden();
            isVisible = false;
        }
        super.onPause();
    }

    @Override
    public void onHiddenChanged(boolean hidden) {//默认fragment创建的时候是可见的，但是不会调用该方法！切换可见状态的时候会调用，但是调用onResume，onPause的时候却不会调用
        super.onHiddenChanged(hidden);
        if (!hidden) {
            onVisible();
            isVisible = true;
        } else {
            onHidden();
            isVisible = false;
        }
    }
}
