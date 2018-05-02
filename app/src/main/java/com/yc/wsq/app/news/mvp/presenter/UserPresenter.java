package com.yc.wsq.app.news.mvp.presenter;

import com.wsq.library.tools.ToastUtils;
import com.yc.wsq.app.news.constant.Constant;
import com.yc.wsq.app.news.constant.ResponseKey;
import com.yc.wsq.app.news.constant.Urls;
import com.yc.wsq.app.news.mvp.callback.Callback;
import com.yc.wsq.app.news.mvp.model.impl.RequestHttpImpl;
import com.yc.wsq.app.news.mvp.model.impl.UserModelImpl;
import com.yc.wsq.app.news.mvp.model.inter.RequestHttpInter;
import com.yc.wsq.app.news.mvp.model.inter.UserModelInter;
import com.yc.wsq.app.news.mvp.view.BaseView;
import com.yc.wsq.app.news.mvp.view.UserRegisterView;
import com.yc.wsq.app.news.mvp.view.UserView;
import com.yc.wsq.app.news.tools.ParamValidate;
import com.yc.wsq.app.news.tools.SharedTools;

import java.util.Map;

public class UserPresenter<T extends BaseView> extends BasePresenter<T> {

    private RequestHttpInter requestHttp;

    public UserPresenter(){
        requestHttp = new RequestHttpImpl();
    }

    /**
     * 用户登录
     * @param param
     */
    public void onUserLogin(Map<String, String> param) throws Exception {

        final UserView view = (UserView) getView();
        if (view != null){
            //参数验证
            try {
                ParamValidate.getInstance().onValidateUserName(param.get(ResponseKey.getInstace().username));
                ParamValidate.getInstance().onValidateUserPsd(param.get(ResponseKey.getInstace().password));
            }catch (Exception e){
                throw new Exception(e.getMessage());
            }
            if (view != null) {
                view.showLoadding();
                String url = Urls.HOST + Urls.LOGIN;
                requestHttp.onSendGet(url, param, new Callback<Map<String, Object>>() {
                    @Override
                    public void onSuccess(Map<String, Object> data) {
                        if (view != null) {
                            SharedTools.getInstance(view.getContext()).onPutData(Constant.getInstance().SESSION, data.get(ResponseKey.getInstace().result).toString());
                            view.onResponseData(data);
                        }
                    }

                    @Override
                    public void onFailure(String msg) {
                        ToastUtils.onToast(msg);
                    }

                    @Override
                    public void onError() {

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
     * 用户注册
     * @param param
     * @throws Exception
     */
    public void onUserRegister(Map<String, String> param) throws Exception{

        final UserRegisterView userView = (UserRegisterView) getView();

        try {
            ParamValidate.getInstance().onValidateUserName(param.get(ResponseKey.getInstace().username));
            ParamValidate.getInstance().onValidateUserPsd(param.get(ResponseKey.getInstace().password), param.get(ResponseKey.getInstace().password2));
            ParamValidate.getInstance().onValidateCode(param.get(ResponseKey.getInstace().code));
        }catch (Exception e){
            throw new Exception(e.getMessage());
        }

        if (userView != null) {
            userView.showLoadding();
            requestHttp.onSendPost("", param, new Callback<Map<String, Object>>() {
                @Override
                public void onSuccess(Map<String, Object> data) {
                    if (userView != null) {
                        userView.onRegisterData(data);
                    }
                }

                @Override
                public void onFailure(String msg) {
                    ToastUtils.onToast(msg);
                }

                @Override
                public void onError() {

                }

                @Override
                public void onComplete() {
                    if (userView != null) {
                        userView.dismissLoadding();
                    }
                }
            });
        }

    }

    /**
     * 获取验证码
     * @param param
     * @throws Exception
     */
    public void onGetValidateCode(Map<String, String> param) throws Exception{

        final UserRegisterView userView = (UserRegisterView) getView();

        try {
            ParamValidate.getInstance().onValidateUserName(param.get(ResponseKey.getInstace().mobile));
        }catch (Exception e){
            throw new Exception(e.getMessage());
        }


        if (userView != null) {
            userView.showLoadding();
            requestHttp.onSendPost("", param, new Callback<Map<String, Object>>() {
                @Override
                public void onSuccess(Map<String, Object> data) {
                    if (userView != null) {
                        userView.onGetValidateData(data);
                    }
                }

                @Override
                public void onFailure(String msg) {
                    ToastUtils.onToast(msg);
                }

                @Override
                public void onError() {

                }

                @Override
                public void onComplete() {
                    if (userView != null) {
                        userView.dismissLoadding();
                    }
                }
            });
        }

    }

    /**
     * 获取积分记录
     * @param param
     * @throws Exception
     */
    public void onGetIntegralRecord(Map<String, String> param) throws Exception{

        final UserView userView = (UserView) getView();

        try {
            ParamValidate.getInstance().onValidateIsNull(param.get(ResponseKey.getInstace().token));
            ParamValidate.getInstance().onValidateIsNull(param.get(ResponseKey.getInstace().uid));
        }catch (Exception e){
            throw new Exception(e.getMessage());
        }


        if (userView != null) {
            userView.showLoadding();
            String url = Urls.HOST+Urls.GET_INTEGRAL_RECORD;
            requestHttp.onSendGet(url, param, new Callback<Map<String, Object>>() {
                @Override
                public void onSuccess(Map<String, Object> data) {
                    if (userView != null) {
                        userView.onResponseData(data);
                    }
                }

                @Override
                public void onFailure(String msg) {
                    ToastUtils.onToast(msg);
                }

                @Override
                public void onError() {

                }

                @Override
                public void onComplete() {
                    if (userView != null) {
                        userView.dismissLoadding();
                    }
                }
            });
        }

    }

    /**
     * 获取交易记录
     * @param param
     * @throws Exception
     */
    public void onGetTradeRecord(Map<String, String> param) throws Exception{

        final UserView userView = (UserView) getView();

//        try {
//            ParamValidate.getInstance().onValidateUserName(param.get(ResponseKey.getInstace().user_name));
//            ParamValidate.getInstance().onValidateUserPsd(param.get(ResponseKey.getInstace().user_psd));
//        }catch (Exception e){
//            throw new Exception(e.getMessage());
//        }


        if (userView != null) {
            userView.showLoadding();
            requestHttp.onSendPost("", param, new Callback<Map<String, Object>>() {
                @Override
                public void onSuccess(Map<String, Object> data) {
                    if (userView != null) {
//                        userView.onRegisterData(data);
                    }
                }

                @Override
                public void onFailure(String msg) {
                    ToastUtils.onToast(msg);
                }

                @Override
                public void onError() {

                }

                @Override
                public void onComplete() {
                    if (userView != null) {
                        userView.dismissLoadding();
                    }
                }
            });
        }

    }
}