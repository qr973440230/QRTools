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
    /***
     * 获取StatusBar高度 这个值不会变
     * @param context
     * @return
     */
    public static int getStatusBarHeight(Context context) {
        Resources resources = context.getResources();
        int resourceId = resources.getIdentifier("status_bar_height", "dimen", "android");
        return resources.getDimensionPixelSize(resourceId);
    }
    /****
     * 获取NavigationBar高度 这个值不会变
     * @param context
     * @return
     */
    public static int getNavigationBarHeight(Context context) {
        Resources resources = context.getResources();
        int resourceId = resources.getIdentifier("navigation_bar_height", "dimen", "android");
        return resources.getDimensionPixelSize(resourceId);
    }
    /****
     * 获取DecorView高度
     * @param activity
     * @return
     */
    public static int getDecorViewHeight(Activity activity){
        View decorView = activity.getWindow().getDecorView();
        return decorView.getMeasuredHeight();
    }
    /****
     *获取DecorView可见高度
     * @param activity
     * @return
     */
    public static int getDecorViewVisibleHeight(Activity activity){
        Rect rect = new Rect();
        activity.getWindow().getDecorView().getWindowVisibleDisplayFrame(rect);
        return rect.bottom;
    }
    /****
     * 获取DecorView不可见高度
     * @param activity
     * @return
     */
    public static int getDecorViewInvisibleHeight(Activity activity){
        int decorViewHeight = getDecorViewHeight(activity); // decorView 测量的高度
        int visibleHeight = getDecorViewVisibleHeight(activity);
        return Math.abs(decorViewHeight - visibleHeight);
    }

    /****
     * 导航栏是否可见
     * @param activity
     * @return
     */
    public static boolean isNavigationBarVisible(Activity activity){
        int decorViewHeight = getDecorViewHeight(activity);
        int decorViewVisibleHeight = getDecorViewVisibleHeight(activity);
        int windowRealHeight = getWindowRealHeight(activity);
        int navigationBarHeight = getNavigationBarHeight(activity);

        return decorViewHeight < windowRealHeight ||
                decorViewHeight - decorViewVisibleHeight == navigationBarHeight;
    }

    /****
     * 状态栏是否可见
     * @param activity
     * @return
     */
    public static boolean isStatusBarVisible(Activity activity){
        Rect rect = new Rect();
        activity.getWindow().getDecorView().getWindowVisibleDisplayFrame(rect);
        return rect.top == getStatusBarHeight(activity);
    }
}
