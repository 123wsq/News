package com.yc.wsq.app.news.mvp.presenter;

import com.wsq.library.tools.ToastUtils;
import com.yc.wsq.app.news.bean.SearchBean;
import com.yc.wsq.app.news.constant.ResponseKey;
import com.yc.wsq.app.news.constant.Urls;
import com.yc.wsq.app.news.mvp.callback.Callback;
import com.yc.wsq.app.news.mvp.model.impl.NewsModelImpl;
import com.yc.wsq.app.news.mvp.model.impl.RequestHttpImpl;
import com.yc.wsq.app.news.mvp.model.inter.NewsModelInter;
import com.yc.wsq.app.news.mvp.model.inter.RequestHttpInter;
import com.yc.wsq.app.news.mvp.view.BaseView;
import com.yc.wsq.app.news.mvp.view.NewsDetailsView;
import com.yc.wsq.app.news.mvp.view.NewsView;
import com.yc.wsq.app.news.mvp.view.SearchView;
import com.yc.wsq.app.news.tools.ParamValidate;

import java.util.List;
import java.util.Map;

public class NewsPresenter<T extends BaseView> extends BasePresenter<T> {

    private RequestHttpInter requestHttp;
    private NewsModelInter newsModel;

    public NewsPresenter(){
        requestHttp = new RequestHttpImpl();
        newsModel = new NewsModelImpl();
    }

    /**
     * 获取新闻列表
     * @param param
     */
    public void onGetNewsList(Map<String, String> param) throws Exception {

        final NewsView view = (NewsView) getView();
        if (view != null) {

            if (view != null) {
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
    public void onGetNewsType(Map<String, String> param) throws Exception{
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
     * 搜索记录
     * @throws Exception
     */
    public void onGetSearchRecord() throws Exception{
        final SearchView view = (SearchView) getView();
        if (view != null) {

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