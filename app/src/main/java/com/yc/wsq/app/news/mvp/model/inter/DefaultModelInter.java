package com.yc.wsq.app.news.mvp.model.inter;


import com.yc.wsq.app.news.mvp.callback.Callback;

import java.util.List;

/**
 * Created by Administrator on 2018/3/9 0009.
 */

public interface DefaultModelInter {

    void showData(Callback<List<String>> callback) throws Exception;

}
