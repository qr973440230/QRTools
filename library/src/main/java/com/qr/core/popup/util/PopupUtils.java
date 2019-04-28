package com.qr.core.popup.util;

import android.app.Activity;
import android.graphics.Rect;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.OvershootInterpolator;
import android.widget.EditText;

import com.qr.core.popup.base.BasePopupView;
import com.qr.core.utils.WindowUtils;

import java.util.ArrayList;

import static com.qr.core.utils.WindowUtils.getStatusBarHeight;

public class PopupUtils {
    public static boolean isInRect(float x, float y, Rect rect) {
        return x >= rect.left && x <= rect.right && y >= rect.top && y <= rect.bottom;
    }
    public static void findAllEditText(ArrayList<EditText> list, ViewGroup group) {
        for (int i = 0; i < group.getChildCount(); i++) {
            View v = group.getChildAt(i);
            if (v instanceof EditText && v.getVisibility() == View.VISIBLE) {
                list.add((EditText) v);
            } else if (v instanceof ViewGroup) {
                findAllEditText(list, (ViewGroup) v);
            }
        }
    }
    public static void moveDown(BasePopupView pv) {
        // 移动到原来的位置
        pv.getPopupContentView().animate().translationY(0)
                .setInterpolator(new OvershootInterpolator(0))
                .setDuration(300).start();
    }
    public static void moveUpToKeyboard(int keyboardHeight, BasePopupView pv) {
        Activity activity = (Activity) pv.getContext();
        if(activity == null){
            return;
        }

        //判断是否盖住输入框
        ArrayList<EditText> allEts = new ArrayList<>();
        findAllEditText(allEts, pv);
        EditText focusEt = null;
        for (EditText et : allEts) {
            if (et.isFocused()) {
                focusEt = et;
                break;
            }
        }

        int focusEtTop = 0;
        if (focusEt != null) {
            int[] locations = new int[2];
            focusEt.getLocationInWindow(locations);
            focusEtTop = locations[1];
        }

        int dy = 0;
        // 状态栏高度
        int statusBarHeight = getStatusBarHeight(activity);
        // 获得ImplView的底部
        int[] locations = new int[2];
        pv.getPopupImplView().getLocationInWindow(locations);
        int implViewBottom = locations[1] + pv.getPopupImplView().getMeasuredHeight();
        // 当前Activity可视高度
        int visibleHeight = WindowUtils.getDecorViewVisibleHeight(activity);

        int deltaY = visibleHeight - implViewBottom;
        if(deltaY >= 0){
            // 窗体显示在键盘之上
            return;
        }

        // 窗体显示在键盘之下
        deltaY = Math.abs(deltaY);

        if(WindowUtils.isStatusBarVisible(activity)){
            // 状态栏可见
            if(focusEt != null && focusEtTop - deltaY < statusBarHeight){
                // edit被状态栏被遮住
                deltaY = focusEtTop - statusBarHeight;
            }
        }else{
            // 状态栏不可见
            if(focusEt != null && focusEtTop - deltaY < 0){
                // edit被屏幕顶部遮住
                deltaY = focusEtTop;
            }
        }

        dy = deltaY;

        pv.getPopupContentView().animate().translationY(-dy)
                .setDuration(250)
                .setInterpolator(new OvershootInterpolator(0))
                .start();
    }
}
