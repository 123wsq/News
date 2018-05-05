package com.yc.wsq.app.news.constant;

public class Urls {

    public static final String HOST = "http://api.yczmj.cn/";

    /**
     * 获取验证码
     */
    public static final String GET_VALIDATE_CODE = "api/send_validate_code";

    /**
     * 注册
     */
    public static final String REGISTER = "user/reg";

    public static final String LOGIN = "user/login";

    /**
     * 新闻列表
     */
    public static final String GET_NEWS_LIST = "/Article/articleList";

    /**
     * 新闻分类
     */
    public static final String GET_NEWS_TYPE= "Article/categoryList";

    /**
     * 新闻详情
     */
    public static final String GET_NEWS_DETAILS= "Article/detail";

    /**
     * 阅读积分
     */
    public static final String READ_INTEGRAL = "Article/jifen";

    /**
     * 积分记录
     */
    public static final String GET_INTEGRAL_RECORD = "user/account";

    /**
     * 公益列表
     */
    public static final String BENEFIT_LIST = "Topic/topicList";

    /**
     * 公益详情
     */
    public static final String BENEFIT_DETAILS = "Topic/detail";

    /**
     * 商城页面
     */
    public static final String  SHOPPING = "http://h.yczmj.cn/Mobile/Index/index.html";

    /**
     * 会员升级
     */
    public static final String UPGRADE = "user/upgrade";
    /**
     * 账户充值
     */
    public static final String  RECHARGE= "user/chongzhi";

    /**
     * 充值成功通知服务器
     */
    public static final String NOTIFICATION_SERVER = "user/chongzhiback";
}
