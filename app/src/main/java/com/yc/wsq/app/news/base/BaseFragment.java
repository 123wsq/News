package com.yc.wsq.app.news.base;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.wsq.library.struct.FunctionsManage;
import com.wsq.library.views.view.LoadingDialog;
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

        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView();
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
        if (context instanceof MainActivity){
            MainActivity mBaseActivity = (MainActivity) context;

            try {
                if (getTag() != null)
                mBaseActivity.setFunctionsForFragment(getTag());
            }catch (Exception e){
                e.printStackTrace();
            }

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
    public void onResume() {
        super.onResume();
        Log.d(TAG, "执行时间： "+(System.currentTimeMillis()- start_Time)+" ms");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (ipresenter != null) {
            ipresenter.deachView();
        }
    }
}
