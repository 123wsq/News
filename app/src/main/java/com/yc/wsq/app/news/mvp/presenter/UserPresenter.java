package com.yc.wsq.app.news.mvp.presenter;

import android.content.Intent;
import android.support.v7.widget.RecyclerView;

import com.wsq.library.tools.ToastUtils;
import com.wsq.library.utils.AppManager;
import com.wsq.library.utils.Utils;
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
import com.yc.wsq.app.news.mvp.view.UpdateUserInfoView;
import com.yc.wsq.app.news.mvp.view.UserLoginView;
import com.yc.wsq.app.news.mvp.view.UserMainView;
import com.yc.wsq.app.news.mvp.view.UserRegisterView;
import com.yc.wsq.app.news.mvp.view.UserView;
import com.yc.wsq.app.news.mvp.view.WithdrawView;
import com.yc.wsq.app.news.tools.ParamValidate;
import com.yc.wsq.app.news.tools.SharedTools;

import java.io.File;
import java.util.Map;

import cn.sharesdk.framework.authorize.RegisterView;

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

        final UserLoginView view = (UserLoginView) getView();
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

    public void onCheckIMEI(Map<String, String> param) throws Exception{
        final UserLoginView view = (UserLoginView) getView();
        if(view != null){
            try {
                ParamValidate.getInstance().onValidateIsNull(param.get(ResponseKey.getInstace().unionid));
                ParamValidate.getInstance().onValidateIsNull(param.get(ResponseKey.getInstace().openid));
                ParamValidate.getInstance().onValidateIsNull(param.get(ResponseKey.getInstace().nickname));
                ParamValidate.getInstance().onValidateIsNull(param.get(ResponseKey.getInstace().headimgurl));
                ParamValidate.getInstance().onValidateIsNull(param.get(ResponseKey.getInstace().imei));

            }catch (Exception e){
                e.printStackTrace();
            }

//            view.showLoadding();
            String url = Urls.HOST + Urls.CHECK_IEME;
            requestHttp.onSendGet(url, param, new Callback<Map<String, Object>>() {
                @Override
                public void onSuccess(Map<String, Object> data) {
                    if(view !=null)
                    view.onCheckIMEIResponseData(data);
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
//                    if(view !=null)
//                        view.dismissLoadding();
                }
            });
        }
    }


    /**
     * 用户登出
     * @param param
     * @throws Exception
     */
    public void onUserLogOut(Map<String, String> param) throws Exception {

        final UpdateUserInfoView view = (UpdateUserInfoView) getView();
        if (view != null){

            if (view != null) {
                String url = Urls.HOST + Urls.LOGOUT;
                requestHttp.onSendGet(url, param, new Callback<Map<String, Object>>() {
                    @Override
                    public void onSuccess(Map<String, Object> data) {
                        if (view != null) {
                            view.onUserLogOutResponseData(data);
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
     * 验证码 验证
     * @param param
     * @throws Exception
     */
    public void onCheckValidateCode(Map<String, String> param) throws Exception{

        final UserRegisterView userView = (UserRegisterView) getView();

        try {
            ParamValidate.getInstance().onValidateUserName(param.get(ResponseKey.getInstace().mobile));
            ParamValidate.getInstance().onValidateCode(param.get(ResponseKey.getInstace().code));
        }catch (Exception e){
            throw new Exception(e.getMessage());
        }


        if (userView != null) {
            userView.showLoadding();
            String url = Urls.HOST + Urls.CHECK_VALIDATE_CODE;
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
//                    ToastUtils.onToast(msg);
//                    if (url != null)
//                        userView.onReLogin();

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

        try {
            ParamValidate.getInstance().onValidateIsNull(param.get(ResponseKey.getInstace().uid));
            ParamValidate.getInstance().onValidateIsNull(param.get(ResponseKey.getInstace().token));
        }catch (Exception e){
            throw new Exception(e.getMessage());
        }


        if (userView != null) {
            userView.showLoadding();
            String url = Urls.HOST +Urls.WITHDRAW_LIST;
            requestHttp.onSendPost(url, param, new Callback<Map<String, Object>>() {
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
     * 设置提现密码
     * @param param
     * @throws Exception
     */
    public void onSettingWithdrawPassword(Map<String, String> param) throws Exception{

        final UserView userView = (UserView) getView();


        if (userView != null) {
            userView.showLoadding();
            String url = Urls.HOST +Urls.SETTING_PAYWD;
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
     * app版本检测
     * @param param
     * @throws Exception
     */
    public void onGetAppVersion(Map<String, String> param) throws Exception{

        final UserView userView = (UserView) getView();

//        try {
//            ParamValidate.getInstance().onValidateUserName(param.get(ResponseKey.getInstace().user_name));
//            ParamValidate.getInstance().onValidateUserPsd(param.get(ResponseKey.getInstace().user_psd));
//        }catch (Exception e){
//            throw new Exception(e.getMessage());
//        }


        if (userView != null) {
            String url = Urls.HOST +Urls.CHECK_APP;
            requestHttp.onSendPost(url, param, new Callback<Map<String, Object>>() {
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

                }


                @Override
                public void onComplete() {

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

        final UserView view = (UserView) getView();

//        try {
//            ParamValidate.getInstance().onValidateUserName(param.get(ResponseKey.getInstace().user_name));
//            ParamValidate.getInstance().onValidateUserPsd(param.get(ResponseKey.getInstace().user_psd));
//        }catch (Exception e){
//            throw new Exception(e.getMessage());
//        }


        if (view != null) {
            view.showLoadding();
            String url = Urls.HOST +Urls.APPLY_WITHDRAW;
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


    /**
     * 提交邀请码
     * @param param
     * @throws Exception
     */
    public void onSubmitInviteCode(Map<String, String> param) throws Exception{

        final UserView view = (UserView) getView();

        try {
            ParamValidate.getInstance().onValidateIsNull(param.get(ResponseKey.getInstace().first_leader));
        }catch (Exception e){
            throw new Exception(e.getMessage());
        }


        if (view != null) {
            view.showLoadding();
            String url = Urls.HOST +Urls.SET_INVITE;
            requestHttp.onSendGet(url, param, new Callback<Map<String, Object>>() {
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


    /**
     * 获取徒弟列表
     * @param param
     * @throws Exception
     */
    public void onGetApprenticeList(Map<String, String> param) throws Exception{

        final UserView view = (UserView) getView();

        try {
            ParamValidate.getInstance().onValidateIsNull(param.get(ResponseKey.getInstace().uid));
            ParamValidate.getInstance().onValidateIsNull(param.get(ResponseKey.getInstace().token));
            ParamValidate.getInstance().onValidateIsNull(param.get(ResponseKey.getInstace().type));
        }catch (Exception e){
            throw new Exception(e.getMessage());
        }


        if (view != null) {
            view.showLoadding();
            String url = Urls.HOST +Urls.DISCIPLE_LIST;
            requestHttp.onSendGet(url, param, new Callback<Map<String, Object>>() {
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


    /**
     * 设置提现账户
     * @param param
     * @throws Exception
     */
    public void onSettingWithdrawAccount(Map<String, String> param) throws Exception{

        final UserView view = (UserView) getView();

        try {
            ParamValidate.getInstance().onValidateIsNull(param.get(ResponseKey.getInstace().user_id));
            ParamValidate.getInstance().onValidateIsNull(param.get(ResponseKey.getInstace().bank_type));
            ParamValidate.getInstance().onValidateIsNull(param.get(ResponseKey.getInstace().bank_card));
            ParamValidate.getInstance().onValidateIsNull(param.get(ResponseKey.getInstace().realname));
        }catch (Exception e){
            throw new Exception(e.getMessage());
        }


        if (view != null) {
            view.showLoadding();
            String url = Urls.HOST +Urls.SETTING_WITHDRAW_ACCOUNT;
            requestHttp.onSendGet(url, param, new Callback<Map<String, Object>>() {
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

    /**
     * 修改用户信息返回
     * @param param
     * @throws Exception
     */
    public void onUpdateAccountInfo(Map<String, String> param) throws Exception{

        final UpdateUserInfoView view = (UpdateUserInfoView) getView();

        if(param.containsKey(ResponseKey.getInstace().password) && param.containsKey(ResponseKey.getInstace().newpassword)){
            try {
                ParamValidate.getInstance().onValidateUserPsd(param.get(ResponseKey.getInstace().password));
                ParamValidate.getInstance().onValidateUserPsd(param.get(ResponseKey.getInstace().newpassword), param.get(ResponseKey.getInstace().newpassword2));
            }catch (Exception e){
                throw new Exception(e.getMessage());
            }
        }

        param.remove(ResponseKey.getInstace().newpassword2);
        if (view != null) {
            String url = Urls.HOST +Urls.UPDATE_USER_INFO;
            view.showLoadding();
            requestHttp.onSendGet(url, param, new Callback<Map<String, Object>>() {
                @Override
                public void onSuccess(Map<String, Object> data) {
                    if (view != null) {
                        view.onUpdateUserResponseData(data);
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

                    if(view!= null)
                      view.dismissLoadding();
                }
            });
        }

    }


    /**
     * 上传头像
     * @throws Exception
     */
    public void onUploadUserHeader(String path, Map<String, String> param) throws Exception{

        final UpdateUserInfoView view = (UpdateUserInfoView) getView();

        File file = new File(path);
        if (!file.exists()){
            throw  new Exception("文件不存在");
        }

        if (view != null) {
            String url = Urls.HOST +Urls.UPDATE_USER_INFO;
            requestHttp.onUploadImage(url, file, ResponseKey.getInstace().head_pic, "image", param, new Callback<Map<String, Object>>() {
                @Override
                public void onSuccess(Map<String, Object> data) {
                    if (view != null) {
                        view.onUpdateUserResponseData(data);
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
     * 忘记密码
     * @param param
     * @throws Exception
     */
    public void onForgetPassword( Map<String, String> param) throws Exception{

        final UserRegisterView view = (UserRegisterView) getView();

        try {
            ParamValidate.getInstance().onValidateUserName(param.get(ResponseKey.getInstace().mobile));
            ParamValidate.getInstance().onValidateUserPsd(param.get(ResponseKey.getInstace().password), param.get(ResponseKey.getInstace().password2));
            ParamValidate.getInstance().onValidateCode(param.get(ResponseKey.getInstace().code));
        }catch (Exception e){
            throw new Exception(e.getMessage());
        }
        param.remove(ResponseKey.getInstace().password2);
        if (view != null) {
            view.showLoadding();
            String url = Urls.HOST +Urls.FORGET_PASSWORD;
            requestHttp.onSendGet(url,  param, new Callback<Map<String, Object>>() {
                @Override
                public void onSuccess(Map<String, Object> data) {
                    if (view != null) {
                        view.onRegisterData(data);
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

                    if (view!= null)
                        view.dismissLoadding();
                }
            });
        }

    }
}