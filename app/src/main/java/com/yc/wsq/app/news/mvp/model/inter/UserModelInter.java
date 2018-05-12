package com.yc.wsq.app.news.mvp.model.inter;

import com.yc.wsq.app.news.mvp.callback.Callback;

import java.util.Map;

public interface UserModelInter {

    /**
     * 用户登录
     * @param url
     * @param param
     * @throws Exception
     */
    void onLoginUser(String url, Map<String, String> param, Callback<Map<String, Object>> callback) throws Exception;

    /**
     * 用户登出
     * @param url
     * @param param
     * @throws Exception
     */
    void onLogoutUser(String url, Map<String, String> param, Callback<Map<String, Object>> callback) throws Exception;

    /**
     * 用户注册
     * @param url
     * @param param
     * @throws Exception
     */
    void onRegister(String url, Map<String, String> param, Callback<Map<String, Object>> callback) throws  Exception;


    /**
     * 获取验证码
     * @param url
     * @param param
     * @param callback
     * @throws Exception
     */
    void onGetValidateCode(String url, Map<String, String> param, Callback<Map<String, Object>> callback) throws  Exception;




}