package com.yc.wsq.app.news.mvp.view;

import java.util.Map;

public interface NewsDetailsView extends BaseView{

    
    void onReadNewsResponse(Map<String, Object> result);

    void onNewsStateResponse(Map<String, Object> result);

    void onNewsCollectResponse(Map<String, Object> result);
}
