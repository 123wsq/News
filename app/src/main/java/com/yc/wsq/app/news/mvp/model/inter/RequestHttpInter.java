package com.yc.wsq.app.news.mvp.model.inter;

import com.yc.wsq.app.news.mvp.callback.Callback;

import java.util.Map;

public interface RequestHttpInter {


    /**
     * 发送get请求
     * @param url
     * @param param
     * @param callback
     * @throws Exception
     */
    void onSendGet(String url, Map<String, String> param, Callback<Map<String, Object>> callback) throws Exception;


    /**
     * 发送Post请求
     * @param url
     * @param param
     * @param callback
     * @throws Exception
     */
    void onSendPost(String url, Map<String, String> param, Callback<Map<String, Object>> callback) throws Exception;



}
