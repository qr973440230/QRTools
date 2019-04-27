package com.qr.core.popup.util;

import android.app.Activity;
import android.graphics.Rect;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.OvershootInterpolator;
import android.widget.EditText;

import com.qr.core.popup.base.BasePopupView;
import com.qr.core.popup.base.BottomPopupView;
import com.qr.core.popup.base.CenterPopupView;
import com.qr.core.utils.WindowUtils;

import java.util.ArrayList;

import static com.qr.core.utils.WindowUtils.getStatusBarHeight;
import static com.qr.core.utils.WindowUtils.getWindowHeight;
import static com.qr.core.utils.WindowUtils.getWindowRealHeight;
import static com.qr.core.utils.WindowUtils.getWindowWidth;

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
        //暂时忽略PartShadow弹窗和AttachPopupView
        pv.getPopupContentView().animate().translationY(0)
                .setInterpolator(new OvershootInterpolator(0))
                .setDuration(300).start();
    }
    public static void moveUpToKeyboard(int keyboardHeight, BasePopupView pv) {
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

        int dy = 0;
        int popupHeight = pv.getPopupContentView().getHeight();
        int popupWidth = pv.getPopupContentView().getWidth();
        if (pv.getPopupImplView() != null) {
            popupHeight = Math.min(popupHeight, pv.getPopupImplView().getMeasuredHeight());
            popupWidth = Math.min(popupWidth, pv.getPopupImplView().getMeasuredWidth());
        }
        int focusEtTop = 0;
        int focusBottom = 0;
        if (focusEt != null) {
            int[] locations = new int[2];
            focusEt.getLocationInWindow(locations);
            focusEtTop = locations[1];
            focusBottom = focusEtTop + focusEt.getMeasuredHeight();
        }

//        int windowHeight = WindowUtils.getDecorViewVisibleHeight((Activity) pv.getContext());
        int windowHeight = WindowUtils.getWindowHeight(pv.getContext());
        //执行上移
        if ((popupWidth == getWindowWidth(pv.getContext()) &&
                (popupHeight == (getWindowHeight(pv.getContext()) - getStatusBarHeight(pv.getContext()))))){
            // 如果是全屏弹窗，特殊处理，只要输入框没被盖住，就不移动。
            if (focusBottom + keyboardHeight < windowHeight) {
                return;
            }
        }

        if (pv instanceof CenterPopupView) {
            int targetY = keyboardHeight - (windowHeight - popupHeight + getStatusBarHeight(pv.getContext())) / 2; //上移到下边贴着输入法的高度

            if (focusEt != null && focusEtTop - targetY < 0) {
                targetY += focusEtTop - targetY - getStatusBarHeight(pv.getContext());//限制不能被状态栏遮住
            }
            dy = targetY;
        } else if (pv instanceof BottomPopupView) {
            dy = keyboardHeight;
            if (focusEt != null && focusEtTop - dy < 0) {
                dy += focusEtTop - dy - getStatusBarHeight(pv.getContext());//限制不能被状态栏遮住
            }
        }

        pv.getPopupContentView().animate().translationY(-dy)
                .setDuration(250)
                .setInterpolator(new OvershootInterpolator(0))
                .start();
    }
}
