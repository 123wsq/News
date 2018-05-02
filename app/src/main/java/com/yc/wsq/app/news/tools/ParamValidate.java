package com.yc.wsq.app.news.tools;

import android.text.TextUtils;

import com.wsq.library.utils.RegexUtils;

public class ParamValidate {

    private static ParamValidate validate;

    private ParamValidate(){

    }

    public static ParamValidate getInstance(){

        if(validate == null){
            synchronized (ParamValidate.class){
                if(validate == null){
                    validate = new ParamValidate();
                }
            }
        }
        return validate;
    }

    /**
     * 用户名验证,在本项目中的用户名是手机号，在此只需要验证下手机号码是否正确
     * @param userName
     * @throws Exception
     */
    public void onValidateUserName(String userName)throws Exception{

        if (TextUtils.isEmpty(userName)){
            throw new Exception("手机号码不能为空");
        }
        if(!RegexUtils.isMobileExact(userName)){
            throw new Exception("请输入正确的手机号码");
        }


    }

    /**
     * 用户密码验证
     * @param password
     * @throws Exception
     */
    public void onValidateUserPsd(String...password) throws Exception{

        if (TextUtils.isEmpty(password[0])){
            throw new Exception("密码不能为空");
        }
        if (password[0].length() < 6 || password[0].length() > 18){
            throw new Exception("请输入6-18位的密码");
        }
        if (password.length ==2) {
            if (!password[0].equals(password[1])) {
                throw new Exception("两次密码不一致");
            }
        }

    }

    /**
     * 验证用户验证码，在此主要是验证长度
     * @param code
     * @throws Exception
     */
    public void onValidateCode(String code) throws Exception{

        if (TextUtils.isEmpty(code)){
            throw new Exception("请输入验证码");
        }
        if (code.length() != 6){
            throw new Exception("验证码必须为6位");
        }
    }

    /**
     * 搜索内容验证
     * @param str
     */
    public void onValidateKeyWords(String str) throws Exception{

        if (TextUtils.isEmpty(str)){
            throw new Exception("搜索内容不能为空");
        }
    }

    public void onValidateIsNull(String param)throws Exception{
        if (TextUtils.isEmpty(param)){
            throw new Exception("参数不能为空");
        }
    }

    public void onValidateIsNull(String...param)throws Exception{

        for (int i = 0; i < param.length; i++) {
            if (TextUtils.isEmpty(param[i])){
                throw new Exception("参数不能为空");
            }
        }

    }
}