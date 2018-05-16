package com.yc.wsq.app.news.constant;

public class Urls {

    public static final String HOST = "https://api.yczmj.cn/";

    /**
     * 商城页面
     */
    public static final String  SHOPPING = "https://yczmj.cn/Mobile/Index/index.html";

    /**
     * App更新地址
     */
    public static final String APP_UPDATE = "https://www.pgyer.com/pMWs";

    /**
     * 获取验证码
     */
    public static final String GET_VALIDATE_CODE = "api/send_validate_code";

    /**
     * 验证码验证
     */
    public static final String CHECK_VALIDATE_CODE = "api/check_validate_code";

    /**
     * 注册
     */
    public static final String REGISTER = "user/reg";

    public static final String LOGIN = "user/login";

    /**
     * 用户登出
     */
    public static final String LOGOUT = "user/logout";

    /**
     * 设置支付密码
     */
    public static final String SETTING_PAYWD = "api/paypwd";

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

    /**
     * 收藏
     */
    public static final String COLLOCT = "Article/collect";

    /**
     * 收藏状态
     */
    public static final String COLLECT_STATE = "Article/iscollect";

    /**
     * 收藏列表
     */
    public static final String COLLECT_LIST = "user/collect";

    /**
     *设置用户邀请码
     */
    public static final String SET_INVITE = "user/firstLeader";

    /**
     * 获取用户信息
     */
    public static final String  GET_USER_INFO = "user/info";

    /**
     * 获取徒弟列表
     */
    public static final String  DISCIPLE_LIST= "user/disciple";

    /**
     * 设置提现账户
     */
    public static final String SETTING_WITHDRAW_ACCOUNT= "user/bankInfo";

    /**
     * 申请提现
     */
    public static final String APPLY_WITHDRAW = "user/withdraw";

    /**
     * 提现列表
     */
    public static final String WITHDRAW_LIST = "user/withdrawalsList";

    /**
     * App检测
     */
    public static final String CHECK_APP = "app/latest";

    /**
     * 发送文章评论
     */
    public static final String SEND_COMMENT = "topic/addArtCom";

    /**
     * 获取文章评论列表
     */
    public static final String GET_ARTICLE_LIST = "topic/artCom";

    /**
     * 修改用户信息
     */
    public static final String UPDATE_USER_INFO = "user/edit";

    /**
     * 忘记密码
     */
    public static final String FORGET_PASSWORD = "api/mobile_user_code";
}
