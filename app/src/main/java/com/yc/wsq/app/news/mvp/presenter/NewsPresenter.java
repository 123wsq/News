package com.yc.wsq.app.news.mvp.presenter;

import android.text.TextUtils;

import com.orhanobut.logger.Logger;
import com.wsq.library.tools.ToastUtils;
import com.yc.wsq.app.news.bean.CatTitleBean;
import com.yc.wsq.app.news.bean.NewsBean;
import com.yc.wsq.app.news.bean.SearchBean;
import com.yc.wsq.app.news.constant.ResponseKey;
import com.yc.wsq.app.news.constant.Urls;
import com.yc.wsq.app.news.mvp.callback.Callback;
import com.yc.wsq.app.news.mvp.model.impl.NewsModelImpl;
import com.yc.wsq.app.news.mvp.model.impl.RequestHttpImpl;
import com.yc.wsq.app.news.mvp.model.inter.NewsModelInter;
import com.yc.wsq.app.news.mvp.model.inter.RequestHttpInter;
import com.yc.wsq.app.news.mvp.view.BaseView;
import com.yc.wsq.app.news.mvp.view.CollectView;
import com.yc.wsq.app.news.mvp.view.HomeNewsView;
import com.yc.wsq.app.news.mvp.view.NewsDetailsView;
import com.yc.wsq.app.news.mvp.view.NewsView;
import com.yc.wsq.app.news.mvp.view.SearchView;
import com.yc.wsq.app.news.mvp.view.UserView;
import com.yc.wsq.app.news.tools.NetworkUtils;
import com.yc.wsq.app.news.tools.ParamValidate;

import org.litepal.crud.DataSupport;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class NewsPresenter<T extends BaseView> extends BasePresenter<T> {

    private RequestHttpInter requestHttp;
    private NewsModelInter newsModel;

    public NewsPresenter(){
        requestHttp = new RequestHttpImpl();
        newsModel = new NewsModelImpl();
    }

    public void onGetNewsList(final Map<String, String> param) throws Exception {
        final NewsView view = (NewsView) getView();


        if (view!= null){
            if (!NetworkUtils.isNetworkAvailable(view.getContext())){

                //首先将缓存的数据返回
                view.onNewsResponse(newsModel.onGetNativeNewsData(param));
            }else {
                //开始获取网络数据
                String url = Urls.HOST + Urls.GET_NEWS_LIST;
                requestHttp.onSendPost(url, param, new Callback<Map<String, Object>>() {
                    @Override
                    public void onSuccess(Map<String, Object> data) {

                        if (view != null) {
                            try {
                                newsModel.onSaveNativeNewsData(param, data);
                                view.onNewsResponse(newsModel.onGetNativeNewsData(param));
                            } catch (Exception e) {

                                e.printStackTrace();
                            }

                        }
                    }

                    @Override
                    public void onFailure(String msg) {
                        ToastUtils.onToast(msg);
                    }

                    @Override
                    public void onOutTime(String msg) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });
            }
        }

    }



    /**
     * 获取新闻列表
     * @param param
     */
    public void onGetNewsLists(Map<String, String> param) throws Exception {

        final NewsView view = (NewsView) getView();
        if (view != null) {

            if (view != null) {
                String url = Urls.HOST + Urls.GET_NEWS_LIST;

                requestHttp.onSendPost(url, param, new Callback<Map<String, Object>>() {
                    @Override
                    public void onSuccess(Map<String, Object> data) {
                        if (view != null) {
                            List<Map<String, Object>> list = (List<Map<String, Object>>) data.get(ResponseKey.getInstace().data);
                            for (int i = 0; i < list.size(); i++) {
                                Map<String, Object> map = list.get(i);
                                int article_id = (int) map.get(ResponseKey.getInstace().article_id);
                                int count = DataSupport.where(ResponseKey.getInstace().article_id+"=?", article_id+"").count(NewsBean.class);
                                Logger.d(article_id+" 新闻个数："+count);
                                if(count == 0 ){
                                    NewsBean bean = new NewsBean();
                                    bean.setArticle_id(article_id);
                                    bean.setTitle(map.get(ResponseKey.getInstace().title)+"");
                                    bean.setIs_recommend(map.get(ResponseKey.getInstace().is_recommend) +"");
                                    bean.setClick( map.get(ResponseKey.getInstace().click) +"");
                                    bean.setSource(map.get(ResponseKey.getInstace().source) +"");
                                    bean.setThumb(map.get(ResponseKey.getInstace().thumb)+"");
                                    bean.setIsRead(0+"");
                                    bean.save();
                                }
                            }
                            view.onNewsResponse(data);
                        }
                    }

                    @Override
                    public void onFailure(String msg) {
                        ToastUtils.onToast(msg);
                    }

                    @Override
                    public void onOutTime(String msg) {

                        ToastUtils.onToast(msg);
                        if (view != null)
                        view.onReLogin();
                    }


                    @Override
                    public void onComplete() {

                    }
                });

            }
        }
    }



    /**
     * 新闻阅读加积分
     * @param param
     * @throws Exception
     */
    public void onReadIntegral(Map<String, String> param) throws Exception{
        final NewsDetailsView view = (NewsDetailsView) getView();
        if (view != null) {
            //参数验证
            try {
                ParamValidate.getInstance().onValidateIsNull(param.get(ResponseKey.getInstace().id));
                ParamValidate.getInstance().onValidateIsNull(param.get(ResponseKey.getInstace().uid));
                ParamValidate.getInstance().onValidateIsNull(param.get(ResponseKey.getInstace().token));
            } catch (Exception e) {
                throw new Exception(e.getMessage());
            }
            if (view != null) {
                String url = Urls.HOST + Urls.READ_INTEGRAL;

                requestHttp.onSendPost(url, param, new Callback<Map<String, Object>>() {
                    @Override
                    public void onSuccess(Map<String, Object> data) {
                        if (view != null) {
                            view.onReadNewsResponse(data);
                        }
                    }

                    @Override
                    public void onFailure(String msg) {
//                        ToastUtils.onToast(msg);
                    }

                    @Override
                    public void onOutTime(String msg) {
                        ToastUtils.onToast(msg);
//                        if (view != null)
//                            view.onReLogin();
                    }


                    @Override
                    public void onComplete() {

                    }
                });

            }
        }
    }

    public void onGetNewsType(final Map<String, String> param) throws Exception{
        final NewsView view = (NewsView) getView();

        if(view != null){
            //在没有网络的情况下读取本地数据
            if (!NetworkUtils.isNetworkAvailable(view.getContext())){

                view.onNewsTypeResponse(newsModel.onGetNativeNewsTypeData(param));
            }else{
                String url = Urls.HOST + Urls.GET_NEWS_TYPE;
                requestHttp.onSendGet(url, param, new Callback<Map<String, Object>>() {
                    @Override
                    public void onSuccess(Map<String, Object> data) {
                        if (view != null) {
                            try {
                                newsModel.onSaveNativeNewsTypeData(param, data);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            view.onNewsTypeResponse(data);
                        }
                    }

                    @Override
                    public void onFailure(String msg) {
                        ToastUtils.onToast(msg);
                    }

                    @Override
                    public void onOutTime(String msg) {
                        ToastUtils.onToast(msg);
                        if (view != null)
                            view.onReLogin();
                    }


                    @Override
                    public void onComplete() {

                    }
                });
            }
        }
    }

    /**
     * 获取新闻类型
     * @param param
     * @throws Exception
     */
    public void onGetNewsTypes(Map<String, String> param) throws Exception{
        final NewsView view = (NewsView) getView();
        if (view != null) {

            if (view != null) {
                String url = Urls.HOST + Urls.GET_NEWS_TYPE;

                requestHttp.onSendGet(url, param, new Callback<Map<String, Object>>() {
                    @Override
                    public void onSuccess(Map<String, Object> data) {
                        if (view != null) {
                            view.onNewsTypeResponse(data);
                        }
                    }

                    @Override
                    public void onFailure(String msg) {
                        ToastUtils.onToast(msg);
                    }

                    @Override
                    public void onOutTime(String msg) {
                        ToastUtils.onToast(msg);
                        if (view != null)
                            view.onReLogin();
                    }


                    @Override
                    public void onComplete() {

                    }
                });

            }
        }
    }


    /**
     * 搜索新闻
     * @param param
     * @throws Exception
     */
    public void onSearchNews(Map<String, String> param) throws Exception{
        final NewsView view = (NewsView) getView();
        if (view != null) {
            //参数验证
            try {
                ParamValidate.getInstance().onValidateKeyWords(param.get(ResponseKey.getInstace().keywords));
            } catch (Exception e) {
                throw new Exception(e.getMessage());
            }
            if (view != null) {
                view.showLoadding();
                String url = Urls.HOST + Urls.GET_NEWS_LIST;

                requestHttp.onSendPost(url, param, new Callback<Map<String, Object>>() {
                    @Override
                    public void onSuccess(Map<String, Object> data) {
                        if (view != null) {
                            view.onNewsResponse(data);
                        }
                    }

                    @Override
                    public void onFailure(String msg) {
                        ToastUtils.onToast(msg);
                    }

                    @Override
                    public void onOutTime(String msg) {
                        ToastUtils.onToast(msg);
                        if (view != null)
                            view.onReLogin();
                    }

                    @Override
                    public void onComplete() {
                        if (view != null) {
                            view.dismissLoadding();
                        }
                    }
                });

            }
        }
    }

    /**
     * 热门搜索
     * @param param
     * @throws Exception
     */
    public void onHotSearchNews(Map<String, String> param) throws Exception{
        final SearchView view = (SearchView) getView();


        if (view != null) {

            if (view != null) {
                newsModel.onGetHotSearch(new Callback<Map<String, Object>>() {
                    @Override
                    public void onSuccess(Map<String, Object> data) {
                        if (view != null) {
                            view.onLoadHotSearch(data);
                        }
                    }

                    @Override
                    public void onFailure(String msg) {
                        ToastUtils.onToast(msg);
                    }

                    @Override
                    public void onOutTime(String msg) {
                        ToastUtils.onToast(msg);
                        if (view != null)
                            view.onReLogin();
                    }


                    @Override
                    public void onComplete() {

                    }
                });

            }
        }
    }

    /**
     * 文章收藏
     * @param param
     * @throws Exception
     */
    public void onAddCollect(Map<String, String> param) throws Exception{
        final NewsDetailsView view = (NewsDetailsView) getView();
        if (view != null) {
            //参数验证
            try {
                ParamValidate.getInstance().onValidateIsNull(param.get(ResponseKey.getInstace().id));
                ParamValidate.getInstance().onValidateIsNull(param.get(ResponseKey.getInstace().uid));
                ParamValidate.getInstance().onValidateIsNull(param.get(ResponseKey.getInstace().token));
                ParamValidate.getInstance().onValidateIsNull(param.get(ResponseKey.getInstace().act));
            } catch (Exception e) {
                throw new Exception(e.getMessage());
            }

            view.showLoadding();
            String url = Urls.HOST +Urls.COLLOCT;
            requestHttp.onSendGet(url, param, new Callback<Map<String, Object>>() {
                @Override
                public void onSuccess(Map<String, Object> data) {

                    if (view !=null){
                            view.onNewsCollectResponse(data);
                    }
                }

                @Override
                public void onFailure(String msg) {
                    ToastUtils.onToast(msg);
                }

                @Override
                public void onOutTime(String msg) {
                    ToastUtils.onToast(msg);
                    if (view !=null)
                    view.onReLogin();
                }

                @Override
                public void onComplete() {

                    if (view !=null)
                        view.dismissLoadding();
                }
            });

        }
    }



    /**
     * 文章收藏状态获取
     * @param param
     * @throws Exception
     */
    public void onGetCollectState(Map<String, String> param) throws Exception{
        final NewsDetailsView view = (NewsDetailsView) getView();
        if (view != null) {
            //参数验证
            try {
                ParamValidate.getInstance().onValidateIsNull(param.get(ResponseKey.getInstace().id));
                ParamValidate.getInstance().onValidateIsNull(param.get(ResponseKey.getInstace().uid));
            } catch (Exception e) {
                throw new Exception(e.getMessage());
            }

            view.showLoadding();
            String url = Urls.HOST +Urls.COLLECT_STATE;
            requestHttp.onSendGet(url, param, new Callback<Map<String, Object>>() {
                @Override
                public void onSuccess(Map<String, Object> data) {

                    if (view !=null){
                        view.onNewsStateResponse(data);
                    }
                }

                @Override
                public void onFailure(String msg) {
//                    ToastUtils.onToast(msg);
                }

                @Override
                public void onOutTime(String msg) {
//                    ToastUtils.onToast(msg);
//                    if (view !=null)
//                        view.onReLogin();
                }

                @Override
                public void onComplete() {

                    if (view !=null)
                        view.dismissLoadding();
                }
            });

        }
    }

    /**
     * 收藏列表
     * @param param
     * @throws Exception
     */
    public void onGetCollectList(Map<String, String> param) throws Exception{

        final CollectView view = (CollectView) getView();

        try {
            ParamValidate.getInstance().onValidateIsNull(param.get(ResponseKey.getInstace().uid));
            ParamValidate.getInstance().onValidateIsNull(param.get(ResponseKey.getInstace().token));
        }catch (Exception e){
            throw new Exception(e.getMessage());
        }


        if (view != null) {
            view.showLoadding();
            String url = Urls.HOST +Urls.COLLECT_LIST;
            requestHttp.onSendPost(url, param, new Callback<Map<String, Object>>() {
                @Override
                public void onSuccess(Map<String, Object> data) {
                    if (view != null) {
                        view.onCollectListResponse(data);
                    }
                }

                @Override
                public void onFailure(String msg) {
                    ToastUtils.onToast(msg);
                }

                @Override
                public void onOutTime(String msg) {
                    ToastUtils.onToast(msg);
                    if (view != null)
                        view.onReLogin();
                }


                @Override
                public void onComplete() {
                    if (view != null) {
                        view.dismissLoadding();
                    }
                }
            });
        }
    }

    /**
     * 搜索记录
     * @throws Exception
     */
    public void onGetSearchRecord() throws Exception{
        final SearchView view = (SearchView) getView();
        if (view != null) {

            newsModel.onGetNativeSearchRecord(new Callback<List<SearchBean>>() {
                @Override
                public void onSuccess(List<SearchBean> data) {
                    if (view != null) {
                        view.onLoadNativeSearchRecord(data);
                    }
                }

                @Override
                public void onFailure(String msg) {
                    ToastUtils.onToast(msg);
                }

                @Override
                public void onOutTime(String msg) {
                    ToastUtils.onToast(msg);
                    if (view != null)
                        view.onReLogin();
                }

                @Override
                public void onComplete() {

                }
            });

        }
    }


    /**
     * 写文章评论
     * @throws Exception
     */
    public void onReadArticleComment (Map<String, String> param) throws Exception{
        final NewsDetailsView view = (NewsDetailsView) getView();
        if (view != null) {

            try {
                ParamValidate.getInstance().onValidateIsNull(param.get(ResponseKey.getInstace().user_id));
                ParamValidate.getInstance().onValidateIsNull(param.get(ResponseKey.getInstace().content));
                ParamValidate.getInstance().onValidateIsNull(param.get(ResponseKey.getInstace().article_id));
                ParamValidate.getInstance().onValidateIsNull(param.get(ResponseKey.getInstace().is_anonymous));
            }catch (Exception e){
                throw new Exception(e.getMessage());
            }
            param.put(ResponseKey.getInstace().ip_facility, "1");
            view.showLoadding();
            String url =Urls.HOST + Urls.SEND_COMMENT;

            requestHttp.onSendGet(url, param, new Callback<Map<String, Object>>() {
                @Override
                public void onSuccess(Map<String, Object> data) {
                    if (view !=  null){
                        view.onArticleCommentResponse(data);
                    }
                }

                @Override
                public void onFailure(String msg) {
                    ToastUtils.onToast(msg);
                }

                @Override
                public void onOutTime(String msg) {
                    ToastUtils.onToast(msg);
                    if (view != null){
                        view.onReLogin();
                    }
                }

                @Override
                public void onComplete() {
                    if (view !=null)
                        view.dismissLoadding();
                }
            });

        }
    }

    /**
     * 获取文章评论列表
     * @param param
     * @throws Exception
     */
    public void onArticleCommentList (Map<String, String> param) throws Exception{
        final NewsView view = (NewsView) getView();
        if (view != null) {

            try {
                ParamValidate.getInstance().onValidateIsNull(param.get(ResponseKey.getInstace().user_id));
                ParamValidate.getInstance().onValidateIsNull(param.get(ResponseKey.getInstace().article_id));
            }catch (Exception e){
                throw new Exception(e.getMessage());
            }
            view.showLoadding();
            String url =Urls.HOST + Urls.GET_ARTICLE_LIST;

            requestHttp.onSendGet(url, param, new Callback<Map<String, Object>>() {
                @Override
                public void onSuccess(Map<String, Object> data) {
                    if (view !=  null){
                        view.onNewsResponse(data);
                    }
                }

                @Override
                public void onFailure(String msg) {
                    ToastUtils.onToast(msg);
                }

                @Override
                public void onOutTime(String msg) {
                    ToastUtils.onToast(msg);
                    if (view != null){
                        view.onReLogin();
                    }
                }

                @Override
                public void onComplete() {
                    if (view !=null)
                        view.dismissLoadding();
                }
            });

        }
    }



    /**
     * 删除搜索记录
     * @throws Exception
     */
    public void onRemoveNativiteSearchRecord(int id) throws Exception{
        final SearchView view = (SearchView) getView();
        if (view != null) {

            if (view != null) {
                newsModel.onRemoveNativeSearchRecord(id);

            }
        }
    }

    /**
     * 清空搜索记录
     * @throws Exception
     */
    public void onRemoveAllNativiteSearchRecord() throws Exception{
        final SearchView view = (SearchView) getView();
        if (view != null) {

            if (view != null) {
                newsModel.onRemoveAllNativeSearchRecord();

            }
        }
    }
}