package com.yc.wsq.app.news.mvp.presenter;

import com.wsq.library.tools.ToastUtils;
import com.yc.wsq.app.news.constant.Urls;
import com.yc.wsq.app.news.mvp.callback.Callback;
import com.yc.wsq.app.news.mvp.model.impl.BenefitModelImpl;
import com.yc.wsq.app.news.mvp.model.impl.NewsModelImpl;
import com.yc.wsq.app.news.mvp.model.impl.RequestHttpImpl;
import com.yc.wsq.app.news.mvp.model.inter.BenefitModelInter;
import com.yc.wsq.app.news.mvp.model.inter.NewsModelInter;
import com.yc.wsq.app.news.mvp.model.inter.RequestHttpInter;
import com.yc.wsq.app.news.mvp.view.BaseView;
import com.yc.wsq.app.news.mvp.view.BenefitView;
import com.yc.wsq.app.news.mvp.view.NewsView;

import java.util.Map;

public class BenefitPresenter<T extends BaseView> extends BasePresenter<T> {

    private RequestHttpInter requestHttp;

    public BenefitPresenter(){
        requestHttp = new RequestHttpImpl();
    }

    /**
     * 获取公益列表
     * @param param
     */
    public void onGetBenefitList(Map<String, String> param) throws Exception {

        final BenefitView view = (BenefitView) getView();
        if (view != null){

            String url = Urls.HOST+Urls.BENEFIT_LIST;
            requestHttp.onSendGet(url, param, new Callback<Map<String, Object>>() {
                @Override
                public void onSuccess(Map<String, Object> data) {
                    if (view != null){
                        view.onBenefitResponse(data);
                    }
                }

                @Override
                public void onFailure(String msg) {
                    ToastUtils.onToast(msg);
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