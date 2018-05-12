package com.yc.wsq.app.news.mvp.view;

import java.util.Map;

public interface NewsDetailsView extends BaseView{


    /**
     * 新闻阅读积分返回
     * @param result
     */
    void onReadNewsResponse(Map<String, Object> result);

    /**
     * 获取新闻状态返回
     * @param result
     */
    void onNewsStateResponse(Map<String, Object> result);

    /**
     * 新闻收藏返回
     * @param result
     */
    void onNewsCollectResponse(Map<String, Object> result);

    /**
     * 文章评论返回
     * @param result
     */
    void onArticleCommentResponse(Map<String, Object> result);
}
