package com.qr.core.utils;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Rect;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.FrameLayout;

import static com.qr.core.utils.WindowUtils.getDecorViewInvisibleHeight;

/**
 * Description:
 * Create by dance, at 2018/12/17
 */
public final class KeyboardUtils {
    private static int sDecorViewInvisibleHeightPre;
    private static ViewTreeObserver.OnGlobalLayoutListener onGlobalLayoutListener;
    private static OnSoftInputChangedListener onSoftInputChangedListener;

    private KeyboardUtils() {
    }

    /**
     * Register soft input changed listener.
     *
     * @param activity The activity.
     * @param listener The soft input changed listener.
     */
    public static void registerSoftInputChangedListener(final Activity activity, final OnSoftInputChangedListener listener) {
        final int flags = activity.getWindow().getAttributes().flags;
        if ((flags & WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS) != 0) {
            activity.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        }
        final FrameLayout contentView = activity.findViewById(android.R.id.content);
        sDecorViewInvisibleHeightPre = getDecorViewInvisibleHeight(activity);
        onSoftInputChangedListener = listener;
        onGlobalLayoutListener = new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                if (onSoftInputChangedListener != null) {
                    int height = getDecorViewInvisibleHeight(activity);
                    if (sDecorViewInvisibleHeightPre != height) {
                        onSoftInputChangedListener.onSoftInputChanged(height);
                        sDecorViewInvisibleHeightPre = height;
                    }
                }
            }
        };
        contentView.getViewTreeObserver()
                .addOnGlobalLayoutListener(onGlobalLayoutListener);
    }

    public static void removeLayoutChangeListener(View decorView){
        View contentView = decorView.findViewById(android.R.id.content);
        contentView.getViewTreeObserver().removeGlobalOnLayoutListener(onGlobalLayoutListener);
        onGlobalLayoutListener = null;
        onSoftInputChangedListener = null;
    }

    public static void showSoftInput(View view) {
        InputMethodManager imm = (InputMethodManager) view.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.showSoftInput(view, InputMethodManager.SHOW_FORCED);
    }

    public static void hideSoftInput(View view) {
        InputMethodManager imm = (InputMethodManager) view.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    public interface OnSoftInputChangedListener {
        void onSoftInputChanged(int height);
    }
}