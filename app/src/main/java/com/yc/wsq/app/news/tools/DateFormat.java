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
}
