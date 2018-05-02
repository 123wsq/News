package com.yc.wsq.app.news.mvp.model.inter;

import com.yc.wsq.app.news.mvp.callback.Callback;

import java.util.Map;

public interface BenefitModelInter {

    /**
     * 获取公益列表
     * @param url
     * @param param
     * @param callback
     * @throws Exception
     */
    void onGetBenefitList(String url, Map<String, String> param, Callback<Map<String, Object>> callback) throws Exception;

    /**
     * 获取公益详情
     * @param url
     * @param param
     * @param callback
     * @throws Exception
     */
    void onGetBenefitInfo(String url, Map<String, String> param, Callback<Map<String, Object>> callback) throws Exception;
}
