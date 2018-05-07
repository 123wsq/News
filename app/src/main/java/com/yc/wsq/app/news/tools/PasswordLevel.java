package com.yc.wsq.app.news.tools;

public class PasswordLevel {

    //判断数字
    private static boolean isDigit(char c){
        if(c >= '0' && c <='9'){
            return true;
        }
        return false;
    }
    //判断小写字母
    private static boolean isLetterLowerCase(char c){
        if((c >= 'a' && c <= 'z')){
            return true;
        }
        return false;
    }

    /**
     * 判断大写字母
     * @param c
     * @return
     */
    private static  boolean isLetterUpperCase(char c){
        if((c >= 'A' && c <= 'Z')){
            return true;
        }
        return false;
    }

    public static int onValidateLevel(String str){
        char[] ch = str.toCharArray();

        boolean[] b = new boolean[4];
        for (char c : ch){

            if (isDigit(c)){
                b[0] = true;
            } else if(isLetterLowerCase(c)){
                b[1] = true;
            }else if(isLetterUpperCase(c)){
                b[2] = true;
            }else {
                b[3] = true;
            }
        }

        int typeNum = 0;

        for (int i = 0; i < b.length; i++) {
            if (b[i]){
                typeNum++;
            }
        }

        return typeNum;
    }
}
