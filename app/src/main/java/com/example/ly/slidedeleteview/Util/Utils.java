package com.example.ly.slidedeleteview.Util;

import android.content.Context;
import android.util.DisplayMetrics;
import android.view.WindowManager;

public class Utils {

    /**
     * 获得屏幕宽度
     */
    public static int getScreenWidth(Context context) {
        //获取窗口管理服务
        WindowManager wm = (WindowManager) context
                .getSystemService(Context.WINDOW_SERVICE);
        //屏幕参数对象
        DisplayMetrics outMetrics = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(outMetrics);
        return outMetrics.widthPixels;
    }

}
