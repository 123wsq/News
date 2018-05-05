package com.yc.wsq.app.news.mvp.presenter;

import com.wsq.library.tools.ToastUtils;
import com.yc.wsq.app.news.mvp.callback.Callback;
import com.yc.wsq.app.news.mvp.model.impl.RequestHttpImpl;
import com.yc.wsq.app.news.mvp.model.inter.RequestHttpInter;
import com.yc.wsq.app.news.mvp.view.BaseView;
import com.yc.wsq.app.news.mvp.view.ShopView;

import java.util.Map;

public class ShopPresenter<T extends BaseView> extends BasePresenter<T> {


    private RequestHttpInter requestHttp;
    public ShopPresenter(){
        requestHttp = new RequestHttpImpl();
    }

    /**
     * 获取商品列表
     * @param param
     */
    public void onGetShopList(Map<String, String> param) throws Exception {

        final ShopView view = (ShopView) getView();
        if (view != null){
            //参数验证
            try {
//                ParamValidate.getInstance().onValidateUserName(param.get(ResponseKey.getInstace().user_name));
//                ParamValidate.getInstance().onValidateUserPsd(param.get(ResponseKey.getInstace().user_psd));
            }catch (Exception e){
                ToastUtils.onToast(e.getMessage());
                throw new Exception(e.getMessage());
            }
            view.showLoadding();
            requestHttp.onSendGet("", param, new Callback<Map<String, Object>>() {
                @Override
                public void onSuccess(Map<String, Object> data) {
                    if (view != null){
                       view.onShopResponse(data);
                   }
                }

                @Override
                public void onFailure(String msg) {
                    ToastUtils.onToast(msg);
                }

                @Override
                public void onOutTime(String msg) {
                    ToastUtils.onToast(msg);
                    if (view != null)
                        view.onReLogin();
                }



                @Override
                public void onComplete() {
                    if (view != null){
                       view.dismissLoadding();
                   }
                }
            });

        }
    }
}