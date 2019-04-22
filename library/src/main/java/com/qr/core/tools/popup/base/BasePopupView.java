package com.qr.core.tools.popup.base;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;

public abstract class BasePopupView extends FrameLayout {
    public BasePopupView(@NonNull Context context) {
        super(context);

        View view = LayoutInflater.from(context).inflate(getPopupViewLayoutId(), this, false);
        view.setAlpha(0f);
        addView(view);
    }

    public void show(){
        final Activity activity = (Activity)getContext();
        final ViewGroup viewGroup = (ViewGroup) activity.getWindow().getDecorView();
        viewGroup.post(new Runnable() {
            @Override
            public void run() {
                // DecorView完成布局 将PopupView添加到DecorView
                viewGroup.addView(BasePopupView.this,new FrameLayout.LayoutParams(LayoutParams.MATCH_PARENT,
                        LayoutParams.MATCH_PARENT));

                onCreate();

                BasePopupView.this.post(new Runnable() {
                    @Override
                    public void run() {
                        View view = BasePopupView.this.getChildAt(0);
                        view.setAlpha(1f);
                        onShow();
                    }
                });
            }
        });
    }

    public void dismiss(){
        post(new Runnable() {
            @Override
            public void run() {
                onDismiss();
                final Activity activity = (Activity)getContext();
                final ViewGroup viewGroup = (ViewGroup) activity.getWindow().getDecorView();
                viewGroup.removeView(BasePopupView.this);
            }
        });
    }

    // 生命周期
    protected void onCreate(){
    }
    protected void onShow(){
    }
    protected void onDismiss(){
    }
    @LayoutRes
    protected abstract int getPopupViewLayoutId();
}
