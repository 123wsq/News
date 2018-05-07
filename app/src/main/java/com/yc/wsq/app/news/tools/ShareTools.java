package com.yc.wsq.app.news.tools;

import android.content.Context;

import cn.sharesdk.onekeyshare.OnekeyShare;
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
    public static void showShare(Context context, String platform, String title,String titleUrl, String content) {
        OnekeyShare oks = new OnekeyShare();

        if (platform != null) {
            oks.setPlatform(platform);
        }

        //关闭sso授权
        oks.disableSSOWhenAuthorize();

        // title标题，微信、QQ和QQ空间等平台使用
        oks.setTitle(title);

        if(platform.equals(QQ.NAME) || platform.equals(QZone.NAME)){
            // titleUrl QQ和QQ空间跳转链接
            oks.setTitleUrl(titleUrl);
        }
        if(platform.equals(Wechat.NAME) || platform.equals(WechatMoments.NAME)){
            // url在微信、微博，Facebook等平台中使用
            oks.setUrl(titleUrl);
        }

        // text是分享文本，所有平台都需要这个字段
        oks.setText(content);
        // imagePath是图片的本地路径，Linked-In以外的平台都支持此参数
//        oks.setImagePath("/sdcard/test.jpg");//确保SDcard下面存在此张图片
        // url在微信、微博，Facebook等平台中使用
//        oks.setUrl("http://sharesdk.cn");
        // comment是我对这条分享的评论，仅在人人网使用
//        oks.setComment("我是测试评论文本");
        // 启动分享GUI
        oks.show(context);
    }
}
