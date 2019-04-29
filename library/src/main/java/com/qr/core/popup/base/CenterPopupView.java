package com.qr.core.popup.base;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;

import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;

import com.qr.core.R;
import com.qr.core.popup.animator.PopupAnimator;
import com.qr.core.popup.animator.ScaleAlphaAnimator;

import static com.qr.core.popup.animator.PopupAnimation.ScaleAlphaFromCenter;


public abstract class CenterPopupView extends BasePopupView {
    public CenterPopupView(@NonNull Context context) {
        super(context);

        FrameLayout centerPopupView = findViewById(R.id._center_popup_container);
        View view = LayoutInflater.from(getContext()).inflate(getImplLayoutId(), centerPopupView, false);
        centerPopupView.addView(view);
    }

    @Override
    protected final int getPopupViewLayoutId() {
        return R.layout._center_popup_view;
    }

    @NonNull
    @Override
    protected PopupAnimator getDefaultPopupAnimator() {
        return new ScaleAlphaAnimator(getPopupContentView(), ScaleAlphaFromCenter);
    }
}
