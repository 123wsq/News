package com.yc.wsq.app.news.mvp.view;

import java.util.Map;

public interface WithdrawView extends BaseView{


    /**
     * 申请提现返回
     * @param result
     */
    void onApplyWithdrawResponse(Map<String, Object> result);

    /**
     * 验证提现密码
     * @param result
     */
    void onValidateWithdrawPasswordResponse(Map<String, Object> result);
}
