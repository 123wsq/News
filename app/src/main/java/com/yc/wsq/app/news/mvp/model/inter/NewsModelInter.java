package com.yc.wsq.app.news.mvp.model.inter;

import com.yc.wsq.app.news.bean.SearchBean;
import com.yc.wsq.app.news.mvp.callback.Callback;

import java.util.List;
import java.util.Map;

public interface NewsModelInter {


    /**
     * 获取本地新闻
     * @return
     * @throws Exception
     */
    Map<String, Object> onGetNativeNewsData(Map<String, String> param) throws Exception;

    /**
     * 获取本地新闻类型
     * @return
     * @throws Exception
     */
    Map<String, Object> onGetNativeNewsTypeData(Map<String, String> param) throws Exception;

    /**
     * 保存到本地新闻
     * @return
     * @throws Exception
     */
    void onSaveNativeNewsData(Map<String, String> param, Map<String, Object> resultData) throws Exception;

    /**
     * 保存本地新闻类型
     * @return
     * @throws Exception
     */
    void onSaveNativeNewsTypeData(Map<String, String> param, Map<String, Object> resultData) throws Exception;



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
