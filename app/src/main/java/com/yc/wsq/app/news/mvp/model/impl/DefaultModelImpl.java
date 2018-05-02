package com.yc.wsq.app.news.mvp.model.impl;

import com.yc.wsq.app.news.mvp.callback.Callback;
import com.yc.wsq.app.news.mvp.model.inter.DefaultModelInter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2018/3/9 0009.
 */

public class DefaultModelImpl implements DefaultModelInter {
    @Override
    public void showData(Callback<List<String>> callback) {
        List<String> list = new ArrayList<>();
        list.add("RecycView");
        list.add("水印");
        list.add("索引");
        list.add("城市");
        list.add("Utils验证");
        list.add("万能接口");
        callback.onSuccess(list);
        callback.onComplete();
    }


}
