package com.yc.wsq.app.news.views;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.bigkoo.pickerview.adapter.ArrayWheelAdapter;
import com.contrarywind.listener.OnItemSelectedListener;
import com.contrarywind.view.WheelView;
import com.orhanobut.logger.Logger;
import com.wsq.library.utils.DensityUtil;
import com.yc.wsq.app.news.R;
import com.yc.wsq.app.news.views.listener.OnCalendarResultCallBack;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class CalendarPopup extends PopupWindow{

    private Activity mContext;
    private OnCalendarResultCallBack mCallBack;
    private View popupView;
    private LinearLayout ll_calendar, ll_timer;
    private WheelView wv_year, wv_month, wv_day, wv_hour, wv_minute;
    private TextView tv_popup_title, tv_cancel;

    public static final  int type_all = 0;
    public static final  int type_calendar = 1;
    public static final  int type_timer = 2;
    private List<Integer> mListYear;
    private List<Integer> mListMonth;
    private List<Integer> mListDay;
    private List<Integer> mListHour;
    private List<Integer> mListMinute;
    private Calendar mCalendar;
    private int curYear, curMonth, curDay, curHour, curMinute;

    private int type = 0;

    public CalendarPopup(Activity context, int type, OnCalendarResultCallBack calendarResultCallBack){
        this.mContext = context;
        this.type = type;
        this.mCallBack = calendarResultCallBack;

        popupView = LayoutInflater.from(mContext).inflate(R.layout.layout_popup_calendar,null, false);

        onInitWheelView();
        initPopup();
    }

    public void initPopup(){

        int w = mContext.getResources().getDisplayMetrics().widthPixels;
        int h = mContext.getResources().getDisplayMetrics().heightPixels;
        // 设置按钮监听
        // 设置SelectPicPopupWindow的View
        this.setContentView(popupView);
        // 设置SelectPicPopupWindow弹出窗体的宽
        this.setWidth((int)(w*0.9));
        //
        // 设置SelectPicPopupWindow弹出窗体的高
        int height = DensityUtil.dp2px(mContext, 45+30*6 + 20 +40);
//        if (null != mData) {
//            if (h / 2 <= DensityUtil.dp2px(mContext, (mData.size() * 50) + 90 + 30)) {
//                height = h / 2;
//            } else {
//                height = DensityUtil.dp2px(mContext, (mData.size() * 50) + 90+ 30);
//            }
//        }else{
//            height = h/2;
//        }
        this.setHeight(height);
        // 设置SelectPicPopupWindow弹出窗体可点击
        this.setFocusable(true);
        // 设置SelectPicPopupWindow弹出窗体动画效果
        this.setAnimationStyle(R.style.style_pop);
        // 实例化一个ColorDrawable颜色为半透明
        ColorDrawable dw = new ColorDrawable(0x00000000);
        // 设置SelectPicPopupWindow弹出窗体的背景
        this.setBackgroundDrawable(new BitmapDrawable());
        this.setOutsideTouchable(true);
        //在PopupWindow里面就加上下面代码，让键盘弹出时，不会挡住pop窗口。
        this.setInputMethodMode(PopupWindow.INPUT_METHOD_NEEDED);

//        this.setOnDismissListener(new PoponDismissListener());

        popupView.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                // TODO Auto-generated method stub

//					dismiss();

                return false;
            }
        });

    }

    @Override
    public void showAtLocation(View parent, int gravity, int x, int y) {
        super.showAtLocation(parent, gravity, x, y);
        backgroundAlpha(0.5f);
    }

    @Override
    public void dismiss() {
        super.dismiss();
        backgroundAlpha(1f);
    }

    /**
     * 设置添加屏幕的背景透明度
     * @param bgAlpha
     */
    private void backgroundAlpha(float bgAlpha) {
        WindowManager.LayoutParams lp = mContext.getWindow().getAttributes();
        lp.alpha = bgAlpha;
        mContext.getWindow().setAttributes(lp);
    }

    private void onInitWheelView(){

        ll_calendar = popupView.findViewById(R.id.ll_calendar);
        ll_timer = popupView.findViewById(R.id.ll_timer);
        wv_year = popupView.findViewById(R.id.wv_year);
        wv_month = popupView.findViewById(R.id.wv_month);
        wv_day = popupView.findViewById(R.id.wv_day);
        wv_hour = popupView.findViewById(R.id.wv_hour);
        wv_minute = popupView.findViewById(R.id.wv_minute);
        tv_popup_title = popupView.findViewById(R.id.tv_popup_title);
        tv_cancel = popupView.findViewById(R.id.tv_cancel);

        if(type == type_all){
            ll_timer.setVisibility(View.VISIBLE);
            ll_calendar.setVisibility(View.VISIBLE);
            tv_popup_title.setText(mContext.getString(R.string.str_please_selector_text));
        }else if(type == type_calendar){
            ll_timer.setVisibility(View.GONE);
            ll_calendar.setVisibility(View.VISIBLE);
            tv_popup_title.setText(mContext.getString(R.string.str_please_date_text));
        }else if(type == type_timer){
            ll_timer.setVisibility(View.VISIBLE);
            ll_calendar.setVisibility(View.GONE);
            tv_popup_title.setText(mContext.getString(R.string.str_please_time_text));
        }

        wv_year.setOnItemSelectedListener(new OnItemSelectedListener() {
            @Override
            public void onItemSelected(int index) {
                curYear = mListYear.get(index);
            }
        });
        wv_month.setOnItemSelectedListener(new OnItemSelectedListener() {
            @Override
            public void onItemSelected(int index) {
                curMonth = mListMonth.get(index);
            }
        });
        wv_day.setOnItemSelectedListener(new OnItemSelectedListener() {
            @Override
            public void onItemSelected(int index) {
                curDay = mListDay.get(index);
            }
        });
        wv_hour.setOnItemSelectedListener(new OnItemSelectedListener() {
            @Override
            public void onItemSelected(int index) {
                curHour = mListHour.get(index);
            }
        });
        wv_minute.setOnItemSelectedListener(new OnItemSelectedListener() {
            @Override
            public void onItemSelected(int index) {
                curMinute = mListMinute.get(index);
            }
        });
        tv_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });


        onInitData();
        setCyclic();
        onGetYear();
        onGetMonth();
        onGetDay();
        onGetHour();
        onGetMinute();
    }

    /**
     * 初始化数据
     */
    private void onInitData(){

        mListYear = new ArrayList<>();
        mListMonth = new ArrayList<>();
        mListDay = new ArrayList<>();
        mListHour = new ArrayList<>();
        mListMinute = new ArrayList<>();

        mCalendar = Calendar.getInstance();
        curYear = mCalendar.get(Calendar.YEAR);
        curMonth = mCalendar.get(Calendar.MONTH) + 1;
        curDay = mCalendar.get(Calendar.DAY_OF_MONTH);
        curHour = mCalendar.get(Calendar.HOUR_OF_DAY);
        curMinute = mCalendar.get(Calendar.MINUTE);


        Logger.d("当前时间： year="+curYear+", month="+curMonth+", day=" +curDay+", hour="+curHour+", minute="+curMinute);
    }

    /**
     * 获取年份
     */
    private void onGetYear(){


        for (int i = 1970; i <= curYear; i++) {
            mListYear.add(i);
        }
        wv_year.setAdapter(new ArrayWheelAdapter(mListYear));
        wv_year.setCurrentItem(mListYear.size()-1);
    }

    /**
     * 获取月份
     */
    private void onGetMonth(){
        for (int i = 1; i <= 12; i++) {
            mListMonth.add(i);
        }
        wv_month.setAdapter(new ArrayWheelAdapter(mListMonth));
        wv_month.setCurrentItem(curMonth-1);
    }

    /**
     * 获取天数
     */
    private void onGetDay(){

        int totalDay = 0;
        switch (curMonth){
            case 1:
            case 3:
            case 5:
            case 7:
            case 8:
            case 10:
            case 12:
                totalDay = 31;
                break;
            case 4:
            case 6:
            case 9:
            case 11:
                totalDay =30;
                break;
            case 2:
                if (curYear % 100 ==0){
                    if (curYear % 400 ==0){
                        totalDay = 29;
                    }else {
                        totalDay = 28;
                    }
                }else  if(curYear % 4 ==0){
                    totalDay = 29;
                }else {
                    totalDay = 28;
                }

                break;

        }
        for (int i = 1; i <= totalDay; i++) {
            mListDay.add(i);
        }
        wv_day.setAdapter(new ArrayWheelAdapter(mListDay));
        wv_day.setCurrentItem(curDay-1);
    }

    /**
     * 获取小时
     */
    private void onGetHour(){
        for (int i = 0; i < 24; i++) {
            mListHour.add(i);
        }
        wv_hour.setAdapter(new ArrayWheelAdapter(mListHour));
        wv_hour.setCurrentItem(curHour);
    }

    /**
     * 获取分钟
     */
    private void onGetMinute(){

        for (int i = 0; i < 60; i++) {
            mListMinute.add(i);
        }
        wv_minute.setAdapter(new ArrayWheelAdapter(mListMinute));
        wv_minute.setCurrentItem(curMinute);
    }
    /**
     * 设置循环
     */
    private void setCyclic(){

        wv_year.setCyclic(true);
        wv_month.setCyclic(true);
        wv_day.setCyclic(true);
        wv_hour.setCyclic(true);
        wv_minute.setCyclic(true);
    }



}
