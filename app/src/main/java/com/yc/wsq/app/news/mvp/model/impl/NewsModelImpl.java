package com.yc.wsq.app.news.mvp.model.impl;

import com.yc.wsq.app.news.bean.SearchBean;
import com.yc.wsq.app.news.constant.ResponseKey;
import com.yc.wsq.app.news.mvp.callback.Callback;
import com.yc.wsq.app.news.mvp.model.inter.NewsModelInter;

import org.litepal.crud.DataSupport;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class NewsModelImpl implements NewsModelInter {

    /**
     * 热门搜索
     * @param callback
     * @throws Exception
     */
    @Override
    public void onGetHotSearch(Callback<Map<String, Object>> callback) throws Exception {

        Map<String, Object> result = new HashMap<>();
        List<String> list = new ArrayList<>();
        list.add("楚雄  冰雹");
        list.add("星巴克关闭店面");
        list.add("魅族内讧");
        list.add("学生刮蹭百万豪车");
        list.add("老布什夫人去世");
        result.put(ResponseKey.getInstace().data, list);

        callback.onSuccess(result);
        callback.onComplete();
    }

    /**
     * 获取本地搜索记录
     * @param callback
     * @throws Exception
     */
    @Override
    public void onGetNativeSearchRecord(Callback<List<SearchBean>> callback) throws Exception {
        List<SearchBean> list = DataSupport.findAll(SearchBean.class);
        callback.onSuccess(list);
        callback.onComplete();
    }

    @Override
    public void onRemoveNativeSearchRecord(int id) throws Exception {
        DataSupport.delete(SearchBean.class, id);
    }

    @Override
    public void onRemoveAllNativeSearchRecord() throws Exception {

        DataSupport.deleteAll(SearchBean.class);
    }
}
