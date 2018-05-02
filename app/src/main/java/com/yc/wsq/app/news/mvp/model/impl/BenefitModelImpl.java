package com.yc.wsq.app.news.mvp.model.impl;

import com.yc.wsq.app.news.constant.ResponseKey;
import com.yc.wsq.app.news.mvp.callback.Callback;
import com.yc.wsq.app.news.mvp.model.inter.BenefitModelInter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BenefitModelImpl implements BenefitModelInter{
    @Override
    public void onGetBenefitList(String url, Map<String, String> param, Callback<Map<String, Object>> callback) throws Exception {

        List<Map<String, Object>> list = new ArrayList<>();
        Map<String, Object> data = new HashMap<>();
        data.put(ResponseKey.getInstace().rsp_code, "200");
        data.put(ResponseKey.getInstace().rsp_msg, "请求成功");

        for (int i = 0; i < 10; i++) {
            Map<String, Object> map = new HashMap<>();
            map.put(ResponseKey.getInstace().benefit_header, "http://img0.imgtn.bdimg.com/it/u=902957917,3639658485&fm=27&gp=0.jpg");
            map.put(ResponseKey.getInstace().title, "东方斯卡");
            map.put(ResponseKey.getInstace().benefit_desc, "这个东西真是太好了");
            map.put(ResponseKey.getInstace().benefit_time, "04-24");
            list.add(map);
        }
        data.put(ResponseKey.getInstace().benefit, list);

        callback.onSuccess(data);
        callback.onComplete();
    }

    @Override
    public void onGetBenefitInfo(String url, Map<String, String> param, Callback<Map<String, Object>> callback) throws Exception {


    }
}
