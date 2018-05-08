package com.yc.wsq.app.news.mvp.presenter;

import android.content.Intent;
import android.support.v7.widget.RecyclerView;

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
import com.yc.wsq.app.news.mvp.view.RechargeView;
import com.yc.wsq.app.news.mvp.view.UserMainView;
import com.yc.wsq.app.news.mvp.view.UserRegisterView;
import com.yc.wsq.app.news.mvp.view.UserView;
import com.yc.wsq.app.news.mvp.view.WithdrawView;
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
//                            SharedTools.getInstance(view.getContext()).onPutData(Constant.getInstance().SESSION, data.get(ResponseKey.getInstace().result).toString());
                            view.onResponseData(data);
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
            String url = Urls.HOST + Urls.REGISTER;
            requestHttp.onSendPost(url, param, new Callback<Map<String, Object>>() {
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
                public void onOutTime(String msg) {

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
            String url = Urls.HOST + Urls.GET_VALIDATE_CODE;
            requestHttp.onSendPost(url, param, new Callback<Map<String, Object>>() {
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
                public void onOutTime(String msg) {

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
     * 获取用户信息
     * @param param
     * @throws Exception
     */
    public void onGetUserInfo(Map<String, String> param) throws Exception{

        final UserMainView userView = (UserMainView) getView();

        try {
            ParamValidate.getInstance().onValidateIsNull(param.get(ResponseKey.getInstace().uid));
            ParamValidate.getInstance().onValidateIsNull(param.get(ResponseKey.getInstace().token));
        }catch (Exception e){
            throw new Exception(e.getMessage());
        }


        if (userView != null) {
            final String url = Urls.HOST + Urls.GET_USER_INFO;
            requestHttp.onSendPost(url, param, new Callback<Map<String, Object>>() {
                @Override
                public void onSuccess(Map<String, Object> data) {
                    if (userView != null) {
                        userView.onUserInfoResponse(data);
                    }
                }

                @Override
                public void onFailure(String msg) {
                    ToastUtils.onToast(msg);
                }

                @Override
                public void onOutTime(String msg) {
                    ToastUtils.onToast(msg);
                    if (url != null)
                        userView.onReLogin();

                }

                @Override
                public void onComplete() {

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
                public void onOutTime(String msg) {
                    ToastUtils.onToast(msg);
                    if (userView != null)
                        userView.onReLogin();
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
                public void onOutTime(String msg) {
                    ToastUtils.onToast(msg);
                    if (userView != null)
                        userView.onReLogin();
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
     * 设置提现密码
     * @param param
     * @throws Exception
     */
    public void onSettingWithdrawPassword(Map<String, String> param) throws Exception{

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
                public void onOutTime(String msg) {
                    ToastUtils.onToast(msg);
                    if (userView != null)
                        userView.onReLogin();
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
     * 验证提现密码
     * @param param
     * @throws Exception
     */
    public void onValidateWithdrawPassword(Map<String, String> param) throws Exception{

        final WithdrawView view = (WithdrawView) getView();

//        try {
//            ParamValidate.getInstance().onValidateUserName(param.get(ResponseKey.getInstace().user_name));
//            ParamValidate.getInstance().onValidateUserPsd(param.get(ResponseKey.getInstace().user_psd));
//        }catch (Exception e){
//            throw new Exception(e.getMessage());
//        }


        if (view != null) {
            view.showLoadding();
            requestHttp.onSendPost("", param, new Callback<Map<String, Object>>() {
                @Override
                public void onSuccess(Map<String, Object> data) {
                    if (view != null) {
                        view.onValidateWithdrawPasswordResponse(data);
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
     * 添加银行卡
     * @param param
     * @throws Exception
     */
    public void onAddBankCard(Map<String, String> param) throws Exception{

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
                public void onOutTime(String msg) {
                    ToastUtils.onToast(msg);
                    if (userView != null)
                        userView.onReLogin();
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
     * 获取充值交易信息
     * @param param
     * @throws Exception
     */
    public void onGetTradeDetails(Map<String, String> param) throws Exception{

        final RechargeView view = (RechargeView) getView();

        try {
            ParamValidate.getInstance().onValidateIsNull(param.get(ResponseKey.getInstace().uid));
            ParamValidate.getInstance().onValidateIsNull(param.get(ResponseKey.getInstace().token));
        }catch (Exception e){
            throw new Exception(e.getMessage());
        }


        if (view != null) {
            view.showLoadding();
            String url = Urls.HOST + Urls.RECHARGE;
            requestHttp.onSendPost(url, param, new Callback<Map<String, Object>>() {
                @Override
                public void onSuccess(Map<String, Object> data) {
                    if (view != null) {
                        view.onGetRechargeResponse(data);
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
     * 充值成功通知服务器
     * @param param
     * @throws Exception
     */
    public void onNotificationServer(Map<String, String> param) throws Exception{

        final RechargeView view = (RechargeView) getView();

        try {
            ParamValidate.getInstance().onValidateIsNull(param.get(ResponseKey.getInstace().uid));
            ParamValidate.getInstance().onValidateIsNull(param.get(ResponseKey.getInstace().token));
        }catch (Exception e){
            throw new Exception(e.getMessage());
        }


        if (view != null) {
            view.showLoadding();
            String url = Urls.HOST + Urls.NOTIFICATION_SERVER;
            requestHttp.onSendPost(url, param, new Callback<Map<String, Object>>() {
                @Override
                public void onSuccess(Map<String, Object> data) {
                    if (view != null) {
                        view.onNotificationResponse(data);
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
     * 申请提现
     * @param param
     * @throws Exception
     */
    public void onApplyWithdraw(Map<String, String> param) throws Exception{

        final WithdrawView view = (WithdrawView) getView();

//        try {
//            ParamValidate.getInstance().onValidateUserName(param.get(ResponseKey.getInstace().user_name));
//            ParamValidate.getInstance().onValidateUserPsd(param.get(ResponseKey.getInstace().user_psd));
//        }catch (Exception e){
//            throw new Exception(e.getMessage());
//        }


        if (view != null) {
            view.showLoadding();
            requestHttp.onSendPost("", param, new Callback<Map<String, Object>>() {
                @Override
                public void onSuccess(Map<String, Object> data) {
                    if (view != null) {
                        view.onApplyWithdrawResponse(data);
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
     * 收藏列表
     * @param param
     * @throws Exception
     */
    public void onGetCollectList(Map<String, String> param) throws Exception{

        final UserView view = (UserView) getView();

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
                        view.onResponseData(data);
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