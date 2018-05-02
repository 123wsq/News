package com.yc.wsq.app.news.okhttp.listener;

import java.util.Map;

/**
 * Http请求相应
 * Created by wsq on 2018/1/3.
 */

public interface OnBaseHttpResponseListener {

    /**
     *
     * 请求成功
     * @param result
     */
    void onSuccess(Map<String, Object> result);


    /**
     * 请求失败
     */
    void onFailure(Map<String, Object> result);


}
