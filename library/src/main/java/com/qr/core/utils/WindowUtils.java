package com.qr.core.utils;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Rect;
import android.os.Build;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;

public class WindowUtils {
    public static int getWindowWidth(Context context) {
        return getWindowMetrics(context).widthPixels;
    }

    // 这个高度是DecorView - NavBar的高度 不管隐不隐藏NavBar 这个值是不会变的
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
     * 当前Activity 是否全屏
     * Dialog 不要使用
     * 长宽测量完毕后才能调用(应该onWindowFocusChanged之后),之前调用总是返回false
     **/
    public static boolean isFullScreen(Activity activity){
        return !isNavigationBarVisible(activity) && !isStatusBarVisible(activity);
    }

    /**
     * 获取未被软键盘遮住的DecorViewHeight
     */
    public static int getDecorViewVisibleHeight(Activity activity){
        Rect rect = new Rect();
        activity.getWindow().getDecorView().getWindowVisibleDisplayFrame(rect);
        return rect.bottom;
    }

    private static int sDecorViewDelta = 0;
    public static int getDecorViewInvisibleHeight(Activity activity){
        int visibleHeight = getDecorViewVisibleHeight(activity);
        int decorViewHeight = getDecorViewHeight(activity);
        int delta = Math.abs(decorViewHeight - visibleHeight);
        if(delta <= getNavBarHeight(activity)){
            // 小于等于导航栏的高度 这个就是状态栏的高度
            sDecorViewDelta = delta;
            return 0;
        }

        return delta - sDecorViewDelta;
    }

    /**
     * 获取根节点View的高度
     * Dialog 不要使用
     * 长宽测量完毕后才能调用(应该onWindowFocusChanged之后),之前调用总是DecorViewHeight - StatusBarHeight - NavigationBarHeight
     */
    public static int getRootViewHeight(Activity activity){
        int decorViewHeight = getDecorViewHeight(activity);
        int statusBarHeight = 0;
        if(isStatusBarVisible(activity)){
            statusBarHeight = getStatusBarHeight(activity);
        }
        int navigationBarHeight = 0;
        if(isNavigationBarVisible(activity)){
            navigationBarHeight = getNavBarHeight(activity);
        }

        return decorViewHeight - statusBarHeight - navigationBarHeight;
    }

    /**
     * 获取DecorView高度
     * */
    public static int getDecorViewHeight(Activity activity){
        View decorView = activity.getWindow().getDecorView();
        int measuredHeight = decorView.getMeasuredHeight();
        if(measuredHeight <= 0){
            measuredHeight = getWindowRealHeight(activity);
        }
        return measuredHeight;
    }

    /**
     * 状态栏是否可见
     * Dialog 不要使用
     * 长宽测量完毕后才能调用(应该onWindowFocusChanged之后), 之前调用总是返回true
     */
    public static boolean isStatusBarVisible(Activity activity){
        View decorView = activity.getWindow().getDecorView();
        if(decorView.getMeasuredHeight() <= 0){
            return true;
        }

        int childCount = ((ViewGroup) decorView).getChildCount();
        if(childCount > 1 &&
                isStatusBar(((ViewGroup) decorView).getChildAt(1), (ViewGroup) decorView)){
            return true;
        }

        // 两个View以上
        return childCount > 2 &&
                isStatusBar(((ViewGroup) decorView).getChildAt(2), (ViewGroup) decorView);
    }

    private static boolean isStatusBar(View childView, ViewGroup decorView){
        return childView.getTop() == 0 &&
                childView.getMeasuredWidth() == decorView.getMeasuredWidth() &&
                childView.getBottom() < decorView.getBottom();
    }

    // 状态栏高度 不会变
    public static int getStatusBarHeight(Context context) {
        Resources resources = context.getResources();
        int resourceId = resources.getIdentifier("status_bar_height", "dimen", "android");
        return resources.getDimensionPixelSize(resourceId);
    }

    /**
     * 导航栏是否可见
     * Dialog 不要使用
     * 长宽测量完毕后才能调用(应该onWindowFocusChanged之后), 之前调用总是返回true
     */
    public static boolean isNavigationBarVisible(Activity activity){
        View decorView = activity.getWindow().getDecorView();
        if(decorView.getMeasuredHeight() <= 0){
            return true;
        }

        int childCount = ((ViewGroup) decorView).getChildCount();
        if(childCount > 1 &&
                isNavigationBar(((ViewGroup) decorView).getChildAt(1), (ViewGroup) decorView)){
            return true;
        }

        // 两个View以上
        return childCount > 2 &&
                isNavigationBar(((ViewGroup) decorView).getChildAt(2), (ViewGroup) decorView);
    }

    private static boolean isNavigationBar(View childView, ViewGroup decorView){
        return childView.getTop() > decorView.getTop() &&
                childView.getMeasuredWidth() == decorView.getMeasuredWidth() &&
                childView.getBottom() == decorView.getBottom();
    }

    // NavBar高度 不会变
    public static int getNavBarHeight(Context context) {
        Resources resources = context.getResources();
        int resourceId = resources.getIdentifier("navigation_bar_height", "dimen", "android");
        return resources.getDimensionPixelSize(resourceId);
    }
}
