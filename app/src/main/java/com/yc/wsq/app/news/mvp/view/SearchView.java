package com.yc.wsq.app.news.mvp.view;

import com.yc.wsq.app.news.bean.SearchBean;

import java.util.List;
import java.util.Map;

public interface SearchView extends BaseView{

    /**
     * 加载热门
     * @param result
     */
    void onLoadHotSearch(Map<String, Object> result);

    /**
     * 搜索返回
     * @param result
     */
    void onResponseSearch(Map<String, Object> result);

    /**
     * 搜索记录
     */

    void onLoadNativeSearchRecord(List<SearchBean> list);


}
