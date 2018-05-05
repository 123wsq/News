package com.yc.wsq.app.news.tools;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.widget.RadioButton;

import com.wsq.library.utils.DensityUtil;

public class ViewSize {

    /**
     * 设置raidoButton的图片大小
     * @param context
     * @param radioButton
     * @param drawableId
     */
    public static void onSetRadioButtonSize(Context context, RadioButton radioButton, int drawableId){

        Drawable drawableFirst = context.getResources().getDrawable(drawableId);
        drawableFirst.setBounds(0, 0, DensityUtil.dp2px(context, 20), DensityUtil.dp2px(context, 20));//第一0是距左右边距离，第二0是距上下边距离，第三69长度,第四宽度
        radioButton.setCompoundDrawables(null, drawableFirst, null, null);
    }
}
