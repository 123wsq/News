package com.yc.wsq.app.news.tools;

import android.content.Context;

import com.orhanobut.logger.Logger;
import com.wsq.library.tools.ToastUtils;

import java.util.HashMap;

import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.PlatformActionListener;
import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.tencent.qq.QQ;
import cn.sharesdk.tencent.qzone.QZone;
import cn.sharesdk.wechat.friends.Wechat;
import cn.sharesdk.wechat.moments.WechatMoments;

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
     * @param platform  1 微信 2朋友圈  3 qq  4空间
     */
    public static void onShare(int platform, String url){

        switch (platform){
            case 1:
//                onWechatShare(url);
                break;
            case 2:
//                onWechatMomentsShare(url);
                break;
            case 3:

                break;
            case 4:

                break;
        }
    }

    /**
     * 微信分享
     */
    private static void onWechatShare(String url){
        Platform.ShareParams params = new Platform.ShareParams();
        params.setShareType(Platform.SHARE_TEXT);

        params.setText("这个是需要分享的内容");
//        params.setTitle("标题");
        params.setAddress(url);
//        params.setSiteUrl();

        Platform wechat = ShareSDK.getPlatform(Wechat.NAME);

        wechat.setPlatformActionListener(new PlatformActionListener() {
            @Override
            public void onComplete(Platform platform, int i, HashMap<String, Object> hashMap) {
                ToastUtils.onToast("分享成功");
            }

            @Override
            public void onError(Platform platform, int i, Throwable throwable) {
                ToastUtils.onToast("分享失败");
                throwable.printStackTrace();
            }

            @Override
            public void onCancel(Platform platform, int i) {
                ToastUtils.onToast("取消分享");
            }
        });
        wechat.share(params);
    }

    /**
     * 朋友圈分享
     */
    public static void onWechatMomentsShare(String url){
        Platform.ShareParams params = new Platform.ShareParams();
        params.setShareType(Platform.SHARE_WEBPAGE);
        params.setAddress(url);
        params.setText("这个是需要分享的内容");
        params.setTitle("标题");

        Platform wechat = ShareSDK.getPlatform(WechatMoments.NAME);
        wechat.setPlatformActionListener(new PlatformActionListener() {
            @Override
            public void onComplete(Platform platform, int i, HashMap<String, Object> hashMap) {
                ToastUtils.onToast("分享成功");
            }

            @Override
            public void onError(Platform platform, int i, Throwable throwable) {
                ToastUtils.onToast("分享失败");
                throwable.printStackTrace();
            }

            @Override
            public void onCancel(Platform platform, int i) {
                ToastUtils.onToast("取消分享");
            }
        });
        wechat.share(params);
    }
}
