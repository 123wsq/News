package com.yc.wsq.app.news.mvp.model.inter;

import com.yc.wsq.app.news.mvp.callback.Callback;

import java.util.Map;

public interface ShopModelInter {

    /**
     * 获取商品列表
     * @param url
     * @param param
     * @param callback
     * @throws Exception
     */
    void onGetShopList(String url, Map<String, String> param, Callback<Map<String, Object>> callback) throws Exception;


}
