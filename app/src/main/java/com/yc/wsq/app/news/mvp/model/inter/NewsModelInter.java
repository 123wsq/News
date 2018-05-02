package com.yc.wsq.app.news.mvp.model.inter;

import com.yc.wsq.app.news.bean.SearchBean;
import com.yc.wsq.app.news.mvp.callback.Callback;

import java.util.List;
import java.util.Map;

public interface NewsModelInter {

    /**
     * 获取热门搜索
     * @param url
     * @param param
     * @param callback
     * @throws Exception
     */
    void onGetHotSearch(Callback<Map<String, Object>> callback) throws Exception;

    /**
     * 获取本地搜索记录
     * @param callback
     * @throws Exception
     */
    void onGetNativeSearchRecord(Callback<List<SearchBean>> callback) throws Exception;

    /**
     * 删除记录
     * @param id
     * @throws Exception
     */
    void onRemoveNativeSearchRecord(int id) throws Exception;


    /**
     * 清空记录
     * @throws Exception
     */
    void onRemoveAllNativeSearchRecord() throws Exception;

}
