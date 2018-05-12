package com.yc.wsq.app.news.mvp.model.impl;

import com.orhanobut.logger.Logger;
import com.yc.wsq.app.news.constant.ResponseKey;
import com.yc.wsq.app.news.mvp.callback.Callback;
import com.yc.wsq.app.news.mvp.model.inter.UserModelInter;
import com.yc.wsq.app.news.okhttp.CallBackUtil;
import com.yc.wsq.app.news.okhttp.OkhttpUtil;
import com.yc.wsq.app.news.tools.ParamFormat;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Call;

public class UserModelImpl  implements UserModelInter{

    /**
     * 用户登录
     * @param url
     * @param param
     * @throws Exception
     */
    @Override
    public void onLoginUser(String url, Map<String, String> param, Callback<Map<String, Object>> callback) throws Exception {


        callback.onSuccess(null);
        callback.onComplete();
    }

    /**
     * 用户登出
     * @param url
     * @param param
     * @throws Exception
     */
    @Override
    public void onLogoutUser(String url, Map<String, String> param, Callback<Map<String, Object>> callback) throws Exception {

    }

    /**
     * 用户注册
     * @param url
     * @param param
     * @throws Exception
     */
    @Override
    public void onRegister(String url, Map<String, String> param, final Callback<Map<String, Object>> callback) throws Exception {

        OkhttpUtil.okHttpPost(url, param, new CallBackUtil.CallBackString() {
            @Override
            public void onFailure(Call call, Exception e) {
                callback.onFailure("请求失败");
                callback.onComplete();
            }

            @Override
            public void onResponse(String s) {

                Logger.d("Response  "+s);
                try {
                    Map<String, Object> result = ParamFormat.onAllJsonToMap(s);
                    callback.onSuccess(result);
                } catch (Exception e) {
                    callback.onFailure("错误的数据格式");
                    e.printStackTrace();
                }
                callback.onComplete();
            }
        });
    }

    /**
     * 获取验证码
     * @param url
     * @param param
     * @param callback
     * @throws Exception
     */
    @Override
    public void onGetValidateCode(String url, Map<String, String> param, final Callback<Map<String, Object>> callback) throws Exception {

        OkhttpUtil.okHttpPost(url, param, new CallBackUtil.CallBackString() {
            @Override
            public void onFailure(Call call, Exception e) {

                callback.onFailure("请求失败");
                callback.onComplete();
                e.printStackTrace();
            }

            @Override
            public void onResponse(String s) {

                Logger.d("Response  "+s);
                try {
                    Map<String, Object> result = ParamFormat.onAllJsonToMap(s);
                    callback.onSuccess(result);

                } catch (Exception e) {
                    callback.onFailure("错误的格式");
                    e.printStackTrace();
                }
                callback.onComplete();
            }

        });
    }


}