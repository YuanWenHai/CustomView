package com.will.customview.utils;

import android.content.Context;

/**
 * created  by will on 2018/6/5 15:25
 */
public class Utils {

    public static int Dp2Px(Context context, int dp){
        return (int)(context.getResources().getDisplayMetrics().density * dp);
    }
}
