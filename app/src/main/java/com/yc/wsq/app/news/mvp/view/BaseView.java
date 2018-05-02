package com.yc.wsq.app.news.mvp.view;

import android.content.Context;

/**
 * Created by Administrator on 2018/3/9 0009.
 */

public interface BaseView {

    void showLoadding();

    void dismissLoadding();

    void loadFail(String errorMsg);

    Context getContext();
}
