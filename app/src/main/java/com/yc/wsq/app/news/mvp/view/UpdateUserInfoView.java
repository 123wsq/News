package com.yc.wsq.app.news.mvp.view;

import java.util.Map;

public interface UpdateUserInfoView extends BaseView{

    void onUserLogOutResponseData(Map<String, Object> result);

    void onUpdateHeaderResponseData(Map<String, Object> result);

    void onUpdateUserResponseData(Map<String, Object> result);
}
