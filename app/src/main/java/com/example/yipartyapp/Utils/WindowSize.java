package com.example.yipartyapp.Utils;

import android.content.Context;
import android.view.WindowManager;

/**
 * 控制图片展示界面窗体大小工具类
 */
public class WindowSize {
    public static int getWidth(Context context) {
        WindowManager manager = (WindowManager) context
                .getSystemService(Context.WINDOW_SERVICE);
        int width = manager.getDefaultDisplay().getWidth();
        return width;
    }

}
