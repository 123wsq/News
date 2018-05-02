package com.yc.wsq.app.news.mvp.presenter;

import com.wsq.library.tools.ToastUtils;
import com.yc.wsq.app.news.mvp.callback.Callback;
import com.yc.wsq.app.news.mvp.model.impl.BenefitModelImpl;
import com.yc.wsq.app.news.mvp.model.impl.ShopModelImpl;
import com.yc.wsq.app.news.mvp.model.inter.BenefitModelInter;
import com.yc.wsq.app.news.mvp.model.inter.ShopModelInter;
import com.yc.wsq.app.news.mvp.view.BaseView;
import com.yc.wsq.app.news.mvp.view.BenefitView;
import com.yc.wsq.app.news.mvp.view.ShopView;

import java.util.Map;

public class ShopPresenter<T extends BaseView> extends BasePresenter<T> {

    private ShopModelInter shopModel;

    public ShopPresenter(){
        shopModel = new ShopModelImpl();
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
            shopModel.onGetShopList("", param, new Callback<Map<String, Object>>() {
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
               public void onError() {
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