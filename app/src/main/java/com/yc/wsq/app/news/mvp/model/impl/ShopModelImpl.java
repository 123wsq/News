package com.yc.wsq.app.news.mvp.model.impl;

import com.yc.wsq.app.news.constant.ResponseKey;
import com.yc.wsq.app.news.mvp.callback.Callback;
import com.yc.wsq.app.news.mvp.model.inter.ShopModelInter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ShopModelImpl implements ShopModelInter{
    @Override
    public void onGetShopList(String url, Map<String, String> param, Callback<Map<String, Object>> callback) throws Exception {

        List<Map<String, Object>> list = new ArrayList<>();
        Map<String, Object> data = new HashMap<>();
        data.put(ResponseKey.getInstace().rsp_code, "200");
        data.put(ResponseKey.getInstace().rsp_msg, "请求成功");

        for (int i = 0; i < 10; i++) {
            Map<String, Object> map = new HashMap<>();
            map.put(ResponseKey.getInstace().title, "小米 红米 4A 移动合约版 2GB内存 16GB ROM 香槟金色 移动联通电信4G手机 双卡双待");
            map.put(ResponseKey.getInstace().images, "https://img20.360buyimg.com/vc/jfs/t3184/319/6842564732/166617/b108feaa/58ae9b6fNb75e9c6d.jpg");
            if (i % 2 == 0){
                map.put(ResponseKey.getInstace().goods_type,"新品推荐"+i);
            }
            list.add(map);
        }
        data.put(ResponseKey.getInstace().goods, list);

        callback.onSuccess(data);
        callback.onComplete();
    }
}
