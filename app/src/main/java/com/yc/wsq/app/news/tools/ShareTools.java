package com.yc.wsq.app.news.tools;

import android.content.Context;

import com.orhanobut.logger.Logger;
import com.wsq.library.tools.ToastUtils;
import com.yc.wsq.app.news.loader.OnAuthLoginListener;

import java.util.HashMap;

import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.PlatformActionListener;
import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.tencent.qq.QQ;
import cn.sharesdk.wechat.friends.Wechat;

public class ShareTools {

    /**
     *
     * @param context
     * @param platform  分享平台
     * @param title  标题
     * @param content  内容
     */
//    public static void showShare(Context context, String platform, String title,String titleUrl, String content) {
//        OnekeyShare oks = new OnekeyShare();
//
//        if (platform != null) {
//            oks.setPlatform(platform);
//        }
//
//        //关闭sso授权
//        oks.disableSSOWhenAuthorize();
//
//        // title标题，微信、QQ和QQ空间等平台使用
//        oks.setTitle(title);
//
//        if(platform.equals(QQ.NAME) || platform.equals(QZone.NAME)){
//            // titleUrl QQ和QQ空间跳转链接
//            oks.setTitleUrl(titleUrl);
//        }
//        if(platform.equals(Wechat.NAME) || platform.equals(WechatMoments.NAME)){
//            // url在微信、微博，Facebook等平台中使用
//            oks.setUrl(titleUrl);
//        }
//
//        // text是分享文本，所有平台都需要这个字段
//        oks.setText(content);
//        // imagePath是图片的本地路径，Linked-In以外的平台都支持此参数
////        oks.setImagePath("/sdcard/test.jpg");//确保SDcard下面存在此张图片
//        // url在微信、微博，Facebook等平台中使用
////        oks.setUrl("http://sharesdk.cn");
//        // comment是我对这条分享的评论，仅在人人网使用
////        oks.setComment("我是测试评论文本");
//        // 启动分享GUI
//
//        oks.setCallback(new PlatformActionListener() {
//            @Override
//            public void onComplete(Platform platform, int i, HashMap<String, Object> hashMap) {
//
//            }
//
//            @Override
//            public void onError(Platform platform, int i, Throwable throwable) {
//                throwable.printStackTrace();
//                Logger.d(platform.toString());
//            }
//
//            @Override
//            public void onCancel(Platform platform, int i) {
//
//            }
//        });
//        oks.show(context);
//    }


    /**
     *
     * @param platform  1 微信 2朋友圈  3 qq  4 空间  5,wechat登录  6 qq登录
     */
    public static void onShare(int platform, String title, String url, OnAuthLoginListener loginListener){

        switch (platform){
            case 1:
                onWechatShare(title, url, loginListener);
                break;
            case 2:
//                onWechatMomentsShare(url);
                break;
            case 3:

                break;
            case 4:

                break;
            case 5:
                onWechatLogin(loginListener);
                break;
        }
    }

    /**
     * 微信分享
     */
    private static void onWechatShare(String title, String url, OnAuthLoginListener loginListener){
        Platform.ShareParams params = new Platform.ShareParams();
        params.setShareType(Platform.SHARE_WEBPAGE);
        params.setTitle(title);
        params.setText(title);
        params.setUrl(url);
        params.setImageUrl("https://gss1.bdstatic.com/9vo3dSag_xI4khGkpoWK1HF6hhy/baike/c0%3Dbaike92%2C5%2C5%2C92%2C30/sign=c4931b2f2334349b600b66d7a8837eab/94cad1c8a786c9179e80a80cc23d70cf3bc75700.jpg");
        Platform platform = ShareSDK.getPlatform(Wechat.NAME);
        platform.setPlatformActionListener(new AuthListener(loginListener));
        platform.share(params);
    }


    /**
     * 微信授权登录
     */
    private static void onWechatLogin(OnAuthLoginListener loginListener){
        Platform weixinfd = ShareSDK.getPlatform(Wechat.NAME);

        //授权回调监听，监听oncomplete，onerror，oncancel三种状态
        weixinfd.setPlatformActionListener(new AuthListener(loginListener));

        //移除授权状态和本地缓存，下次授权会重新授权
        weixinfd.removeAccount(true);
        //SSO授权，传false默认是客户端授权，没有客户端授权或者不支持客户端授权会跳web授权
        weixinfd.SSOSetting(true);
        if(weixinfd.isClientValid()){
            //判断是否存在授权凭条的客户端，true是有客户端，false是无
        }
        if(weixinfd.isAuthValid()){
        //判断是否已经存在授权状态，可以根据自己的登录逻辑设置
//            ToastUtils.onToast("已经授权过了");
            loginListener.onAuthState(0, weixinfd);
            return;
        }
        //要功能，不要数据
//        weixinfd.authorize();
        //要数据不要功能，主要体现在不会重复出现授权界面
        weixinfd.showUser(null);
    }

    /**
     * 授权监听
     */
    static class AuthListener implements PlatformActionListener{

        private OnAuthLoginListener loginListener;
        public AuthListener(OnAuthLoginListener loginListener){
            this.loginListener = loginListener;
        }

        @Override
        public void onComplete(Platform platform, int i, HashMap<String, Object> hashMap) {
//            获取用户名称
//            String user_name = platform.getDb().getUserName();
//            //获取用户头像
//            String image = platform.getDb().getUserIcon();
//            String token = platform.getDb().getToken();
//            Logger.d(user_name+"============"+image+"========="+token);
            loginListener.onAuthState(0, platform);
        }

        @Override
        public void onError(Platform platform, int i, Throwable throwable) {
            loginListener.onAuthState(1, platform);
//            ToastUtils.onToast("授权失败");
        }

        @Override
        public void onCancel(Platform platform, int i) {
//            ToastUtils.onToast("取消授权");
            loginListener.onAuthState(2, platform);
        }
    };

//    /**
//     * 分享监听
//     */
//    static PlatformActionListener listener = new PlatformActionListener() {
//        @Override
//        public void onComplete(Platform platform, int i, HashMap<String, Object> hashMap) {
//            ToastUtils.onToast("分享成功");
//
//
//        }
//
//        @Override
//        public void onError(Platform platform, int i, Throwable throwable) {
//            ToastUtils.onToast("分享失败");
//            throwable.printStackTrace();
//        }
//
//        @Override
//        public void onCancel(Platform platform, int i) {
//            ToastUtils.onToast("取消分享");
//        }
//    };
}
