package com.yc.wsq.app.news.tools;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DateFormat {

    public static final String DATE_FORMAT_1 = "yyyy-MM-dd HH:mm:ss";
    public static final String DATE_FORMAT_2 = "MM-dd";
    public static String onDateFormat(String sdate, String format){

        SimpleDateFormat dateFormat = new SimpleDateFormat(format);
        try {
            Date date = dateFormat.parse(sdate);
            SimpleDateFormat format1 = new SimpleDateFormat(DATE_FORMAT_2);
            return format1.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return "";
    }

    public static final String onGetCurIntervalTime(String millisecond){

        long oldTime = Long.parseLong(millisecond);
        long curTime = System.currentTimeMillis()/1000;
        long time = curTime -oldTime;
        if (time<60){
            return time + "秒前";
        }else if(time < 60 * 60){
            return (time / 60)+"分钟前";
        }else if(time < 60 * 60 * 24){
            return (time / 60 / 60)+"小时前";
        }else if(time < 60* 60 * 24 * 30){
            return (time / 60 / 60 / 24)+"天前";
        }else if(time < 60* 60 * 24 * 30 *12){
            return (time / 60 / 60 / 24 / 30)+"月前";
        }else {
            return (time / 60 / 60 / 24 / 30 / 12)+"年前";
        }

    }
}
