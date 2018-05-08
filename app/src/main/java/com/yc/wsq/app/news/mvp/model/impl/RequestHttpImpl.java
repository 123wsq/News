package com.yc.wsq.app.news.mvp.model.impl;

import android.graphics.Bitmap;

import com.orhanobut.logger.Logger;
import com.yc.wsq.app.news.constant.ResponseKey;
import com.yc.wsq.app.news.mvp.callback.Callback;
import com.yc.wsq.app.news.mvp.model.inter.RequestHttpInter;
import com.yc.wsq.app.news.okhttp.CallBackUtil;
import com.yc.wsq.app.news.okhttp.OkhttpUtil;
import com.yc.wsq.app.news.tools.ParamFormat;

import java.util.Map;

import okhttp3.Call;
import okhttp3.OkHttpClient;

public class RequestHttpImpl implements RequestHttpInter{

    /**
     *   网络请求参数说明
     *    status = -1  请求失败
     *           = 0   登录超时
     *           = 1   请求成功
     */


    @Override
    public void onSendGet(String url, Map<String, String> param, final Callback<Map<String, Object>> callback) throws Exception {

        Logger.d("请求地址:" + url+", param"+ param);

        OkhttpUtil.okHttpGet(url, param, new CallBackUtil.CallBackString() {
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
                    int status = (int) result.get(ResponseKey.getInstace().rsp_status);
                    switch (status){
                        case -1:
                            callback.onFailure(result.get(ResponseKey.getInstace().rsp_msg)+"");
                            break;
                        case 0:
                            callback.onOutTime(result.get(ResponseKey.getInstace().rsp_msg)+"");
                            break;
                            default:
                                callback.onSuccess(result);
                                break;
                    }

                } catch (Exception e) {
                    callback.onFailure("错误的数据格式");
                    e.printStackTrace();
                }
                callback.onComplete();
            }
        });
    }

    @Override
    public void onSendPost(String url, Map<String, String> param, final Callback<Map<String, Object>> callback) throws Exception {

        Logger.d("请求地址:" + url+", param"+ param);
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
                    int status = (int) result.get(ResponseKey.getInstace().rsp_status);
                    switch (status){
                        case -1:
                            callback.onFailure(result.get(ResponseKey.getInstace().rsp_msg)+"");
                            break;
                        case 0:
                            callback.onOutTime(result.get(ResponseKey.getInstace().rsp_msg)+"");
                            break;
                        case 1:
                            callback.onSuccess(result);
                            break;
                        default:

                            break;
                    }

                } catch (Exception e) {
                    callback.onFailure("错误的数据格式");
                    e.printStackTrace();
                }
                callback.onComplete();
            }
        });
    }

}
