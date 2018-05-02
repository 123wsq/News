package com.yc.wsq.app.news.mvp.view;

import java.util.Map;

public interface UserView extends BaseView{

    void onResponseData(Map<String, Object> result);
}