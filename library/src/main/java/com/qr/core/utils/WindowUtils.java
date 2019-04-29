package com.qr.core.utils;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Rect;
import android.os.Build;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.WindowManager;

public class WindowUtils {
    public static int getWindowWidth(Context context) {
        return getWindowMetrics(context).widthPixels;
    }
    public static int getWindowHeight(Context context) {
        return getWindowMetrics(context).heightPixels;
    }
    public static DisplayMetrics getWindowMetrics(Context context){
         DisplayMetrics displayMetrics = new DisplayMetrics();
        ((WindowManager) context.getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay().getMetrics(displayMetrics);
        return displayMetrics;
    }
    public static int getWindowRealWidth(Context context){
        return getWindowRealMetrics(context).widthPixels;
    }
    public static int getWindowRealHeight(Context context){
        return getWindowRealMetrics(context).heightPixels;
    }
    public static DisplayMetrics getWindowRealMetrics(Context context){
        DisplayMetrics displayMetrics = new DisplayMetrics();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            ((WindowManager)context.getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay().getRealMetrics(displayMetrics);
        }else{
            ((WindowManager)context.getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay().getMetrics(displayMetrics);
        }
        return displayMetrics;
    }

    /**
     * 获取DecorView高度
     * */
    public static int getDecorViewHeight(Activity activity){
        View decorView = activity.getWindow().getDecorView();
        int measuredHeight = decorView.getMeasuredHeight();
        if(measuredHeight <= 0){
            throw new UnsupportedOperationException("DecorView Not Be Measured");
        }
        return measuredHeight;
    }

    /**
     * 获取未被软键盘遮住的DecorViewHeight
     */
    public static int getDecorViewVisibleHeight(Activity activity){
        Rect rect = new Rect();
        activity.getWindow().getDecorView().getWindowVisibleDisplayFrame(rect);
        return rect.bottom;
    }

    public static int getDecorViewInvisibleHeight(Activity activity){
        int decorViewHeight = getDecorViewHeight(activity); // decorView 测量的高度
        int visibleHeight = getDecorViewVisibleHeight(activity);
        return Math.abs(decorViewHeight - visibleHeight);
    }

    // StatusBar高度 不会变
    public static int getStatusBarHeight(Context context) {
        Resources resources = context.getResources();
        int resourceId = resources.getIdentifier("status_bar_height", "dimen", "android");
        return resources.getDimensionPixelSize(resourceId);
    }

    // NavBar高度 不会变
    public static int getNavBarHeight(Context context) {
        Resources resources = context.getResources();
        int resourceId = resources.getIdentifier("navigation_bar_height", "dimen", "android");
        return resources.getDimensionPixelSize(resourceId);
    }
}
