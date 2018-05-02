package com.yc.wsq.app.news.mvp.view;

import java.util.Map;

public interface NewsView extends BaseView{

    
    void onNewsResponse(Map<String, Object> result);

    void onNewsTypeResponse(Map<String, Object> result);
}
