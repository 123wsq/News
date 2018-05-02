package com.yc.wsq.app.news.okhttp.listener;

import java.util.Map;

/**
 * Created by wsq on 2018/1/4.
 */

public interface OnBaseUpFileHttpResponseListener {


    /**
     *
     * 上传成功
     * @param result
     */
    void onSuccess(Map<String, Object> result);

    /**
     * 上传进度
     * @param total
     * @param progress
     */
    void onProgress(long total, float progress);
    /**
     * 请求失败
     */
    void onFailure(Map<String, Object> result);


}
