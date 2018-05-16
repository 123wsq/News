package com.yc.wsq.app.news.mvp.view;

import java.util.List;
import java.util.Map;

public interface HomeNewsView extends BaseView{


    /**
     * 加载网络新闻
     * @param result
     */
    void onNewsResponse(Map<String, Object> result);

    /**
     * 加载网络中新闻类型
     * @param result
     */
    void onNewsTypeResponse(Map<String, Object> result);

    /**
     * 加载本地新闻
     * @param result
     */
    void onLoadNativeNewsData(List<Map<String, Object>> result);

    /**
     * 加载本地新闻类型
     * @param result
     */
    void onLoadNativeNewsTypeData(List<Map<String, Object>> result);

}
