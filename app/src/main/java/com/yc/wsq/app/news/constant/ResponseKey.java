package com.yc.wsq.app.news.constant;

public class ResponseKey {

    private static ResponseKey key;

    private ResponseKey(){

    }
    public static ResponseKey getInstace(){

        if (key == null){

            synchronized (ResponseKey.class){
                if (key == null){
                    key = new ResponseKey();
                }
            }
        }
        return key;
    }


    public final String session = "session";

    /**
     * 手机号码
     */
    public final String mobile = "mobile";

    /**
     * 用户名
     */
    public final String username = "username";

    /**
     * 验证码使用场景
     */
    public final String scene = "scene";

    /**
     * 密码
     */
    public final String password = "password";
    public final String password2 = "password2";
    public final String  keywords = "keywords";
    public final String  max_id = "max_id";
    public final String  min_id = "min_id";
    public final String article_id = "article_id";
    public final String id = "id";
    /**
     * 验证码
     */
    public final String code = "code";

    /**
     * 邀请码
     */
    public final String invite = "invite";


    /**
     * 返回码
     */
    public final String rsp_status = "status";
    /**
     * 返回消息
     */
    public final String rsp_msg = "msg";

    public final String data = "data";
    public final String title = "title";
    public final String thumb = "thumb";
    public final String is_recommend = "is_recommend";
    public final String click = "click";
    public final String result = "result";
    public final String cat_name = "cat_name";
    public final String cat_id = "cat_id";
    public final String user_id = "user_id";
    public final String email = "email";
    public final String paypwd = "paypwd";
    public final String birthday = "birthday";
    public final String user_money = "user_money";
    public final String frozen_money = "frozen_money";
    public final String distribut_money = "distribut_money";
    public final String underling_number = "underling_number";
    public final String pay_points = "pay_points";
    public final String head_pic = "head_pic";
    public final String nickname = "nickname";
    public final String total_amount = "total_amount";
    public final String token = "token";
    public final String act = "act";
    public final String is_vip = "is_vip";
    public final String level_name = "level_name";
    public final String uid = "uid";
    public final String points = "points";
    public final String type_name = "type_name";
    public final String user_points = "user_points";
    public final String change_time = "change_time";
    public final String log_id = "log_id";
    public final String source = "source";
    public final String description = "description";
    public final String ctime = "ctime";
    public final String type = "type";
    public final String money_type = "money_type";
    public final String price = "price";
    public final String partner = "partner";
    public final String private_key = "private_key";
    public final String order_sn = "order_sn";
    public final String times = "times";
    public final String sex = "sex";
    public final String platform= "platform";


    /**
     * ********************************************************************************************
     * 测试key
     */

    public final String rsp_code = "rsp_code";


    public final String news = "news";
    public final String images = "images";
    public final String image = "image";
    public final String goods = "goods";
    public final String goods_type = "goods_type";


    public final String trade = "trade";
    public final String trade_type = "trade_type";
    public final String trade_money = "trade_money";
    public final String trade_time = "trade_time";
    public final String trade_total = "trade_total";

}