package com.yc.wsq.app.news.mvp.model.impl;

import android.text.TextUtils;

import com.orhanobut.logger.Logger;
import com.yc.wsq.app.news.bean.CatTitleBean;
import com.yc.wsq.app.news.bean.NewsBean;
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


    @Override
    public Map<String, Object> onGetNativeNewsData(Map<String, String> param) throws Exception {

        String cat_id = param.get(ResponseKey.getInstace().cat_id);
        String max_id = param.get(ResponseKey.getInstace().max_id);
        String min_id = param.get(ResponseKey.getInstace().min_id);

        List<NewsBean> list = new ArrayList<>();
        //当所有的Id 都为空的情况下
        if(TextUtils.isEmpty(max_id) && TextUtils.isEmpty(min_id)){
            List<NewsBean> list1 = DataSupport.where(cat_id.equals("0") ? ResponseKey.getInstace().is_recommend+" = 1" : ResponseKey.getInstace().cat_id+" = "+cat_id)
                    .order(ResponseKey.getInstace().article_id+" desc").offset(0).limit(15).find(NewsBean.class);
            list.addAll(list1);
            //在max——id不为空的情况下  表示是刷新
        }else if(!TextUtils.isEmpty(max_id) && TextUtils.isEmpty(min_id)){
            List<NewsBean>  list2 = DataSupport.where((cat_id.equals("0") ? ResponseKey.getInstace().is_recommend+" = 1 and " : ResponseKey.getInstace().cat_id+" = "+cat_id+" and ") +ResponseKey.getInstace().article_id+" > "+ max_id)
                    .order(ResponseKey.getInstace().article_id+" desc").offset(0).limit(15).find(NewsBean.class);
            list.addAll(list2);
            //当min_id不为空的情况下，表示是加载更多
        }else if(TextUtils.isEmpty(max_id) && !TextUtils.isEmpty(min_id)){
            List<NewsBean> list3;
            if(cat_id.equals("0")){
                list3 = DataSupport.where(ResponseKey.getInstace().is_recommend+" = 1 and "+ ResponseKey.getInstace().article_id+" < "+ min_id)
                        .order(ResponseKey.getInstace().article_id+" desc").offset(0).limit(15).find(NewsBean.class);
            }else{
                list3 = DataSupport.where(ResponseKey.getInstace().cat_id+" = "+cat_id+" and " + ResponseKey.getInstace().article_id+" < "+ min_id)
                        .order(ResponseKey.getInstace().article_id+" desc").offset(0).limit(15).find(NewsBean.class);
            }
            list.addAll(list3);
        }

        List<Map<String, Object>> data = new ArrayList<>();
        for (int i = 0; i < list.size(); i++) {
            Map<String, Object> map = new HashMap<>();
            NewsBean bean = list.get(i);
            map.put(ResponseKey.getInstace().article_id, bean.getArticle_id());
            map.put(ResponseKey.getInstace().title, bean.getTitle());
            map.put(ResponseKey.getInstace().is_recommend, bean.getIs_recommend());
            map.put(ResponseKey.getInstace().click, bean.getClick());
            map.put(ResponseKey.getInstace().source, bean.getSource());
            map.put(ResponseKey.getInstace().thumb, bean.getThumb());
            map.put(ResponseKey.getInstace().isRead, bean.getIsRead());
            data.add(map);
        }
        Map<String, Object> map = new HashMap<>();
        map.put(ResponseKey.getInstace().data, data);
        return map;
    }

    @Override
    public Map<String, Object> onGetNativeNewsTypeData(Map<String, String> param) throws Exception {

        List<CatTitleBean> list = DataSupport.order(ResponseKey.getInstace().sort_order).find(CatTitleBean.class);
        Map<String, Object> data = new HashMap<>();
        List<Map<String, Object>> list1 = new ArrayList<>();
        for (int i = 0; i < list.size(); i++) {
            CatTitleBean bean = list.get(i);
            Map<String, Object> map = new HashMap<>();
            map.put(ResponseKey.getInstace().cat_id, bean.getCat_id());
            map.put(ResponseKey.getInstace().cat_name, bean.getCat_name());
            map.put(ResponseKey.getInstace().id, bean.getId());
            map.put(ResponseKey.getInstace().cat_type, bean.getCat_type());
            list1.add(map);
        }
        data.put(ResponseKey.getInstace().result, list1);
        return data;
    }

    @Override
    public void onSaveNativeNewsData(Map<String, String> param, Map<String, Object> resultData) throws Exception {

        List<Map<String, Object>> list = (List<Map<String, Object>>) resultData.get(ResponseKey.getInstace().data);
        for (int i = 0; i < list.size(); i++) {
            Map<String, Object> map = list.get(i);
            int article_id = (int) map.get(ResponseKey.getInstace().article_id);
            int count = DataSupport.where(ResponseKey.getInstace().article_id + " = "+ article_id).count(NewsBean.class);
            if (count == 0) {
                NewsBean bean = new NewsBean();
                bean.setArticle_id(article_id);
                bean.setTitle(map.get(ResponseKey.getInstace().title) + "");
                bean.setIs_recommend(map.get(ResponseKey.getInstace().is_recommend) + "");
                bean.setClick(map.get(ResponseKey.getInstace().click) + "");
                bean.setSource(map.get(ResponseKey.getInstace().source) + "");
                bean.setThumb(map.get(ResponseKey.getInstace().thumb) + "");
                bean.setCat_id(map.get(ResponseKey.getInstace().cat_id)+"");
                bean.setIsRead(0 + "");
                bean.save();
            }
        }
    }

    @Override
    public void onSaveNativeNewsTypeData(Map<String, String> param, Map<String, Object> resultData) throws Exception {

        List<Map<String, Object>> list = (List<Map<String, Object>>) resultData.get(ResponseKey.getInstace().result);
        for (int i = 0; i < list.size(); i++) {
            Map<String, Object> map = list.get(i);
            int cat_id = (int) map.get(ResponseKey.getInstace().cat_id);
            int count = DataSupport.where(ResponseKey.getInstace().cat_id+" = "+ cat_id).count(CatTitleBean.class);
            if(count==0){
                CatTitleBean bean = new CatTitleBean(cat_id, map.get(ResponseKey.getInstace().cat_name)+"",
                        map.get(ResponseKey.getInstace().id)+"", map.get(ResponseKey.getInstace().sort_order)+"", (int)map.get(ResponseKey.getInstace().cat_type));
                bean.save();
            }
        }
    }

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
