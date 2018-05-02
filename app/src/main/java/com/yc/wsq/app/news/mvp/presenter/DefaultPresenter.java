package com.yc.wsq.app.news.mvp.presenter;


import com.wsq.library.tools.ToastUtils;
import com.yc.wsq.app.news.mvp.callback.Callback;
import com.yc.wsq.app.news.mvp.model.impl.DefaultModelImpl;
import com.yc.wsq.app.news.mvp.model.inter.DefaultModelInter;
import com.yc.wsq.app.news.mvp.view.BaseView;
import com.yc.wsq.app.news.mvp.view.DefaultView;

import java.util.List;

/**
 * Created by Administrator on 2018/3/9 0009.
 */

public class DefaultPresenter<T extends BaseView> extends BasePresenter<T> {

    private DefaultModelInter defaultModel;
    public DefaultPresenter() {
        defaultModel = new DefaultModelImpl();
    }

    public void showData() throws Exception {
        final DefaultView view = (DefaultView) getView();
        if (view!=null){
            defaultModel.showData(new Callback<List<String>>() {
                @Override
                public void onSuccess(List<String> data) {
                    view.showData(data);
                }

                @Override
                public void onFailure(String msg) {

                }

                @Override
                public void onError() {

                }

                @Override
                public void onComplete() {

                }
            });
        }
    }


}
