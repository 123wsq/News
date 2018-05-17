package com.yc.wsq.app.news.loader;

import cn.sharesdk.framework.Platform;

public interface OnAuthLoginListener {

    /**
     *
     * @param state  0 成功  1失败   1取消
     * @param platform
     */
    void onAuthState(int state, Platform platform);
}
