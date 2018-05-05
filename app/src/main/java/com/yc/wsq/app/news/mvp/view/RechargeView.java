package com.yc.wsq.app.news.mvp.view;

import java.util.Map;

public interface RechargeView extends BaseView{


    /**
     * 充值信息获取
     * @param result
     */
    void onGetRechargeResponse(Map<String, Object> result);

    /**
     * 充值通知服务器返回
     * @param result
     */
    void onNotificationResponse(Map<String, Object> result);
}
