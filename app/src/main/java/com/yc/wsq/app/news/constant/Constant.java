package com.yc.wsq.app.news.constant;

public class Constant {

    public static Constant constant;

    private Constant(){

    }
    public static Constant getInstance(){
        if (constant == null){
            constant = new Constant();
        }
        return constant;
    }

    public String SHARED_NAME = "Shared_constant";

    public final String ISFIRST = "isFirst";
    public final String SESSION = "SESSION";
    public final String COOKIE= "COOKIE";

    /**
     * 用户登录信息返回
     */
    public final String USER_RESULT = "USER_RESULT";

}
