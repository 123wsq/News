package com.yc.wsq.app.news.mvp.view;

import java.util.Map;

public interface UserRegisterView extends BaseView{


    /**
     * 获取验证码
     * @param result
     */
    void onGetValidateData(Map<String, Object> result);

    /**
     * 注册
     * @param result
     */
    void onRegisterData(Map<String, Object> result);
}